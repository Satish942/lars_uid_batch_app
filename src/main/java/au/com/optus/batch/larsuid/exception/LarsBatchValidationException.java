/**
 * FileName      : $Id: LarsuidValidationException.java
 *
 * Copyright Notice: ©2004 Singapore Telecom Pte Ltd -- Confidential and Proprietary
 *
 * All rights reserved.
 * This software is the confidential and proprietary information of SingTel Pte Ltd
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with SingTel.
 */
package au.com.optus.batch.larsuid.exception;

import au.com.optus.mcas.core.exception.UncheckedException;

/**
 * @author Prity Rani
 *
 */
public class LarsBatchValidationException extends UncheckedException {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -361552856686879288L;

    /**
     * Constructor.
     * @param message Message
     */
    public LarsBatchValidationException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * Constructor.
     * @param cause Cause
     */
    public LarsBatchValidationException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * Constructor.
     * @param message Message
     * @param cause Cause
     */
    public LarsBatchValidationException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}
