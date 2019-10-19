package ar.edu.itba.pod.server;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.*;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.net.Inet4Address;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Server {
    public static void main(String[] args) {

        // TODO Chekear esto, creo que no deberia hacer nada el server

        Config cfg = new Config();
        cfg.getGroupConfig().setName("lity").setPassword("lito123");

        try {
            cfg.getNetworkConfig().getJoin().getTcpIpConfig().addMember(Inet4Address.getLocalHost().getHostAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }

        final HazelcastInstance hInstance = Hazelcast.newHazelcastInstance(cfg);
    }
}
