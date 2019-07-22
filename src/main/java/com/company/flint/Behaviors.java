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
        public void evaluateNext(Evaluator evaluator) {
            Long nameDot = evaluator.getSymbolTable().getSymbolCodeFromString(".");
            
            evaluator.eval(new Behavior[] {
                Behaviors.evalSentence1,
                Behaviors.tryConsumeAndJumpIfNotEquals(nameDot, 4),
                Behaviors.pop,
                Behaviors.jump(0),
                Behaviors.resp
            });
        }
    };
    
    public static Behavior evalSentence1 = new Behavior() {
        @Override
        public void evaluateNext(Evaluator evaluator) {
            Long nameImplies = evaluator.getSymbolTable().getSymbolCodeFromString("implies");
            
            evaluator.eval(new Behavior[] {
                Behaviors.evalSentence2,
                Behaviors.tryConsumeAndJumpIfNotEquals(nameImplies, 8),
                Behaviors.jumpIfFalse(6),
                Behaviors.evalSentence1, // Evaluate true block
                Behaviors.consume, // Ignore false block
                Behaviors.jump(8), // Jump to end
                Behaviors.consume, // Ignore true block
                Behaviors.evalSentence1, // Evaluate false block
                Behaviors.resp
            });
        }
    };
    
    public static Behavior jumpIfFalse(int index) {
        return new Behavior() {
            @Override
            public void evaluateNext(Evaluator evaluator) {
                Object obj = evaluator.getFrame().pop();
                if(evaluator.isFalse(obj)) {
                    evaluator.getFrame().setIp(index);
                } else {
                    evaluator.getFrame().incIp();
                }
            }
        };
    }
    
    public static Behavior consume = new Behavior() {
        @Override
        public void evaluateNext(Evaluator evaluator) {
            evaluator.getFrame().getMessageStream().consume();
            evaluator.getFrame().incIp();
        }
    };
    
    public static Behavior evalSentence2 = new Behavior() {
        @Override
        public void evaluateNext(Evaluator evaluator) {
            if(evaluator.getFrame().getMessageStream().peek() instanceof Long) {
                Long name = (Long) evaluator.getFrame().getMessageStream().consume();
                Long setToSymbolCode = evaluator.getSymbolTable().getSymbolCodeFromString("setTo");

                if(evaluator.getFrame().getMessageStream().peek().equals(setToSymbolCode)) {
                    evaluator.getFrame().getMessageStream().consume();

                    // symbol:id '=' value:expression
                    
                    // Always as expression
                    evaluator.eval(new Behavior[] {
                        Behaviors.evalSentence2,
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
                // Once the object finishes its consumption of the message 
                // stream, then optionals message chaining is performed.
                // Some special messages are performed after the message stream
                // is forwarded.
                // E.g. the '=>' ::? message is processed.
                
                // For now, simply respond with the object
                
                // Should be evaluated
                //evaluator.getFrame().push(evaluator.getFrame().getMessageStream().peek());
                Object obj = evaluator.getFrame().getMessageStream().consume();
                evaluator.getFrame().push(obj);

                evaluator.getFrame().incIp();
            }
        }
    };
    
    public static Behavior resp = new Behavior() {
        @Override
        public void evaluateNext(Evaluator evaluator) {
            evaluator.respondWith();
        }
    };
    
    public static Behavior stop = new Behavior() {
        @Override
        public void evaluateNext(Evaluator evaluator) {
            evaluator.stop();
        }
    };
    
    public static Behavior dup = new Behavior() {
        @Override
        public void evaluateNext(Evaluator evaluator) {
            evaluator.getFrame().dup();
            evaluator.getFrame().incIp();
        }
    };
    
    public static Behavior pop = new Behavior() {
        @Override
        public void evaluateNext(Evaluator evaluator) {
            evaluator.getFrame().pop();
            evaluator.getFrame().incIp();
        }
    };
    
    public static Behavior store(long name) {
        return new Behavior() {
            @Override
            public void evaluateNext(Evaluator evaluator) {
                evaluator.getFrame().store(name);
                evaluator.getFrame().incIp();
            }
        };
    }

    public static Behavior load(Long name) {
        return new Behavior() {
            @Override
            public void evaluateNext(Evaluator evaluator) {
                evaluator.getFrame().load(name);
                evaluator.getFrame().incIp();
            }
        };
    }

    private static Behavior tryConsumeAndJumpIfNotEquals(Object obj, int index) {
        return new Behavior() {
            @Override
            public void evaluateNext(Evaluator evaluator) {
                if(obj.equals(evaluator.getFrame().getMessageStream().peek())) {
                    evaluator.getFrame().getMessageStream().consume();
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
            public void evaluateNext(Evaluator evaluator) {
                evaluator.getFrame().setIp(index);
            }
        };
    }
}
