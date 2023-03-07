package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import static org.junit.jupiter.api.Assertions.*;

public class ComparisonOperatorsTest {
    @Test
    public void testLess() {
        assertTrue(ComparisonOperators.LESS.satisfied("Ana", "Jasna"));
        assertFalse(ComparisonOperators.LESS.satisfied("Jasna", "Ana"));
        assertFalse(ComparisonOperators.LESS.satisfied("Jasna", "Jasna"));
    }

    @Test
    public void testLessOrEquals() {
        assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied("Ana", "Jasna"));
        assertFalse(ComparisonOperators.LESS_OR_EQUALS.satisfied("Jasna", "Ana"));
        assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied("Jasna", "Jasna"));
    }

    @Test
    public void testGreater() {
        assertFalse(ComparisonOperators.GREATER.satisfied("Ana", "Jasna"));
        assertTrue(ComparisonOperators.GREATER.satisfied("Jasna", "Ana"));
        assertFalse(ComparisonOperators.GREATER.satisfied("Jasna", "Jasna"));
    }

    @Test
    public void testGreaterOrEquals() {
        assertFalse(ComparisonOperators.GREATER_OR_EQUALS.satisfied("Ana", "Jasna"));
        assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied("Jasna", "Ana"));
        assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied("Jasna", "Jasna"));
    }

    @Test
    public void testEquals() {
        assertFalse(ComparisonOperators.EQUALS.satisfied("Ana", "Jasna"));
        assertFalse(ComparisonOperators.EQUALS.satisfied("Jasna", "Ana"));
        assertTrue(ComparisonOperators.EQUALS.satisfied("Jasna", "Jasna"));
    }

    @Test
    public void testNotEquals() {
        assertTrue(ComparisonOperators.NOT_EQUALS.satisfied("Ana", "Jasna"));
        assertTrue(ComparisonOperators.NOT_EQUALS.satisfied("Jasna", "Ana"));
        assertFalse(ComparisonOperators.NOT_EQUALS.satisfied("Jasna", "Jasna"));
    }

    @Test
    public void testLike() {
        assertTrue(ComparisonOperators.LIKE.satisfied("Zagreb", "Zagreb"));
        assertTrue(ComparisonOperators.LIKE.satisfied("AAA", "A*"));
        assertTrue(ComparisonOperators.LIKE.satisfied("AAAA", "A*"));
        assertTrue(ComparisonOperators.LIKE.satisfied("AAAA", "*A"));
        assertTrue(ComparisonOperators.LIKE.satisfied("AAAA", "A*A"));
        assertTrue(ComparisonOperators.LIKE.satisfied("AAAA", "AA*A"));
        assertTrue(ComparisonOperators.LIKE.satisfied("AAAA", "A*AA"));
        assertTrue(ComparisonOperators.LIKE.satisfied("AAAA", "AA*AA"));
        assertThrows(IllegalArgumentException.class, () -> ComparisonOperators.LIKE.satisfied("AAAA", "A*A*A"));
        assertFalse(ComparisonOperators.LIKE.satisfied("AAAA", "AAA*AA"));
        assertFalse(ComparisonOperators.LIKE.satisfied("AAAA", "AA*AAA"));
        assertTrue(ComparisonOperators.LIKE.satisfied("AAAA", "AAAA*"));
        assertTrue(ComparisonOperators.LIKE.satisfied("AAAA", "*AAAA"));
        assertFalse(ComparisonOperators.LIKE.satisfied("AAA", "AA*AA"));
        assertTrue(ComparisonOperators.LIKE.satisfied("AAAA", "*"));
        assertTrue(ComparisonOperators.LIKE.satisfied("Jambrović", "jamb*"));
    }
}
