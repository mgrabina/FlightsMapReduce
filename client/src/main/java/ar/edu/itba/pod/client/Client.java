package ar.edu.itba.pod.client;

import ar.edu.itba.pod.api.Airport;
import ar.edu.itba.pod.api.Movement;
import ar.edu.itba.pod.api.collators.CabotageMovementsPerAirlineCollator;
import ar.edu.itba.pod.api.collators.DestinyAirportBySrcAirportCollator;
import ar.edu.itba.pod.api.collators.PrivateMovementsPerAirlineCollator;
import ar.edu.itba.pod.api.mappers.*;
import ar.edu.itba.pod.api.collators.GroupByAmountCollator;
import ar.edu.itba.pod.api.mappers.CabotageMovementsMapper;
import ar.edu.itba.pod.api.mappers.MovementsByAirportMapper;
import ar.edu.itba.pod.api.reducers.*;
import ar.edu.itba.pod.client.helpers.CSVhelper;
import ar.edu.itba.pod.client.helpers.CommandLineHelper;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.*;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import javafx.util.Pair;
import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static java.lang.System.exit;

public class Client {

    private static Logger logger = LoggerFactory.getLogger(Client.class);
    private static ClientConfig hazelcastCfg;
    private static HazelcastInstance hInstance;


    public static void main(String[] args) {
        CommandLine commandLine = CommandLineHelper.getOptions(args);
        Integer limit = Integer.valueOf(commandLine.getOptionValue("n"));

        validateParameters(commandLine);

        String airportDataFile = "data/aeropuertos.csv";
        String flightsDataFile = "data/movimientos.csv";

        // Absolute Path
        airportDataFile = new File(airportDataFile).getAbsolutePath();
        flightsDataFile = new File(flightsDataFile).getAbsolutePath();

        // Load Configurations and data files
        hazelcastCfg = new ClientConfig();
        hazelcastCfg.getGroupConfig().setName("lity").setPassword("lito123");
        hInstance = HazelcastClient.newHazelcastClient(hazelcastCfg);
        final ReplicatedMap<String, Airport> airportsMap = hInstance.getReplicatedMap("airport-list");
        final IList<Movement> flightsList = hInstance.getList("flights-list");
        airportsMap.clear();
        flightsList.clear();

        // Loads data from files
        CSVhelper.loadAirports(airportsMap, airportDataFile);
        CSVhelper.loadFlights(flightsList, flightsDataFile);

        // Configure job
        final KeyValueSource<String, Movement> flightsSource = KeyValueSource.fromList(flightsList);
        JobTracker jobTracker = hInstance.getJobTracker("default");
        Job<String, Movement> job = jobTracker.newJob(flightsSource);

        // Execute query
        switch (commandLine.getOptionValue("query")){
            case "1": query1(job, airportsMap); break;
            case "2": query2(job, limit); break;
            case "3": query3(job); break;
            case "4": query4(job, limit, airportsMap, commandLine.getOptionValue("oaci")); break;
            case "5": query5(job, limit, airportsMap); break;
            case "6": query6(job, airportsMap, Integer.valueOf(commandLine.getOptionValue("min"))); break;
            default: logger.error("Invalid Input.");
        }
        System.out.println("Query Finished");
        exit(0);
    }

    private static void validateParameters(CommandLine commandLine) {
        switch (commandLine.getOptionValue("query")){
            case "2":
            case "5":
                if (!Optional.ofNullable(commandLine.getOptionValue("n")).isPresent()){
                System.out.println("Paramenter N is required");
                exit(1);
            }
            break;
            case "4": if (!Optional.ofNullable(commandLine.getOptionValue("oaci")).isPresent() ||
                    !Optional.ofNullable(commandLine.getOptionValue("n")).isPresent()){
                System.out.println("Paramenter N and OACI are required");
                exit(1);
            }
            break;
            case "6":
                if (!Optional.ofNullable(commandLine.getOptionValue("min")).isPresent()){
                    System.out.println("Paramenter min is required");
                    exit(1);
                }
                break;
        }
    }

    private static void query1(Job job, Map<String, Airport> airports){
        ICompletableFuture<Map<String, Integer>> future = job
                .mapper( new MovementsByAirportMapper() )
                .reducer( new MovementsByAirportReducer() )
                .submit();
        try {
            Map<String, Integer> result = future.get();
            CSVhelper.writeQuery1Csv("query1.csv", result, airports);
        } catch (InterruptedException e) {  // TODO: More explicit error messages.
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //Ojo con el join, no se si no deberia ser distribuido tmb y por eso es un ReplicatedMap
    private static void query2(Job job, Integer quantityOfResults){
        ICompletableFuture<List<Map.Entry<String, Double>>> future = job
                .mapper( new CabotageMovementsMapper() )
                .reducer( new CabotagePercentageReducer() )
                .submit(new CabotageMovementsPerAirlineCollator(quantityOfResults));
        try {
            List<Map.Entry<String, Double>> result = future.get();
            CSVhelper.writeQuery2Csv("query2.csv", result);
        } catch (InterruptedException e) {  // TODO: More explicit error messages.
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void query3(Job job){

        ICompletableFuture<Map<String, Integer>> future = job
                .mapper(new MovementsByAirportMapper())
                .reducer(new MovementsByAirportReducer())
                .submit();
        try {
            Map<String, Integer> result = future.get();
            IMap<String, Integer> partialMapQ3 = hInstance.getMap("q5-aux");

            for (Map.Entry<String, Integer> entry : result.entrySet()) {
                partialMapQ3.put(entry.getKey(), entry.getValue());
            }

            final KeyValueSource<String, Integer> partialSource = KeyValueSource.fromMap(partialMapQ3);
            JobTracker jobTracker = hInstance.getJobTracker("default");
            Job<String, Integer> job2 = jobTracker.newJob(partialSource);

            ICompletableFuture<Map<Integer, Set<Pair<String, String>>>> future2 = job2
                    .mapper(new ReverseMovMapper())
                    .reducer(new GroupByAmountReducer())
                    .submit(new GroupByAmountCollator());

            Map<Integer, Set<Pair<String, String>>> result2 = future2.get();

            System.out.println(result2);
            CSVhelper.writeQuery3Csv("query3.csv", result2);
        } catch (InterruptedException e) {  // TODO: More explicit error messages.
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }


    }

    private static void query4(Job job, Integer quantityOfResults,
                               ReplicatedMap<String, Airport> airportsMap, final String srcOaci){
        ICompletableFuture<List<Map.Entry<String, Integer>>> future = job
                .mapper( new DestinyAirportPerSrcAirportMapper(srcOaci))
                .reducer( new DestinyAiportPerSrcAirportReducer())
                .submit(new DestinyAirportBySrcAirportCollator(quantityOfResults, airportsMap));
        try {
            List<Map.Entry<String, Integer>> results = future.get();
            CSVhelper.writeQuery4Csv("query4.csv", results);
        } catch (InterruptedException e) {  // TODO: More explicit error messages.
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void query5(Job job, Integer quantityOfResults, ReplicatedMap<String, Airport> airportsMap){
        ICompletableFuture<List<Map.Entry<String, Double>>> future = job
                .mapper( new PrivateMovementsMapper() )
                .reducer( new PrivateFlightsPercentageReducer() )
                .submit(new PrivateMovementsPerAirlineCollator(quantityOfResults, airportsMap));
        try {
            List<Map.Entry<String, Double>> result = future.get();
            CSVhelper.writeQuery5Csv("query5.csv", result);
        } catch (InterruptedException e) {  // TODO: More explicit error messages.
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void query6(Job job, ReplicatedMap<String, Airport> airportsMap, Integer min){

        // TODO Implement
    }

}
