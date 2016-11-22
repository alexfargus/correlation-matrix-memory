/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cmm;

import java.util.EnumSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class TokenCollector implements Collector<Entry<Integer, Integer>, Map<Integer, Integer>, Token> {

    private final Integer length;

    public TokenCollector(Integer length) {
        this.length = length;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.noneOf(Characteristics.class);
    }

    @Override
    public Supplier<Map<Integer, Integer>> supplier() {
        return ConcurrentHashMap::new;
    }

    @Override
    public BiConsumer<Map<Integer, Integer>, Entry<Integer, Integer>> accumulator() {
        return (map, entry) -> map.merge(entry.getKey(), entry.getValue(), (v1, v2) -> v1 + v2);
    }

    @Override
    public BinaryOperator<Map<Integer, Integer>> combiner() {
        return this::mergeMaps;
    }

    private Map<Integer, Integer> mergeMaps(Map<Integer, Integer> a, Map<Integer, Integer> b){
        b.forEach((k, v) -> a.merge(k, v, Integer::sum));
        return a;
    }
                
    @Override
    public Function<Map<Integer, Integer>, Token> finisher() {
        return map -> Token.fromMap(length, map);
    }

}
