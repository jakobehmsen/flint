/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.flint;

import java.util.List;

/**
 *
 * @author jakob
 */
public class DefaultMessageMapper implements MessageMapper<String, Character, String, List<Object>, String, Object> {
    @Override
    public String createString(String str) {
        return "\"" + str + "\"";
    }

    @Override
    public Character createChar(char ch) {
        return ch;
    }

    @Override
    public String createSymbol(String str) {
        return str;
    }

    @Override
    public List<Object> createList(List<Object> objects) {
        return objects;
    }

    @Override
    public String createError(String msg) {
        return msg;
    } 
}
