package au.com.optus.batch.larsuid.bean;

import au.com.optus.batch.larsuid.bean.ControlFileInfo;

public class ErrorObject extends ControlFileInfo{
    
    private String serviceId;
    public String getServiceId() {
        return serviceId;
    }
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
    public String getRejectReson() {
        return rejectReson;
    }
    public void setRejectReson(String rejectReson) {
        this.rejectReson = rejectReson;
    }
    private String rejectReson;

}
