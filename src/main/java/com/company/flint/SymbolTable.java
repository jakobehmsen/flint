/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.flint;

import java.util.Hashtable;
import java.util.stream.Collectors;

/**
 *
 * @author jakob
 */
public class SymbolTable {
    private Hashtable<String, Long> stringToCodeMap = new Hashtable<>();
    private Hashtable<Long, String> codeToStringMap = new Hashtable<>();
    
    public long getSymbolCodeFromString(String str) {
        return stringToCodeMap.computeIfAbsent(str, s -> {
            long sc = (long)stringToCodeMap.size();
            codeToStringMap.put(sc, str);
            return sc;
        });
    }
    
    public String getStringFromSymbolCode(long sc) {
        return codeToStringMap.get(sc);
    }

    public String toString(Hashtable<Long, Object> map) {
        return map.entrySet().stream()
                .collect(Collectors.toMap(e -> getStringFromSymbolCode(e.getKey()), e -> e.getValue()))
                .toString();
    }
}
