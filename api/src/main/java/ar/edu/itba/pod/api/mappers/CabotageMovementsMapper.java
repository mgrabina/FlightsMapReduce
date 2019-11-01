package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.Movement;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class CabotageMovementsMapper implements Mapper<String, Movement, String, Integer> {
    private static final long serialVersionUID = -4478648180677155273L;

    @Override
    public void map(String code, Movement movement, Context<String, Integer> context) {
            context.emit(movement.getAirline(), 1);
    }
}
