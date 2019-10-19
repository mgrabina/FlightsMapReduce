package ar.edu.itba.pod.client;

import ar.edu.itba.pod.client.helpers.CommandLineHelper;
import ar.edu.itba.pod.*;
import ar.edu.itba.pod.client.helpers.CSVhelper;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IList;
import com.hazelcast.mapreduce.KeyValueSource;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {

    private static Logger logger = LoggerFactory.getLogger(Client.class);
    private static ClientConfig hazelcastCfg;
    private static HazelcastInstance hInstance;


    public static void main(String[] args) {

        hazelcastCfg = new ClientConfig();
        hazelcastCfg.getGroupConfig().setName("lity").setPassword("lito123");
        hInstance = HazelcastClient.newHazelcastClient(hazelcastCfg);
        final IList<Airport> airportsList = hInstance.getList("airport-list");
        final IList<Flight> flightsList = hInstance.getList("flights-list");
        airportsList.clear();
        flightsList.clear();

        CSVhelper.loadAirpors(airportsList, "");

        final KeyValueSource<String, String> airportSource = KeyValueSource.fromList(airportsList);
        final KeyValueSource<String, String> flightsSource = KeyValueSource.fromList(flightsList);




    }

    private static CommandLine getOptions(String[] args){

        Options options = new Options();

        Option ip = new Option("DserverAddress", "DserverAddress", true, "IP address of the server");
        ip.setRequired(true);
        options.addOption(ip);

        Option table = new Option("Did", "Did", true, "Table id");
        table.setRequired(false);
        options.addOption(table);

        Option state = new Option("Dstate", "Dstate", true, "State name");
        state.setRequired(false);
        options.addOption(state);

        Option path = new Option("DoutPath", "DoutPath", true, "Path to elections results");
        path.setRequired(true);
        options.addOption(path);

        return CommandLineHelper.generateCommandLineParser(options, args);
    }


}
