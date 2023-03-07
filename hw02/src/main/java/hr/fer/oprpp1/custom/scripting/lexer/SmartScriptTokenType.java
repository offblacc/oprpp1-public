package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * Token type enumeration fot the SmartScriptLexer and SmartScriptParser.
 */
public enum SmartScriptTokenType {
    BASIC, // - covers all text outside of tags
    TAG, // for, = or any other tag name
    BOUND, // - for {$ or $}
    TAG_STRING, // - string inside of tags
    EOF, // - END OF FILE
    END // - meaning the previous token was the last one
}