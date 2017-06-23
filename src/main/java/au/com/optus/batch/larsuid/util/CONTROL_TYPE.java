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
public enum CONTROL_TYPE {
    /**
     * Customer file.
     */
	DAILY_SRV_LIST("DAILY_SRV_LIST"),
    /**
     * Voucher File.
     */
	MARK_PR_EXTRACT("MARK_PR_EXTRACT");

    /**
     * Private value.
     */
    private String value;

    /**
     * Constructor.
     * @param value Value
     */
    CONTROL_TYPE(String pValue) {
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
