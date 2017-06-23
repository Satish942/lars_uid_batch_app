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


public class LarsMarketRewardsRunner extends SchedulerJob{
	 
	 private static final Logger LOGGER = LoggerFactory.getLogger(LarsMarketRewardsRunner.class);
	 
	 @Override
	    protected void executeJob(final JobExecutionContext context) throws JobExecutionException {
	        LOGGER.debug(" ============= Main executeJob() STARTS ========== ");
	        try {
	            final String jobName = context.getJobDetail().getName();
	            LOGGER.debug("executeJob() Invoked by JOB Name:------>> {} ", jobName);
        final ApplicationContext appCtx =
                (ApplicationContext) context.getScheduler().getContext().get(APPLICATION_CONTEXT_KEY);

           // JobLauncher jobLauncher = (JobLauncher) appCtx.getBean("jobLauncherLarsUid");
            JobLauncher jobLauncher = (JobLauncher) appCtx.getBean("jobLauncher");
            
            //Job job = (Job) appCtx.getBean("jobLarsUidRelRwdRec");
            
            Job job = (Job) appCtx.getBean("jobLarsMarketRewardsUidRelRwdRec");
      
            JobParameters jobParameters =
                    new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
                JobExecution execution = jobLauncher.run(job, jobParameters);

                LOGGER.debug("Finished execution of Batch Job '{}' WITH STATUS '{}' and EXIT STATUS '{}'", jobName,
                    execution.getExitStatus());

            } catch (final SchedulerException e) {
                LOGGER.error("*** Exception occured in Main. {}", e);
            } catch (final Exception e) {
                LOGGER.error("*** Exception occured in Main. {}", e);
            }

            LOGGER.debug(" ===========Main executeJob() ENDS ========== ");
  
	}
	
}