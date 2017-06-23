package au.com.optus.batch.larsuid.bean;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;


import org.springframework.jdbc.core.RowMapper;

/**
 * @author Namrata jain
 *
 */

public class MarketRewardsRowMapper implements RowMapper<MarketRewardsAwareBean>{
	
	private String dateFormat;
	
		
    public String getDateFormat() {
		return dateFormat;
	}


	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}


	public MarketRewardsAwareBean mapRow(ResultSet rs, int rowNum) throws SQLException {    	
    	
    	SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    
    	MarketRewardsAwareBean result = new MarketRewardsAwareBean();
    	result.setUniqueId(rs.getLong("uniqueId")); 
    	result.setServiceNumber(rs.getString("serviceNumber"));
    	result.setLpdfirstName(rs.getString("lpdfirstName"));
    	result.setLpdlastName(rs.getString("lpdlastName"));
    	result.setLpdemailAddress(rs.getString("lpdemailAddress"));
    	if(rs.getString("lpdbirthDate") != null){
       		result.setLpdbirthDate(sdf.format(rs.getDate("lpdbirthDate")));        		
        	}    	
    	result.setLpdbusName(rs.getString("lpdbusName"));
    	result.setLpdindustryname(rs.getString("lpdindustryname"));
    	result.setLpdemployeeCount(rs.getInt("lpdemployeeCount"));
    	result.setLpdaddressText(rs.getString("lpdaddressText"));
    	result.setLpdaddressStateCode(rs.getString("lpdaddressStateCode"));
    	result.setLpdaddressPostCode(rs.getString("lpdaddressPostCode"));
    	result.setLpdlifestyleInterests(rs.getString("lpdlifestyleInterests"));
    	result.setLpdrewardPrefText(rs.getString("lpdrewardPrefText"));
    	result.setLpdmovieViewPref(rs.getString("lpdmovieViewPref"));
    	result.setLpdmovieGenrePref(rs.getString("lpdmovieGenrePref"));
    	result.setLpdSubscriptionCount(rs.getString("lpdSubscriptionCount"));
    	result.setLpdchildage(rs.getString("lpdchildage"));
    	
    	if(rs.getString("lpdinfoCollectionDate") != null){
    		result.setLpdinfoCollectionDate(sdf.format(rs.getDate("lpdinfoCollectionDate")));
 		}
    	
        return result;
       
       
    }
    
}
