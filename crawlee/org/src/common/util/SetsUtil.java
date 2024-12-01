package org.src.common.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SetsUtil {
    private SetsUtil(){}

    public static <T> Set<T> difference(Set<T> one, Set<T> two){
        Set<T> diff = new HashSet<>();
        Iterator<T> walker = one.size() > two.size() ? one.iterator() : two.iterator();
        while(walker.hasNext()){
            T item = walker.next();
            if(one.contains(item) && two.contains(item)){
                continue;
            }
            diff.add(item);
        }
        return diff;

    }


}
