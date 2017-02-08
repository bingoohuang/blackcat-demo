package com.github.bingoohuang.blackcat.consumer.job;

import com.github.bingoohuang.blackcat.server.base.BlackcatJob;
import com.github.bingoohuang.blackcat.server.base.MsgService;
import com.github.bingoohuang.blackcat.server.base.QuartzScheduler;
import com.github.bingoohuang.utils.net.HttpReq;
import lombok.SneakyThrows;
import lombok.val;
import org.n3r.diamond.client.Miner;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.joda.time.DateTime.now;
import static org.joda.time.format.DateTimeFormat.forPattern;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Component
public class BackupCheckerJob implements BlackcatJob {
    @Autowired MsgService msgService;

    @SneakyThrows public void scheduleJob(QuartzScheduler scheduler) {
        // 微信消息发送作业及触发器
        val backupRemindJob = createBackupRemindJob();
        val wxTrigger = createWxMsgTrigger();
        scheduler.scheduleJob(backupRemindJob, wxTrigger);
    }

    private JobDetail createBackupRemindJob() {
        val job = newJob(BackupRemindJop.class).build();
        job.getJobDataMap().put("handler", this);
        return job;
    }

    public String getBackupRemindMsg() {
        val today = forPattern("yyyyMMdd").print(now());
        val miner = new Miner().getMiner("blackcat", "backup");
        val urlPrefix = miner.getString("urlPrefix");
        val errLogPath = urlPrefix + today + "/err.log";
        val result = new HttpReq(errLogPath).get();
        return "OK".equals(result)
                ? "南方中心数据备份成功!"
                : "备份中心数据备份异常,详情请查看备份目录:err.log";
    }

    public static class BackupRemindJop implements Job {
        @Override @SneakyThrows
        public void execute(JobExecutionContext context) {
            val jobDataMap = context.getJobDetail().getJobDataMap();
            val handler = (BackupCheckerJob) jobDataMap.get("handler");

            handler.triggerWxMsg();
        }
    }

    static final String GROUP_ID = "blackcat";
    static final String DATA_ID = "backup.remind.cron.schedule";

    private Trigger createWxMsgTrigger() {
        // http://www.cronmaker.com/
        val cron = new Miner().getStone(GROUP_ID, DATA_ID, "0 10 08 1/1 * ?");
        return newTrigger().withSchedule(cronSchedule(cron)).build();
    }

    private void triggerWxMsg() {
        val msg = getBackupRemindMsg();

        msgService.sendMsg("备份中心备份告警", msg);
    }
}
