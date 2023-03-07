package hr.fer.oprpp1.demo;

public class TestLexerManually {
    public static void main(String[] args) {
        String docBody = "A tag follows {$= \"Joe \\\"Long\\\" Smith\"$}.";

        var lexer = new hr.fer.oprpp1.custom.scripting.lexer.SmartScriptLexer(docBody);
        while (lexer.nextToken().getType() != hr.fer.oprpp1.custom.scripting.lexer.SmartScriptTokenType.EOF) {
            System.out.println(lexer.getToken().getValue());
        }
    }
}
