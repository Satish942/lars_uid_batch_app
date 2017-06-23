/**
 * FileName      : $Id: BatchUtil.java 2014-11-19 06:51:25Z $
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

/**
 * @author Prity Rani
 *
 */
public final class BatchUtil {

    /**
     * This key will be persisted in the stepExecution which will be used to get the summary
     * of records failed and passed.
     */
    public static final String DAILY_SUCCESS_COUNT_KEY = "DAILY_SUCCESS_COUNT_KEY";

    public static final String DAILY_FAILED_COUNT_KEY = "DAILY_FAILED_COUNT_KEY";

    public static final String DAILY_CTL_COUNT_KEY = "DAILY_CTL_COUNT_KEY";

    public static final String TLC_MIGRATION_CTL_COUNT_KEY = "TLC_MIGRATION_CTL_COUNT_KEY";

    public static final String TLC_MIGRATION_FAILED_COUNT_KEY = "TLC_MIGRATION_FAILED_COUNT_KEY";

    public static final String TLC_MIGRATION_FAILED_LIST_KEY = "TLC_MIGRATION_FAILED_LIST_KEY";

    public static final String INIT_LOAD_CTL_COUNT_KEY = "INIT_LOAD_CTL_COUNT_KEY";

    public static final String INIT_LOAD_FAILED_COUNT_KEY = "INIT_LOAD_FAILED_COUNT_KEY";

    public static final String INIT_LOAD_FAILED_LIST_KEY = "INIT_LOAD_FAILED_LIST_KEY";

    public static final String INCOMING_FILE_EXIST_KEY = "INCOMING_FILE_EXIST_KEY";

    public static final String LIFESTYLE_REL_REC_SUCCESS_STATUS = "0";

    public static final String LIFESTYLE_REL_REC_FAILURE_STATUS = "1";

    /**
     * Private constructor.
     */
    private BatchUtil() {
        // TODO Auto-generated constructor stub
    }
}
