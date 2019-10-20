package ar.edu.itba.pod.client;

import ar.edu.itba.pod.api.Airport;
import ar.edu.itba.pod.api.Movement;
import ar.edu.itba.pod.api.collators.CabotageMovementsPerAirlineCollator;
import ar.edu.itba.pod.api.mappers.CabotageMovementsMapper;
import ar.edu.itba.pod.api.mappers.MovementsByAirportMapper;
import ar.edu.itba.pod.api.reducers.CabotagePercentageReducer;
import ar.edu.itba.pod.api.reducers.MovementsByAirportReducer;
import ar.edu.itba.pod.client.helpers.CSVhelper;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IList;
import com.hazelcast.core.ReplicatedMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.lang.System.exit;

public class Client {

    private static Logger logger = LoggerFactory.getLogger(Client.class);
    private static ClientConfig hazelcastCfg;
    private static HazelcastInstance hInstance;


    public static void main(String[] args) {
        // Read Parameters
//        CommandLine commandLine = CommandLineHelper.getOptions(args);

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

        final KeyValueSource<String, Movement> flightsSource = KeyValueSource.fromList(flightsList);

        JobTracker jobTracker = hInstance.getJobTracker("default");
        Job<String, Movement> job = jobTracker.newJob(flightsSource);

//        switch (commandLine.getOptionValue("query")){
        switch ("2"){
            case "1": query1(job, airportsMap); break;
            case "2": query2(job, 5); break;
            case "3": query3(airportsMap, flightsSource, job); break;
            case "4": query4(airportsMap, flightsSource, job); break;
            case "5": query5(airportsMap, flightsSource, job); break;
            case "6": query6(airportsMap, flightsSource, job); break;
            default: logger.error("Invalid Input.");
        }
        System.out.println("Query Finished");
        exit(0);
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

    private static void query2(Job job, Integer quantityOfResults){
        ICompletableFuture<List<Map.Entry<String, Double>>> future = job
                .mapper( new CabotageMovementsMapper() )
                .reducer( new CabotagePercentageReducer() )
                .submit(new CabotageMovementsPerAirlineCollator(quantityOfResults));
        try {
            List<Map.Entry<String, Double>> result = future.get();
            CSVhelper.writeQuery2Csv("query2.csv", result, quantityOfResults);
        } catch (InterruptedException e) {  // TODO: More explicit error messages.
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void query3(ReplicatedMap<String, Airport> airportsMap,
                               KeyValueSource<String, Movement> flights,
                               Job job
                               ){
    }

    private static void query4(ReplicatedMap<String, Airport> airportsMap,
                               KeyValueSource<String, Movement> flights,
                               Job job
                               ){
    }

    private static void query5(ReplicatedMap<String, Airport> airportsMap,
                               KeyValueSource<String, Movement> flights,
                               Job job
                               ){
    }

    private static void query6(ReplicatedMap<String, Airport> airportsMap,
                               KeyValueSource<String, Movement> flights,
                               Job job
                               ){
    }

}
