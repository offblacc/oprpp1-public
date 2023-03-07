package hr.fer.oprpp1.custom.scripting.lexer;

import hr.fer.oprpp1.custom.scripting.elems.*;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;

public class SmartScriptLexer {
    /**
     * Data that is being processed. Constructor takes a string and converts it to a
     * char array called data.
     */
    private final char[] data;
    /**
     * Current position in the data array we're processing.
     */
    private int currentIndex;
    /**
     * The last generated token. When a new one is generated, before returning it
     * this is where it's saved, as it proves useful in the next call.
     */
    private SmartScriptToken token;
    /**
     * Current state of the lexer.
     */
    private SmartScriptLexerState state;
    /**
     * Used to flag numbers represented as a string to not be parsed as numbers.
     * Also, if there is a string inside a tag, this can help us determine if it's a
     * string or a variable.
     */
    private boolean isString = false;

    public SmartScriptLexer(String text) {
        if (text == null) {
            throw new SmartScriptParserException("Text cannot be null.");
        }
        data = text.toCharArray();
        currentIndex = 0; // first unread char
        token = null;
        state = SmartScriptLexerState.BASIC;
    }

    /**
     * Returns the current state.
     * 
     * @return current state
     */
    public Object getState() {
        return state;
    }

    /**
     * Returns the current token.
     * 
     * @return current token
     */
    public SmartScriptToken getToken() {
        return token;
    }

    /**
     * Returns the next token. This method only has primitive functionality,
     * delegating the actual token generation to appropriate methods.
     * 
     * @return - the next token.
     * @throws SmartScriptParserException - if there is an error in the input.
     */
    public SmartScriptToken nextToken() {
        isString = false;
        if (token != null && token.getType() == SmartScriptTokenType.EOF) { // if last generated token was type EOF
            throw new SmartScriptParserException("No more tokens available.");
        }
        if (currentIndex >= data.length) {
            token = new SmartScriptToken(null, SmartScriptTokenType.EOF);
            return token;
        }

        if (state == SmartScriptLexerState.BASIC) {
            return nextTokenBasic();
        } else {
            return nextTokenTag();
        }
    }

    /**
     * Generates the next token in the BASIC state. If a tag is encountered, the
     * state is changed to TAG.
     * 
     * @return - the next token
     * @throws SmartScriptParserException - if there is an error in the input.
     */
    private SmartScriptToken nextTokenBasic() {
        StringBuilder sb = new StringBuilder();
        if ((data[currentIndex] == '{')) {
            if (data[currentIndex + 1] == '$') {
                state = SmartScriptLexerState.TAG;
                currentIndex += 2;
                token = new SmartScriptToken(new ElementString("{$"), SmartScriptTokenType.BOUND);
                return token;
            }
        }

        while (currentIndex < data.length) {
            if (data[currentIndex] == '\\') {
                if (currentIndex + 1 == data.length) {
                    throw new SmartScriptParserException("Invalid escape sequence.");
                }
                if (data[currentIndex + 1] == '{' || data[currentIndex + 1] == '\\') {
                    sb.append(data[currentIndex + 1]);
                    currentIndex += 2;
                } else {
                    throw new SmartScriptParserException("Invalid escape sequence.");
                }
            } else {
                if (data[currentIndex] == '{') {
                    if (currentIndex + 1 == data.length) {
                        sb.append(data[currentIndex]);
                        currentIndex++;
                        token = new SmartScriptToken(new ElementString(sb.toString()), SmartScriptTokenType.BASIC);
                        return token;
                    } else if (data[currentIndex + 1] == '$') {
                        token = new SmartScriptToken(new ElementString(sb.toString()), SmartScriptTokenType.BASIC);
                        return token;
                    }
                }
                sb.append(data[currentIndex]);
                currentIndex++;
            }
        }
        token = new SmartScriptToken(new ElementString(sb.toString()), SmartScriptTokenType.BASIC);
        return token;
    }

    /**
     * Generates the next token in the TAG state. If the closing brackets ($}) are
     * encountered, the state is changed to BASIC.
     * 
     * @return - the next token
     */
    private SmartScriptToken nextTokenTag() {
        String word = readNextToken();
        if (word.equals("$}")) {
            state = SmartScriptLexerState.BASIC;
            token = new SmartScriptToken(new ElementString("$}"), SmartScriptTokenType.BOUND);
            return token;
        }

        if ((Character.isDigit(word.charAt(0)) || word.charAt(0) == '-') && !isString) {
            return parsePotentialNumber(word);
        } else {
            if (token.getType() == SmartScriptTokenType.BOUND) {
                if (word.equalsIgnoreCase("END")) {
                    token = new SmartScriptToken(new ElementString("END"), SmartScriptTokenType.END);
                } else {
                    token = new SmartScriptToken(new ElementString(word), SmartScriptTokenType.TAG);
                }
            } else {
                if (word.charAt(0) == '@') {
                    token = new SmartScriptToken(new ElementFunction(word), SmartScriptTokenType.BASIC);
                } else if (word.length() == 1 && (new String("+-*/^").indexOf(word.charAt(0)) != -1)) {
                    token = new SmartScriptToken(new ElementOperator(word), SmartScriptTokenType.BASIC);
                } else if (isString) {
                    token = new SmartScriptToken(new ElementString(word, true), SmartScriptTokenType.TAG_STRING);
                } else {
                    token = new SmartScriptToken(new ElementVariable(word), SmartScriptTokenType.BASIC);
                }
            }
        }
        return token;

    }

    /**
     * Parses a potential number. If the number is an integer, it is parsed as such.
     * If it is a double, it is parsed as such. This method is called if there's a
     * word starting with a number and it determines which type of number it is, if
     * it is a number, and returns the appropriate token. If it is not a number, it
     * is a string. In that case parser will check if it is legal.
     * 
     * @param word - the word to be parsed
     * @return - the next token
     */
    private SmartScriptToken parsePotentialNumber(String word) {
        int dotCount = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == '.') {
                dotCount++;
            }
        }
        if (dotCount == 1) {
            double d = Double.parseDouble(word);
            token = new SmartScriptToken(new ElementConstantDouble(d), SmartScriptTokenType.BASIC);
            return token;
        } else if (dotCount == 0) {
            try {
                int i = Integer.parseInt(word);
                token = new SmartScriptToken(new ElementConstantInteger(i), SmartScriptTokenType.BASIC);
                return token;
            } catch (NumberFormatException ex) {
                token = new SmartScriptToken(new ElementOperator(word), SmartScriptTokenType.TAG_STRING);
                return token;
            }
        } else {
            token = new SmartScriptToken(new ElementString(word), SmartScriptTokenType.BASIC);
            return token;
        }
    }

    /**
     * Reads the next word inside the tag and returns it as a string. Any state
     * change or type determining is left to the nextTokenTag method.
     * 
     * @return - the next word in the tag
     */
    private String readNextToken() {
        while (currentIndex < data.length && Character.isWhitespace(data[currentIndex])) {
            currentIndex++;
        }

        if (currentIndex + 1 < data.length) {
            if (data[currentIndex] == '$' && data[currentIndex + 1] == '}') {
                currentIndex += 2;
                return "$}";
            }
        }

        if (data[currentIndex] == '=') {
            currentIndex++;
            return "=";
        }

        if ("+*/^".indexOf(data[currentIndex]) != -1
                || (data[currentIndex] == '-' && data[currentIndex + 1] == ' ')) {
            return Character.toString(data[currentIndex++]);
        }
        StringBuilder sb = new StringBuilder();

        while (currentIndex < data.length) {
            if (data[currentIndex] == '@') {
                while (currentIndex < data.length && !Character.isWhitespace(data[currentIndex])
                        && data[currentIndex] != '"') {
                    if (data[currentIndex] == '$' && currentIndex + 1 < data.length && data[currentIndex + 1] == '}') {
                        break;
                    }
                    sb.append(data[currentIndex++]);
                }
                return sb.toString();
            } else if (data[currentIndex] == '"') {
                isString = true;
                currentIndex++;
                while (data[currentIndex] != '"') {
                    if (data[currentIndex] == '\\') {
                        // ----- escape seq inside string ----------
                        if (currentIndex < data.length - 1) {
                            if (data[currentIndex + 1] == '\\' || data[currentIndex + 1] == '"') {
                                sb.append(data[currentIndex + 1]);
                                currentIndex += 2;
                            } else if (data[currentIndex + 1] == 'n') {
                                sb.append('\n');
                                currentIndex += 2;
                            } else if (data[currentIndex + 1] == 'r'){
                                sb.append('\r');
                                currentIndex += 2;
                            } else if (data[currentIndex + 1] == 't') {
                                sb.append('\t');
                                currentIndex += 2;
                            } else {
                                throw new SmartScriptParserException("Invalid escape sequence" + data[currentIndex + 1]);
                            }
                        }
                        else {
                            throw new SmartScriptParserException("Invalid escape sequence " + data[currentIndex + 1]);
                        }
                        // -----------------------------------------
                    } else {
                        sb.append(data[currentIndex++]);
                    }
                }
                currentIndex++;
                return sb.toString();
            } else if (data[currentIndex] == '\\') {
                throw new SmartScriptParserException("Illegal character.");
            } else {
                char first = data[currentIndex];
                if (first == '$' && currentIndex + 1 < data.length && data[currentIndex + 1] == '}') {
                    return sb.toString();
                }
                if (Character.isDigit(first) || first == '-') {
                    sb.append(data[currentIndex++]);
                    while (currentIndex < data.length) {
                        if (Character.isDigit(data[currentIndex]) || data[currentIndex] == '.') {
                            sb.append(data[currentIndex++]);
                        } else {
                            break;
                        }
                    }
                }
                if (Character.isLetter(first)) {
                    while (currentIndex < data.length) {
                        if (Character.isWhitespace(data[currentIndex]) || (data[currentIndex] == '$'
                                && currentIndex + 1 < data.length && data[currentIndex + 1] == '}' || data[currentIndex] == '-' || data[currentIndex] == '"')) {
                            return sb.toString();
                        }
                        sb.append(data[currentIndex++]);
                    }
                }
                return sb.toString();
            }
        }
        throw new SmartScriptParserException("Unknown parser error. Try checking your input.");
    }
}
