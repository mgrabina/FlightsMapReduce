package helpers;

import com.hazelcast.mapreduce.Collator;

import java.util.Map;

public class WordCountCollator implements Collator<Map.Entry<String, Long>, Long> {
    @Override
    public Long collate( Iterable<Map.Entry<String, Long>> values ) {
        long sum = 0;
        for ( Map.Entry<String, Long> entry : values ) {
            sum += entry.getValue();
        }
        return sum;
    }
}
