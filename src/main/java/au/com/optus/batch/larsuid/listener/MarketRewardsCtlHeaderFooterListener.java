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
import java.math.BigDecimal;


import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;


import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;



import au.com.optus.batch.larsuid.bean.MarketRewardsRowMapper;

import au.com.optus.batch.larsuid.util.BATCH_TYPE;
import au.com.optus.batch.larsuid.util.CONTROL_TYPE;
import au.com.optus.batch.larsuid.util.ResponseFileNameGenerator;


/**
 * @author Namrata jain
 *
 */
public class MarketRewardsCtlHeaderFooterListener implements FlatFileFooterCallback, FlatFileHeaderCallback, StepExecutionListener 
     {
	
	@Autowired
	private ResponseFileNameGenerator responseFileNameGenerator;
	
    private FlatFileItemWriter<MarketRewardsRowMapper> delegate;
    
    private int recordCount = 1;
 
    @Override
    public void writeHeader(Writer writer) throws IOException {
         	writer.write("HEADER|" + responseFileNameGenerator.getCtlFileName(CONTROL_TYPE.MARK_PR_EXTRACT.toString())+".CTL");
    }

    @Override
    public void writeFooter(Writer writer) throws IOException {
    	writer.write(responseFileNameGenerator.getDataFileName(CONTROL_TYPE.MARK_PR_EXTRACT.toString(), BATCH_TYPE.MARKETING_FILE.toString())+"\n");
    	writer.write("TRAILER|" + String.format("%010d",recordCount));
    }
    
    public void setDelegate(FlatFileItemWriter<MarketRewardsRowMapper> delegate) {
        this.delegate = delegate;
    }
  
  
	@Override
	public ExitStatus afterStep(StepExecution arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void beforeStep(StepExecution arg0) {
		// TODO Auto-generated method stub
		
	}

	
	
    
   
}
