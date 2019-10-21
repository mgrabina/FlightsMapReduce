package ar.edu.itba.pod.api.collators;

import com.hazelcast.mapreduce.Collator;

import java.util.*;

public class CabotageMovementsPerAirlineCollator implements Collator<Map.Entry<String, Double>, List<Map.Entry<String, Double>>> {

    private int limit;

    public CabotageMovementsPerAirlineCollator(int limit) {
        this.limit = limit;
    }

    @Override
    public List<Map.Entry<String, Double>> collate(Iterable<Map.Entry<String, Double>> iterable) {
        List<Map.Entry<String, Double>> list = new LinkedList<>();
        Map.Entry<String, Double> others = new AbstractMap.SimpleEntry<>("Otros", 0d);
        for (Map.Entry<String, Double> entry : iterable){
            if (entry.getKey().equals("N/A")){
                others.setValue(entry.getValue());
            } else {
                list.add(entry);
            }
        }
        list.sort((o1, o2) -> {
            if (!o1.getValue().equals(o2.getValue())){
                return o2.getValue().compareTo(o1.getValue());  //First, comparing by value - desc
            }
            return o1.getKey().compareTo(o2.getKey());          //Second, comparing by key alpha order asc
        });
        List<Map.Entry<String, Double>> ret = list.subList(0, limit);
        List<Map.Entry<String, Double>> othersEntriesList = list.subList(limit, list.size());
        othersEntriesList.forEach(entry -> others.setValue(others.getValue() + entry.getValue()));
        ret.add(others);
        return ret;
    }
}
