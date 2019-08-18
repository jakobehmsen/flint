/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.flint;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author jakob
 */
public class Frame {
    private MessageStream messageStream;
    private List<Behavior> behaviorList;
    private int ip;
    private Frame sender;
    private Map<Long, Object> locals;
    private State state;
    
    private Stack<Object> stack = new Stack<>();

    public Frame(MessageStream messageStream, Behavior[] behaviorArray, Frame sender, Map<Long, Object> locals) {
        this.messageStream = messageStream;
        this.behaviorList = new ArrayList<>(Arrays.asList(behaviorArray));
        this.sender = sender;
        this.locals = locals;
    }

    public Frame(MessageStream messageStream, State state, Frame sender, Map<Long, Object> locals) {
        this.messageStream = messageStream;
        this.state = state;
        this.sender = sender;
        this.locals = locals;
    }

    public MessageStream getMessageStream() {
        return messageStream;
    }
    
    public void evaluateNext(Evaluator evaluator) {
        behaviorList.get(ip).evaluateNext(evaluator);
    }
    
    public void evaluateNext2(Evaluator evaluator) {
        System.out.println(state);
        state = state.nextState(evaluator);
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
        sender.incIp();
    }
    
    public void push(Object obj) {
        stack.push(obj);
    }
    
    public Object pop() {
        return stack.pop();
    }

    public void store(long name) {
        Object obj = pop();
        locals.put(name, obj);
    }

    public void dup() {
        push(stack.peek());
    }

    public Frame newForEval(Behavior[] behaviors) {
        return new Frame(messageStream, behaviors, this, locals);
    }

    public Frame newForEval(State state) {
        return new Frame(messageStream, state, this, locals);
    }

    public void load(Long name) {
        push(locals.get(name));
    }

    public void setIp(int index) {
        ip = index;
    }

    public Behavior resolveObjectBehavior(Object rootObject) {
        if(rootObject instanceof Long) {
            return Behaviors.evalSentenceSymbol((Long)rootObject);
        } else if(rootObject instanceof BigDecimal) {
            return Behaviors.push((BigDecimal)rootObject);
        } else {
            return new Behavior() {
                @Override
                public void evaluateNext(Evaluator evaluator) {
                    evaluator.eval(new Behavior[] {
                        Behaviors.evalSentenceImplies,
                        Behaviors.resp
                    });
                }
            };
        }
    }
}
