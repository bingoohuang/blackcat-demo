package com.github.bingoohuang.blackcat.consumer.checkfields;

import org.n3r.eql.Eqll;
import org.n3r.eql.eqler.annotations.EqlerConfig;
import org.n3r.eql.eqler.annotations.Sql;

import java.util.List;

@EqlerConfig(eql = Eqll.class)
public interface InfoSchemaDao {
    @Sql("SELECT DISTINCT table_schema " +
            "FROM information_schema.tables " +
            "WHERE table_schema NOT IN ('information_schema', 'performance_schema', 'sys', 'mysql')")
    List<String> selectSchemas();

    @Sql("SELECT table_name " +
            "FROM information_schema.tables " +
            "WHERE table_type = 'BASE TABLE' " +
            "AND table_schema = '##'")
    List<String> selectTables(String schema);

    @Sql("SELECT table_schema, table_name " +
            "FROM information_schema.tables " +
            "WHERE table_schema NOT IN ('information_schema', 'performance_schema', 'sys', 'mysql', '##') " +
            "AND table_type = 'BASE TABLE' " +
            "ORDER BY table_schema, table_name")
    void selectAllTables(String stdSchema, TableSchemaMapper mapper);

    @Sql("SELECT table_schema, table_name, column_name, column_default, column_type, is_nullable " +
            "FROM information_schema.COLUMNS " +
            "WHERE table_schema NOT IN ('information_schema', 'performance_schema', 'sys', 'mysql', '##') " +
            "ORDER BY table_schema, table_name, column_name")
    void selectAllColumns(String stdSchema, TableColumnMapper mapper);

    @Sql("SELECT table_schema, table_name, column_name, column_default, column_type, is_nullable " +
            "FROM information_schema.COLUMNS " +
            "WHERE table_schema = '##' " +
            "ORDER BY table_name, column_name")
    void selectStdSchemaTableColumns(String stdSchema, SchemaTableColumnMapper mapper);
}
