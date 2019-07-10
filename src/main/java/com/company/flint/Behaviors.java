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
    public static Behavior evalParagraph = new Behavior() {
        @Override
        public void evaluateNext(Evaluator evaluator, MessageStream messageStream) {
            Long nameDot = evaluator.getSymbolTable().getSymbolCodeFromString(".");
            
            evaluator.eval(new Behavior[] {
                Behaviors.evalSentence,
                Behaviors.tryConsumeAndJumpIfNotEquals(nameDot, 4),
                Behaviors.pop,
                Behaviors.jump(0),
                Behaviors.resp
            });
        }
    };
    
    public static Behavior evalSentence = new Behavior() {
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
                        Behaviors.evalSentence,
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
    
    public static Behavior pop = new Behavior() {
        @Override
        public void evaluateNext(Evaluator evaluator, MessageStream messageStream) {
            evaluator.getFrame().pop();
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

    private static Behavior tryConsumeAndJumpIfNotEquals(Object obj, int index) {
        return new Behavior() {
            @Override
            public void evaluateNext(Evaluator evaluator, MessageStream messageStream) {
                if(obj.equals(messageStream.peek())) {
                    messageStream.consume();
                    evaluator.getFrame().incIp();
                } else {
                    evaluator.getFrame().setIp(index);
                }
            }
        };
    }

    public static Behavior jump(int index) {
        return new Behavior() {
            @Override
            public void evaluateNext(Evaluator evaluator, MessageStream messageStream) {
                evaluator.getFrame().setIp(index);
            }
        };
    }
}
