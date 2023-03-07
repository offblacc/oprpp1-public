package hr.fer.oprpp1.demo;

import hr.fer.oprpp1.hw02.prob1.Lexer;

public class escapeSequenceDemo {
    public static void main(String[] args) {
        Lexer lexer = new Lexer("   \\a    ");
        System.out.println(lexer.nextToken().getValue());
    }
    
}
