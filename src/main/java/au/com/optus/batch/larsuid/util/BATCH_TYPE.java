/**
 * FileName      : $Id: BATCH_TYPE.java 2014-11-19 06:51:25Z $
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
public enum BATCH_TYPE {
    /**
     * Customer file.
     */
    SERVICE_FILE("SVCUGN"),
    /**
     * Voucher File.
     */
    MARKETING_FILE("MARKPR"),

    /**
     * Voucher File.
     */
    ERROR_RESPONSE_FILE("SVCUID"),
    
    /**
     * Voucher File.
     */
    TLC_MIGRATION_FILE("TLCMEM");
    

    /**
     * Private value.
     */
    private String value;

    /**
     * Constructor.
     * @param value Value
     */
    BATCH_TYPE(String pValue) {
        this.value = pValue;
    }

    /**
     * Constructor.
     * @return String value
     */
    public String toString() {
        return value;
    }
};
