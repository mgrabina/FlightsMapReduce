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

        Option query = new Option("Dquery", "Dquery", true, "Query number");
        query.setRequired(true);
        options.addOption(query);

        Option ip = new Option("Daddresses", "Daddresses", true, "IP addresses of the nodes");
        ip.setRequired(false);
        options.addOption(ip);

        Option outPath = new Option("DoutPath", "DoutPath", true, "Path for output");
        outPath.setRequired(false);
        options.addOption(outPath);


        Option inPath = new Option("DinPath", "DinPath", true, "Path for input");
        inPath.setRequired(false);
        options.addOption(inPath);

        Option n = new Option("Dn", "Dn", true, "Limit of results");
        n.setRequired(false);
        options.addOption(n);

        Option oaci = new Option("Doaci", "Doaci", true, "OACI code of the Airport");
        oaci.setRequired(false);
        options.addOption(oaci);

        Option min = new Option("Dmin", "Dmin", true, "Minimum of movements");
        min.setRequired(false);
        options.addOption(min);

        return CommandLineHelper.generateCommandLineParser(options, args);
    }
}
