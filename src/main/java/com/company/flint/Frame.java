/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.flint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author jakob
 */
public class Frame {
    private MessageStream messageStream;
    //private Behavior behavior;
    private List<Behavior> behaviorList;
    private int ip;
    private Frame sender;
    
    private Stack<Object> stack = new Stack<>();

    public Frame(MessageStream messageStream, Behavior[] behaviorArray, Frame sender) {
        this.messageStream = messageStream;
        this.behaviorList = new ArrayList<>(Arrays.asList(behaviorArray));
        this.sender = sender;
    }
    
    public void evaluateNext(Evaluator evaluator) {
        behaviorList.get(ip).evaluateNext(evaluator, messageStream);
    }
    
    public void injectBehavior(Behavior behavior) {
        behaviorList.add(ip + 1, behavior);
    }
    
    public void incIp() {
        ip++;
    }
    
    public void respondWith(Evaluator evaluator) {
        Object r = pop();
        sender.push(r);
        evaluator.setFrame(sender);
        
        //continuationBehavior.evaluate(evaluator, messageStream, r);
    }
    
    public void push(Object obj) {
        stack.push(obj);
    }
    
    public Object pop() {
        return stack.pop();
    }
}
