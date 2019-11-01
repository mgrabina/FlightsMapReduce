package ar.edu.itba.pod.api.combiners;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

public class CabotageMovementsCombinerFactory implements CombinerFactory<String, Integer, Integer> {
    @Override
    public Combiner<Integer, Integer> newCombiner(String s) {
        return new Combiner<Integer, Integer>() {
            private int quantity = 0;

            @Override
            public void combine(Integer integer) {
                quantity++;
            }

            @Override
            public Integer finalizeChunk() {
                return quantity;
            }
        };
    }
}
