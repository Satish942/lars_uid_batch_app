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
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.ResourceAwareItemWriterItemStream;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import au.com.optus.batch.larsuid.bean.ControlFileInfo;
import au.com.optus.batch.larsuid.bean.CustomerRelationshipInfo;
import au.com.optus.batch.larsuid.bean.TlcMigrationInfo;
import au.com.optus.batch.larsuid.exception.InitialLoadFileExpectedException;
import au.com.optus.batch.larsuid.util.BATCH_TYPE;
import au.com.optus.batch.larsuid.util.BATCH_TYPE_DESCRIPTION;
import au.com.optus.batch.larsuid.util.BatchUtil;
import au.com.optus.batch.larsuid.util.CONTROL_TYPE;
import au.com.optus.batch.larsuid.util.ResponseFileNameGenerator;
import au.com.optus.mcas.sdp.bizservice.larsuid.batch.domain.dao.LarsBatchSummaryDaoIF;
import au.com.optus.mcas.sdp.bizservice.larsuid.batch.domain.model.LarBatchSummary;


/**
 * Listener implementation to write records that get skipped during the import
 * process into a separate file. It writes the record that was received as input
 * and appends the validation failure message as the end.
 *
 * If a record gets skipped during read process then we do not get the input
 * record, so it can not be logged into the file. The exception is logged in
 * error log.
 */
public class TlcMigrationProcessingHandler implements
    InitializingBean, ItemProcessListener<ControlFileInfo,ControlFileInfo>, StepExecutionListener {
	
	/**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TlcMigrationProcessingHandler.class);

    /**
     * Step execution context local copy.
     */
    private ExecutionContext stepContext;
    
    /**
     * Item writer for failed records, injected.
     */
    private ResourceAwareItemWriterItemStream<ControlFileInfo> failedFileWriter;
    
    /**
     * Item writer for failed records, injected.
     */
    private ResourceAwareItemWriterItemStream<ControlFileInfo> ctlFileWriter;
    
    private ResponseFileNameGenerator resFileGenerator;
    
    private String failedFilename;
	
	@Autowired
    private LarsBatchSummaryDaoIF larsBatchSummaryDao;

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
        Validate.notNull(failedFileWriter, "File writer must be supplied before using this listener.");
    }

	@Override
	public void afterProcess(ControlFileInfo arg0, ControlFileInfo arg1) {
		
    	if(arg1!= null && arg1.getKey().equals("TRAILER")){  
    		stepContext.put(BatchUtil.INCOMING_FILE_EXIST_KEY, true);
    		failedFilename = resFileGenerator.getDataFileName(CONTROL_TYPE.DAILY_SRV_LIST.toString(), BATCH_TYPE.TLC_MIGRATION_FILE.toString());
    		writeFailedRecords();
    		writeCtlRecords();
    	}
	}
	
	private void writeFailedRecords(){
		LOGGER.info("ItemProcessingHandler: writeFailedRecords -- Starts");
		List<TlcMigrationInfo> failedRecs = (List<TlcMigrationInfo>)stepContext.get(BatchUtil.TLC_MIGRATION_FAILED_LIST_KEY);
    	stepContext.put(BatchUtil.TLC_MIGRATION_FAILED_COUNT_KEY, failedRecs.size());
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
		larBatchSummary.setBatchType(BATCH_TYPE.TLC_MIGRATION_FILE.toString());
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
    	
    	ControlFileInfo failedRec = new ControlFileInfo();
    	failedRec.setKey(BATCH_TYPE.TLC_MIGRATION_FILE.toString());
    	failedRec.setDescription(failedFilename);
    	ctlRecords.add(failedRec);
    	stepContext.put(BatchUtil.TLC_MIGRATION_CTL_COUNT_KEY, ctlRecords.size());
    	
    	try {
    		ctlFileWriter.write(ctlRecords);
		} catch (Exception e) {
			LOGGER.error("Unable to write ctl records to file"+e);
		}
    	LOGGER.info("ItemProcessingHandler: writeCtlRecords -- Ends");
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
        stepContext.put(BatchUtil.TLC_MIGRATION_FAILED_COUNT_KEY, 0);
        stepContext.put(BatchUtil.TLC_MIGRATION_CTL_COUNT_KEY, 0);	
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
