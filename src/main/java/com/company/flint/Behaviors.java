/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.flint;

/**
 *
 * @author jakob
 */
public class Behaviors {
    public static Behavior evalFromMessageStream = new Behavior() {
        @Override
        public void evaluateNext(Evaluator evaluator, MessageStream messageStream) {
            if(messageStream.peek() instanceof Long) {
                Long name = (Long) messageStream.consume();
                Long setToSymbolCode = evaluator.getSymbolTable().getSymbolCodeFromString("setTo");

                if(messageStream.peek().equals(setToSymbolCode)) {
                    messageStream.consume();

                    // symbol:id '=' value:expression

                    // Always as expression
                    evaluator.eval(new Behavior[] {
                        Behaviors.evalFromMessageStream,
                        Behaviors.dup,
                        Behaviors.store(name),
                        Behaviors.resp
                    });
                } else {
                    // Always as expression
                    evaluator.eval(new Behavior[] {
                        Behaviors.load(name),
                        Behaviors.resp
                    });
                }
            } else {
                // Should look up the behavior of the object, and pass along
                // the message stream for the object
                
                // For now, simply respond with the object
                
                // Should be evaluated
                //evaluator.getFrame().push(messageStream.peek());
                evaluator.getFrame().push(messageStream.consume());

                evaluator.getFrame().incIp();
            }
        }
    };
    
    public static Behavior resp = new Behavior() {
        @Override
        public void evaluateNext(Evaluator evaluator, MessageStream messageStream) {
            evaluator.respondWith();
        }
    };
    
    public static Behavior stop = new Behavior() {
        @Override
        public void evaluateNext(Evaluator evaluator, MessageStream messageStream) {
            evaluator.stop();
        }
    };
    
    public static Behavior dup = new Behavior() {
        @Override
        public void evaluateNext(Evaluator evaluator, MessageStream messageStream) {
            evaluator.getFrame().dup();
            evaluator.getFrame().incIp();
        }
    };
    
    public static Behavior store(long name) {
        return new Behavior() {
            @Override
            public void evaluateNext(Evaluator evaluator, MessageStream messageStream) {
                evaluator.getFrame().store(name);
                evaluator.getFrame().incIp();
            }
        };
    }

    public static Behavior load(Long name) {
        return new Behavior() {
            @Override
            public void evaluateNext(Evaluator evaluator, MessageStream messageStream) {
                evaluator.getFrame().load(name);
                evaluator.getFrame().incIp();
            }
        };
    }
}
