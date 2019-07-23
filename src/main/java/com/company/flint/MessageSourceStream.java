/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.flint;

import java.io.InputStream;
import java.util.Scanner;

/**
 *
 * @author jakob
 */
public class MessageSourceStream {
    private Scanner scanner;
    
    public MessageSourceStream(InputStream inputStream) {
        scanner = new Scanner(inputStream);
        scanner.useDelimiter("!");
    }
    
    public String nextMessageSource() {
        return scanner.next();
    }
}
