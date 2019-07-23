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
public class MessageStream {
    private int index;
    private List<Object> objects;

    public MessageStream(List<Object> objects) {
        this.objects = objects;
    }

    public Object peek() {
        return peek(0);
    }

    public Object peek(int offset) {
        return (index + offset) < objects.size() ? objects.get(index + offset) : null;
    }
    
    public boolean peekEquals(int offset, Object obj) {
        Object peek = peek(offset);
        if(peek != null) {
            return peek.equals(obj);
        }
        return false;
    }

    public Object consume() {
        Object obj = peek(0);
        index++;
        return obj;
    }
}
