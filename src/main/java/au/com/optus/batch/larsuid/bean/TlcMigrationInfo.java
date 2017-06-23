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

import org.springframework.core.io.Resource;


/**
 * @author Prity Rani
 *
 */
public class TlcMigrationInfo extends RelationshipResourceAwareBean {

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
    private String  lpdFirstName;

    /**
     * Response time.
     */
    private String lpdLastName;
    
    /**
     * Response time.
     */
    private String lpdEmailAddr;
    
    /**
     * Response time.
     */
    private String passwordTxt;
    
    /**
     * Response time.
     */
    private String myAccountFlag;
    
    private String rejectionReason;

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

	public String getLpdFirstName() {
		return lpdFirstName;
	}

	public void setLpdFirstName(String lpdFirstName) {
		this.lpdFirstName = lpdFirstName;
	}

	public String getLpdLastName() {
		return lpdLastName;
	}

	public void setLpdLastName(String lpdLastName) {
		this.lpdLastName = lpdLastName;
	}

	public String getLpdEmailAddr() {
		return lpdEmailAddr;
	}

	public void setLpdEmailAddr(String lpdEmailAddr) {
		this.lpdEmailAddr = lpdEmailAddr;
	}

	public String getPasswordTxt() {
		return passwordTxt;
	}

	public void setPasswordTxt(String passwordTxt) {
		this.passwordTxt = passwordTxt;
	}

	public String getMyAccountFlag() {
		return myAccountFlag;
	}

	public void setMyAccountFlag(String myAccountFlag) {
		this.myAccountFlag = myAccountFlag;
	}

	public void setSourceFileName(String sourceFileName) {
		this.sourceFileName = sourceFileName;
	}

	/**
	 * @return the rejectionReson
	 */
	public String getRejectionReason() {
		return rejectionReason;
	}

	/**
	 * @param rejectionReson the rejectionReson to set
	 */
	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}
    
    
	
    
    
}
