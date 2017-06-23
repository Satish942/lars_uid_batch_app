/**
 * FileName      : $Id: CustomerItemDbMapper.java 2014-11-19 06:51:25Z $
 *
 * Copyright Notice: ©2004 Singapore Telecom Pte Ltd -- Confidential and Proprietary
 *
 * All rights reserved.
 * This software is the confidential and proprietary information of SingTel Pte Ltd
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with SingTel.
 */
package au.com.optus.batch.larsuid.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import au.com.optus.mcas.sdp.bizservice.larsuid.batch.domain.dao.LarsBatchConfigDaoIF;
import au.com.optus.mcas.sdp.bizservice.larsuid.batch.domain.model.LarBatchConfig;


/**
 * @author Prity Rani
 *
 */
public class ResponseFileNameGenerator {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseFileNameGenerator.class);

    /**
     * Date format from property file.
     */
    private String dateFormatInFileName;

    @Autowired
    private LarsBatchConfigDaoIF larsBatchConfigDaoIF;

    /**
     * file name prefix.
     */
    private static final String TARGET_SYS_PREFIX = "UA_";

    /**
     * Zero padding format.
     */
    private static final String TRAILER_ZERO_PADDING = "%06d";

    /**
     * Zero padding format.
     */
    private static final String SUB_SEQUENCE_NUMBER = "001";

    /**
     * Zero padding format.
     */
    private static final String SERVICE_BLOCK_NUMBER = "01";

    /**
     * Zero padding format.
     */
    private static final String MAKETING_BLOCK_NUMBER = "02";

    /**
     * Zero padding format.
     */
    private static final String VERSION_NUMBER = "01";

    /**
     * Zero padding format.
     */
    private static final String FILE_NAME_SEPERATOR = "_";

    /**
     * Zero padding format.
     */
    private static final String DATA_FILE_EXTENSION = ".DAT";
    /**
     * Getter for customer response file name.
     * @return File name
     */
    public String getDataFileName(String controlType, String batchType) {
        //Appending target system
        StringBuffer fileName = new StringBuffer(TARGET_SYS_PREFIX);

        //Appending extract date i.e. today
        SimpleDateFormat sdf = new SimpleDateFormat(this.dateFormatInFileName);
        String date = sdf.format(new Date());
        fileName.append(date);
        fileName.append(FILE_NAME_SEPERATOR);

        //Appending run number
        LarBatchConfig larBatchConfig = larsBatchConfigDaoIF.getBatchConfig(controlType);
        fileName.append(String.format(TRAILER_ZERO_PADDING, larBatchConfig.getFileCounter()));
        fileName.append(FILE_NAME_SEPERATOR);

        //Appending file type
        //fileName.append(BATCH_TYPE.SERVICE_FILE.toString());
        fileName.append(batchType);
        fileName.append(FILE_NAME_SEPERATOR);

        //Appending schedule sequence number (same as run number)
        fileName.append(String.format(TRAILER_ZERO_PADDING, larBatchConfig.getFileCounter()));
        fileName.append(FILE_NAME_SEPERATOR);

        //Appending sub sequence number (always 001 since file size will be less than 700kB)
        fileName.append(SUB_SEQUENCE_NUMBER);
        fileName.append(FILE_NAME_SEPERATOR);

        //Appending Block number (for daily batch 01 and marketing 02)
        if(controlType.equals(CONTROL_TYPE.DAILY_SRV_LIST.toString())){
        	fileName.append(SERVICE_BLOCK_NUMBER);
        }
        else{
        	fileName.append(MAKETING_BLOCK_NUMBER);
        }
        
        fileName.append(FILE_NAME_SEPERATOR);

        //Appending version number
        fileName.append(VERSION_NUMBER);

        //Appending File extension
        fileName.append(DATA_FILE_EXTENSION);

        System.out.println("Success Response file name: " + fileName.toString());

        LOGGER.info("Response file name generated is {}", fileName.toString());
        return fileName.toString();
    }

    /**
     * Getter for customer response file name.
     * @return File name
     */
    public String getCtlFileName(String controlType) {
        //Appending target system
        StringBuffer fileName = new StringBuffer(TARGET_SYS_PREFIX);

        //Appending extract date i.e. today
        SimpleDateFormat sdf = new SimpleDateFormat(this.dateFormatInFileName);
        String date = sdf.format(new Date());
        fileName.append(date);
        fileName.append(FILE_NAME_SEPERATOR);

        //Appending run number
        LarBatchConfig larBatchConfig = larsBatchConfigDaoIF.getBatchConfig(controlType);
        fileName.append(String.format(TRAILER_ZERO_PADDING, larBatchConfig.getFileCounter()));
        fileName.append(FILE_NAME_SEPERATOR);

        //Appending Block number (for daily batch 01 and marketing 02)
        if(controlType.equals(CONTROL_TYPE.DAILY_SRV_LIST.toString())){
        	fileName.append(SERVICE_BLOCK_NUMBER);
        }
        else{
        	fileName.append(MAKETING_BLOCK_NUMBER);
        }
        fileName.append(FILE_NAME_SEPERATOR);

        //Appending version number
        fileName.append(VERSION_NUMBER);

        System.out.println("CTL file name: " + fileName.toString());

        LOGGER.info("Response file name generated is {}", fileName.toString());
        return fileName.toString();
    }

	/**
	 * @return the dateFormatInFileName
	 */
	public String getDateFormatInFileName() {
		return dateFormatInFileName;
	}

	/**
	 * @param dateFormatInFileName the dateFormatInFileName to set
	 */
	public void setDateFormatInFileName(String dateFormatInFileName) {
		this.dateFormatInFileName = dateFormatInFileName;
	}

}
