package ar.edu.itba.pod.api.combiners;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

public class CountCombinerFactory<T, S> implements CombinerFactory<T, S, Integer> {
    private static final long serialVersionUID = 410003611279376181L;

    @Override
    public Combiner<S, Integer> newCombiner(T key ) {
        return new CountCombiner();
    }

    private class CountCombiner extends Combiner<S, Integer>{
        private Integer sum = 0;

        @Override
        public void combine(S value) {
            this.sum++;
        }

        @Override
        public Integer finalizeChunk() {
            return this.sum;
        }

        @Override
        public void reset() {
            this.sum = 0;
        }
    }
}