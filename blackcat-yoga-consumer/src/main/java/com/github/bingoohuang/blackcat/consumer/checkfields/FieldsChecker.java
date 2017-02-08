package com.github.bingoohuang.blackcat.consumer.checkfields;

import lombok.val;
import org.n3r.diamond.client.Miner;
import org.n3r.eql.Eqll;
import org.n3r.eql.config.EqlConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class FieldsChecker {
    @Autowired InfoSchemaDao infoSchemaDao;
    SecureRandom random = new SecureRandom();

    public void checkTableFields(EqlConfig eqlConfig) {
        Eqll.choose(eqlConfig);
        val allSchemas = infoSchemaDao.selectSchemas();
        if (allSchemas.isEmpty()) {
            throw new FieldsCheckException("配置错误, 没有查到任何库");
        }

        val regex = new Miner().getStone("blackcat",
                "ignored.tablename.regex", ".*bac?k");
        val tableNameIgnorePattern = Pattern.compile(regex);

        val stdSchema = getStdSchema(allSchemas);
        val tablesStd = filterOutBakTables(stdSchema, tableNameIgnorePattern);

        val stdColumnMapper = new SchemaTableColumnMapper();
        infoSchemaDao.selectStdSchemaTableColumns(stdSchema, stdColumnMapper);
        val stdFieldsCache = stdColumnMapper.getStdFieldsCache();

        val msg = new StringBuilder();
        val tableSchemaMapper = new TableSchemaMapper(
                stdSchema, tablesStd, tableNameIgnorePattern, msg);
        infoSchemaDao.selectAllTables(stdSchema, tableSchemaMapper);
        tableSchemaMapper.checkTables();

        val tableColumnMapper = new TableColumnMapper(
                stdSchema, tableNameIgnorePattern, stdFieldsCache, msg);
        infoSchemaDao.selectAllColumns(stdSchema, tableColumnMapper);
        tableColumnMapper.checkFields();

        if (msg.length() != 0) throw new FieldsCheckException(msg.toString());

        msg.append("检查完成, 基准库[" + stdSchema + "]，共比对库" + allSchemas.size() + "个");
    }

    private String getStdSchema(List<String> allSchemas) {
        return allSchemas.get(random.nextInt(allSchemas.size()));
    }

    private List<String> filterOutBakTables(String cmpSchema, Pattern pattern) {
        return infoSchemaDao.selectTables(cmpSchema).stream().filter(
                tableName -> !pattern.matcher(tableName.toLowerCase()).matches())
                .collect(Collectors.toList());
    }
}
