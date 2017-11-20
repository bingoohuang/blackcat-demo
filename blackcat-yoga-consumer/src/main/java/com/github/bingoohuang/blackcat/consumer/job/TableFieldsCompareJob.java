package com.github.bingoohuang.blackcat.consumer.job;

import com.github.bingoohuang.blackcat.consumer.checkfields.FieldsCheckException;
import com.github.bingoohuang.blackcat.consumer.checkfields.FieldsChecker;
import com.github.bingoohuang.blackcat.server.base.MsgService;
import com.github.bingoohuang.blackcat.server.base.BlackcatJob;
import com.github.bingoohuang.blackcat.server.base.QuartzScheduler;
import com.google.common.base.Stopwatch;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.n3r.diamond.client.Miner;
import org.n3r.eql.config.EqlConfig;
import org.n3r.eql.config.EqlDiamondConfig;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/*
配置项:
1) Diamond配置数据库连接信息: EqlblackcatConfig^checkfields
2) Diamond配置检查的排程CRON表达式（可选，默认0 10 08,14,17 1/1 * ?): blackcat^tablefieldscheck.schedule
 */

@Slf4j // @Component
public class TableFieldsCompareJob implements BlackcatJob {
    @Autowired MsgService msgService;
    @Autowired FieldsChecker fieldsChecker;

    @SneakyThrows
    public void scheduleJob(QuartzScheduler scheduler) {
        val jobDetail = createJobDetail();
        val trigger = createTrigger();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    private JobDetail createJobDetail() {
        val job = newJob(TableFieldsCompareQuartzJob.class).build();
        job.getJobDataMap().put("handler", this);
        return job;
    }

    String GROUP_ID = "blackcat";
    String DATA_ID = "tablefieldscheck.schedule";

    private Trigger createTrigger() {
        // at 10 minute at 8, 14 and 17 hours every day
        val cron = new Miner().getStone(GROUP_ID, DATA_ID, "0 10 08,14,17 1/1 * ?");
        return newTrigger().withSchedule(cronSchedule(cron)).build();
    }

    public static class TableFieldsCompareQuartzJob implements Job {
        @Override @SneakyThrows
        public void execute(JobExecutionContext context) {
            val jobDataMap = context.getJobDetail().getJobDataMap();
            val handler = (TableFieldsCompareJob) jobDataMap.get("handler");
            handler.checkTableFields();
        }
    }

    private void checkTableFields() {
        val southEqlConfig = new EqlDiamondConfig("southCheckFields");
        val northEqlConfig = new EqlDiamondConfig("northCheckFields");
        val southCheckResult = getCheckTableFieldsResult(southEqlConfig, "南方中心");
        val northCheckResult = getCheckTableFieldsResult(northEqlConfig, "北方中心");

        msgService.sendMsg("表字段检查", southCheckResult + "\n" + northCheckResult);
    }

    private String getCheckTableFieldsResult(EqlConfig eqlConfig, String centerName) {
        val stopwatch = Stopwatch.createStarted();
        try {
            fieldsChecker.checkTableFields(eqlConfig);
        } catch (FieldsCheckException e) {
            stopwatch.stop();
            log.warn("表字段检查{}", e.getMessage());
            return centerName + "表字段检查,耗时" + stopwatch + "\n" + e.getMessage();
        }
        return null;
    }
}
