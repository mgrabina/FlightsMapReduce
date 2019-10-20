package ar.edu.itba.pod.api.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.util.*;

public class GroupByAmountReducer implements ReducerFactory<Integer, String, Set<String>> {

    @Override
    public Reducer<String, Set<String>> newReducer(Integer integer) {
        return new GroupByAmountRed();
    }

    private class GroupByAmountRed extends Reducer<String, Set<String>> {
        private Set<String> set;

        @Override
        public void beginReduce () {
            this.set = new HashSet<>();
        }
        @Override
        public void reduce(String oaci) {
            this.set.add(oaci);
        }
        @Override
        public Set<String> finalizeReduce() {
            return this.set;
        }
    }
}
