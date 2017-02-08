package com.github.bingoohuang.blackcat.consumer.checkfields;

import lombok.SneakyThrows;
import lombok.val;
import org.n3r.eql.map.EqlRowMapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class TableSchemaMapper implements EqlRowMapper {
    final List<String> tablesStd;
    final String stdSchema;
    final Pattern tableNameIgnorePattern;
    final StringBuilder msg;

    List<String> tablesCmp;
    String cmpSchema;

    public TableSchemaMapper(
            String stdSchema,
            List<String> tablesStd,
            Pattern tableNameIgnorePattern, StringBuilder msg) {
        this.stdSchema = stdSchema;
        this.tablesStd = tablesStd;
        this.tableNameIgnorePattern = tableNameIgnorePattern;
        this.msg = msg;
    }

    @Override @SneakyThrows
    public Object mapRow(ResultSet rs, int rowNum, boolean isSingleColumn) {
        val tableSchema = rs.getString(1);
        val tableName = rs.getString(2);
        if (isTableIgnored(tableName)) return null;

        if (!tableSchema.equals(cmpSchema)) {
            checkTables();

            tablesCmp = new ArrayList<>();
            cmpSchema = tableSchema;
        }

        tablesCmp.add(tableName);

        return null;
    }

    private boolean isTableIgnored(String tableName) {
        return tableNameIgnorePattern.matcher(tableName).matches();
    }

    public void checkTables() {
        if (cmpSchema == null) return;

        val diffTablesCmp = new ArrayList<String>(tablesCmp);
        val diffTablesStd = new ArrayList<String>(tablesStd);
        diffTablesCmp.removeAll(tablesStd);
        diffTablesStd.removeAll(tablesCmp);

        if (!diffTablesStd.isEmpty()) {
            msg.append("\n基准库[" + stdSchema + "]多表" + diffTablesStd);
        }

        if (!diffTablesCmp.isEmpty()) {
            if (msg.length() > 0) {
                msg.append("\n对比库[" + cmpSchema + "]多表" + diffTablesCmp);
            } else {
                msg.append("\n对比库[" + cmpSchema
                        + "]比基准库[" + stdSchema + "]多表" + diffTablesCmp);
            }
        }
    }


}
