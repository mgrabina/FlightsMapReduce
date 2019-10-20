package ar.edu.itba.pod.api.collators;

import ar.edu.itba.pod.api.Airport;
import com.hazelcast.core.ReplicatedMap;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

public class PrivateMovementsPerAirlineCollator implements Collator<Map.Entry<String, Double>, List<Map.Entry<String, Double>>> {

    private int limit;
    ReplicatedMap<String, Airport> airportsMap;

    public PrivateMovementsPerAirlineCollator(int limit, ReplicatedMap<String, Airport> airportsMap) {
        this.limit = limit;
        this.airportsMap = airportsMap;
    }

    @Override
    public List<Map.Entry<String, Double>> collate(Iterable<Map.Entry<String, Double>> iterable) {
        List<Map.Entry<String, Double>> list = new LinkedList<>();
        iterable.forEach(entry -> {
            if (airportsMap.containsKey(entry.getKey())){
                list.add(entry);
            }
        });
        list.sort(Map.Entry.comparingByValue());
        return list.subList(0, limit);
    }
}
