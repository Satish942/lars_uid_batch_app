/**
 * FileName      : $Id: LarsUidRelationshipRewardsRecordsBatchRunner.java 2014-11-19 06:51:25Z $
 *
 * Copyright Notice: ©2004 Singapore Telecom Pte Ltd -- Confidential and Proprietary
 *
 * All rights reserved.
 * This software is the confidential and proprietary information of SingTel Pte Ltd
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with SingTel.
 */
package au.com.optus.batch.larsuid.run;

import au.com.optus.mcas.core.scheduler.SchedulerJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;


/**
 * @author Prity Rani
 *
 */
public class LarsUidRelationshipRewardsRecordsBatchRunner extends SchedulerJob {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LarsUidRelationshipRewardsRecordsBatchRunner.class);

    /* (non-Javadoc)
     * @see au.com.optus.mcas.core.scheduler.SchedulerJob#executeJob(org.quartz.JobExecutionContext)
     */
    @Override
    protected void executeJob(final JobExecutionContext context) throws JobExecutionException {
        LOGGER.debug(" ============= LarsUidRelationshipRewardsRecordsBatchRunner executeJob() STARTS ========== ");
        try {
            final String jobName = context.getJobDetail().getName();
            LOGGER.debug("executeJob() Invoked by JOB Name:------>> {} ", jobName);
            final ApplicationContext appCtx =
                (ApplicationContext) context.getScheduler().getContext().get(APPLICATION_CONTEXT_KEY);

            JobLauncher jobLauncher = (JobLauncher) appCtx.getBean("jobLauncher");
            Job job = (Job) appCtx.getBean("jobLarsUidRelRwdRec");
            //We need to pass these fake (Unique) parameter otherwise the batch will not execute second time
            JobParameters jobParameters =
                new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
            JobExecution execution = jobLauncher.run(job, jobParameters);

            LOGGER.debug("Finished execution of Batch Job '{}' WITH STATUS '{}' and EXIT STATUS '{}'", jobName,
                execution.getExitStatus());

        } catch (final SchedulerException e) {
            LOGGER.error("*** Exception occured in LarsUidRelationshipRewardsRecordsBatchRunner. {}", e);
        } catch (final Exception e) {
            LOGGER.error("*** Exception occured in LarsUidRelationshipRewardsRecordsBatchRunner. {}", e);
        }

        LOGGER.debug(" ===========LarsUidRelationshipRewardsRecordsBatchRunner executeJob() ENDS ========== ");
    }

}
