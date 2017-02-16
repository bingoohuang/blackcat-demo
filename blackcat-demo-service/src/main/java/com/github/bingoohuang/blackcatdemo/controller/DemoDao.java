package com.github.bingoohuang.blackcatdemo.controller;

import org.n3r.eql.eqler.annotations.Eqler;
import org.n3r.eql.eqler.annotations.Sql;

@Eqler
public interface DemoDao {
    @Sql({"drop table if exists blackcat_demo", "create table blackcat_demo (id varchar(64), traceid varchar(64))"})
    void createTable();

    @Sql("insert into blackcat_demo(id, traceid) values(##, ##)")
    void demo(String id, String traceId);
}
