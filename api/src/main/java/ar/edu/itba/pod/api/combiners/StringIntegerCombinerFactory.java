package ar.edu.itba.pod.api.combiners;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class StringIntegerCombinerFactory implements CombinerFactory<String, Integer, Integer> {
    @Override
    public Combiner<Integer, Integer> newCombiner(String s) {
        return new Combiner<Integer, Integer>() {
            private AtomicInteger quantity;

            @Override
            public void beginCombine() {
                quantity = new AtomicInteger(0);
            }

            @Override
            public void reset() {
                quantity = new AtomicInteger(0);
            }

            @Override
            public void combine(Integer integer) {
                quantity.set(quantity.get() + integer);
            }

            @Override
            public Integer finalizeChunk() {
                return quantity.get();
            }
        };
    }
}
