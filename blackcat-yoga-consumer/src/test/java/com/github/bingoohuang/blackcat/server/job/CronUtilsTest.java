package com.github.bingoohuang.blackcat.server.job;

import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.Cron;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import lombok.val;
import org.junit.Test;

import static com.cronutils.model.CronType.QUARTZ;

public class CronUtilsTest {
    @Test
    public void test() {
        val cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(QUARTZ);
        CronParser parser = new CronParser(cronDefinition);
        Cron cron = parser.parse("0 10 08,14,17 1/1 * ?");

        // Create a descriptor for a specific Locale
        CronDescriptor descriptor = CronDescriptor.instance();
        String describe = descriptor.describe(cron);
        System.out.println(describe);
    }
}
