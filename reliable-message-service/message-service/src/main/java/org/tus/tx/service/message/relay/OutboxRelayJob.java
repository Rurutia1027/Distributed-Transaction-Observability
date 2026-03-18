package org.tus.tx.service.message.relay;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * Quartz job placeholder for Outbox Relay.
 * Actual scanning, locking, publishing, and retry logic will be implemented next.
 */
@Component
public class OutboxRelayJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // TODO implement outbox relay (scan + send + retry)
    }
}

