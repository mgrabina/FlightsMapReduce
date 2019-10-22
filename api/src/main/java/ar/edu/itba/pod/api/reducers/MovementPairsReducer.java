package ar.edu.itba.pod.api.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.*;

public class MovementPairsReducer implements ReducerFactory<String, Integer, Integer> {

    @Override
    public Reducer<Integer, Integer> newReducer(String str) {
        return new MovementPairRed();
    }

    private class MovementPairRed extends Reducer<Integer, Integer> {
        private volatile Integer sum;

        @Override
        public void beginReduce () {
            this.sum = 0;
        }
        @Override
        public void reduce(Integer value) {
            this.sum+=value;
        }
        @Override
        public Integer finalizeReduce() {
            return this.sum;
        }
    }
}
