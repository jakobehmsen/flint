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
        String src = "myVar setTo \"str\".\nmySecondVar setTo \"str2\". \n\"result\".\nfalse";
        
        System.out.println(src);
        List<Object> message = parser.parse(src);
        System.out.println(message);
        
        Hashtable<Long, Object> locals = new Hashtable<>();
        MessageStream messageStream = new MessageStream(message);
        Evaluator evaluator = new Evaluator(symbolTable);
        Behavior[] behaviorArray = new Behavior[] {
            Behaviors.evalParagraph,
            Behaviors.stop
        };
        Frame frame = new Frame(messageStream, behaviorArray, null, locals);
        evaluator.evaluate(frame);
        
        Object response = frame.pop();
        System.out.println("=>");
        System.out.println(response);
        System.out.println("isFalse=" + evaluator.isFalse(response));
        System.out.println(symbolTable.toString(locals));
    }
}
