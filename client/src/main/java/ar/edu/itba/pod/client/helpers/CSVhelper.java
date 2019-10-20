package ar.edu.itba.pod.client.helpers;

import ar.edu.itba.pod.api.Airport;
import ar.edu.itba.pod.api.Movement;
import com.hazelcast.core.IList;
import com.hazelcast.core.ReplicatedMap;
import javafx.util.Pair;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;

public class CSVhelper {
    private static DecimalFormat df2 = new DecimalFormat("#.##");


    public static void loadAirports(ReplicatedMap<String, Airport> map, String file) {
        try {
            CSVParser csvParser = new CSVParser(
                    Files.newBufferedReader(Paths.get(file)),
                    CSVFormat.newFormat(';').withFirstRecordAsHeader()
            );
            csvParser.forEach(csvRecord -> {
                Airport a = new Airport(csvRecord.get(1), csvRecord.get(4), csvRecord.get(20));
                map.putIfAbsent(a.getOaci(), a);
            });
        } catch (IOException ex) {
            System.out.println("Error while parsing csv file. Exception: " + ex.getMessage());
        }
    }

    public static void loadFlights(IList<Movement> list, String file) {
        try {

            CSVParser csvParser = new CSVParser(
                    Files.newBufferedReader(Paths.get(file)),
                    CSVFormat.newFormat(';').withFirstRecordAsHeader()
            );
            csvParser.forEach(csvRecord -> list.add(new Movement(
                    Movement.FlightType.fromString(csvRecord.get(3)),
                    Movement.MovementType.fromString(csvRecord.get(4)),
                    Movement.FlightClass.fromString(csvRecord.get(2)),
                    csvRecord.get(5),
                    csvRecord.get(6),
                    csvRecord.get(7)
            )));
        } catch (IllegalArgumentException ex){
            System.out.println("Invalid value for enum. Exception: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error while parsing csv file. Exception: " + ex.getMessage());
        }
    }

    public static void writeCsv(String file, Map<String, Double> results) {
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(file));
            final CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.newFormat(';')
                    .withHeader("Porcentaje", "Partido").withRecordSeparator('\n'));

            results.entrySet().stream().sorted((o1, o2) -> {
                if(!o1.getValue().equals(o2.getValue()))
                    return Double.compare(o2.getValue(),o1.getValue());
                return o1.getKey().compareTo(o2.getKey());
            }).forEach(entry -> {
                String party = entry.getKey();
                DecimalFormat format = new DecimalFormat("##.00");
                String percent = format.format(entry.getValue() * 100) + "%";
                try {
                    csvPrinter.printRecord(percent, party);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            csvPrinter.flush();
        } catch (IOException e){
            System.out.println("Error while printing csv file.");
        }
    }

    public static void writeQuery1Csv(String file, Map<String, Integer> results, Map<String, Airport> airports) {
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(file));
            final CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.newFormat(';')
                    .withHeader("OACI", "Denominación", "Movimientos").withRecordSeparator('\n'));
            results.entrySet().stream()
                    .filter(entry -> airports.containsKey(entry.getKey()))
                    .sorted(new Comparator<Map.Entry<String, Integer>>() {
                        @Override
                        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                            if (!o1.getValue().equals(o2.getValue())){
                                return o2.getValue().compareTo(o1.getValue());  //First, comparing by value - desc
                            }
                            return o1.getKey().compareTo(o2.getKey());          //Second, comparing by key alpha order asc
                        }
                    })
                    .forEach(entry -> {
                        try {
                            Airport airport = airports.get(entry.getKey());
                            String quantity = entry.getValue().toString();
                            csvPrinter.printRecord(airport.getOaci(), airport.getDenomination(), quantity);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
            });
            csvPrinter.flush();
        } catch (IOException e){
            System.out.println("Error while printing csv file.");
        }
    }

    public static void writeQuery2Csv(String file, List<Map.Entry<String, Double>> results) {
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(file));
            final CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.newFormat(';')
                    .withHeader("Aerolínea", "Porcentaje").withRecordSeparator('\n'));
            results.forEach(entry -> {
                        try {
                            csvPrinter.printRecord(entry.getKey(), df2.format(entry.getValue()*100d) + "%");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            csvPrinter.flush();
        } catch (IOException e){
            System.out.println("Error while printing csv file.");
        }
    }

    public static void writeQuery5Csv(String file, List<Map.Entry<String, Double>> results) {
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(file));
            final CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.newFormat(';')
                    .withHeader("OACI", "Porcentaje").withRecordSeparator('\n'));
            results.forEach(entry -> {
                try {
                    csvPrinter.printRecord(entry.getKey(), df2.format(entry.getValue()*100d) + "%");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            csvPrinter.flush();
        } catch (IOException e){
            System.out.println("Error while printing csv file.");
        }
    }

    public static void writeQuery3Csv(String file, Map<Integer, Set<Pair<String, String>>> results) {

        SortedSet<Integer> keys = new TreeSet<>(results.keySet()).descendingSet();

        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(file));
            final CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.newFormat(';')
                    .withHeader("Grupo", "Aeropuerto A", "Aeropuerto B").withRecordSeparator('\n'));
            for (Integer key : keys) {
                Set<Pair<String, String>> pairs = results.get(key);

                for(Pair p : pairs){
                    try {
                        csvPrinter.printRecord(key, p.getKey().toString(), p.getValue().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            csvPrinter.flush();
        } catch (IOException e){
            System.out.println("Error while printing csv file.");
        }
    }
}
