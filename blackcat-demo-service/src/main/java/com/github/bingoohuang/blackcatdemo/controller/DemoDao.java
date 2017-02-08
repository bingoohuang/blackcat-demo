package com.github.bingoohuang.blackcatdemo.controller;

import org.n3r.eql.eqler.annotations.Eqler;
import org.n3r.eql.eqler.annotations.Sql;

@Eqler
public interface DemoDao {
    /*
    create table blackcat_demo (
        id varchar(64),
        traceid varchar(64)
    );
     */
    @Sql("insert into blackcat_demo(id, traceid) values(##, ##)")
    void demo(String id, String traceId);
}
