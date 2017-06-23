package au.com.optus.batch.larsuid.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ResponseList {
    
    static List<ErrorObject> errorList=new ArrayList<ErrorObject>();

    public static List<ErrorObject> getErrorList() {
        return errorList;
    }

    public static void setErrorList(List<ErrorObject> errorList) {
        ResponseList.errorList = errorList;
    }

}
