package helpers;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

class WordCountCombinerFactory implements CombinerFactory<String, Long, Long> {

    @Override
    public Combiner<Long, Long> newCombiner(String s) {
        return new WordCountCombiner();
    }
    class WordCountCombiner extends Combiner<Long, Long> {
        private long sum = 0;
        @Override
        public void combine( Long value ) {
            sum++;
        }
        @Override
        public Long finalizeChunk() {
            return sum;
        }

        @Override
        public void reset() {
            sum = 0;
        }
    }
}
