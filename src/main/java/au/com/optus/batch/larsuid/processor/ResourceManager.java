package au.com.optus.batch.larsuid.processor;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import au.com.singtel.group.bizservice.larsuid.service.facade.LarsuidServiceFacadeIF;
import au.com.singtel.group.bizservice.larsuid.service.model.CreateUIDForSrvNumberRequestDtoModel;
import au.com.singtel.group.bizservice.larsuid.service.model.CreateUIDForSrvNumberResponseDtoModel;

@Component("resourceManager")
@Transactional(propagation =Propagation.REQUIRES_NEW)
public class ResourceManager {
	
	@Autowired
    @Qualifier(LarsuidServiceFacadeIF.FACADE_ID)
    private LarsuidServiceFacadeIF facade;
	 
	public CreateUIDForSrvNumberResponseDtoModel UIDsave(CreateUIDForSrvNumberRequestDtoModel peRule) {
	    CreateUIDForSrvNumberResponseDtoModel model = null;
	    try {
	    	model = facade.createUIDForSrvNumber(peRule);
	    } catch (Exception ex) {
	        System.out.println("Transaction CreateUIDForSrvNumberResponseDtoModel failed to persist");
	    }
	    return model;
	}

}
