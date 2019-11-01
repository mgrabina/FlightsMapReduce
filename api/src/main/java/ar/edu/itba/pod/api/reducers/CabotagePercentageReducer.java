package ar.edu.itba.pod.api.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class CabotagePercentageReducer implements ReducerFactory<String, Integer, Double> {
    private static final long serialVersionUID = -4213292631235182815L;

    private AtomicInteger total;

    public CabotagePercentageReducer() {
        total = new AtomicInteger(0);
    }

    @Override
    public Reducer<Integer, Double> newReducer(final String airline) {
        return new Reducer<Integer, Double>() {
            private AtomicInteger cabotage;

            @Override
            public void beginReduce() {
                cabotage = new AtomicInteger(0);
            }

            @Override
            public void reduce(Integer cabotages) {
                cabotage.set(cabotage.get() + cabotages);
                total.set(total.get() + cabotages);
            }

            @Override
            public Double finalizeReduce() {
                return (double) cabotage.get() / (double)total.get();
            }
        };
    }
}
