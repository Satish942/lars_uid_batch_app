/**
 * FileName      : $Id: DatabaseDeleteTasklet.java 2014-11-19 06:51:25Z $
 *
 * Copyright Notice: ©2004 Singapore Telecom Pte Ltd -- Confidential and Proprietary
 *
 * All rights reserved.
 * This software is the confidential and proprietary information of SingTel Pte Ltd
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with SingTel.
 */
package au.com.optus.batch.larsuid.tasklets;




import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import au.com.optus.batch.larsuid.util.BATCH_TYPE;
import au.com.optus.batch.larsuid.util.BATCH_TYPE_DESCRIPTION;
import au.com.optus.batch.larsuid.util.CONTROL_TYPE;
import au.com.optus.batch.larsuid.util.ResponseFileNameGenerator;
import au.com.optus.mcas.sdp.bizservice.larsuid.batch.domain.dao.LarsBatchSummaryDaoIF;
import au.com.optus.mcas.sdp.bizservice.larsuid.batch.domain.dao.LarsUidBatchDaoIF;
import au.com.optus.mcas.sdp.bizservice.larsuid.batch.domain.model.LarBatchSummary;

/**
 *
 * @author Namrata Jain
 *
 */

public class MarketRewardsItemProcessingTasklet implements Tasklet {
	
	/**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MarketRewardsItemProcessingTasklet.class);

       
    private String dateFormat;
    
    @Autowired
    private LarsUidBatchDaoIF larsuidBatchDao;
	
	@Autowired
    private LarsBatchSummaryDaoIF larsBatchSummaryDao;
	
	private ResponseFileNameGenerator resFileGenerator;

	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext )
			throws Exception {
		LOGGER.info("MarketRewardsItemProcessingTasklet: saveSuccessDataFileSummary -- Starts");
    	LarBatchSummary larBatchSummary = new LarBatchSummary();    	
    	larBatchSummary.setBatchType(BATCH_TYPE.MARKETING_FILE.toString());    	
    	larBatchSummary.setBatchTypeDescription(BATCH_TYPE_DESCRIPTION.SUCCESS_RESPONSE.toString());
    	larBatchSummary.setFileName(resFileGenerator.getDataFileName(CONTROL_TYPE.MARK_PR_EXTRACT.toString(), BATCH_TYPE.MARKETING_FILE.toString()));  	
    	larBatchSummary.setNoOfRecordsProcessed((Integer) chunkContext.getStepContext().getJobExecutionContext().get("recordProcess"));    	   	
    	larBatchSummary.setProcessDate(new Date());
    	larsBatchSummaryDao.createBatchSummary(larBatchSummary);
    	LOGGER.info("MarketRewardsItemProcessingTasklet: saveSuccessDataFileSummary -- Ends");
		return RepeatStatus.FINISHED;
	}


	public String getDateFormat() {
		return dateFormat;
	}


	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}


	public LarsUidBatchDaoIF getLarsuidBatchDao() {
		return larsuidBatchDao;
	}


	public void setLarsuidBatchDao(LarsUidBatchDaoIF larsuidBatchDao) {
		this.larsuidBatchDao = larsuidBatchDao;
	}


	public LarsBatchSummaryDaoIF getLarsBatchSummaryDao() {
		return larsBatchSummaryDao;
	}


	public void setLarsBatchSummaryDao(LarsBatchSummaryDaoIF larsBatchSummaryDao) {
		this.larsBatchSummaryDao = larsBatchSummaryDao;
	}


	public ResponseFileNameGenerator getResFileGenerator() {
		return resFileGenerator;
	}


	public void setResFileGenerator(ResponseFileNameGenerator resFileGenerator) {
		this.resFileGenerator = resFileGenerator;
	}


	
}
