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
public class States {
    public static State finish = new State() {
        @Override
        public State nextState(Evaluator evaluator) {
            evaluator.stop();
            return this;
        }

        @Override
        public String toString() {
            return "finish";
        }
    };
    
    public static State invoke(State.Callable c, State.Continuation continuation) {
        return new State() {
            @Override
            public State nextState(Evaluator evaluator) {
                return c.invoke(evaluator, continuation);
            }

            @Override
            public String toString() {
                return "invoke " + c + " for " + continuation;
            }
        };
    }
    
    public static State continueWith(Object result, State.Continuation continuation) {
        return new State() {
            @Override
            public State nextState(Evaluator evaluator) {
                return continuation.nextState(evaluator, result);
            }

            @Override
            public String toString() {
                return "continue-with " + (result != null ? result + " for " : "") + continuation;
            }
        };
    }
    
    public static State.Continuation always(State state) {
        return new State.Continuation() {
            @Override
            public State nextState(Evaluator evaluator, Object result) {
                return state;
            }

            @Override
            public String toString() {
                return "" + state;
            }
        };
    }
    
    public static State.Callable respond(State.Callable expr) {
        return new State.Callable() {
            @Override
            public State invoke(Evaluator evaluator, State.Continuation continuation) {
                return States.invoke(expr, new State.Continuation() {
                    @Override
                    public State nextState(Evaluator evaluator, Object result) {
                        evaluator.getFrame().push(result);
                        evaluator.getFrame().respondWith(evaluator);
                        return continueWith(result, continuation);
                    }
                });
            }
        };
    }
    
    public static State.Callable push(State.Callable expr) {
        return new State.Callable() {
            @Override
            public State invoke(Evaluator evaluator, State.Continuation continuation) {
                return States.invoke(expr, new State.Continuation() {
                    @Override
                    public State nextState(Evaluator evaluator, Object result) {
                        evaluator.getFrame().push(result);
                        return continueWith(null, continuation);
                    }

                    @Override
                    public String toString() {
                        return "push";
                    }
                });
            }

            @Override
            public String toString() {
                return "push " + expr;
            }
        };
    }
    
    public static State.Callable exprs() {
        return null;
    }
    
    public static State.Callable expr() {
        return new State.Callable() {
            @Override
            public State invoke(Evaluator evaluator, State.Continuation continuation) {
                if(evaluator.getFrame().getMessageStream().peek() instanceof Long) {
                    if(evaluator.getFrame().getMessageStream().peekEquals(1, evaluator.getSymbolTable().getSymbolCodeFromString("<-"))) {
                        Long name = (Long) evaluator.getFrame().getMessageStream().consume();
                        evaluator.getFrame().getMessageStream().consume();
                        
                        return States.invoke(expr(), new State.Continuation() {
                            @Override
                            public State nextState(Evaluator evaluator, Object result) {
                                evaluator.getFrame().push(result);
                                evaluator.getFrame().store(name);
                                return continueWith(result, continuation);
                            }

                            @Override
                            public String toString() {
                                return "expr";
                            }
                        });
                    }
                }
                
                return States.invoke(expr1(), continuation);
            }

            @Override
            public String toString() {
                return "expr";
            }
        };
    }
    
    public static State.Callable expr1() {
        return new State.Callable() {
            @Override
            public State invoke(Evaluator evaluator, State.Continuation continuation) {
                Object obj = evaluator.getFrame().getMessageStream().consume();
                return continueWith(obj, continuation);
            }

            @Override
            public String toString() {
                return "expr1";
            }
        };
    }
    
    public static State.Callable consume() {
        return new State.Callable() {
            @Override
            public State invoke(Evaluator evaluator, State.Continuation continuation) {
                Object obj = evaluator.getFrame().getMessageStream().consume();
                return continueWith(obj, continuation);
            }

            @Override
            public String toString() {
                return "consume";
            }
        };
    }
}
