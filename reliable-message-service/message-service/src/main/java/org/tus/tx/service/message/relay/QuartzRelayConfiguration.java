package org.tus.tx.service.message.relay;

import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.quartz.JobBuilder.newJob;

@Configuration
public class QuartzRelayConfiguration {

    @Bean
    public JobDetail outboxRelayJobDetail() {
        return newJob(OutboxRelayJob.class)
                .withIdentity("outboxRelayJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger outboxRelayTrigger(JobDetail outboxRelayJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(outboxRelayJobDetail)
                .withIdentity("outboxRelayTrigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(5)
                        .repeatForever())
                .build();
    }
}

