package ar.edu.itba.pod.api.mappers;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

public class IdentityMap2 implements Mapper<String, Integer, String, Integer> {
    private static final long serialVersionUID = -4478648180677155273L;

    @Override
    public void map(String code, Integer value, Context<String, Integer> context) {

        String realCode = code.split("/")[0];
        String[] oacis = realCode.split("_");

        if (!oacis[0].equals(oacis[1])){
            context.emit(realCode, value);
        }
    }
}
