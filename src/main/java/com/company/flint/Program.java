/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.flint;

import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author jakob
 */
public class Program {
    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable();
        MessageParser parser = new MessageParser(new DefaultMessageMapper(symbolTable));
        String src = "myVar setTo \"str\"";
        List<Object> message = parser.parse(src);
        System.out.println(message);
        
        Hashtable<Long, Object> locals = new Hashtable<>();
        MessageStream messageStream = new MessageStream(message);
        Evaluator evaluator = new Evaluator(symbolTable);
        Behavior[] behaviorArray = new Behavior[] {
            Behaviors.evalFromMessageStream,
            Behaviors.stop
        };
        Frame frame = new Frame(messageStream, behaviorArray, null, locals);
        evaluator.evaluate(frame);
        
        System.out.println(src);
        System.out.println("=>");
        System.out.println(frame.pop());
        System.out.println(symbolTable.toString(locals));
    }
}
