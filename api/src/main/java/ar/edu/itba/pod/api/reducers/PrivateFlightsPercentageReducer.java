package ar.edu.itba.pod.api.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class PrivateFlightsPercentageReducer implements ReducerFactory<String, Boolean, Double> {
    private static final long serialVersionUID = -4213292631235182815L;


    public PrivateFlightsPercentageReducer() {
    }

    @Override
    public Reducer<Boolean, Double> newReducer(final String code) {
        return new Reducer<Boolean, Double>() {
            private int privateMovements;
            private int total;

            @Override
            public void beginReduce() {
                privateMovements = 0;
                total = 0;
            }

            @Override
            public void reduce(Boolean isPrivate) {
                if (isPrivate){
                    privateMovements++;
                }
                total++;
            }

            @Override
            public Double finalizeReduce() {
                return (double) privateMovements / (double)total;
            }
        };
    }
}
