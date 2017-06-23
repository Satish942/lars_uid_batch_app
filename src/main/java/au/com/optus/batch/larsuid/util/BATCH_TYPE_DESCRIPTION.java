/**
 * FileName      : $Id: BATCH_TYPE_DESCRIPTION.java 2014-11-19 06:51:25Z $
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
public enum BATCH_TYPE_DESCRIPTION {
    /**
     * Customer file.
     */
    SUCCESS_RESPONSE("Success response"),
    /**
     * Voucher File.
     */
    ERROR_RESPONSE("Error response"),

    /**
     * Voucher File.
     */
    MKTPR_EXTRACT("Daily marketing profile extract"),

    /**
     * Voucher File.
     */
    INITIAL_LOAD("Initial load"),

    /**
     * Voucher File.
     */
    DAILY_SERVICE("Daily service list"),
    
    /**
     * Voucher File.
     */
    TLC_MIGRATION("TLC migration");

    /**
     * Private value.
     */
    private String value;
    
    /**
     * Constructor.
     * @param value Value
     */
    BATCH_TYPE_DESCRIPTION(String pValue) {
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
