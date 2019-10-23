package ar.edu.itba.pod.client.helpers;

import ar.edu.itba.pod.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Timestamp {

    private Logger logger;
    private PrintWriter printer;

    public Timestamp(){

        try{
            this.printer = new PrintWriter("timestamp.txt", "UTF-8");
        } catch (IOException e){
            System.out.println("Could not create printer.");
        }

        this.logger =  LoggerFactory.getLogger(Client.class);
    }

    public void write(String message){

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSSS");
        String formattedDate = sdf.format(date);

        this.printer.println(formattedDate + " - " + message);
        this.logger.info(message);
    }

    public void close(){
        this.printer.close();
    }
}
