/**
 * FileName      : $Id: RelationshipRewardsItemProcessor.java 
 *
 * Copyright Notice: ©2004 Singapore Telecom Pte Ltd -- Confidential and Proprietary
 *
 * All rights reserved.
 * This software is the confidential and proprietary information of SingTel Pte Ltd
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with SingTel.
 */
package au.com.optus.batch.larsuid.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import au.com.optus.batch.larsuid.bean.ControlFileInfo;
import au.com.optus.batch.larsuid.bean.TlcMigrationInfo;
import au.com.optus.batch.larsuid.util.BATCH_TYPE;
import au.com.optus.batch.larsuid.util.BATCH_TYPE_DESCRIPTION;
import au.com.optus.batch.larsuid.util.BatchUtil;
import au.com.optus.mcas.sdp.bizservice.larsuid.batch.domain.dao.LarsBatchSummaryDaoIF;
import au.com.optus.mcas.sdp.bizservice.larsuid.batch.domain.dao.LarsUidBatchDaoIF;
import au.com.optus.mcas.sdp.bizservice.larsuid.batch.domain.model.LarBatchSummary;
import au.com.optus.mcas.sdp.bizservice.larsuid.batch.domain.model.LifestyleRewardsRecords;
import au.com.singtel.group.bizservice.larregistration.application.exception.LarServiceException;
import au.com.singtel.group.bizservice.larregistration.application.facade.LarRegistrationServiceFacadeIF;
import au.com.singtel.group.bizservice.larregistration.application.model.ChangeServiceNumberRequestDtoModel;
import au.com.singtel.group.bizservice.larregistration.application.model.ChangeServiceNumberResponseDtoModel;
import au.com.singtel.group.bizservice.larregistration.application.model.DeregisterLifestyleForServiceRequestDtoModel;
import au.com.singtel.group.bizservice.larregistration.application.model.DeregisterLifestyleForServiceResponseDtoModel;
import au.com.singtel.group.bizservice.larsuid.service.exception.LarsuidServiceException;
import au.com.singtel.group.bizservice.larsuid.service.facade.LarsuidServiceFacadeIF;
import au.com.singtel.group.bizservice.larsuid.service.model.CreateUIDForSrvNumberRequestDtoModel;
import au.com.singtel.group.bizservice.larsuid.service.model.CreateUIDForSrvNumberResponseDtoModel;
import au.com.singtel.group.bizservice.larsuid.service.model.GetUIDForSrvNumberRequestDtoModel;
import au.com.singtel.group.bizservice.larsuid.service.model.GetUIDForSrvNumberResponseDtoModel;
import au.com.singtel.group.bizservice.larsuid.service.model.UpdateUIDForUidNumberRequestDtoModel;
import au.com.singtel.group.bizservice.larsuid.service.model.UpdateUIDForUidNumberResponseDtoModel;
import au.com.optus.batch.larsuid.error.LARErrorResponse;
import au.com.optus.batch.larsuid.error.LARErrorCodeProcessor;


/**
 * @author Prity Rani
 *
 */
public class TlcMigrationItemProcessor implements ItemProcessor<ControlFileInfo, ControlFileInfo> { 
    
    private static final Logger LOGGER = LoggerFactory
        .getLogger(TlcMigrationItemProcessor.class);
	
	private String incomingFileDirectory;
	
	private String fileDelimeter;

    /**
     * Step execution context local copy.
     */
    private ExecutionContext stepContext;
	
	@Autowired
    private LarsBatchSummaryDaoIF larsBatchSummaryDao;

    @Autowired
    @Qualifier(LarsuidServiceFacadeIF.FACADE_ID)
    private LarsuidServiceFacadeIF larsuidFacade;
    
    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
		this.stepContext = stepExecution.getExecutionContext();
		stepContext.put(BatchUtil.TLC_MIGRATION_FAILED_LIST_KEY, new ArrayList<TlcMigrationInfo>());
	}

    /* (non-Javadoc)
     * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
     */
    @Override
    public ControlFileInfo process(ControlFileInfo item) throws Exception {
    	System.out.println("Batch_TYPE: "+item.getKey());
    	
    	if(item.getKey().equalsIgnoreCase(BATCH_TYPE.SERVICE_FILE.toString()) && !StringUtils.isEmpty(item.getDescription())){
    		System.out.println("File name: "+item.getDescription());
    		
    		saveDataFileSummary(item.getDescription());
    		   		
    		BufferedReader br = null;
    		List<TlcMigrationInfo> failedRecs = new ArrayList<TlcMigrationInfo>();

    		try {
    			String sCurrentLine;
    			br = new BufferedReader(new FileReader(incomingFileDirectory + item.getDescription()));

    			/* Reading .DAT file */
    			while ((sCurrentLine = br.readLine()) != null) {
    				
    				boolean failed = false;
    				String rejectionReason = "";
    				
    				//ignore header and trailer
    				if(!sCurrentLine.isEmpty() && !sCurrentLine.startsWith("HEADER") && !sCurrentLine.startsWith("TRAILER")){
    					
    					System.out.println("Data record: "+sCurrentLine);
    					
    					String data[] = sCurrentLine.split("\\" + fileDelimeter);
    					
    					TlcMigrationInfo tlcMigrationInfo = convertToBean(data);
    					if(StringUtils.isEmpty(tlcMigrationInfo.getServiceNumber())){
    						failed = true;
    						rejectionReason = "UN_PROCESSED";
    					}
    					else{
    						GetUIDForSrvNumberRequestDtoModel getUIDForSrvNumber =
    	                            new GetUIDForSrvNumberRequestDtoModel();
                            getUIDForSrvNumber.setServiceId(tlcMigrationInfo.getServiceNumber());
                            GetUIDForSrvNumberResponseDtoModel response = null;
                            try {

                                response = larsuidFacade.getUIDForSrvNumber(getUIDForSrvNumber);

                                if (!StringUtils.isEmpty(response.getUid())) {
                                    System.out.println("Lars response.getUid() found.." + response.getUid());
                                    // UID exist  
                                    UpdateUIDForUidNumberRequestDtoModel updateUIDForUidNumber =
                                        new UpdateUIDForUidNumberRequestDtoModel();
                                    updateUIDForUidNumber.setPartyId(tlcMigrationInfo.getPartyId());
                                    //updateUIDForUidNumber.setRelationshipFlag(tlcMigrationInfo.getRelationalShipFlag());
                                    updateUIDForUidNumber.setLifestyleFlag("Y");
                                    updateUIDForUidNumber.setUid(response.getUid());
                                    updateUIDForUidNumber.setServiceId(tlcMigrationInfo.getServiceNumber());
                                    UpdateUIDForUidNumberResponseDtoModel modelResponse = null;
                                    try {
                                        modelResponse =
                                            larsuidFacade.updateUIDForUidNumber(updateUIDForUidNumber);
                                        // Update is Success
                                        if (modelResponse != null) {
                                            if (modelResponse.getStatus().equals("SUCCESS")) {
                                                System.out.println("updateUIDForUidNumber Success");
                                            }
                                        }
                                    } catch (LarsuidServiceException e) {
                                        // Update is Failed
                                    	failed = true;
                                    	rejectionReason = e.getMessage();
                                    }
                                }

                            } catch (LarsuidServiceException e) {
                                System.out.println("Lars getUIDForSrvNumber raised..");
                                // UID doesn't exist 
                                CreateUIDForSrvNumberRequestDtoModel createUIDForSrvNumber =
                                    new CreateUIDForSrvNumberRequestDtoModel();
                                createUIDForSrvNumber.setServiceId(tlcMigrationInfo.getServiceNumber());
                                createUIDForSrvNumber.setPartyId(null);
                                createUIDForSrvNumber.setLifestyleFlag("Y");
                                createUIDForSrvNumber.setRelationshipFlag("N");
                                CreateUIDForSrvNumberResponseDtoModel modelResponse = null;
                                // Create Scenario
                                try {
                                    modelResponse =
                                        larsuidFacade.createUIDForSrvNumber(createUIDForSrvNumber);
                                    // Create is Success
                                    if (modelResponse != null) {
                                        if (modelResponse.getUid() != null) {
                                            System.out.println("createUIDForSrvNumber Success");
                                        }
                                    }
                                } catch (LarsuidServiceException ex) {
                                    // Create is Failed
                                	failed = true;
                                	rejectionReason = e.getMessage();
                                }
                            }
    					}
                        
                        if(failed)
                        {
                        	//TODO: put it into a failed list
                        	tlcMigrationInfo.setRejectionReason(rejectionReason);
                        	failedRecs.add(tlcMigrationInfo);
                        }
    				}
    			}//end of while loop
				stepContext.put(BatchUtil.TLC_MIGRATION_FAILED_LIST_KEY, failedRecs);
    		} catch (IOException e) {
    			e.printStackTrace();
    		} finally {
    			try {
    				if (br != null)br.close();
    			} catch (IOException ex) {
    				ex.printStackTrace();
    			}
    		}
    	}
        return item;
    }
    
    private TlcMigrationInfo convertToBean(String data[])
	{
    	TlcMigrationInfo tlcMigrationInfo = new TlcMigrationInfo();
		tlcMigrationInfo.setUniqueId(data[0]);
		tlcMigrationInfo.setServiceNumber(data[1]);
		tlcMigrationInfo.setLpdFirstName(data[2]);
		tlcMigrationInfo.setLpdLastName(data[3]);
		tlcMigrationInfo.setLpdEmailAddr(data[4]);
		tlcMigrationInfo.setPasswordTxt(data[5]);
		tlcMigrationInfo.setMyAccountFlag(data[6]);
			
		return tlcMigrationInfo;
	}
    
    private void saveDataFileSummary(String dataFileName){
    	LarBatchSummary larBatchSummary = new LarBatchSummary();
    	larBatchSummary.setBatchType(BATCH_TYPE.TLC_MIGRATION_FILE.toString());
    	larBatchSummary.setBatchTypeDescription(BATCH_TYPE_DESCRIPTION.TLC_MIGRATION.toString());
    	larBatchSummary.setFileName(dataFileName);
    	larBatchSummary.setProcessDate(new Date());
    	larsBatchSummaryDao.createBatchSummary(larBatchSummary);
    }

	/**
	 * @return the incomingFileDirectory
	 */
	public String getIncomingFileDirectory() {
		return incomingFileDirectory;
	}

	/**
	 * @param incomingFileDirectory the incomingFileDirectory to set
	 */
	public void setIncomingFileDirectory(String incomingFileDirectory) {
		this.incomingFileDirectory = incomingFileDirectory;
	}

	/**
	 * @return the fileDelimeter
	 */
	public String getFileDelimeter() {
		return fileDelimeter;
	}

	/**
	 * @param fileDelimeter the fileDelimeter to set
	 */
	public void setFileDelimeter(String fileDelimeter) {
		this.fileDelimeter = fileDelimeter;
	}
}
