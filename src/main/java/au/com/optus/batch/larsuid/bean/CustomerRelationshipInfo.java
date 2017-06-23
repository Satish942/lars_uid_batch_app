/**
 * FileName      : $Id: CustomerRelationshipInfo.java
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

import java.util.Date;

import org.springframework.core.io.Resource;


/**
 * @author Prity Rani
 *
 */
public class CustomerRelationshipInfo extends RelationshipResourceAwareBean {

    /**
     * Serial.
     */
    private static final long serialVersionUID = -7536984924097680367L;

    /**
     * Description.
     */
    private Resource resource;

    /**
     * From resource aware bean.
     */
    private String sourceFileName;
    
    /**
     * File name from the record is read.
     */
    private int recordType;

    /**
     * Response time.
     */
    private String serviceNumberNew;
    
    /**
     * Response time.
     */
    private String relationshipFlag;
    
    /**
     * Response time.
     */
    private String rejectionReason;

    /*@Override
    public void setResource(Resource resource) {
        this.resource = resource;
        this.sourceFileName = resource.getFilename();
    }*/

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

	/**
	 * @return the recordType
	 */
	public int getRecordType() {
		return recordType;
	}

	/**
	 * @param recordType the recordType to set
	 */
	public void setRecordType(int recordType) {
		this.recordType = recordType;
	}

	/**
	 * @return the serviceNumberNew
	 */
	public String getServiceNumberNew() {
		return serviceNumberNew;
	}

	/**
	 * @param serviceNumberNew the serviceNumberNew to set
	 */
	public void setServiceNumberNew(String serviceNumberNew) {
		this.serviceNumberNew = serviceNumberNew;
	}

	/**
	 * @return the relationshipFlag
	 */
	public String getRelationshipFlag() {
		return relationshipFlag;
	}

	/**
	 * @param relationshipFlag the relationshipFlag to set
	 */
	public void setRelationshipFlag(String relationshipFlag) {
		this.relationshipFlag = relationshipFlag;
	}

	/**
	 * @return the rejectionReason
	 */
	public String getRejectionReason() {
		return rejectionReason;
	}

	/**
	 * @param rejectionReason the rejectionReason to set
	 */
	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}
	
    
    
}
