/**
 * FileName      : $Id: BaseItemProcessor.java 2014-11-19 06:51:25Z $
 *
 * Copyright Notice: ©2004 Singapore Telecom Pte Ltd -- Confidential and Proprietary
 *
 * All rights reserved.
 * This software is the confidential and proprietary information of SingTel Pte Ltd
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with SingTel.
 */
package au.com.optus.batch.larsuid.processor;

import au.com.optus.batch.larsuid.bean.RelationshipResourceAwareBean;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;


/**
 * @author Prity Rani
 *
 */
public abstract class BaseItemProcessor implements ItemProcessor<RelationshipResourceAwareBean, RelationshipResourceAwareBean> {

    /**
     * Validator.
     */
    private Validator validator;

    /**
     * Binds the result and validates.
     * @param item Item to check
     * @return binding result
     */
    protected BindingResult bindAndValidate(RelationshipResourceAwareBean item) {
        DataBinder binder = new DataBinder(item);
        binder.setValidator(validator);

        binder.validate();

        return binder.getBindingResult();
    }

    /**
     * Converts error to messages string.
     * @param results Binding results
     * @return String message
     */
    protected String buildValidationException(BindingResult results) {
        StringBuilder msg = new StringBuilder();
        for (ObjectError error : results.getAllErrors()) {
            msg.append(error.getDefaultMessage());
        }
        return msg.toString();
    }

    /**
     * @param validator the validator to set
     */
    public void setValidator(Validator validator) {
        this.validator = validator;
    }
}
