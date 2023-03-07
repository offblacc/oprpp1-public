package hr.fer.oprpp1.hw04.db;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;


public class QueryLexerTest {
    @Test
    public void testQuery() {
        String q = "query jmbag = \"0000000003\"";
        QueryLexer lexer = new QueryLexer(q);
        
        QueryToken token = lexer.nextToken();
        token = lexer.nextToken();
        
        assertEquals("jmbag", token.getValue());
        assertEquals(QueryTokenType.JMBAG, token.getType());

        token = lexer.nextToken();
        assertEquals("=", token.getValue());
        assertEquals(QueryTokenType.COMPARISON_OPERATOR, token.getType());

        token = lexer.nextToken();
        assertEquals("0000000003", token.getValue());
        assertEquals(QueryTokenType.STRING, token.getType());

        token = lexer.nextToken();
        assertEquals(null, token.getValue());
        assertEquals(QueryTokenType.EOF, token.getType());
    }

    @Test
    public void testQuery2() {
        String q = " query jmbag = \"0000000003\" AND lastName LIKE \"B*\"";
        QueryLexer lexer = new QueryLexer(q);

        QueryToken token = lexer.nextToken();
        token = lexer.nextToken();

       
        assertEquals("jmbag", token.getValue());
        assertEquals(QueryTokenType.JMBAG, token.getType());

        token = lexer.nextToken();
        assertEquals("=", token.getValue());
        assertEquals(QueryTokenType.COMPARISON_OPERATOR, token.getType());

        token = lexer.nextToken();
        assertEquals("0000000003", token.getValue());
        assertEquals(QueryTokenType.STRING, token.getType());

        token = lexer.nextToken();
        assertEquals("AND", token.getValue());
        assertEquals(QueryTokenType.AND, token.getType());

        token = lexer.nextToken();
        assertEquals("lastName", token.getValue());
        assertEquals(QueryTokenType.LASTNAME, token.getType());

        token = lexer.nextToken();
        assertEquals("LIKE", token.getValue());
        assertEquals(QueryTokenType.LIKE, token.getType());

        token = lexer.nextToken();
        assertEquals("B*", token.getValue());
        assertEquals(QueryTokenType.STRING, token.getType());

        token = lexer.nextToken();
        assertEquals(null, token.getValue());
        assertEquals(QueryTokenType.EOF, token.getType());

        assertThrows(NoSuchElementException.class, () -> lexer.nextToken());
    }

    @Test
    public void testQuery3() {
        String q = "query jmbag = \"0000000003\" AND lastName LIKE \"L*\"";
        QueryLexer lexer = new QueryLexer(q);

        QueryToken token = lexer.nextToken();
        token = lexer.nextToken();

       
        assertEquals("jmbag", token.getValue());
        assertEquals(QueryTokenType.JMBAG, token.getType());

        token = lexer.nextToken();
        assertEquals("=", token.getValue());
        assertEquals(QueryTokenType.COMPARISON_OPERATOR, token.getType());

        token = lexer.nextToken();
        assertEquals("0000000003", token.getValue());
        assertEquals(QueryTokenType.STRING, token.getType());

        token = lexer.nextToken();
        assertEquals("AND", token.getValue());
        assertEquals(QueryTokenType.AND, token.getType());

        token = lexer.nextToken();
        assertEquals("lastName", token.getValue());
        assertEquals(QueryTokenType.LASTNAME, token.getType());

        token = lexer.nextToken();
        assertEquals("LIKE", token.getValue());
        assertEquals(QueryTokenType.LIKE, token.getType());

        token = lexer.nextToken();
        assertEquals("L*", token.getValue());
        assertEquals(QueryTokenType.STRING, token.getType());

        token = lexer.nextToken();
        assertEquals(null, token.getValue());
        assertEquals(QueryTokenType.EOF, token.getType());

        assertThrows(NoSuchElementException.class, () -> lexer.nextToken());
    }

    @Test
    public void testQuery4() {
        String q = "query lastName LIKE \"Be*\"";
        QueryLexer lexer = new QueryLexer(q);

        QueryToken token = lexer.nextToken();
        token = lexer.nextToken();

       
        assertEquals("lastName", token.getValue());
        assertEquals(QueryTokenType.LASTNAME, token.getType());

        token = lexer.nextToken();
        assertEquals("LIKE", token.getValue());
        assertEquals(QueryTokenType.LIKE, token.getType());

        token = lexer.nextToken();
        assertEquals("Be*", token.getValue());
        assertEquals(QueryTokenType.STRING, token.getType());

        token = lexer.nextToken();
        assertEquals(null, token.getValue());
        assertEquals(QueryTokenType.EOF, token.getType());
        
        assertThrows(NoSuchElementException.class, () -> lexer.nextToken());
    }

    @Test
    public void testLessThan() {
        String q = "query jmbag <\"0000000003\"";
        QueryLexer lexer = new QueryLexer(q);

        QueryToken token = lexer.nextToken();
        token = lexer.nextToken();

        assertEquals("jmbag", token.getValue());
        assertEquals(QueryTokenType.JMBAG, token.getType());

        token = lexer.nextToken();
        assertEquals("<", token.getValue());
        assertEquals(QueryTokenType.COMPARISON_OPERATOR, token.getType());

        token = lexer.nextToken();
        assertEquals("0000000003", token.getValue());
        assertEquals(QueryTokenType.STRING, token.getType());

        token = lexer.nextToken();
        assertEquals(null, token.getValue());
        assertEquals(QueryTokenType.EOF, token.getType());
        
        assertThrows(NoSuchElementException.class, () -> lexer.nextToken());
    }

    @Test
    public void testLessOrEqual() {
        String q = "query jmbag <=\"0000000003\"";
        QueryLexer lexer = new QueryLexer(q);

        QueryToken token = lexer.nextToken();
        token = lexer.nextToken();

        assertEquals("jmbag", token.getValue());
        assertEquals(QueryTokenType.JMBAG, token.getType());

        token = lexer.nextToken();
        assertEquals("<=", token.getValue());
        assertEquals(QueryTokenType.COMPARISON_OPERATOR, token.getType());

        token = lexer.nextToken();
        assertEquals("0000000003", token.getValue());
        assertEquals(QueryTokenType.STRING, token.getType());

        token = lexer.nextToken();
        assertEquals(null, token.getValue());
        assertEquals(QueryTokenType.EOF, token.getType());
        
        assertThrows(NoSuchElementException.class, () -> lexer.nextToken());
    }

    @Test
    public void testGreaterThan() {
        String q = "query jmbag >\"0000000003\"";
        QueryLexer lexer = new QueryLexer(q);

        QueryToken token = lexer.nextToken();
        token = lexer.nextToken();

        
        assertEquals("jmbag", token.getValue());
        assertEquals(QueryTokenType.JMBAG, token.getType());

        token = lexer.nextToken();
        assertEquals(">", token.getValue());
        assertEquals(QueryTokenType.COMPARISON_OPERATOR, token.getType());

        token = lexer.nextToken();
        assertEquals("0000000003", token.getValue());
        assertEquals(QueryTokenType.STRING, token.getType());

        token = lexer.nextToken();
        assertEquals(null, token.getValue());
        assertEquals(QueryTokenType.EOF, token.getType());
        
        assertThrows(NoSuchElementException.class, () -> lexer.nextToken());
    }

    @Test
    public void testGreaterOrEqual() {
        String q = "query jmbag >=\"0000000003\"";
        QueryLexer lexer = new QueryLexer(q);

        QueryToken token = lexer.nextToken();
        token = lexer.nextToken();

        
        assertEquals("jmbag", token.getValue());
        assertEquals(QueryTokenType.JMBAG, token.getType());

        token = lexer.nextToken();
        assertEquals(">=", token.getValue());
        assertEquals(QueryTokenType.COMPARISON_OPERATOR, token.getType());

        token = lexer.nextToken();
        assertEquals("0000000003", token.getValue());
        assertEquals(QueryTokenType.STRING, token.getType());

        token = lexer.nextToken();
        assertEquals(null, token.getValue());
        assertEquals(QueryTokenType.EOF, token.getType());
        
        assertThrows(NoSuchElementException.class, () -> lexer.nextToken());
    }

    @Test
    public void testEqual() {
        String q = "query jmbag =\"0000000003\"";
        QueryLexer lexer = new QueryLexer(q);

        QueryToken token = lexer.nextToken();
        token = lexer.nextToken();

      
        assertEquals("jmbag", token.getValue());
        assertEquals(QueryTokenType.JMBAG, token.getType());

        token = lexer.nextToken();
        assertEquals("=", token.getValue());
        assertEquals(QueryTokenType.COMPARISON_OPERATOR, token.getType());

        token = lexer.nextToken();
        assertEquals("0000000003", token.getValue());
        assertEquals(QueryTokenType.STRING, token.getType());

        token = lexer.nextToken();
        assertEquals(null, token.getValue());
        assertEquals(QueryTokenType.EOF, token.getType());
        
        assertThrows(NoSuchElementException.class, () -> lexer.nextToken());
    }

    @Test
    public void testNotEqual() {
        String q = "query jmbag !=\"0000000003\"";
        QueryLexer lexer = new QueryLexer(q);

        QueryToken token = lexer.nextToken();
        token = lexer.nextToken();

       
        assertEquals("jmbag", token.getValue());
        assertEquals(QueryTokenType.JMBAG, token.getType());

        token = lexer.nextToken();
        assertEquals("!=", token.getValue());
        assertEquals(QueryTokenType.COMPARISON_OPERATOR, token.getType());

        token = lexer.nextToken();
        assertEquals("0000000003", token.getValue());
        assertEquals(QueryTokenType.STRING, token.getType());

        token = lexer.nextToken();
        assertEquals(null, token.getValue());
        assertEquals(QueryTokenType.EOF, token.getType());
        
        assertThrows(NoSuchElementException.class, () -> lexer.nextToken());
    }
}
