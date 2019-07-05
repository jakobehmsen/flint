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
public class ContextBehavior implements Behavior {
    private Hashtable<Long, Object> map;

    public ContextBehavior() {
        this.map = new Hashtable<>();
    }
    
    public Behavior evaluateNext = new Behavior() {
        @Override
        public void evaluateNext(Evaluator evaluator, MessageStream messageStream) {
            Object obj = messageStream.consume();
            
            if(obj instanceof String) {
                evaluator.getFrame().push(obj);
                evaluator.getFrame().incIp();
            } else {
                // Create new frame
            }
        }
    };
    
    public Behavior handleSetTo = new Behavior() {
        @Override
        public void evaluateNext(Evaluator evaluator, MessageStream messageStream) {
            Object r = evaluator.getFrame().pop();
            Long name = (Long) evaluator.getFrame().pop();
            map.put(name, r);
            evaluator.getFrame().push(r);
            
            evaluator.getFrame().incIp();
        }
    };
    
    @Override
    public void evaluateNext(Evaluator evaluator, MessageStream messageStream) {
        if(messageStream.peek() instanceof Long) {
            Long name = (Long) messageStream.consume();
            Long setToSymbolCode = evaluator.getSymbolTable().getSymbolCodeFromString("setTo");
            
            if(messageStream.peek().equals(setToSymbolCode)) {
                messageStream.consume();
            
                // Perhaps, the context should be pushed onto the stack?
                evaluator.getFrame().push(name);
                evaluator.getFrame().injectBehavior(handleSetTo);
                evaluator.getFrame().injectBehavior(evaluateNext);
                
                evaluator.getFrame().incIp();
                
                //evaluator.getFrame().setBehavior(handleSetTo);
                //messageStream.evaluateNext(evaluator, this);
            } else {
                Object obj = map.get(name);
                evaluator.getFrame().push(obj);
                
                evaluator.getFrame().incIp();
            }
        } else {
            // Should be evaluated
            //evaluator.getFrame().push(messageStream.peek());
            evaluator.getFrame().injectBehavior(evaluateNext);
                
            evaluator.getFrame().incIp();
        }
    }
}
