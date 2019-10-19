package ar.edu.itba.pod.client.helpers;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CSVhelper {

    public static void loadAirports(IList<Airport> list, String file) {

        CSVParser csvParser = null;

        try {
            Reader reader = Files.newBufferedReader(Paths.get(file));
            csvParser = new CSVParser(reader, CSVFormat.newFormat(';').withFirstRecordAsHeader());
        } catch (IOException ex) {
            System.out.println("Error while parsing csv file.");
        }

        for (CSVRecord csvRecord : csvParser) {

            String oaci = csvRecord.get(1);
            String denomination = csvRecord.get(4);
            String province = csvRecord.get(20);

            Airport a = new Airport(oaci, denomination, province);
            list.add(a);
        }
    }

    public static void loadFlights(IList<Airport> list, String file) {

        CSVParser csvParser = null;

        try {
            Reader reader = Files.newBufferedReader(Paths.get(file));
            csvParser = new CSVParser(reader, CSVFormat.newFormat(';').withFirstRecordAsHeader());
        } catch (IOException ex) {
            System.out.println("Error while parsing csv file.");
        }

        for (CSVRecord csvRecord : csvParser) {

            Flight.FlightType fType = FlightType(csvRecord.get(2));
            Flight.MovementType movementType = MovementType(csvRecord.get(3));
            Flight.FlightClass flightClass = FlightClass(csvRecord.get(4));
            String srcOaci = csvRecord.get(5);
            String destOaci = csvRecord.get(6);

            Flight f = new Flight(fType, movementType, flightClass, srcOaci, destOaci);
            list.add(a);
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
