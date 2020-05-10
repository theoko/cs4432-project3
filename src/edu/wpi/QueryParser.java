package edu.wpi;

public class QueryParser {
    public static QueryType parse(String query) {
        query = query.toLowerCase();
        if (query.startsWith("select a.col1, a.col2, b.col1, b.col2 from a, b")) {
            // Hash join
            return QueryType.HASH_JOIN;
        } else if (query.startsWith("select count(*) from a, b")) {
            // Nested loop join
            return QueryType.NESTED_LOOP_JOIN;
        } else if (query.startsWith("select col2, ")) {
            if (query.contains("sum")) {
                if (query.contains("from a group")) {
                    // A: Hash-based aggregation (sum)
                    return QueryType.HASH_BASED_AGGREGATION_SUM_A;
                } else if (query.contains("from b group")) {
                    // A: Hash-based aggregation (sum)
                    return QueryType.HASH_BASED_AGGREGATION_SUM_B;
                } else {
                    return null;
                }
            } else if (query.contains("avg")) {
                if (query.contains("from a group")) {
                    // Hash-based aggregation (average)
                    return QueryType.HASH_BASED_AGGREGATION_AVG_A;
                } else if (query.contains("from b group")) {
                    // Hash-based aggregation (average)
                    return QueryType.HASH_BASED_AGGREGATION_AVG_B;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
        return null;
    }
}
