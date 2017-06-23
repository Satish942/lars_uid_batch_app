package au.com.optus.batch.larsuid.batch;

import static org.junit.Assert.assertEquals;
import java.util.Date;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test_larsuid-batch-context.xml",
"classpath:/spring/jobs/job-initial-load.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
/*@Transactional(propagation = Propagation.REQUIRED)*/
public class IntialLoadBatchTest {
    
    private static final Logger LOGGER = LoggerFactory
        .getLogger(IntialLoadBatchTest.class);

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    
    @Test
    @Ignore
    public void launchJob() throws Exception {
       
        //JobExecution jobExecution = jobLauncherTestUtils.launchStep("stepProcessLarsUidIntialLoad");
        LOGGER.info("Intiail Time:"+ new Date().getTime());
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        LOGGER.info("JOB ID: "+jobExecution.getJobId());
        LOGGER.info("ID: "+jobExecution.getAllFailureExceptions());
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        LOGGER.info("End Time:"+ new Date().getTime());
        
    }

}
