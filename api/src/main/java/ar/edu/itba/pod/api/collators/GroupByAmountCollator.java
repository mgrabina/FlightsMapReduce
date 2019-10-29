package ar.edu.itba.pod.api.collators;

import com.hazelcast.mapreduce.Collator;

import java.util.*;

public class GroupByAmountCollator implements Collator<Map.Entry<Integer, Set<String>>, Map<Integer, Set<Map.Entry<String,String>>>> {
    @Override
    public Map<Integer, Set<Map.Entry<String,String>>> collate(Iterable<Map.Entry<Integer, Set<String>>> values ) {
        Map<Integer, Set<Map.Entry<String,String>>> res = new HashMap<>();

        for ( Map.Entry<Integer, Set<String>> entry : values ) {

            if (entry.getValue().size() > 1){

                res.put(entry.getKey(), new TreeSet<Map.Entry<String, String>>(new Comparator<Map.Entry<String, String>>() {
                    @Override
                    public int compare(Map.Entry p1, Map.Entry p2) {
                        String p1String = p1.getKey().toString();
                        String p2String = p2.getKey().toString();

                        if (p1String.compareTo(p2String) != 0){
                            return p1String.compareTo(p2String);
                        } else {
                            return p1.getValue().toString().compareTo(p2.getValue().toString());
                        }

                    }
                }));

                //res.put(entry.getKey(), new HashSet<>());

                for (String s : entry.getValue()){
                    for (String s2 : entry.getValue()){

                        if (!s.equals(s2)) {

                            Map.Entry<String, String> Pair;

                            if (s.compareTo(s2) <= 0){
                                Pair  = new AbstractMap.SimpleEntry<>(s, s2);
                            } else {
                                Pair = new AbstractMap.SimpleEntry<>(s2, s);
                            }

                            res.get(entry.getKey()).add(Pair);
                        }
                    }
                }
            }
        }

        return res;
    }
}
