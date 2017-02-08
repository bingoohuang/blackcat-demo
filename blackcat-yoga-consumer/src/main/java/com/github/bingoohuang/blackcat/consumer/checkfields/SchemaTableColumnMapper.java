package com.github.bingoohuang.blackcat.consumer.checkfields;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import org.n3r.eql.map.EqlRowMapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchemaTableColumnMapper implements EqlRowMapper {
    @Getter Map<String, List<TableColumn>> stdFieldsCache = new HashMap<>();
    List<TableColumn> tableColumns;
    String stdTableName;

    @Override @SneakyThrows
    public Object mapRow(ResultSet rs, int rowNum, boolean isSingleColumn) {
        val tableName = rs.getString(2);
        val columnName = rs.getString(3);
        val columnDefault = rs.getString(4);
        val columnType = rs.getString(5);
        val isNullable = rs.getString(6);

        if (stdTableName == null || !stdTableName.equals(tableName)) {
            tableColumns = new ArrayList<>();
            stdTableName = tableName;
            stdFieldsCache.put(stdTableName, tableColumns);
        }

        tableColumns.add(new TableColumn(columnName, columnDefault, columnType, isNullable));

        return null;
    }
}
