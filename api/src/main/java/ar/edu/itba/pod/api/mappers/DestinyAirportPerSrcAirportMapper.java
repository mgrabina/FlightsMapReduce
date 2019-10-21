package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.Movement;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class DestinyAirportPerSrcAirportMapper implements Mapper<String, Movement, String, Integer> {
    private static final long serialVersionUID = -4478648180677155273L;

    private final String srcOaci;

    public DestinyAirportPerSrcAirportMapper(String srcOaci) {
        this.srcOaci = srcOaci;
    }

    @Override
    public void map(String code, Movement movement, Context<String, Integer> context) {
        if (movement.getMovementType() == Movement.MovementType.TAKEOFF
            && movement.getSrcOaci().equals(srcOaci)){
            context.emit(movement.getDestOaci(), 1);
        }
    }
}
