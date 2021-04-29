package com.dicicip.starter.util.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DB {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;


    public List<Map<String, Object>> select(String sql) {
        return jdbcTemplate.queryForList(sql, new HashMap());
    }

    public Map row(String sql) {
        try {
            Object row = jdbcTemplate.queryForMap(sql, new HashMap());
            return (Map) row;
        } catch (Exception ex) {
            return new HashMap();
        }
    }

    public Map row(String sql, Map<String, Object> param) {
        try {
            return jdbcTemplate.queryForMap(sql, param);
        } catch (Exception ex) {
            return new HashMap();
        }
    }

    public List<Map<String, Object>> select(String sql, Map<String, Object> param) {
        return jdbcTemplate.queryForList(sql, param);
    }

    public Object insert(Object entity) {
        entityManager.persist(entity);
        return entity;
    }

    public void insertRaw(String query) {
        jdbcTemplate.execute(query, null);
    }

    public void insert(String table, Map<String, Object> field) {

        int set = field.size();
        int i = 0;
        String sql = "INSERT INTO " + table;
        String[] columnList = new String[set];
        String[] valueList = new String[set];

        for (Map.Entry<String, Object> entry : field.entrySet()) {
            columnList[i] = entry.getKey();
            valueList[i] = ":" + entry.getKey();
            i++;
        }

        sql += "(" + StringUtils.arrayToCommaDelimitedString(columnList) + ") VALUES ("
                + StringUtils.arrayToCommaDelimitedString(valueList) + ")";

        jdbcTemplate.update(sql, field);
    }

    public void update(String table, Map<String, Object> field, Map<String, Object> condition) {
        int set = field.size();
        int setCondition = condition.size();
        int i = 0;
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "UPDATE " + table + " SET ";

        for (Map.Entry<String, Object> entry : field.entrySet()) {
            sql += entry.getKey() + "= :" + entry.getKey();
            params.put(entry.getKey(), entry.getValue());
            sql += (i < set - 1) ? ", " : "";
            i++;
        }


        int j = 0;
        sql += " WHERE ";
        for (Map.Entry<String, Object> entry : condition.entrySet()) {
            params.put(entry.getKey(), entry.getValue());
            sql += entry.getKey() + "= :" + entry.getKey();
            sql += (j < setCondition - 1) ? " AND " : "";
            j++;
        }

        jdbcTemplate.update(sql, params);
    }

    public void updateRaw(String query) {
        jdbcTemplate.update(query, new HashMap<>());
    }

    public void delete(String table, Map<String, Object> condition) {
        String sql = "DELETE FROM " + table;

        sql += " WHERE ";
        for (Map.Entry<String, Object> entry : condition.entrySet()) {
            sql += entry.getKey() + "= :" + entry.getKey();
        }

        jdbcTemplate.update(sql, condition);
    }

    public void deleteRaw(String query) {
        jdbcTemplate.update(query, new HashMap<>());
    }

}
