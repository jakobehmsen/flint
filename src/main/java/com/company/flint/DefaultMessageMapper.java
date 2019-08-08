/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.flint;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author jakob
 */
public class DefaultMessageMapper implements MessageMapper<String, Character, Object, List<Object>, String, Number, Object> {
    private SymbolTable symbolTable;

    public DefaultMessageMapper(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }
    
    @Override
    public String createString(String str) {
        return str;
    }

    @Override
    public Character createChar(char ch) {
        return ch;
    }

    @Override
    public Object createSymbol(String str) {
        if(str.equals("false")) {
            return false;
        }
        
        return symbolTable.getSymbolCodeFromString(str);
    }

    @Override
    public List<Object> createList(List<Object> objects) {
        return objects;
    }

    @Override
    public String createError(String msg) {
        return msg;
    } 

    @Override
    public Number createNumber(String str) {
        return new BigDecimal(str);
    }
}
