/**
 * FileName      : $Id: RelationshipDbCleanupTasklet.java 2014-11-19 06:51:25Z $
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import au.com.optus.mcas.sdp.bizservice.larsuid.batch.domain.dao.LarsUidBatchDaoIF;

/**
 *
 * @author Prity Rani
 *
 */
public class RelationshipDbCleanupTasklet implements Tasklet {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RelationshipDbCleanupTasklet.class);

    /**
     * Dao reference.
     */
    @Autowired
    private LarsUidBatchDaoIF larsuidBatchDao;
    
    /**
     * Deletes files from the given path.
     * @param contribution contribution
     * @param chunkContext context
     * @return repeat status
     * @throws Exception Runtime error
     *
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    	int deleteStatus = larsuidBatchDao.deleteAllLifestyleRewardRecords();
    	System.out.println("delete status: "+deleteStatus);
        return RepeatStatus.FINISHED;
    }
}
