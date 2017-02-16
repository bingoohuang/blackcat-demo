package com.github.bingoohuang.blackcat.consumer.job;

import com.github.bingoohuang.blackcat.consumer.facade.MerchantApi;
import com.github.bingoohuang.blackcat.server.base.BlackcatJob;
import com.github.bingoohuang.blackcat.server.base.MsgService;
import com.github.bingoohuang.blackcat.server.base.QuartzScheduler;
import com.github.bingoohuang.utils.redis.Redis;
import lombok.SneakyThrows;
import lombok.val;
import org.n3r.diamond.client.Miner;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.joda.time.DateTime.now;
import static org.joda.time.format.DateTimeFormat.forPattern;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

//@Component
public class YogaClassNotificationMsgJob implements BlackcatJob {
    @Autowired MsgService msgService;
    @Autowired Redis redis;
    @Autowired MerchantApi merchantApi;

    @SneakyThrows public void scheduleJob(QuartzScheduler scheduler) {
        // 微信消息发送作业及触发器
        val wxMsgJob = createWxMsgJob();
        val wxTrigger = createWxMsgTrigger();
        scheduler.scheduleJob(wxMsgJob, wxTrigger);
    }

    private JobDetail createWxMsgJob() {
        val job = newJob(RemindMsgJob.class).build();
        job.getJobDataMap().put("handler", this);
        return job;
    }

    public String getWxRemindTemplateMsg() {
        val today = forPattern("yyyyMMdd").print(now());
        val key = "YogaClassNotificationCount:" + today;
        val remindCoachTimes = defaultIfEmpty(redis.get(key + "coach"), "0");
        val remindMemberTimes = defaultIfEmpty(redis.get(key + "member"), "0");

        return "今日累计发送定时提醒: 教练" + remindCoachTimes
                + "条;会员" + remindMemberTimes + "条!";
    }

    public String getAnotherWxRemindTemplateMsg() {
        return merchantApi.getClassNotificationCount();
    }

    public static class RemindMsgJob implements Job {
        @Override @SneakyThrows
        public void execute(JobExecutionContext context) {
            val jobDataMap = context.getJobDetail().getJobDataMap();
            val handler = (YogaClassNotificationMsgJob) jobDataMap.get("handler");

            handler.triggerWxMsg();
        }
    }

    static final String GROUP_ID = "blackcat";
    static final String DATA_ID = "template.remind.cron.schedule";

    private Trigger createWxMsgTrigger() {
        // http://www.cronmaker.com/
        val cron = new Miner().getStone(GROUP_ID, DATA_ID, "0 10 20 1/1 * ?");
        return newTrigger().withSchedule(cronSchedule(cron)).build();
    }

    private void triggerWxMsg() {
        val deployArea = new Miner().getMiner("blackcat", "area").getString("area");
        val deployAreaZh = "south-center".equals(deployArea) ? "南方中心" : "北方中心";
        val anotherAreaZh = "south-center".equals(deployArea) ? "北方中心" : "南方中心";

        val alertTimesDployAreaMsg = getWxRemindTemplateMsg();
        val alertTimesAnotherAreaMsg = getAnotherWxRemindTemplateMsg();
        val alertTimesMsg = deployAreaZh + ":\n" + alertTimesDployAreaMsg
                + "\n" + anotherAreaZh + ":\n" + alertTimesAnotherAreaMsg;

        msgService.sendMsg("定时模板消息提醒", alertTimesMsg);
    }
}
