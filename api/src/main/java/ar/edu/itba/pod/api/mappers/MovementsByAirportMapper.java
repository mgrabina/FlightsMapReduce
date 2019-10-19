package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.Movement;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class MovementsByAirportMapper implements Mapper<String, Movement, String, Integer> {
    private static final long serialVersionUID = -4478648180677155273L;

    @Override
    public void map(String code, Movement movement, Context<String, Integer> context) {
        if(movement.getMovementType() == Movement.MovementType.TAKEOFF) {
            context.emit(movement.getSrcOaci(), 1);
        } else if (movement.getMovementType() == Movement.MovementType.LANDING) {
            context.emit(movement.getDestOaci(), 1);
        }
    }
}
