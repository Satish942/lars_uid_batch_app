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
package au.com.optus.batch.larsuid.listener;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;

import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;

import org.springframework.beans.factory.annotation.Autowired;


import au.com.optus.batch.larsuid.util.BATCH_TYPE;

import au.com.optus.batch.larsuid.util.CONTROL_TYPE;
import au.com.optus.batch.larsuid.util.ResponseFileNameGenerator;


/**
 * @author Namrata Jain
 *
 */
public class MarketRewardsHeaderFooterListener extends StepExecutionListenerSupport implements FlatFileFooterCallback, FlatFileHeaderCallback 
     {
	@Autowired
	private ResponseFileNameGenerator responseFileNameGenerator;
    
    private StepExecution stepExecution;
 
   @Override
    public void writeHeader(Writer writer) throws IOException {
        	writer.write("HEADER|" + responseFileNameGenerator.getDataFileName(CONTROL_TYPE.MARK_PR_EXTRACT.toString(), BATCH_TYPE.MARKETING_FILE.toString()));
    }
    
    @Override
    public void writeFooter(Writer writer) throws IOException {
    	System.out.println( "trailer display "+String.format("%010d", stepExecution.getWriteCount()));
    	stepExecution.getJobExecution().getExecutionContext().put("recordProcess", stepExecution.getWriteCount());
	     writer.write("TRAILER|"+ String.format("%010d", stepExecution.getWriteCount()));
    	    	
    }
    
    @Override
	public void beforeStep(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}
    
     
}
