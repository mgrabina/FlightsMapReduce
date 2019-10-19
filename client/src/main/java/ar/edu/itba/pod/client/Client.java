package ar.edu.itba.pod.client;

import ar.edu.itba.pod.api.Airport;
import ar.edu.itba.pod.api.Flight;
import ar.edu.itba.pod.client.helpers.CommandLineHelper;
import ar.edu.itba.pod.client.helpers.CSVhelper;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IList;
import com.hazelcast.core.ReplicatedMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobCompletableFuture;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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
        final IList<Flight> flightsList = hInstance.getList("flights-list");
        airportsMap.clear();
        flightsList.clear();

        // Loads data from files
        CSVhelper.loadAirports(airportsMap, airportDataFile);
        CSVhelper.loadFlights(flightsList, flightsDataFile);

        final KeyValueSource<String, Flight> flightsSource = KeyValueSource.fromList(flightsList);

        JobTracker jobTracker = hInstance.getJobTracker("default");
        Job<String, Flight> job = jobTracker.newJob(flightsSource);

//        switch (commandLine.getOptionValue("query")){
        switch ("1"){
            case "1": query1(airportsMap, flightsSource, job); break;
            case "2": query2(airportsMap, flightsSource, job); break;
            case "3": query3(airportsMap, flightsSource, job); break;
            case "4": query4(airportsMap, flightsSource, job); break;
            case "5": query5(airportsMap, flightsSource, job); break;
            case "6": query6(airportsMap, flightsSource, job); break;
            default: logger.error("Invalid Input.");
        }
    }

    private static void query1(ReplicatedMap<String, Airport> airportsMap,
                               KeyValueSource<String, Flight> flights,
                               Job job
                               ){
//        ICompletableFuture<Map<String, Long>> future2 = job
//                .mapper( new TokenizerMapper() )
//                .reducer( new WordCountReducerFactory() )
//                .submit();
//        try {
//            Map<String, Long> result = future2.get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
    }

    private static void query2(ReplicatedMap<String, Airport> airportsMap,
                               KeyValueSource<String, Flight> flights,
                               Job job
                               ){
    }

    private static void query3(ReplicatedMap<String, Airport> airportsMap,
                               KeyValueSource<String, Flight> flights,
                               Job job
                               ){
    }

    private static void query4(ReplicatedMap<String, Airport> airportsMap,
                               KeyValueSource<String, Flight> flights,
                               Job job
                               ){
    }

    private static void query5(ReplicatedMap<String, Airport> airportsMap,
                               KeyValueSource<String, Flight> flights,
                               Job job
                               ){
    }

    private static void query6(ReplicatedMap<String, Airport> airportsMap,
                               KeyValueSource<String, Flight> flights,
                               Job job
                               ){
    }

}
