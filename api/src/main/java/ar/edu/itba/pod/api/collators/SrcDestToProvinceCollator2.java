package ar.edu.itba.pod.api.collators;

import ar.edu.itba.pod.api.Airport;
import com.hazelcast.core.ReplicatedMap;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

public class SrcDestToProvinceCollator2 implements Collator<Map.Entry<String, Integer>, List<Map.Entry<String, Integer>>> {

    private final Map<String, Airport> airportsMap;

    public SrcDestToProvinceCollator2(Map<String, Airport> airportsMap) {
        this.airportsMap = airportsMap;
    }

    @Override
    public List<Map.Entry<String, Integer>> collate(Iterable<Map.Entry<String, Integer>> iterable) {

        List<Map.Entry<String, Integer>> list = new LinkedList<>();

        iterable.forEach(entry -> {

            String srcOaci = entry.getKey().split("_")[0];
            String destOaci = entry.getKey().split("_")[1];

            if (airportsMap.containsKey(srcOaci) && airportsMap.containsKey(destOaci)){
                Airport srcAirport = this.airportsMap.get(srcOaci);
                Airport destAirport = this.airportsMap.get(destOaci);

                String srcProv = srcAirport.getProvince();
                String destProv = destAirport.getProvince();

                if (srcProv.compareTo(destProv) < 0){
                    Map.Entry<String, Integer> e = new AbstractMap.SimpleEntry<>(srcProv + "_" + destProv, entry.getValue());
                    list.add(e);
                } else {
                    Map.Entry<String, Integer> e = new AbstractMap.SimpleEntry<>(destProv + "_" + srcProv, entry.getValue());
                    list.add(e);
                }

            }
        });

        return list;
    }
}
