package ar.edu.itba.pod.api;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Movement implements Serializable {

    private FlightType flightType;
    private MovementType movementType;
    private FlightClass flightClass;
    private String srcOaci;
    private String destOaci;


    public static enum FlightType {
        CABOTAGE("Cabotaje"),
        INTERNATIONAL("Internacional"),
        NA("N/A");

        String value;

        @Override
        public String toString() {
            return value;
        }

        private final static Map<String, FlightType> valuesByName = Arrays.stream(FlightType.values())
                .collect(Collectors.toMap(model -> model.value, Function.identity()));

        public static FlightType fromString(String value){
            return FlightType.valuesByName.get(value);
        }

        FlightType(String val) {
            this.value = val;
        }
    }

    public static enum MovementType {
        TAKEOFF("Despegue"),
        LANDING("Aterrizaje");

        String value;

        @Override
        public String toString() {
            return value;
        }

        private final static Map<String, MovementType> valuesByName = Arrays.stream(MovementType.values())
                .collect(Collectors.toMap(model -> model.value, Function.identity()));

        public static MovementType fromString(String value){
            return MovementType.valuesByName.get(value);
        }

        MovementType(String val) {
            this.value = val;
        }
    }

    public static enum FlightClass {
        NONREGULAR("No Regular"),
        REGULAR("Regular"),
        PRIVATE_EXT("Vuelo Privado con Matrícula Extranjera"),
        PRIVATE_LOCAL("Vuelo Privado con Matrícula Nacional");

        String value;

        @Override
        public String toString() {
            return value;
        }

        private final static Map<String, FlightClass> valuesByName = Arrays.stream(FlightClass.values())
                .collect(Collectors.toMap(model -> model.value, Function.identity()));

        public static FlightClass fromString(String value){
            return FlightClass.valuesByName.get(value);
        }

        FlightClass(String val) {
            this.value = val;
        }
    }

    public Movement(Movement.FlightType flightType, Movement.MovementType movementType, Movement.FlightClass flightClass, String srcOaci, String destOaci) {
        this.flightType = flightType;
        this.movementType = movementType;
        this.flightClass = flightClass;
        this.srcOaci = srcOaci;
        this.destOaci = destOaci;
    }

    public Movement.FlightType getFlightType() {
        return flightType;
    }

    public void setFlightType(Movement.FlightType flightType) {
        this.flightType = flightType;
    }

    public Movement.MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(Movement.MovementType movementType) {
        this.movementType = movementType;
    }

    public Movement.FlightClass getFlightClass() {
        return flightClass;
    }

    public void setFlightClass(Movement.FlightClass flightClass) {
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
        return "ar.edu.itba.pod.api.Movement{" +
                "flightType=" + flightType.toString() +
                ", movementType=" + movementType.toString() +
                ", flightClass=" + flightClass.toString() +
                ", srcOaci='" + srcOaci + '\'' +
                ", destOaci='" + destOaci + '\'' +
                '}';
    }
}