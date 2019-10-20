package ar.edu.itba.pod.api.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

public class CabotagePercentageReducer implements ReducerFactory<String, Boolean, Double> {
    private static final long serialVersionUID = -4213292631235182815L;

    private int total;

    public CabotagePercentageReducer() {
        total = 0;
    }

    @Override
    public Reducer<Boolean, Double> newReducer(final String airline) {
        return new Reducer<Boolean, Double>() {
            private int cabotage;

            @Override
            public void beginReduce() {
                cabotage = 0;
            }

            @Override
            public void reduce(Boolean isCabotage) {
                if (isCabotage){
                    cabotage++;
                    total++;
                }
            }

            @Override
            public Double finalizeReduce() {
                return (double) cabotage / (double)total;
            }
        };
    }
}
