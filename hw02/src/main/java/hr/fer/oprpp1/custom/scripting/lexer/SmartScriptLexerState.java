package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * States for the SmartScriptLexer. Basic state is reading text outside tags,
 * tag
 * state is reading text inside tags.
 */
public enum SmartScriptLexerState {
    BASIC,
    TAG
}
