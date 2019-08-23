/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.flint;

import java.util.List;
import java.util.Map;

/**
 *
 * @author jakob
 */
public class MessageStream {
    private int index;
    private List<Object> objects;

    public MessageStream(List<Object> objects) {
        this.objects = objects;
    }

    public Object peek() {
        return peek(0);
    }

    public Object peek(int offset) {
        return (index + offset) < objects.size() ? objects.get(index + offset) : null;
    }
    
    public boolean peekEquals(int offset, Object obj) {
        Object peek = peek(offset);
        if(peek != null) {
            return peek.equals(obj);
        }
        return false;
    }

    public Object consume() {
        Object obj = peek(0);
        index++;
        return obj;
    }

    public boolean hasMore() {
        return index < objects.size();
    }
    
    public Object exprs(Map<Long, Object> locals, SymbolTable symbolTable, MessageStream messageStream) {
        Object value = null;
        
        while(true) {
            value = expr(locals, symbolTable, messageStream);
            if(peekEquals(0, symbolTable.getSymbolCodeFromString("."))) {
                consume();
                continue;
            }
            break;
        }
        
        return value;
    }
    
    public Object expr(Map<Long, Object> locals, SymbolTable symbolTable, MessageStream messageStream) {
        if(peek(0) instanceof Long && peekEquals(1, symbolTable.getSymbolCodeFromString("<-"))) {
            Long name = (Long)consume();
            consume();
            
            Object value = expr(locals, symbolTable, messageStream);
            locals.put(name, value);
            
            return value;
        } else {
            return expr1(locals, symbolTable, messageStream);
        }
    }
    
    public Object expr1(Map<Long, Object> locals, SymbolTable symbolTable, MessageStream messageStream) {
        Object value = expr2(locals, symbolTable, messageStream);
        
        if(peekEquals(0, symbolTable.getSymbolCodeFromString("=>"))) {
            consume();
            
            if(Boolean.TRUE.equals(value)) {
                value = expr(locals, symbolTable, messageStream);
                consume();
            } else {
                consume();
                value = expr(locals, symbolTable, messageStream);
            }
        }
        
        return value;
    }
    
    public Object expr2(Map<Long, Object> locals, SymbolTable symbolTable, MessageStream messageStream) {
        Object value = expr3(locals, symbolTable, messageStream);
        
        if(hasMore() && !peekEquals(0, symbolTable.getSymbolCodeFromString("."))) {
            return passTo(locals, symbolTable, messageStream, value);
        }
        
        return value;
    }
    
    public Object expr3(Map<Long, Object> locals, SymbolTable symbolTable, MessageStream messageStream) {
        if(peekEquals(0, symbolTable.getSymbolCodeFromString("looksAt"))) {
            consume();
            Object symbol = consume();
            Object obj = messageStream.consume();
            
            return obj.equals(symbol);
        } else if(peekEquals(0, symbolTable.getSymbolCodeFromString("to"))) {
            consume();
            List<Object> behavior = (List<Object>) consume();
            
            return new To(behavior);
        } else if(peek(0) instanceof Long) {
            Long name = (Long) consume();
            return locals.get(name);
        } else {
            return consume();
        }
    }

    public Object passTo(Map<Long, Object> locals, SymbolTable symbolTable, MessageStream messageStream, Object value) {
        if(value instanceof To) {
            MessageStream toBehavior = new MessageStream(((To)value).behavior);
            return toBehavior.expr(locals, symbolTable, this);
        } else {
            return value;
        }
    }
}
