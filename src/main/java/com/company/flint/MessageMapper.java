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
public interface MessageMapper<S extends O, C extends O, B extends O, L extends O, E extends O, N extends O, O> {
    S createString(String str);
    N createNumber(String str);
    C createChar(char ch);
    B createSymbol(String str);
    L createList(List<O> objects);
    E createError(String msg);
}
