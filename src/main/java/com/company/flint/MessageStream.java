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
        return index < objects.size() ? objects.get(index) : null;
    }

    public Object consume() {
        Object obj = peek();
        index++;
        return obj;
    }
    
    /*public void evaluateNext(Evaluator evaluator, Behavior behavior) {
        evaluator.evaluate(behavior, this);
    }*/
}
