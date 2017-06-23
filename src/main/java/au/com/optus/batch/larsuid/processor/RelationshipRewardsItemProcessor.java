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

import au.com.optus.batch.larsuid.bean.ControlFileInfo;
import au.com.optus.batch.larsuid.error.LARErrorCodeProcessor;
import au.com.optus.batch.larsuid.error.LARErrorResponse;
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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


/**
 * @author Prity Rani
 *
 */
public class RelationshipRewardsItemProcessor implements ItemProcessor<ControlFileInfo, ControlFileInfo> {   
	
	private String dateFormat;
	private String incomingFileDirectory;
	
	private String fileDelimeter;
	
	@Autowired
    private LarsUidBatchDaoIF larsuidBatchDao;
	
	@Autowired
    private LarsBatchSummaryDaoIF larsBatchSummaryDao;

    @Autowired
    @Qualifier(LarRegistrationServiceFacadeIF.FACADE_ID)
    private LarRegistrationServiceFacadeIF larRegistrationServiceFacade;

    @Autowired
    @Qualifier(LarsuidServiceFacadeIF.FACADE_ID)
    private LarsuidServiceFacadeIF larsuidFacade;

    @Autowired
    private LARErrorCodeProcessor larErrPro;

    /* (non-Javadoc)
     * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
     */
    @Override
    public ControlFileInfo process(ControlFileInfo item) throws Exception {
    	System.out.println("Batch_TYPE: "+item.getKey());
    	// Test to delete 2 lines
    	LARErrorResponse rejectReson1 = larErrPro.getErrorResponse("lar.app.service.error.code.", "LAR-REG-0008");
        System.out.println("rejectReson1 :::" +rejectReson1.getDestinationErrorCode());
    	
    	if(item.getKey().equalsIgnoreCase(BATCH_TYPE.SERVICE_FILE.toString()) && !StringUtils.isEmpty(item.getDescription())){
    		System.out.println("File name: "+item.getDescription());
    		
    		saveDataFileSummary(item.getDescription());
    		   		
    		BufferedReader br = null;
    		LARErrorResponse rejectReson = null;

    		try {
    			String sCurrentLine;
    			br = new BufferedReader(new FileReader(incomingFileDirectory + item.getDescription()));

    			/* Reading .DAT file */
    			while ((sCurrentLine = br.readLine()) != null) {
    				boolean validRecord = true;
    				
    				//ignore header and trailer
    				if(!sCurrentLine.isEmpty() && !sCurrentLine.startsWith("HEADER") && !sCurrentLine.startsWith("TRAILER")){
    					
    					System.out.println("Data record: "+sCurrentLine);
    					
    					String data[] = sCurrentLine.split("\\" + fileDelimeter);
    					
    					LifestyleRewardsRecords lifeReRec = convertToBean(data);
    					larsuidBatchDao.createLifestyleRewardRecords(lifeReRec);
    					
    					if(StringUtils.isEmpty(lifeReRec.getServiceIdentity())){
    						validRecord = false;
    					}
    					else{
    						switch(lifeReRec.getRecordType()){
        					case 10:System.out.println("recort type 10");
    		                        DeregisterLifestyleForServiceRequestDtoModel dRequest =
    		                            new DeregisterLifestyleForServiceRequestDtoModel();
    		
    		                        dRequest.setRewardsService(lifeReRec.getServiceIdentity());
    		                        DeregisterLifestyleForServiceResponseDtoModel model = null;
    		                        try {
    		                            model = larRegistrationServiceFacade.deregisterLifestyleForService(dRequest);
    		
    		                        } catch (LarServiceException ex) {
    		                            System.out.println("Exception Raised on 10 type info" + ex.getExceptionInfo());
    		                            System.out.println("Exception Raised on 10 type faultcode "+ ex.getExceptionInfo().getFaultCode());
    		                            lifeReRec.setStatus(BatchUtil.LIFESTYLE_REL_REC_FAILURE_STATUS);
    		                            rejectReson =
    		                                larErrPro.getErrorResponse("lar.app.service.error.code.", ex.getExceptionInfo()
    		                                    .getFaultCode());
    		                            lifeReRec.setRejectionReason(rejectReson.getDestinationErrorCode());
    		                            larsuidBatchDao.updateLifestyleRewardRecords(lifeReRec);
    		                        }
    		                        break;
        					case 20:System.out.println("recort type 20");
		        					if(StringUtils.isEmpty(lifeReRec.getNewServiceIdentity())){
		        						validRecord = false;
		        					}
		        					else{
		        						ChangeServiceNumberRequestDtoModel changeServiceNumberRequest =
		    		                            new ChangeServiceNumberRequestDtoModel();
		    		                        changeServiceNumberRequest.setNewServiceId(lifeReRec.getNewServiceIdentity());
		    		                        changeServiceNumberRequest.setCurrentServiceId(lifeReRec.getServiceIdentity());
		    		                        ChangeServiceNumberResponseDtoModel cRequest = null;
		    		                        try {
		    		                            cRequest =
		    		                                larRegistrationServiceFacade.changeServiceNumber(changeServiceNumberRequest);
		    		
		    		                        } catch (LarServiceException ex) {
		    		                            System.out.println("Exception Raised on 20 type file" + ex.getExceptionInfo());
		    		                            lifeReRec.setStatus(BatchUtil.LIFESTYLE_REL_REC_FAILURE_STATUS);
		    		                            rejectReson =
		    		                                larErrPro.getErrorResponse("lar.app.service.error.code.", ex.getExceptionInfo()
		    		                                    .getFaultCode());
		    		                            lifeReRec.setRejectionReason(rejectReson.getDestinationErrorCode());
		    		                            larsuidBatchDao.updateLifestyleRewardRecords(lifeReRec);
		    		                        } 
		        					}
    		                        break;
        					case 30:System.out.println("recort type 30");
    		                        GetUIDForSrvNumberRequestDtoModel getUIDForSrvNumber =
    		                            new GetUIDForSrvNumberRequestDtoModel();
    		                        getUIDForSrvNumber.setServiceId(lifeReRec.getServiceIdentity());
    		                        GetUIDForSrvNumberResponseDtoModel response = null;
    		                        try {
    		
    		                            response = larsuidFacade.getUIDForSrvNumber(getUIDForSrvNumber);
    		
    		                            if (!StringUtils.isEmpty(response.getUid())) {
    		                                System.out.println("Lars response.getUid() found.." + response.getUid());
    		                                System.out.println("ServiceIdentity:" +lifeReRec.getServiceIdentity());
    		                                // UID exist  
    		                                UpdateUIDForUidNumberRequestDtoModel updateUIDForUidNumber =
    		                                    new UpdateUIDForUidNumberRequestDtoModel();
    		                                updateUIDForUidNumber.setPartyId(lifeReRec.getPartyId());
    		                                updateUIDForUidNumber.setRelationshipFlag(lifeReRec.getRelationalShipFlag());
    		                                updateUIDForUidNumber.setLifestyleFlag("N");
    		                                updateUIDForUidNumber.setUid(response.getUid());
    		                                updateUIDForUidNumber.setServiceId(lifeReRec.getServiceIdentity());
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
    		                                    System.out.println("Exception Raised on updateUIDForUidNumber"
    		                                        + e.getExceptionInfo());
    		                                    lifeReRec.setStatus(BatchUtil.LIFESTYLE_REL_REC_FAILURE_STATUS);
    		                                    rejectReson =
    		                                        larErrPro.getErrorResponse("lar.app.service.error.code.", e
    		                                            .getExceptionInfo().getFaultCode());
    		                                    lifeReRec.setRejectionReason(rejectReson.getDestinationErrorCode());
    		                                    larsuidBatchDao.updateLifestyleRewardRecords(lifeReRec);
    		                                }
    		                            }
    		
    		                        } catch (LarsuidServiceException e) {
    		                            System.out.println("Lars getUIDForSrvNumber raised..");
    		                            // UID doesn't exist 
    		                            CreateUIDForSrvNumberRequestDtoModel createUIDForSrvNumber =
    		                                new CreateUIDForSrvNumberRequestDtoModel();
    		                            createUIDForSrvNumber.setServiceId(lifeReRec.getServiceIdentity());
    		                            createUIDForSrvNumber.setPartyId(lifeReRec.getPartyId());
    		                            createUIDForSrvNumber.setLifestyleFlag("N");
    		                            createUIDForSrvNumber.setRelationshipFlag(lifeReRec.getRelationalShipFlag());
    		                            CreateUIDForSrvNumberResponseDtoModel modelResponse = null;
    		                            // Create Scenario
    		                            try {
    		                                modelResponse =
    		                                    larsuidFacade.createUIDForSrvNumber(createUIDForSrvNumber);
    		                                // Create is Success
    		                                if (modelResponse != null) {
    		                                    if (modelResponse.getUid() != null) {
    		                                        System.out.println("createUIDForSrvNumber Success");
    		                                        lifeReRec.setUniqueId(modelResponse.getUid());
    		                                        larsuidBatchDao.updateLifestyleRewardRecords(lifeReRec);
    		                                    }
    		                                }
    		
    		                            } catch (LarsuidServiceException ex) {
    		                                // Create is Failed
    		                                System.out.println("Exception Raised on createUIDForSrvNumber"
    		                                    + ex.getExceptionInfo());
    		                                lifeReRec.setStatus(BatchUtil.LIFESTYLE_REL_REC_FAILURE_STATUS);
    		                                rejectReson =
    		                                    larErrPro.getErrorResponse("lar.app.service.error.code.", ex.getExceptionInfo()
    		                                        .getFaultCode());
    		                                lifeReRec.setRejectionReason(rejectReson.getDestinationErrorCode());
    		                                larsuidBatchDao.updateLifestyleRewardRecords(lifeReRec);
    		                            }
    		                        }
    		                        break;
    						default:System.out.println("wrong rec type");
    								
        					}
    					}
    					if(!validRecord){
    						lifeReRec.setStatus(BatchUtil.LIFESTYLE_REL_REC_FAILURE_STATUS);
                            lifeReRec.setRejectionReason("UN_PROCESSED");
                            larsuidBatchDao.updateLifestyleRewardRecords(lifeReRec);
    					}
    					
    				}
    			}

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
    
    private LifestyleRewardsRecords convertToBean(String data[])
    {
    	SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    	LifestyleRewardsRecords lifestyleRewardsRecords = new LifestyleRewardsRecords();
    	
    	if(!StringUtils.isEmpty(data[0])){
    		lifestyleRewardsRecords.setRecordType(Integer.valueOf(data[0]));
    	}
    	lifestyleRewardsRecords.setUniqueId(data[1]);
    	lifestyleRewardsRecords.setPartyId(data[2]);
		lifestyleRewardsRecords.setServiceIdentity(data[3]);
		lifestyleRewardsRecords.setNewServiceIdentity(data[4]);
		lifestyleRewardsRecords.setRelationalShipFlag(data[5]);
		try {
			lifestyleRewardsRecords.setEffectiveDate(sdf.parse(data[6]));
			lifestyleRewardsRecords.setExtractDate(sdf.parse(data[7]));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lifestyleRewardsRecords.setStatus(BatchUtil.LIFESTYLE_REL_REC_SUCCESS_STATUS);
		
		return lifestyleRewardsRecords;
    }
    
    private void saveDataFileSummary(String dataFileName){
    	LarBatchSummary larBatchSummary = new LarBatchSummary();
    	larBatchSummary.setBatchType(BATCH_TYPE.SERVICE_FILE.toString());
    	larBatchSummary.setBatchTypeDescription(BATCH_TYPE_DESCRIPTION.DAILY_SERVICE.toString());
    	larBatchSummary.setFileName(dataFileName);
    	larBatchSummary.setProcessDate(new Date());
    	larsBatchSummaryDao.createBatchSummary(larBatchSummary);
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
