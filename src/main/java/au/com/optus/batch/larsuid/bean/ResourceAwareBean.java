/**
 * FileName      : $Id: ResourceAwareBean.java 2014-11-19 06:51:25Z $
 *
 * Copyright Notice: ©2004 Singapore Telecom Pte Ltd -- Confidential and Proprietary
 *
 * All rights reserved.
 * This software is the confidential and proprietary information of SingTel Pte Ltd
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with SingTel.
 */
package au.com.optus.batch.larsuid.bean;

import java.io.Serializable;
import org.springframework.batch.item.ResourceAware;
import org.springframework.core.io.Resource;

/**
 * @author amitso
 *
 */
public class ResourceAwareBean implements Serializable, ResourceAware {

    private static final long serialVersionUID = -5438597388084136199L;

    /**
     * File name from the record is read.
     */
    private String sourceFileName;

    /**
     * Response code.
     */
    private String responseCode = "";

    /**
     * Response info.
     */
    private String responseInfo = "";

    /**
     * Response date.
     */
    private String responseDate = "";

    /**
     * Response time.
     */
    private String responseTime = "";


    /**
     * Constructor.
     */
    public ResourceAwareBean() {
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.springframework.batch.item.ResourceAware#setResource(org.springframework.core.io.Resource)
     */
    @Override
    public void setResource(Resource resource) {
        sourceFileName = resource.getFilename();
    }

    /**
     * @return the sourceFileName.
     */
    public String getSourceFileName() {
        return sourceFileName;
    }

    /**
     * @return the responseCode.
     */
    public String getResponseCode() {
        return responseCode;
    }

    /**
     * @param responseCode the responseCode to set.
     */
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * @return the responseInfo.
     */
    public String getResponseInfo() {
        return responseInfo;
    }

    /**
     * @param responseInfo the responseInfo to set.
     */
    public void setResponseInfo(String responseInfo) {
        this.responseInfo = responseInfo;
    }

    /**
     * @return the responseDate.
     */
    public String getResponseDate() {
        return responseDate;
    }

    /**
     * @param responseDate the responseDate to set.
     */
    public void setResponseDate(String responseDate) {
        this.responseDate = responseDate;
    }

    /**
     * @return the responseTime.
     */
    public String getResponseTime() {
        return responseTime;
    }

    /**
     * @param responseTime the responseTime to set.
     */
    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

}
