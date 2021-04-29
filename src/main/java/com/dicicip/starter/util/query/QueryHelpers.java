package com.dicicip.starter.util.query;

import com.dicicip.starter.util.query.model.QueryListResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryHelpers {

    public static QueryListResult<Class<?>> getData(Map<String, String[]> requestParams, String table, DB db) {

        ObjectMapper objectMapper = new ObjectMapper();

        int limit = 10;
        int offset = 0;
        String orderBy = "id";
        String orderType = "desc";

        if (requestParams.get("limit") != null && !requestParams.get("limit")[0].equals("")) {
            limit = Integer.parseInt(requestParams.get("limit")[0]);
        }

        if (requestParams.get("offset") != null && !requestParams.get("offset")[0].equals("")) {
            offset = Integer.parseInt(requestParams.get("offset")[0]);
        }

        if (requestParams.get("orderBy") != null && !requestParams.get("orderBy")[0].equals("")) {
            orderBy = String.valueOf(requestParams.get("orderBy")[0]);
        }

        if (requestParams.get("orderType") != null && !requestParams.get("orderType")[0].equals("")) {
            orderType = String.valueOf(requestParams.get("orderType")[0]);
        }

        try {
            System.out.println("LIMIT,OFFSET ==> " + limit + "," + offset);
            System.out.println("getData() ==> " + objectMapper.writeValueAsString(requestParams));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        String query = "SELECT"
                + " * "
                + " FROM " + table
//                + " WHERE"
//                + " "
                + " ORDER BY " + orderBy + " " + orderType
                + " LIMIT " + limit
                + " OFFSET " + offset
                + "";

        String queryCount = "SELECT"
                + " count(id) "
                + " FROM " + table;

        return new QueryListResult<Class<?>>(
                objectMapper.convertValue(db.select(query), List.class),
                (db.row(queryCount).get("count") == null) ? 0 : Integer.parseInt(String.valueOf(db.row(queryCount).get("count")))
        );
    }

}
