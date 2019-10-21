package ar.edu.itba.pod.api.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class DestinyAiportPerSrcAirportReducer implements ReducerFactory<String, Integer, Integer> {
    private static final long serialVersionUID = -4213292631235182815L;


    @Override
    public Reducer<Integer, Integer> newReducer(String s) {
        return new Reducer<Integer, Integer>() {
            private int quantity;

            @Override
            public void beginReduce() {
                quantity = 0;
            }

            @Override
            public void reduce(Integer integer) {
                quantity += integer;
            }

            @Override
            public Integer finalizeReduce() {
                return quantity;
            }
        };
    }
}
