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
public class Evaluator {
    private SymbolTable symbolTable;
    private boolean running;
    private Frame frame;

    public Evaluator(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }
    
    public void evaluate(Frame frame) {
        this.frame = frame;
        
        running = true;
        
        while(running) {
            this.frame.evaluateNext(this);
        }
    }
    
    public void evaluate2(Frame frame) {
        this.frame = frame;
        
        running = true;
        
        while(running) {
            this.frame.evaluateNext2(this);
        }
    }
    
    public void eval(Behavior[] behaviors) {
        Frame evalFrame = getFrame().newForEval(behaviors);
        setFrame(evalFrame);
    }
    
    public void stop() {
        running = false;
    }

    public void respondWith() {
        frame.respondWith(this);
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public Frame getFrame() {
        return frame;
    }
    
    public boolean isFalse(Object obj) {
        return Boolean.FALSE.equals(obj);
    }
}
