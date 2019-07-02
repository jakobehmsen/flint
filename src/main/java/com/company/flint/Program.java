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
public class Program {
    public static void main(String[] args) {
        MessageParser parser = new MessageParser(new DefaultMessageMapper());
        Object message = parser.parse("3");
        System.out.println(message);
    }
}
