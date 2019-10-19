package ar.edu.itba.pod.client.helpers;

import ar.edu.itba.pod.api.Airport;
import ar.edu.itba.pod.api.Flight;
import com.hazelcast.core.IList;
import com.hazelcast.core.ReplicatedMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Map;

public class CSVhelper {
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

    public static void loadFlights(IList<Flight> list, String file) {
        try {

            CSVParser csvParser = new CSVParser(
                    Files.newBufferedReader(Paths.get(file)),
                    CSVFormat.newFormat(';').withFirstRecordAsHeader()
            );
            csvParser.forEach(csvRecord -> list.add(new Flight(
                    Flight.FlightType.fromString(csvRecord.get(3)),
                    Flight.MovementType.fromString(csvRecord.get(4)),
                    Flight.FlightClass.fromString(csvRecord.get(2)),
                    csvRecord.get(5),
                    csvRecord.get(6))));
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
}
