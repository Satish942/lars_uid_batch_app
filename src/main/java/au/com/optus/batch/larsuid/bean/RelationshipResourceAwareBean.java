/**
 * FileName      : $Id: RelationshipResourceAwareBean.java 
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
import java.util.Date;

import org.springframework.batch.item.ResourceAware;
import org.springframework.core.io.Resource;

/**
 * @author Prity Rani
 *
 */
public class RelationshipResourceAwareBean extends ControlFileInfo implements Serializable  {

    private static final long serialVersionUID = -5438597388084136199L;

    /**
     * File name from the record is read.
     */
    private String sourceFileName;

    /**
     * Response code.
     */
    private String uniqueId;

    /**
     * Response info.
     */
    private String partyId;

    /**
     * Response date.
     */
    private String serviceNumber;
    
    /**
     * Response time.
     */
    private String effectiveDate;
    
    /**
     * Response time.
     */
    private String extractDate;

    /**
     * Constructor.
     */
    public RelationshipResourceAwareBean() {
        
    }
    
    /* (non-Javadoc)
     * @see org.springframework.batch.item.ResourceAware#setResource(org.springframework.core.io.Resource)
     */
    /*@Override
    public void setResource(Resource resource) {
        sourceFileName = resource.getFilename();
    }*/

    /**
     * @return the sourceFileName.
     */
    public String getSourceFileName() {
        return sourceFileName;
    }

	/**
	 * @return the uniqueId
	 */
	public String getUniqueId() {
		return uniqueId;
	}

	/**
	 * @param uniqueId the uniqueId to set
	 */
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	/**
	 * @return the partyId
	 */
	public String getPartyId() {
		return partyId;
	}

	/**
	 * @param partyId the partyId to set
	 */
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	/**
	 * @return the serviceNumber
	 */
	public String getServiceNumber() {
		return serviceNumber;
	}

	/**
	 * @param serviceNumber the serviceNumber to set
	 */
	public void setServiceNumber(String serviceNumber) {
		this.serviceNumber = serviceNumber;
	}

	/**
	 * @return the effectiveDate
	 */
	public String getEffectiveDate() {
		return effectiveDate;
	}

	/**
	 * @param effectiveDate the effectiveDate to set
	 */
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * @return the extractDate
	 */
	public String getExtractDate() {
		return extractDate;
	}

	/**
	 * @param extractDate the extractDate to set
	 */
	public void setExtractDate(String extractDate) {
		this.extractDate = extractDate;
	}
    
    

}
