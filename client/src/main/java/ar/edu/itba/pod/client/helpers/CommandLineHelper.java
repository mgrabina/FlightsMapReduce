package ar.edu.itba.pod.client.helpers;

import org.apache.commons.cli.*;

public class CommandLineHelper {

    public static CommandLine generateCommandLineParser(Options options, String[] args){
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd=null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }
        return cmd;
    }

    public static CommandLine getOptions(String[] args){

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
