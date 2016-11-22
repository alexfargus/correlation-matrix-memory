/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cmm;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author alex
 */
public class Token extends TreeMap<Integer, Integer> {
    
    private final Integer length;

    private Token(Integer length) {
        this.length = length;
    }
    
    public static Token emptyToken(Integer length) {
        return new Token(length);
    }
    
    public static Token fromMap(Integer length, Map<Integer, Integer> map) {
        final Token token = new Token(length);
        token.putAll(map);
        return token;
    }

    public Integer getLength() {
        return length;
    }
    
    public Integer getCardinality() {
        return this.size();
    }
    
    public Integer getWeight() {
        return this.values()
                .stream()
                .reduce(0, Integer::sum);
    }
}
