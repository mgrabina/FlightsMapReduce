package ar.edu.itba.pod.api.collators;

import com.hazelcast.mapreduce.Collator;
import com.sun.org.apache.xerces.internal.xs.StringList;
import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
import javafx.util.Pair;

import java.util.*;

public class GroupByAmountCollator implements Collator<Map.Entry<Integer, Set<String>>, Map<Integer, Set<Pair<String,String>>>> {
    @Override
    public Map<Integer, Set<Pair<String,String>>> collate( Iterable<Map.Entry<Integer, Set<String>>> values ) {
        Map<Integer, Set<Pair<String,String>>> res = new HashMap<>();

        for ( Map.Entry<Integer, Set<String>> entry : values ) {

            if (entry.getValue().size() > 1){

                res.put(entry.getKey(), new TreeSet<Pair<String, String>>(new Comparator<Pair<String, String>>() {
                    @Override
                    public int compare(Pair p1, Pair p2) {
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

                            Pair<String, String> pair;

                            if (s.compareTo(s2) <= 0){
                                pair  = new Pair<String, String>(s, s2);
                            } else {
                                pair = new Pair<String, String>(s2, s);
                            }

                            res.get(entry.getKey()).add(pair);
                        }
                    }
                }
            }
        }

        return res;
    }
}
