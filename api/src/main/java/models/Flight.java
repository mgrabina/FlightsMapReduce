package ar.edu.itba.pod.api.models;

import java.io.Serializable;

public class Flight implements Serializable {

    private FlightType flightType;
    private MovementType movementType;
    private FlightClass flightClass;
    private String srcOaci;
    private String destOaci;


    public enum FlightType {
        CABOTAGE("Cabotaje"),
        INTERNATIONAL("Internacional"),
        NA("N/A");

        String value;

        @Override
        public String toString() {
            return value;
        }

        FlightType(String val) {
            this.value = val;
        }
    }

    public enum MovementType {
        TAKEOFF("Despegue"),
        LANDING("Aterrizaje");

        String value;

        @Override
        public String toString() {
            return value;
        }

        MovementType(String val) {
            this.value = val;
        }
    }

    public enum FlightClass {
        NONREGULAR("No Regular"),
        REGULAR("Regular"),
        PRIVATE_EXT("Vuelo Privado con Matrícula Extranjera"),
        PRIVATE_LOCAL("Vuelo Privado con Matrícula Nacional");

        String value;

        @Override
        public String toString() {
            return value;
        }

        FlightClass(String val) {
            this.value = val;
        }
    }

    public Flight(ar.edu.itba.pod.api.models.Flight.FlightType flightType, ar.edu.itba.pod.api.models.Flight.MovementType movementType, ar.edu.itba.pod.api.models.Flight.FlightClass flightClass, String srcOaci, String destOaci) {
        this.flightType = flightType;
        this.movementType = movementType;
        this.flightClass = flightClass;
        this.srcOaci = srcOaci;
        this.destOaci = destOaci;
    }

    public ar.edu.itba.pod.api.models.Flight.FlightType getFlightType() {
        return flightType;
    }

    public void setFlightType(ar.edu.itba.pod.api.models.Flight.FlightType flightType) {
        this.flightType = flightType;
    }

    public ar.edu.itba.pod.api.models.Flight.MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(ar.edu.itba.pod.api.models.Flight.MovementType movementType) {
        this.movementType = movementType;
    }

    public ar.edu.itba.pod.api.models.Flight.FlightClass getFlightClass() {
        return flightClass;
    }

    public void setFlightClass(ar.edu.itba.pod.api.models.Flight.FlightClass flightClass) {
        this.flightClass = flightClass;
    }

    public String getSrcOaci() {
        return srcOaci;
    }

    public void setSrcOaci(String srcOaci) {
        this.srcOaci = srcOaci;
    }

    public String getDestOaci() {
        return destOaci;
    }

    public void setDestOaci(String destOaci) {
        this.destOaci = destOaci;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Flight{" +
                "flightType=" + flightType.toString() +
                ", movementType=" + movementType.toString() +
                ", flightClass=" + flightClass.toString() +
                ", srcOaci='" + srcOaci + '\'' +
                ", destOaci='" + destOaci + '\'' +
                '}';
    }
}