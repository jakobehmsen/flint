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
public interface State {
    State nextState(Evaluator evaluator);
    
    public interface Continuation {
        State nextState(Evaluator evaluator, Object result);
    }
    
    public interface Callable {
        State invoke(Evaluator evaluator, Continuation continuation);
    }
}
