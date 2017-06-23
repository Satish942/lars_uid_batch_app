/**
 * FileName      : $Id: IntialLoadItemProcessor.java 
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
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import au.com.optus.batch.larsuid.bean.ControlFileInfo;
import au.com.optus.batch.larsuid.bean.CustomerRelationshipInfo;
import au.com.optus.batch.larsuid.bean.TlcMigrationInfo;
import au.com.optus.batch.larsuid.util.BATCH_TYPE;
import au.com.optus.batch.larsuid.util.BatchUtil;
import au.com.optus.batch.larsuid.bean.ErrorObject;
import au.com.optus.batch.larsuid.bean.ResponseList;
import au.com.optus.mcas.sdp.bizservice.larsuid.domain.dao.hibernate.LarsUidDaoImpl;
import au.com.optus.mcas.sdp.bizservice.larsuid.domain.model.LarsUid;
import au.com.singtel.group.bizservice.larsuid.service.facade.LarsuidServiceFacadeIF;
import au.com.singtel.group.bizservice.larsuid.service.model.CreateUIDForSrvNumberRequestDtoModel;
import au.com.singtel.group.bizservice.larsuid.service.model.CreateUIDForSrvNumberResponseDtoModel;


/**
 * @author optus
 *
 */
public class IntialLoadItemProcessor extends SpringBeanAutowiringSupport implements
    ItemProcessor<ControlFileInfo, ControlFileInfo> {
    
    private static final Logger LOGGER = LoggerFactory
        .getLogger(IntialLoadItemProcessor.class);
    
    private String fileDelimeter;

    private String dateFormat;

    private String incomingFileDirectory;
    
    /**
     * Step execution context local copy.
     */
    private ExecutionContext stepContext;
    
    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.stepContext = stepExecution.getExecutionContext();
        stepContext.put(BatchUtil.INIT_LOAD_FAILED_LIST_KEY, new ArrayList<ErrorObject>());
    }
    
    @Autowired
    @Qualifier("resourceManager")
    private ResourceManager resourceManager;

    /* (non-Javadoc)
     * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
     */
    @Override
    public ControlFileInfo process(ControlFileInfo item) throws Exception {
        LOGGER.info("Batch_TYPE: " + item.getKey());
        if (item.getKey().equalsIgnoreCase(BATCH_TYPE.SERVICE_FILE.toString())) {
            LOGGER.info("File name: " + item.getDescription());
            String genUid = null;
            BufferedReader br = null;
            List<ErrorObject> failedRecs = new ArrayList<ErrorObject>();

            try {
                String sCurrentLine;
                br = new BufferedReader(new FileReader(incomingFileDirectory + item.getDescription()));

                /* Reading .DAT file */
                while ((sCurrentLine = br.readLine()) != null) {
                    if (!sCurrentLine.isEmpty() && !sCurrentLine.startsWith("HEADER")
                        && !sCurrentLine.startsWith("TRAILER")) {

                        LOGGER.info("Data record: " + sCurrentLine);
                        String data[] = sCurrentLine.split("\\|");

                        CustomerRelationshipInfo custRelInfo = convertToBean(data);

                        if (custRelInfo == null) {
                            ErrorObject errObject = new ErrorObject();
                            errObject.setServiceId(sCurrentLine);
                            errObject.setRejectReson("Service-id is not provided");
                            failedRecs.add(errObject);
                        } else {

                            CreateUIDForSrvNumberRequestDtoModel createObj = new CreateUIDForSrvNumberRequestDtoModel();
                            createObj.setServiceId(custRelInfo.getServiceNumber());
                            createObj.setPartyId(custRelInfo.getPartyId());
                            createObj.setLifestyleFlag("N");
                            createObj.setRelationshipFlag(custRelInfo.getRelationshipFlag());

                            CreateUIDForSrvNumberResponseDtoModel model = resourceManager.UIDsave(createObj);
                            genUid = model.getUid();
                            if (!StringUtils.isEmpty(genUid)) {
                                LOGGER.info("I am persisted");
                            } else {
                                LOGGER.info("failed to persist");
                                ErrorObject errObject = new ErrorObject();
                                errObject.setServiceId(sCurrentLine);
                                errObject.setRejectReson("Record provided improper");
                                failedRecs.add(errObject);
                            }
                        }
                    }
                }
                stepContext.put(BatchUtil.INIT_LOAD_FAILED_LIST_KEY, failedRecs);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null)
                        br.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        /*else{
        	return null;
        }*/
        return item;
    }

    public CustomerRelationshipInfo convertToBean(String data[])
    {
        //SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        CustomerRelationshipInfo custRelInfo = new CustomerRelationshipInfo();
        if (!data[0].equals("")) {
            custRelInfo.setRecordType(Integer.valueOf(data[0]));
        }
        if (!data[1].equals("")) {
            custRelInfo.setUniqueId(data[1]);
        }
        if (!data[2].equals("")) {
            custRelInfo.setPartyId(data[2]);
        }
        if(!StringUtils.isEmpty(data[3])) {
        custRelInfo.setServiceNumber(data[3]);
        } else {
          return null;  
        }
        custRelInfo.setServiceNumberNew(data[4]);
        custRelInfo.setRelationshipFlag(data[5]);
        custRelInfo.setEffectiveDate(data[6]);
        custRelInfo.setExtractDate(data[7]);

        return custRelInfo;
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
    
    public String getFileDelimeter() {
        return fileDelimeter;
    }

    public void setFileDelimeter(String fileDelimeter) {
        this.fileDelimeter = fileDelimeter;
    }

}
