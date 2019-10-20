package ar.edu.itba.pod.api.mappers;

import ar.edu.itba.pod.api.Movement;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

//Generalizar mappers
public class ReverseMovMapper implements Mapper<String, Integer, Integer, String> {
    private static final long serialVersionUID = -4478648180677155273L;

    @Override
    public void map(String code, Integer amount, Context<Integer, String> context) {

        if (amount >= 1000){
            Double outVal = (Math.floor(amount / 1000) * 1000);
            context.emit(outVal.intValue(), code);
        }

    }
}
