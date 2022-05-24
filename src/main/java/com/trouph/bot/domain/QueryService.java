package com.trouph.bot.domain;

import org.springframework.stereotype.Service;

import com.graphql_java_generator.exception.GraphQLRequestExecutionException;
import com.graphql_java_generator.exception.GraphQLRequestPreparationException;
import com.trouph.graphql.generated.util.QueryExecutor;

@Service
public class QueryService {

    private final QueryExecutor queryExecutor;

    public QueryService(QueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    public void query() {
        try {
            queryExecutor.card("{ serialNumber }", "");
        } catch (GraphQLRequestExecutionException | GraphQLRequestPreparationException e) {
            throw new RuntimeException(e);
        }
    }
}
