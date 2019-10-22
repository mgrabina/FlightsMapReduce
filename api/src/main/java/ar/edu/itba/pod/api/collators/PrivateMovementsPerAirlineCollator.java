package ar.edu.itba.pod.api.collators;

import ar.edu.itba.pod.api.Airport;
import com.hazelcast.core.ReplicatedMap;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

public class PrivateMovementsPerAirlineCollator implements Collator<Map.Entry<String, Double>, List<Map.Entry<String, Double>>> {

    private int limit;
    Map<String, Airport> airportsMap;

    public PrivateMovementsPerAirlineCollator(int limit, Map<String, Airport> airportsMap) {
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
        list.sort((o1, o2) -> {
            if (!o1.getValue().equals(o2.getValue())){
                return o1.getValue().compareTo(o2.getValue());  //First, comparing by value - asc
            }
            return o1.getKey().compareTo(o2.getKey());          //Second, comparing by key alpha order asc
        });
        return list.subList(0, limit);
    }
}
