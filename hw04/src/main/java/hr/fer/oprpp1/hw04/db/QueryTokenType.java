package hr.fer.oprpp1.hw04.db;

/**
 * Token type enumeration holding all possible token types, used by the
 * QueryLexer and QueryParser for easier parsing.
 */
public enum QueryTokenType {
    QUERYKW, // TODO don't need this - need to refactor a few things to be able to remove it
    JMBAG,
    LASTNAME,
    FIRSTNAME,
    AND,
    STRING,
    LIKE, // LIKE
    COMPARISON_OPERATOR, // > < >= <= = !=
    EOF
}
