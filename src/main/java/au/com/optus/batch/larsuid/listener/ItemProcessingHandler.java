/**
 * FileName      : $Id: ItemProcessingHandler.java 2014-11-19 06:51:25Z $
 *
 * Copyright Notice: ©2004 Singapore Telecom Pte Ltd -- Confidential and Proprietary
 *
 * All rights reserved.
 * This software is the confidential and proprietary information of SingTel Pte Ltd
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with SingTel.
 */
package au.com.optus.batch.larsuid.listener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.ResourceAwareItemWriterItemStream;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import au.com.optus.batch.larsuid.bean.ControlFileInfo;
import au.com.optus.batch.larsuid.bean.CustomerRelationshipInfo;
import au.com.optus.batch.larsuid.exception.InitialLoadFileExpectedException;
import au.com.optus.batch.larsuid.util.BATCH_TYPE;
import au.com.optus.batch.larsuid.util.BATCH_TYPE_DESCRIPTION;
import au.com.optus.batch.larsuid.util.BatchUtil;
import au.com.optus.batch.larsuid.util.CONTROL_TYPE;
import au.com.optus.batch.larsuid.util.ResponseFileNameGenerator;
import au.com.optus.mcas.sdp.bizservice.larsuid.batch.domain.dao.LarsBatchSummaryDaoIF;
import au.com.optus.mcas.sdp.bizservice.larsuid.batch.domain.dao.LarsUidBatchDaoIF;
import au.com.optus.mcas.sdp.bizservice.larsuid.batch.domain.model.LarBatchSummary;
import au.com.optus.mcas.sdp.bizservice.larsuid.batch.domain.model.LifestyleRewardsRecords;


/**
 * Listener implementation to write records that get skipped during the import
 * process into a separate file. It writes the record that was received as input
 * and appends the validation failure message as the end.
 *
 * If a record gets skipped during read process then we do not get the input
 * record, so it can not be logged into the file. The exception is logged in
 * error log.
 */
public class ItemProcessingHandler implements
    InitializingBean, ItemProcessListener<ControlFileInfo,ControlFileInfo>, StepExecutionListener {
	
	/**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemProcessingHandler.class);

    /**
     * Step execution context local copy.
     */
    private ExecutionContext stepContext;
    
    private String dateFormat;

    /**
     * Item writer for failed records, injected.
     */
    private ResourceAwareItemWriterItemStream<ControlFileInfo> successFileWriter;
    
    /**
     * Item writer for failed records, injected.
     */
    private ResourceAwareItemWriterItemStream<ControlFileInfo> failedFileWriter;
    
    /**
     * Item writer for failed records, injected.
     */
    private ResourceAwareItemWriterItemStream<ControlFileInfo> ctlFileWriter;
    
    private ResponseFileNameGenerator resFileGenerator;
    
    private String successFilename;
    
    private String failedFilename;
    
    @Autowired
    private LarsUidBatchDaoIF larsuidBatchDao;
	
	@Autowired
    private LarsBatchSummaryDaoIF larsBatchSummaryDao;

    /**
	 * @return the successFileWriter
	 */
	public ResourceAwareItemWriterItemStream<ControlFileInfo> getSuccessFileWriter() {
		return successFileWriter;
	}

	/**
	 * @param successFileWriter the successFileWriter to set
	 */
	public void setSuccessFileWriter(
			ResourceAwareItemWriterItemStream<ControlFileInfo> successFileWriter) {
		this.successFileWriter = successFileWriter;
	}



	/**
	 * @return the failedFileWriter
	 */
	public ResourceAwareItemWriterItemStream<ControlFileInfo> getFailedFileWriter() {
		return failedFileWriter;
	}



	/**
	 * @param failedFileWriter the failedFileWriter to set
	 */
	public void setFailedFileWriter(
			ResourceAwareItemWriterItemStream<ControlFileInfo> failedFileWriter) {
		this.failedFileWriter = failedFileWriter;
	}

	/**
	 * @return the ctlFileWriter
	 */
	public ResourceAwareItemWriterItemStream<ControlFileInfo> getCtlFileWriter() {
		return ctlFileWriter;
	}

	/**
	 * @param ctlFileWriter the ctlFileWriter to set
	 */
	public void setCtlFileWriter(
			ResourceAwareItemWriterItemStream<ControlFileInfo> ctlFileWriter) {
		this.ctlFileWriter = ctlFileWriter;
	}

    /**
     * Bean processor listener.
     * @throws Exception exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Validate.notNull(successFileWriter, "File writer must be supplied before using this listener.");
        Validate.notNull(failedFileWriter, "File writer must be supplied before using this listener.");
    }

	@Override
	public void afterProcess(ControlFileInfo arg0, ControlFileInfo arg1) {
		
    	if(arg1!= null && arg1.getKey().equals("TRAILER")){  
    		stepContext.put(BatchUtil.INCOMING_FILE_EXIST_KEY, true);
    		successFilename = resFileGenerator.getDataFileName(CONTROL_TYPE.DAILY_SRV_LIST.toString(), BATCH_TYPE.SERVICE_FILE.toString());
    		failedFilename = resFileGenerator.getDataFileName(CONTROL_TYPE.DAILY_SRV_LIST.toString(), BATCH_TYPE.ERROR_RESPONSE_FILE.toString());
    		writeSuccessfulRecords();
    		writeFailedRecords();
    		writeCtlRecords();
    	}
	}
	
	private void writeSuccessfulRecords(){
		LOGGER.info("ItemProcessingHandler: writeSuccessfulRecords -- Starts");
    	List<CustomerRelationshipInfo> successfulRecs = convertBeanToDto(larsuidBatchDao.getNewSuccessFulRecords());
    	stepContext.put(BatchUtil.DAILY_SUCCESS_COUNT_KEY, successfulRecs.size());
    	LOGGER.debug("Total number of successful records: " + successfulRecs.size());
    	
    	try {
    		successFileWriter.write(successfulRecs);
		} catch (Exception e) {
			LOGGER.error("Unable to write success records to file"+e);
		}
    	saveSuccessDataFileSummary(successfulRecs.size());
    	LOGGER.info("ItemProcessingHandler: writeSuccessfulRecords -- Ends");
	}
    
    private void saveSuccessDataFileSummary(int recordsProcessed){
    	LOGGER.info("ItemProcessingHandler: saveSuccessDataFileSummary -- Starts");
    	LarBatchSummary larBatchSummary = new LarBatchSummary();
    	larBatchSummary.setBatchType(BATCH_TYPE.SERVICE_FILE.toString());
    	larBatchSummary.setBatchTypeDescription(BATCH_TYPE_DESCRIPTION.SUCCESS_RESPONSE.toString());
    	larBatchSummary.setFileName(successFilename);
    	larBatchSummary.setNoOfRecordsProcessed(recordsProcessed);
    	larBatchSummary.setProcessDate(new Date());
    	larsBatchSummaryDao.createBatchSummary(larBatchSummary);
    	LOGGER.info("ItemProcessingHandler: saveSuccessDataFileSummary -- Ends");
    }
	
	private void writeFailedRecords(){
		LOGGER.info("ItemProcessingHandler: convertBeanToDto -- Starts");
		List<CustomerRelationshipInfo> failedRecs = convertBeanToDto(larsuidBatchDao.getRejectedLifestyleRewardRecords());
    	stepContext.put(BatchUtil.DAILY_FAILED_COUNT_KEY, failedRecs.size());
    	LOGGER.debug("Total number of successful records: " + failedRecs.size());
    	
    	try {
    		failedFileWriter.write(failedRecs);
		} catch (Exception e) {
			LOGGER.error("Unable to write failed records to file" + e);
		}
    	saveFailedDataFileSummary(failedRecs.size());
    	LOGGER.info("ItemProcessingHandler: writeFailedRecords -- Ends");
	}
    
	private void saveFailedDataFileSummary(int recordsFailed){
		LOGGER.info("ItemProcessingHandler: convertBeanToDto -- Starts");
		LarBatchSummary larBatchSummary = new LarBatchSummary();
		larBatchSummary.setBatchType(BATCH_TYPE.ERROR_RESPONSE_FILE.toString());
		larBatchSummary.setBatchTypeDescription(BATCH_TYPE_DESCRIPTION.ERROR_RESPONSE.toString());
		larBatchSummary.setFileName(failedFilename);
    	larBatchSummary.setNoOfRecordsFailed(recordsFailed);
		larBatchSummary.setProcessDate(new Date());
		larsBatchSummaryDao.createBatchSummary(larBatchSummary);
		LOGGER.info("ItemProcessingHandler: saveFailedDataFileSummary -- Ends");
	}
	
	private void writeCtlRecords(){
		LOGGER.info("ItemProcessingHandler: convertBeanToDto -- Starts");
    	List<ControlFileInfo> ctlRecords = new ArrayList<ControlFileInfo>();
    	
    	ControlFileInfo succesRec = new ControlFileInfo();
    	succesRec.setKey(BATCH_TYPE.SERVICE_FILE.toString());
    	succesRec.setDescription(successFilename);
    	ctlRecords.add(succesRec);
    	
    	ControlFileInfo failedRec = new ControlFileInfo();
    	failedRec.setKey(BATCH_TYPE.ERROR_RESPONSE_FILE.toString());
    	failedRec.setDescription(failedFilename);
    	ctlRecords.add(failedRec);
    	stepContext.put(BatchUtil.DAILY_CTL_COUNT_KEY, ctlRecords.size());
    	
    	try {
    		ctlFileWriter.write(ctlRecords);
		} catch (Exception e) {
			LOGGER.error("Unable to write ctl records to file"+e);
		}
    	LOGGER.info("ItemProcessingHandler: writeCtlRecords -- Ends");
	}
	
	private List<CustomerRelationshipInfo> convertBeanToDto(List<LifestyleRewardsRecords> lrr)
	{
		LOGGER.info("ItemProcessingHandler: convertBeanToDto -- Starts");
		List<CustomerRelationshipInfo> custRelInfoList = new ArrayList<CustomerRelationshipInfo>();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		
		for (Iterator iterator = lrr.iterator(); iterator.hasNext();) {
			LifestyleRewardsRecords lifestyleRewardsRecords = (LifestyleRewardsRecords) iterator
					.next();
			CustomerRelationshipInfo customerRelationshipInfo = new CustomerRelationshipInfo();
			customerRelationshipInfo.setRecordType(lifestyleRewardsRecords.getRecordType());
			customerRelationshipInfo.setUniqueId(lifestyleRewardsRecords.getUniqueId());
			customerRelationshipInfo.setPartyId(lifestyleRewardsRecords.getPartyId());
			customerRelationshipInfo.setServiceNumber(lifestyleRewardsRecords.getServiceIdentity());
			customerRelationshipInfo.setServiceNumberNew(lifestyleRewardsRecords.getNewServiceIdentity());
			customerRelationshipInfo.setRelationshipFlag(lifestyleRewardsRecords.getRelationalShipFlag());
			customerRelationshipInfo.setEffectiveDate(sdf.format(lifestyleRewardsRecords.getEffectiveDate()));
			customerRelationshipInfo.setExtractDate(sdf.format(lifestyleRewardsRecords.getExtractDate()));
			customerRelationshipInfo.setRejectionReason(lifestyleRewardsRecords.getRejectionReason());
			
			custRelInfoList.add(customerRelationshipInfo);
		}
		LOGGER.info("ItemProcessingHandler: convertBeanToDto -- Ends");
		return custRelInfoList;
	}
	
	@Override
	public void beforeProcess(ControlFileInfo arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onProcessError(ControlFileInfo arg0, Exception arg1) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the dateFormat
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * @param dateFormat the dateFormat to set
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * @return the resFileGenerator
	 */
	public ResponseFileNameGenerator getResFileGenerator() {
		return resFileGenerator;
	}

	/**
	 * @param resFileGenerator the resFileGenerator to set
	 */
	public void setResFileGenerator(ResponseFileNameGenerator resFileGenerator) {
		this.resFileGenerator = resFileGenerator;
	}

	/**
	 * @return the successFilename
	 */
	public String getSuccessFilename() {
		return successFilename;
	}

	/**
	 * @param successFilename the successFilename to set
	 */
	public void setSuccessFilename(String successFilename) {
		this.successFilename = successFilename;
	}

	/**
	 * @return the failedFilename
	 */
	public String getFailedFilename() {
		return failedFilename;
	}

	/**
	 * @param failedFilename the failedFilename to set
	 */
	public void setFailedFilename(String failedFilename) {
		this.failedFilename = failedFilename;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.stepContext = stepExecution.getExecutionContext();
        stepContext.put(BatchUtil.DAILY_SUCCESS_COUNT_KEY, 0);
        stepContext.put(BatchUtil.DAILY_FAILED_COUNT_KEY, 0);
        stepContext.put(BatchUtil.DAILY_CTL_COUNT_KEY, 0);	
        stepContext.put(BatchUtil.INCOMING_FILE_EXIST_KEY, false);
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if(!(Boolean)stepExecution.getExecutionContext().get(BatchUtil.INCOMING_FILE_EXIST_KEY)){
			return new ExitStatus("NO RESPONSE");
		}
	   	
		if(stepExecution.getExitStatus().compareTo(ExitStatus.FAILED) !=0 && stepExecution.getReadCount() == 0){
			return new ExitStatus("NO RECORDS");
    	} 
		List<Throwable> errList = stepExecution.getFailureExceptions();
		for (Iterator iterator = errList.iterator(); iterator.hasNext();) {
			Throwable throwable = (Throwable) iterator.next();
			if(throwable.getCause() instanceof InitialLoadFileExpectedException){
				return new ExitStatus("NO RESPONSE");
			}
		}
    	return stepExecution.getExitStatus();
	}
	
	
	
	

}
