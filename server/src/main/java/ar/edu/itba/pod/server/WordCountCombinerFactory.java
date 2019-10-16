package ar.edu.itba.pod.server;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

class WordCountCombinerFactory extends CombinerFactory<String, Long, Long> {

    @Override
    public Combiner<Long, Long> newCombiner(String s) {
        return null;
    }
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
