package ar.edu.itba.pod.server;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.*;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Server {
    public static void main(String[] args) {


        final ClientConfig config = new ClientConfig();
        final HazelcastInstance hz = HazelcastClient.newHazelcastClient(config);
        JobTracker jobTracker = hz.getJobTracker("word-count");

        final IMap<String, String> map = hz.getMap("libros");
        final KeyValueSource<String, String> sourceFromMap = KeyValueSource.fromMap(map);
        final IList<String> list = hz.getList("my-list");
        final KeyValueSource<String, String> sourceFromList = KeyValueSource.fromList(list);

        final Job<String, String> job = jobTracker.newJob( sourceFromMap );
        final ICompletableFuture<Long> future = job
                .keyPredicate(new WordCountKeyPredicate())
                .mapper( new TokenizerMapper() )
                .combiner( new WordCountCombinerFactory() )
                .reducer( new WordCountReducerFactory() )
                .submit(new WordCountCollator());

        // Wait and retrieve the result
        try {
            final Long result = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Job<String, String> job2 = jobTracker.newJob( sourceFromList );
        ICompletableFuture<Map<String, Long>> future2 = job2
                .mapper( new TokenizerMapper() )
                .reducer( new WordCountReducerFactory() )
                .submit();
        // Attach a callback listener
        future2.andThen( buildCallback() );
        // Wait and retrieve the result
        try {
            Map<String, Long> result = future2.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static ExecutionCallback<Map<String, Long>> buildCallback() {
        return new ExecutionCallback<Map<String, Long>>() {
            @Override
            public void onResponse(Map<String, Long> stringLongMap) {
                System.out.println("Response");
            }

            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("Failure");
            }
        };
    }
}
