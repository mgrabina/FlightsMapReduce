package ar.edu.itba.pod.api.collators;

import ar.edu.itba.pod.api.Airport;
import com.hazelcast.core.ReplicatedMap;
import com.hazelcast.mapreduce.Collator;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MovementPairsCollator implements Collator<Map.Entry<String, Integer>, List<Map.Entry<String, Integer>>> {

    private final int limit;
    private final Map<String, Airport> airportsMap;

    public MovementPairsCollator(int limit, Map<String, Airport> airportsMap) {
        this.limit = limit;
        this.airportsMap = airportsMap;
    }

    @Override
    public List<Map.Entry<String, Integer>> collate(Iterable<Map.Entry<String, Integer>> iterable) {

        List<Map.Entry<String, Integer>> list = new LinkedList<>();

        iterable.forEach(entry -> {
            if (entry.getValue() >= limit){
                list.add(entry);
            }
        });

        list.sort((o1, o2) -> {
            if (!o1.getValue().equals(o2.getValue())){
                return o2.getValue().compareTo(o1.getValue());  //First, comparing by value - desc
            }
            return o1.getKey().compareTo(o2.getKey());          //Second, comparing by key alpha order asc
        });

        return list;
    }
}
