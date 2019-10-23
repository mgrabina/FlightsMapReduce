package ar.edu.itba.pod.server;

import com.hazelcast.config.Config;
import com.hazelcast.core.*;
import java.net.Inet4Address;

public class Server {
    public static void main(String[] args) {
        Config cfg = new Config();
        cfg.getGroupConfig().setName("g9").setPassword("g9");

        try {
            cfg.getNetworkConfig().getJoin().getTcpIpConfig().addMember(Inet4Address.getLocalHost().getHostAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }

        final HazelcastInstance hInstance = Hazelcast.newHazelcastInstance(cfg);
    }
}
