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
public class Program {
    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable();
        MessageParser parser = new MessageParser(new DefaultMessageMapper(symbolTable));
        List<Object> message = parser.parse("myVar setTo \"str\"");
        System.out.println(message);
        
        MessageStream messageStream = new MessageStream(message);
        Evaluator evaluator = new Evaluator(symbolTable);
        Behavior[] behaviorArray = new Behavior[] {
            new Behavior() {
                @Override
                public void evaluateNext(Evaluator evaluator, MessageStream messageStream) {
                    evaluator.stop();
                }
            }
        };
        Frame frame = new Frame(null, behaviorArray, null);
        Behavior[] behaviorArrayInner = new Behavior[] {
            new ContextBehavior(),
            new Behavior() {
                @Override
                public void evaluateNext(Evaluator evaluator, MessageStream messageStream) {
                    evaluator.respondWith();
                }
            }
        };
        Frame innerFrame = new Frame(messageStream, behaviorArrayInner, frame);
        evaluator.evaluate(innerFrame);
        
        System.out.println(frame.pop());
    }
}
