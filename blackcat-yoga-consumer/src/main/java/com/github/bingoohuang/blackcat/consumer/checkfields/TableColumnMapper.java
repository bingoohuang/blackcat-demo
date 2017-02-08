package com.github.bingoohuang.blackcat.consumer.checkfields;

import lombok.SneakyThrows;
import lombok.val;
import org.n3r.eql.map.EqlRowMapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class TableColumnMapper implements EqlRowMapper {
    final Map<String, List<TableColumn>> stdFieldsCache;
    final Pattern tableNameIgnorePattern;
    final StringBuilder msg;
    final String stdSchema;

    List<TableColumn> cmpFields;
    String tableSchema0;
    String tableName0;

    public TableColumnMapper(
            String stdSchema, Pattern tableNameIgnorePattern,
            Map<String, List<TableColumn>> stdFieldsCache,
            StringBuilder msg) {
        this.stdSchema = stdSchema;
        this.tableNameIgnorePattern = tableNameIgnorePattern;
        this.stdFieldsCache = stdFieldsCache;
        this.msg = msg;
    }

    @Override @SneakyThrows
    public Object mapRow(ResultSet rs, int rowNum, boolean isSingleColumn) {
        val tableSchema = rs.getString(1);
        val tableName = rs.getString(2);
        if (isTableIgnored(tableName)) return null;

        val columnName = rs.getString(3);
        val columnDefault = rs.getString(4);
        val columnType = rs.getString(5);
        val isNullable = rs.getString(6);

        if (!(tableSchema + "." + tableName).equals(tableSchema0 + "." + tableName0)) {
            checkFields();

            tableSchema0 = tableSchema;
            tableName0 = tableName;
            cmpFields = new ArrayList<>();
        }

        cmpFields.add(new TableColumn(columnName, columnDefault, columnType, isNullable));

        return null;
    }

    private boolean isTableIgnored(String tableName) {
        return tableNameIgnorePattern.matcher(tableName).matches();
    }

    public void checkFields() {
        if (cmpFields == null) return;

        val stdFields = stdFieldsCache.get(tableName0);
        if (stdFields == null) return;

        if (cmpFields.equals(stdFields)) return;

        val diffStdFields = new ArrayList<TableColumn>(stdFields);
        val diffCmpFields = new ArrayList<TableColumn>(cmpFields);
        diffCmpFields.removeAll(stdFields);
        diffStdFields.removeAll(cmpFields);

        msg.append("\n表" + tableName0 + "在基准库[" + stdSchema
                + "]与对比库[" + tableSchema0 + "]中定义不一致"
                + "\n基准库[" + stdSchema + "]中差异:" + diffStdFields
                + "\n对比库[" + tableSchema0 + "]中差异:" + diffCmpFields);
    }
}
