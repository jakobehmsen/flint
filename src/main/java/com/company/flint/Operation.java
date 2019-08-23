/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.flint;

import java.util.Stack;

/**
 *
 * @author jakob
 */
public interface Operation {
    void perform(Stack<Operation> operationStack, Stack<Object> operandStack);
    
    public static class s {
        public static Operation sequence(Operation[] operations) {
            return sequence(operations, 0);
        }
        
        private static Operation sequence(Operation[] operations, int index) {
            return new Operation() {
                @Override
                public void perform(Stack<Operation> operationStack, Stack<Object> operandStack) {
                    if(index < operations.length - 1) {
                        Operation operationProceed = sequence(operations, index + 1);
                        operationStack.push(operationProceed);
                    }
                    
                    Operation operation = operations[index];
                    operationStack.push(operation);
                }
            };
        }
        
    }
}
