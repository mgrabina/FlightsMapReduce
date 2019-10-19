package ar.edu.itba.pod.api.predicators;

import com.hazelcast.mapreduce.KeyPredicate;

public class WordCountKeyPredicate implements KeyPredicate<String> {
    @Override
    public boolean evaluate( String s ) {
        return s != null && s.toLowerCase().contains( "hazelcast" );
    }
}
