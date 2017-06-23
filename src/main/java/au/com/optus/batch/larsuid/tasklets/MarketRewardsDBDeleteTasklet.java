/**
 * FileName      : $Id: DatabaseDeleteTasklet.java 2014-11-19 06:51:25Z $
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


import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import au.com.optus.mcas.sdp.bizservice.lar.registration.domain.dao.LarRegistrationDaoIF;


/**
 *
 * @author Namrata Jain
 *
 */
public class MarketRewardsDBDeleteTasklet implements Tasklet {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MarketRewardsDBDeleteTasklet.class);

    @Autowired
    private LarRegistrationDaoIF larregistrationdao;
    
    private int lastWeekCalenderDate;
    
    
	public int getLastWeekCalenderDate() {
		return lastWeekCalenderDate;
	}

	public void setLastWeekCalenderDate(int lastWeekCalenderDate) {
		this.lastWeekCalenderDate = lastWeekCalenderDate;
	}


	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
			throws Exception {
		
		
		// TODO Auto-generated method stub
		System.out.println("delete all records from lifestyle table market rewards ");	   
	    Calendar calendar = Calendar.getInstance();	    
	    calendar.add(Calendar.DATE,-lastWeekCalenderDate);
	    java.util.Date weekBack = calendar.getTime();	    
	    java.sql.Date newdate = new java.sql.Date(weekBack.getTime());
	    System.out.println(newdate);
	    try{
		larregistrationdao.deleteAllLifeStyleProfile(weekBack);  
	    }catch (Exception e) {
			// TODO: handle exception
	    	e.printStackTrace();
		}
        return RepeatStatus.FINISHED;
	
		
	}
	

}
