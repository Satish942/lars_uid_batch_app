/**
 * FileName      : $Id: FileArchivingTasklet.java 2014-11-19 06:51:25Z $
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

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;


/**
 *
 * @author Prity Rani
 *
 */
public class FileDeletingTasklet implements Tasklet {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileDeletingTasklet.class);

    /**
     * Directory where the file will be archived, injected.
     */
    private String outgoingDirectory;

    /**
     * Deletes files from the given path.
     * @param contribution contribution
     * @param chunkContext context
     * @return repeat status
     * @throws Exception Runtime exception
     *
     */
    
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    	
    	File outgoingFolder = new File(outgoingDirectory);
    	if(outgoingFolder.isDirectory()){
    		File[] sourceFiles = outgoingFolder.listFiles();
    	    for(int i = 0; i < sourceFiles.length; i++) {
    	    	FileUtils.forceDelete(new File(outgoingDirectory + sourceFiles[i].getName()));
    	    	LOGGER.info("Deleting file from {} ", sourceFiles[i]);
    	    }
    	}
        return RepeatStatus.FINISHED;
    }

	/**
	 * @param outgoingDirectory the outgoingDirectory to set
	 */
	public void setOutgoingDirectory(String outgoingDirectory) {
		this.outgoingDirectory = outgoingDirectory;
	}
    

}
