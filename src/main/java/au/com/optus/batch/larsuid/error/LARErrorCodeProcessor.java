/**
 * FileName      : $Id: LARErrorCodeProcessor.java 2015-07-06 06:51:25Z $
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

import au.com.optus.batch.larsuid.error.LARErrorResponse;
import java.util.Properties;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;


/**
 * This class is a error code processor which takes in source error code
 * and gives the corresponding destination errorCode and Description.
 * @author dev
 *
 */
@Component
public class LARErrorCodeProcessor {

    @Resource(name = "errorProperties")
    private Properties errorProperties;

    /**
     * Provide the sourceError code and prefixes for the corresponding module.
     * Returns the error response with destination errorCode and Description.
     * @param errorCodeprefix code prefix
     * @param sourceErrorCode error code
     * @param errorDescPrefix desc prefix.
     * @return OSSFErrorResponse
     */
    public LARErrorResponse getErrorResponse(String errorCodeprefix, String sourceErrorCode, String errorDescPrefix)
    {

        String destinationErrorCode = errorProperties.getProperty(errorCodeprefix + sourceErrorCode);
        String destinationErrorDesc = errorProperties.getProperty(errorDescPrefix + destinationErrorCode);
        String sourceErrorDesc = errorProperties.getProperty(errorDescPrefix + sourceErrorCode);

        LARErrorResponse errorResponse = new LARErrorResponse();
        errorResponse.setSourceErrorCode(sourceErrorCode);
        errorResponse.setDestinationErrorCode(destinationErrorCode);
        errorResponse.setDestErrorDescription(destinationErrorDesc);
        errorResponse.setSourceErrorDescription(sourceErrorDesc);

        return errorResponse;

    }

    /**
     * Provide the sourceError code and prefix for the corresponding module.
     * Returns the error response with destination errorCode and Description.
     * @param prefix code and description prefix
     * @param sourceErrorCode source error code.
     * @return OSSFErrorResponse
     */
    public LARErrorResponse getErrorResponse(String prefix, String sourceErrorCode)
    {
        return getErrorResponse(prefix, sourceErrorCode, prefix);
    }
}
