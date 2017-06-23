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
public class FileArchivingTasklet implements Tasklet {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileArchivingTasklet.class);

    /**
     * Directory where the file will be archived, injected.
     */
    private String archiveDirectory;

    /**
     * Source Directory where the file will be archived, injected.
     */
    private String sourceDirectory;

    /**
     * Deletes files from the given path.
     * @param contribution contribution
     * @param chunkContext context
     * @return repeat status
     * @throws Exception Runtime exception
     *
     */
    @SuppressWarnings("unchecked")
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    	System.out.println("Archieve directory name***********"+archiveDirectory);
    	File sourceFolder = new File(sourceDirectory);
    	if(sourceFolder.isDirectory()){
    		File[] sourceFiles = sourceFolder.listFiles();
    	    for(int i = 0; i < sourceFiles.length; i++) {
    	    	if(!sourceFiles[i].isDirectory()){
        	        File targetFile = new File(archiveDirectory + sourceFiles[i].getName());
        	        FileUtils.moveFile(sourceFiles[i], targetFile);
        	        LOGGER.info("Archiving file from {} to {}", sourceFiles[i], targetFile);
    	    	}
    	    }
    	}
        return RepeatStatus.FINISHED;
    }

    /**
     * @param archiveDirectory the archiveDirectory to set.
     */
    public void setArchiveDirectory(String archiveDirectory) {
        this.archiveDirectory = archiveDirectory;
    }

    /**
     * @param sourceDirectory the sourceDirectory to set.
     */
    public void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

}
