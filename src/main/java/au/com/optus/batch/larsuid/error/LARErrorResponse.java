/**
 * FileName      : $Id: LARErrorResponse.java 2015-07-06 06:51:25Z $
 *
 * Copyright Notice: ©2004 Singapore Telecom Pte Ltd -- Confidential and Proprietary
 *
 * All rights reserved.
 * This software is the confidential and proprietary information of SingTel Pte Ltd
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with SingTel.
 */
package au.com.optus.batch.larsuid.error;

/**
 * @author dev
 *
 */
public class LARErrorResponse {

    private String sourceErrorCode;

    private String destinationErrorCode;

    private String destErrorDescription;

    private String sourceErrorDescription;

    /**
     * @return the sourceErrorCode
     */
    public String getSourceErrorCode() {
        return sourceErrorCode;
    }

    /**
     * @param sourceErrorCode the sourceErrorCode to set
     */
    public void setSourceErrorCode(String sourceErrorCode) {
        this.sourceErrorCode = sourceErrorCode;
    }

    /**
     * @return the destinationErrorCode
     */
    public String getDestinationErrorCode() {
        return destinationErrorCode;
    }

    /**
     * @param destinationErrorCode the destinationErrorCode to set
     */
    public void setDestinationErrorCode(String destinationErrorCode) {
        this.destinationErrorCode = destinationErrorCode;
    }

    /**
     * @return the destErrorDescription
     */
    public String getDestErrorDescription() {
        return destErrorDescription;
    }

    /**
     * @param destErrorDescription the destErrorDescription to set
     */
    public void setDestErrorDescription(String destErrorDescription) {
        this.destErrorDescription = destErrorDescription;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "LARErrorResponse [sourceErrorCode=" + sourceErrorCode + ", destinationErrorCode="
            + destinationErrorCode + ", destErrorDescription=" + destErrorDescription + ", sourceErrorDescription="
            + sourceErrorDescription + "]";
    }

    /**
     * @return the sourceErrorDescription
     */
    public String getSourceErrorDescription() {
        return sourceErrorDescription;
    }

    /**
     * @param sourceErrorDescription the sourceErrorDescription to set
     */
    public void setSourceErrorDescription(String sourceErrorDescription) {
        this.sourceErrorDescription = sourceErrorDescription;
    }

}
