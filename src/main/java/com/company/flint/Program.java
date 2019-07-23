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
        //String src = "myVar means \"str\".\nmySecondVar means \"str2\". \n\"result\".\nfalse";
        //String src = "\"something\" implies \"1\" \"0\"";
        //String src = "false implies \"1\" \"0\"";
        //String src = "myVar means false implies \"1\" \"0\". myVar";
        //String src = "myVar means \"something\" implies \"1\" \"0\".\nmyVar";
        //String src = "\"1\"";
        
        Hashtable<Long, Object> locals = new Hashtable<>();
        
        MessageSourceStream messageSourceStream = new MessageSourceStream(System.in);
        
        while(true) {
            String src = messageSourceStream.nextMessageSource();
            
            if(src.equals("quit")) {
                break;
            }
            
            List<Object> message = parser.parse(src);
            //System.out.println(message);

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
            //System.out.println("isFalse=" + evaluator.isFalse(response));
            //System.out.println(symbolTable.toString(locals));
        }
        
        /*System.out.println(src);
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
        System.out.println(symbolTable.toString(locals));*/
    }
}
