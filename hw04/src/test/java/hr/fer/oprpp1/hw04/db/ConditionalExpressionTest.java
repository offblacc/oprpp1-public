package hr.fer.oprpp1.hw04.db;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ConditionalExpressionTest {
    @Test
    public void testPDF() {
        ConditionalExpression expr = new ConditionalExpression(
                FieldValueGetters.LAST_NAME,
                "Bos*",
                ComparisonOperators.LIKE);
        StudentRecord record = new StudentRecord("0000000003", "Bosnić", "Andrea", 4);
        boolean recordSatisfies = expr.getComparisonOperator().satisfied(
                expr.getFieldGetter().get(record), // returns lastName from given record
                expr.getStringLiteral() // returns "Bos*"
        );
        assertTrue(recordSatisfies);
    }
}
