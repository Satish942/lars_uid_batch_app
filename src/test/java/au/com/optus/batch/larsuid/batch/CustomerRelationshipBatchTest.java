package au.com.optus.batch.larsuid.batch;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/test_larsuid-batch-context.xml",
		"classpath:/spring/jobs/job-larsuid-relationship-rewards-context.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
/*@Transactional(propagation = Propagation.REQUIRED)*/
public class CustomerRelationshipBatchTest {
	
	@Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    
    @Test
    public void launchDailyBatchJob() throws Exception {

        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        System.out.println("Job Stack trace: "+jobExecution.getAllFailureExceptions());
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        
    }

}
