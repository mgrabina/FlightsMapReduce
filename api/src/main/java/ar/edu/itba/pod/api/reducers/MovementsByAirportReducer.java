package ar.edu.itba.pod.api.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class MovementsByAirportReducer implements ReducerFactory<String, Integer, Integer> {
    private static final long serialVersionUID = -4213292631235182815L;

    @Override
    public Reducer<Integer, Integer> newReducer(final String oaci) {
        return new Reducer<Integer, Integer>() {
            private int count;

            @Override
            public void beginReduce() {
                count = 0;
            }

            @Override
            public void reduce(Integer integer) {
                count += integer;
            }

            @Override
            public Integer finalizeReduce() {
                return count;
            }
        };
    }
}
