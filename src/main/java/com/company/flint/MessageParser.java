/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.flint;

import java.util.Arrays;
import java.util.List;
import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Parser.Reference;
import org.codehaus.jparsec.Parsers;
import org.codehaus.jparsec.Scanners;
import static org.codehaus.jparsec.Scanners.isChar;
import org.codehaus.jparsec.Terminals;
import org.codehaus.jparsec.error.ParserException;
import org.codehaus.jparsec.pattern.CharPredicate;
import org.codehaus.jparsec.pattern.CharPredicates;

/**
 *
 * @author jakob
 */
public class MessageParser<S extends O, C extends O, B extends O, L extends O, E extends O, O> {
    private final Parser<Void> WHITESPACE = Scanners.isChar(CharPredicates.IS_WHITESPACE).skipMany();
    private final Parser<Void> COMMENT = Scanners.lineComment(";");
    private final Parser<Void> SKIP = Parsers.or(WHITESPACE, COMMENT);

    private final Parser<S> STRING;
    
    private final Parser<C> CHAR;

    private final CharPredicate SYMBOL_CONSTITUENT = CharPredicates.or(
            CharPredicates.IS_LETTER, CharPredicates.among("0123456789!$%&*+-./:<=>?@[]^_{}~"));
    private final Parser<B> SYMBOL;

    private final Reference<O> LIST = Parser.newReference();

    private final Parser<O> TERM;
    
    private final MessageMapper<S, C, B, L, E, O> compiler;

    public MessageParser(MessageMapper<S, C, B, L, E, O> compiler) {
        this.compiler = compiler;
        
        STRING = Terminals.StringLiteral.DOUBLE_QUOTE_TOKENIZER.map(str -> this.compiler.createString(str));
        CHAR = Scanners.string("#\\").next(Scanners.ANY_CHAR).source().map(s -> this.compiler.createChar(s.charAt(2)));
        SYMBOL = Parsers.or(
            Scanners.isChar(SYMBOL_CONSTITUENT).source(),
            Scanners.isChar(SYMBOL_CONSTITUENT).or(WHITESPACE).many().source().between(isChar('|'), isChar('|')))
            .many1().map(xs -> this.compiler.createSymbol(String.join("", xs)));
        TERM = Parsers.or(STRING, CHAR, SYMBOL, LIST.lazy());
        
        
        LIST.set(TERM.between(SKIP, SKIP).many()
                .map(objects -> (O)compiler.createList(objects))
                .between(isChar('('), isChar(')')));
    }

    public List<O> parse(String input) {
        try {
            return TERM.between(SKIP, SKIP).many().parse(input);
        } catch (ParserException e) {
            return Arrays.asList(compiler.createError(e.getMessage()));
        }
    }
}
