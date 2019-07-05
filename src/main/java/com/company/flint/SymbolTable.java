/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.flint;

import java.util.Hashtable;

/**
 *
 * @author jakob
 */
public class SymbolTable {
    private Hashtable<String, Long> stringToCodeMap = new Hashtable<>();
    
    public long getSymbolCodeFromString(String str) {
        return stringToCodeMap.computeIfAbsent(str, s -> (long)stringToCodeMap.size());
    }
}
