package hr.fer.oprpp1.hw04.db;

import java.util.List;

/**
 * A class that helps filter students based on a given query which is a list of
 * conditional expressions. Students have to pass all of the conditional
 * expressions in order to be accepted.
 */
public class QueryFilter implements IFilter {
    /**
     * List of conditional expressions.
     */
    List<ConditionalExpression> query;

    /**
     * Creates a new query filter.
     * 
     * @param query - list of conditional expressions
     */
    public QueryFilter(List<ConditionalExpression> query) {
        this.query = query;
    }

    /**
     * Returns true if the given student record satisfies the filter.
     * 
     * @param record - student record to be checked
     * @return true if the given student record satisfies the filter, false
     *         otherwise.
     */
    @Override
    public boolean accepts(StudentRecord record) {
        for (ConditionalExpression exp : query) {
            if (!exp.getComparisonOperator().satisfied(exp.getFieldGetter().get(record), exp.getStringLiteral())) {
                return false;
            }
        }
        return true;
    }
}
