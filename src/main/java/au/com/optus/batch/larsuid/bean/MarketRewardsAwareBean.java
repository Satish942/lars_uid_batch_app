package au.com.optus.batch.larsuid.bean;

import java.io.Serializable;
import java.util.Date;



/**
 * @author Namrata Jain
 *
 */
public class MarketRewardsAwareBean {

	//private Resource resource;
	private String sourceFileName;
	private Long uniqueId;
	private String serviceNumber;
	private String lpdfirstName;
	private String lpdlastName;
	private String lpdemailAddress;
	private String lpdbirthDate;
	private String lpdbusName;
	private String lpdindustryname;
	private int lpdemployeeCount;
	private String lpdaddressText;
	private String lpdaddressStateCode;
	private String lpdaddressPostCode;
	private String lpdlifestyleInterests;
	private String lpdrewardPrefText;
	private String lpdmovieViewPref;
	private String lpdmovieGenrePref;
	private String lpdSubscriptionCount;
	private String lpdchildage;
	private String lpdinfoCollectionDate;
	
	
	public MarketRewardsAwareBean() {
		
		// TODO Auto-generated constructor stub
	}

/*	 @Override
	    public void setResource(Resource resource) {
	        sourceFileName = resource.getFilename();
	    }*/

	    /**
	     * @return the sourceFileName.
	     */
	    public String getSourceFileName() {
	        return sourceFileName;
	    }

	public Long getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(Long uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getServiceNumber() {
		return serviceNumber;
	}

	public void setServiceNumber(String serviceNumber) {
		this.serviceNumber = serviceNumber;
	}

	public String getLpdfirstName() {
		return lpdfirstName;
	}

	public void setLpdfirstName(String lpdfirstName) {
		this.lpdfirstName = lpdfirstName;
	}

	public String getLpdlastName() {
		return lpdlastName;
	}

	public void setLpdlastName(String lpdlastName) {
		this.lpdlastName = lpdlastName;
	}

	public String getLpdemailAddress() {
		return lpdemailAddress;
	}

	public void setLpdemailAddress(String lpdemailAddress) {
		this.lpdemailAddress = lpdemailAddress;
	}

	public String getLpdbirthDate() {
		return lpdbirthDate;
	}

	public void setLpdbirthDate(String lpdbirthDate) {
		this.lpdbirthDate = lpdbirthDate;
	}

	public String getLpdbusName() {
		return lpdbusName;
	}

	public void setLpdbusName(String lpdbusName) {
		this.lpdbusName = lpdbusName;
	}

	public String getLpdindustryname() {
		return lpdindustryname;
	}

	public void setLpdindustryname(String lpdindustryname) {
		this.lpdindustryname = lpdindustryname;
	}

	public int getLpdemployeeCount() {
		return lpdemployeeCount;
	}

	public void setLpdemployeeCount(int lpdemployeeCount) {
		this.lpdemployeeCount = lpdemployeeCount;
	}

	public String getLpdaddressText() {
		return lpdaddressText;
	}

	public void setLpdaddressText(String lpdaddressText) {
		this.lpdaddressText = lpdaddressText;
	}

	public String getLpdaddressStateCode() {
		return lpdaddressStateCode;
	}

	public void setLpdaddressStateCode(String lpdaddressStateCode) {
		this.lpdaddressStateCode = lpdaddressStateCode;
	}

	public String getLpdaddressPostCode() {
		return lpdaddressPostCode;
	}

	public void setLpdaddressPostCode(String lpdaddressPostCode) {
		this.lpdaddressPostCode = lpdaddressPostCode;
	}

	public String getLpdlifestyleInterests() {
		return lpdlifestyleInterests;
	}

	public void setLpdlifestyleInterests(String lpdlifestyleInterests) {
		this.lpdlifestyleInterests = lpdlifestyleInterests;
	}

	public String getLpdrewardPrefText() {
		return lpdrewardPrefText;
	}

	public void setLpdrewardPrefText(String lpdrewardPrefText) {
		this.lpdrewardPrefText = lpdrewardPrefText;
	}

	public String getLpdmovieViewPref() {
		return lpdmovieViewPref;
	}

	public void setLpdmovieViewPref(String lpdmovieViewPref) {
		this.lpdmovieViewPref = lpdmovieViewPref;
	}

	public String getLpdmovieGenrePref() {
		return lpdmovieGenrePref;
	}

	public void setLpdmovieGenrePref(String lpdmovieGenrePref) {
		this.lpdmovieGenrePref = lpdmovieGenrePref;
	}

	public String getLpdSubscriptionCount() {
		return lpdSubscriptionCount;
	}

	public void setLpdSubscriptionCount(String lpdSubscriptionCount) {
		this.lpdSubscriptionCount = lpdSubscriptionCount;
	}

	public String getLpdchildage() {
		return lpdchildage;
	}

	public void setLpdchildage(String lpdchildage) {
		this.lpdchildage = lpdchildage;
	}

	/**
	 * @return the lpdinfoCollectionDate
	 */
	public String getLpdinfoCollectionDate() {
		return lpdinfoCollectionDate;
	}

	/**
	 * @param lpdinfoCollectionDate the lpdinfoCollectionDate to set
	 */
	public void setLpdinfoCollectionDate(String lpdinfoCollectionDate) {
		this.lpdinfoCollectionDate = lpdinfoCollectionDate;
	}
	
	

	

}
