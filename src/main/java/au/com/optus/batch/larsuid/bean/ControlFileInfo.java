/**
 * FileName      : $Id: ControlFileInfo.java 2014-11-19 06:51:25Z $
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

import org.springframework.core.io.Resource;


/**
 * @author Prity Rani
 *
 */
public class ControlFileInfo extends ResourceAwareBean {

    /**
     * Serail.
     */
    private static final long serialVersionUID = -7536984924097680367L;

    /**
     * Header key.
     */
    private String key;

    /**
     * Header value.
     */
    private String description;

    /**
     * Descriptioon.
     */
    private Resource resource;

    /**
     * From resource aware bean.
     */
    private String sourceFileName;

    /**
     * @return the type
     */
    public String getKey() {
        return key;
    }

    /**
     * @param type the type to set
     */
    public void setKey(String type) {
        this.key = type;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
        this.sourceFileName = resource.getFilename();
    }

    /**
     * @return the resource
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * @return the sourceFileName
     */
    public String getSourceFileName() {
        return sourceFileName;
    }
}
