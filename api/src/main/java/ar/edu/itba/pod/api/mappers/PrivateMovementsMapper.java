package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.Movement;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class PrivateMovementsMapper implements Mapper<String, Movement, String, Boolean> {
    private static final long serialVersionUID = -4478648180677155273L;

    @Override
    public void map(String code, Movement movement, Context<String, Boolean> context) {
        context.emit(movement.getDestOaci(),
                movement.getFlightClass() == Movement.FlightClass.PRIVATE_EXT
                || movement.getFlightClass() == Movement.FlightClass.PRIVATE_LOCAL);

        context.emit(movement.getSrcOaci(),
                movement.getFlightClass() == Movement.FlightClass.PRIVATE_EXT
                || movement.getFlightClass() == Movement.FlightClass.PRIVATE_LOCAL);
    }
}
