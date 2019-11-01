package ar.edu.itba.pod.client;

import ar.edu.itba.pod.api.Airport;
import ar.edu.itba.pod.api.Movement;
import ar.edu.itba.pod.api.collators.*;
import ar.edu.itba.pod.api.combiners.CabotageMovementsCombinerFactory;
import ar.edu.itba.pod.api.keypredicates.CabotageKeyPredicate;
import ar.edu.itba.pod.api.mappers.*;
import ar.edu.itba.pod.api.mappers.CabotageMovementsMapper;
import ar.edu.itba.pod.api.mappers.MovementsByAirportMapper;
import ar.edu.itba.pod.api.reducers.*;
import ar.edu.itba.pod.client.helpers.CSVhelper;
import ar.edu.itba.pod.client.helpers.CommandLineHelper;
import ar.edu.itba.pod.client.helpers.Timestamp;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.*;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.apache.commons.cli.CommandLine;
import java.io.File;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.System.exit;

public class Client {

    private static ClientConfig hazelcastCfg;
    private static HazelcastInstance hInstance;
    private static Timestamp timestamp;

    public static void main(String[] args) {

        CommandLine commandLine = CommandLineHelper.getOptions(args);
        Integer limit = null;
        if (Optional.ofNullable(commandLine.getOptionValue("Dn")).isPresent())
            limit = Integer.valueOf(commandLine.getOptionValue("Dn"));
        String outPath = Optional.ofNullable(commandLine.getOptionValue("DoutPath")).orElse(Paths.get("../../../out").normalize().toAbsolutePath().toString());
        Integer queryNumber = Integer.valueOf(commandLine.getOptionValue("Dquery"));

        validateParameters(commandLine);

        timestamp = new Timestamp(outPath, queryNumber);
        String inPath = Optional.ofNullable(commandLine.getOptionValue("DinPath")).orElse(Paths.get("../../../data").normalize().toAbsolutePath().toString());
        String airportDataFile = inPath + "/aeropuertos.csv";
        String flightsDataFile = inPath + "/movimientos.csv";

        // Absolute Path
        airportDataFile = new File(airportDataFile).getAbsolutePath();
        flightsDataFile = new File(flightsDataFile).getAbsolutePath();

        String addresses = Optional.ofNullable(commandLine.getOptionValue("Daddresses")).orElse("127.0.0.1");

        timestamp.write("Iniciando configuración...");

        // Load Configurations and data files
        hazelcastCfg = new ClientConfig();
        hazelcastCfg.setProperty("hazelcast.logging.type", "none");
        hazelcastCfg.getGroupConfig().setName("g9").setPassword("g9");
        String[] addressesArray = addresses.split(";");
        System.out.println(Arrays.toString(addressesArray));
        hazelcastCfg.addAddress(addressesArray);
        hInstance = HazelcastClient.newHazelcastClient(hazelcastCfg);
        final Map<String, Airport> airportsMap = hInstance.getReplicatedMap("airport-list");
        final IList<Movement> flightsList = hInstance.getList("flights-list");
        airportsMap.clear();
        flightsList.clear();

        // Loads data from files
        timestamp.write("Inicio de la lectura del archivo");

        CSVhelper.loadAirports(airportsMap, airportDataFile);
        CSVhelper.loadFlights(flightsList, flightsDataFile);

        timestamp.write("Fin de la lectura del archivo");
        Long millis = System.currentTimeMillis();

        // Configure job
        final KeyValueSource<String, Movement> flightsSource = KeyValueSource.fromList(flightsList);
        JobTracker jobTracker = hInstance.getJobTracker("default");
        Job<String, Movement> job = jobTracker.newJob(flightsSource);

        // Execute query

        timestamp.write("Inicio del trabajo map/reduce");

        switch (queryNumber){
            case 1: query1(job, airportsMap, outPath); break;
            case 2: query2(job, limit, outPath); break;
            case 3: query3(job, outPath); break;
            case 4: query4(job, limit, airportsMap, commandLine.getOptionValue("Doaci"), outPath); break;
            case 5: query5(job, limit, airportsMap, outPath); break;
            case 6: query6(job, airportsMap, Integer.valueOf(commandLine.getOptionValue("Dmin")), outPath); break;
            default: System.out.println("Error");
        }

        millis = System.currentTimeMillis() - millis;
        timestamp.write("Fin del trabajo map/reduce. Duración: " + millis + " milisegundos.");

        timestamp.close();

        exit(0);
    }

    private static void validateParameters(CommandLine commandLine) {
        switch (commandLine.getOptionValue("Dquery")){
            case "2":
            case "5":
                if (!Optional.ofNullable(commandLine.getOptionValue("Dn")).isPresent()){
                System.out.println("Paramenter N is required");
                exit(1);
            }
            break;
            case "4": if (!Optional.ofNullable(commandLine.getOptionValue("Doaci")).isPresent() ||
                    !Optional.ofNullable(commandLine.getOptionValue("Dn")).isPresent()){
                System.out.println("Paramenter N and OACI are required");
                exit(1);
            }
            break;
            case "6":
                if (!Optional.ofNullable(commandLine.getOptionValue("Dmin")).isPresent()){
                    System.out.println("Paramenter min is required");
                    exit(1);
                }
                break;
        }
    }

    private static void query1(Job job, Map<String, Airport> airports, String outPath){

        ICompletableFuture<Map<String, Integer>> future = job
                .mapper( new MovementsByAirportMapper() )
                .reducer( new MovementsByAirportReducer() )
                .submit();
        try {
            Map<String, Integer> result = future.get();
            CSVhelper.writeQuery1Csv(outPath, result, airports);
        } catch (Exception e) {
            System.out.println("No pudimos procesar la información.");
            exit(1);
        }

    }

    private static void query2(Job job, Integer quantityOfResults, String outPath){

        ICompletableFuture<List<Map.Entry<String, Double>>> future = job
//                .keyPredicate(new CabotageKeyPredicate())
                .mapper( new CabotageMovementsMapper() )
//                .combiner(new CabotageMovementsCombinerFactory())

                .reducer( new CabotagePercentageReducer() )
                .submit(new CabotageMovementsPerAirlineCollator(quantityOfResults));
        try {
            List<Map.Entry<String, Double>> result = future.get();
            CSVhelper.writeQuery2Csv(outPath, result);
        } catch (Exception e){
            System.out.println("No pudimos procesar la información.");
            exit(1);
        }
    }

    private static void query3(Job job, String outPath){

        ICompletableFuture<Map<String, Integer>> future = job
                .mapper(new MovementsByAirportMapper())
                .reducer(new MovementsByAirportReducer())
                .submit();
        try {
            Map<String, Integer> result = future.get();
            IMap<String, Integer> partialMapQ3 = hInstance.getMap("q3-aux");

            for (Map.Entry<String, Integer> entry : result.entrySet()) {
                partialMapQ3.put(entry.getKey(), entry.getValue());
            }

            final KeyValueSource<String, Integer> partialSource = KeyValueSource.fromMap(partialMapQ3);
            JobTracker jobTracker = hInstance.getJobTracker("default");
            Job<String, Integer> job2 = jobTracker.newJob(partialSource);

            ICompletableFuture<Map<Integer, Set<Map.Entry<String, String>>>> future2 = job2
                    .mapper(new ReverseMovMapper())
                    .reducer(new GroupByAmountReducer())
                    .submit(new GroupByAmountCollator());

            Map<Integer, Set<Map.Entry<String, String>>> result2 = future2.get();

            CSVhelper.writeQuery3Csv(outPath, result2);
        } catch (Exception e){
            System.out.println("No pudimos procesar la información.");
            exit(1);
        }

    }

    private static void query4(Job job, Integer quantityOfResults,
                               Map<String, Airport> airportsMap, final String srcOaci, String outPath){

        ICompletableFuture<List<Map.Entry<String, Integer>>> future = job
                .mapper( new DestinyAirportPerSrcAirportMapper(srcOaci))
                .reducer( new DestinyAiportPerSrcAirportReducer())
                .submit(new DestinyAirportBySrcAirportCollator(quantityOfResults, airportsMap));
        try {
            List<Map.Entry<String, Integer>> results = future.get();
            CSVhelper.writeQuery4Csv(outPath, results);
        } catch (Exception e){
            System.out.println("No pudimos procesar la información.");
            exit(1);
        }
    }

    private static void query5(Job job, Integer quantityOfResults, Map<String, Airport> airportsMap,
                               String outPath){

        ICompletableFuture<List<Map.Entry<String, Double>>> future = job
                .mapper( new PrivateMovementsMapper() )
                .reducer( new PrivateFlightsPercentageReducer() )
                .submit(new PrivateMovementsPerAirlineCollator(quantityOfResults, airportsMap));
        try {
            List<Map.Entry<String, Double>> result = future.get();
            CSVhelper.writeQuery5Csv(outPath, result);
        } catch (Exception e){
            System.out.println("No pudimos procesar la información.");
            exit(1);
        }
    }

    private static void query6(Job job, Map<String, Airport> airportsMap, Integer min, String outPath){

        ICompletableFuture<List<Map.Entry<String, Integer>>> future = job
                .mapper( new MovementsMapperSrcDest() )
                .reducer( new MovementPairsReducer() )
                .submit( new SrcDestToProvinceCollator2(airportsMap));

        try {
            List<Map.Entry<String, Integer>> result = future.get();

            IMap<String, Integer> partialMapQ6 = hInstance.getMap("q6-aux");

            for (int i = 0; i < result.size(); i++) {
                Map.Entry<String, Integer> entry = result.get(i);
                partialMapQ6.put(entry.getKey() + "/" + i, entry.getValue());
            }

            final KeyValueSource<String, Integer> partialSource = KeyValueSource.fromMap(partialMapQ6);
            JobTracker jobTracker = hInstance.getJobTracker("default");
            Job<String, Integer> job2 = jobTracker.newJob(partialSource);

            ICompletableFuture<List<Map.Entry<String, Integer>>> future2 = job2
                    .mapper(new IdentityMap2())
                    .reducer(new MovementPairsReducer())
                    .submit( new MovementPairsCollator(1000, airportsMap));

            List<Map.Entry<String,Integer>> result2 = future2.get();

            CSVhelper.writeQuery6Csv(outPath, result2);
        } catch (Exception e){
            System.out.println("No pudimos procesar la información.");
            exit(1);
        }
    }

}
