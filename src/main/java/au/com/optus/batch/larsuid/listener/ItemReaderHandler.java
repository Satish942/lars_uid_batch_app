package au.com.optus.batch.larsuid.listener;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.beans.factory.annotation.Autowired;

import au.com.optus.batch.larsuid.bean.ControlFileInfo;
import au.com.optus.batch.larsuid.exception.InitialLoadFileExpectedException;
import au.com.optus.batch.larsuid.exception.LarsBatchValidationException;
import au.com.optus.batch.larsuid.util.CONTROL_TYPE;
import au.com.optus.batch.larsuid.util.ResponseFileNameGenerator;
import au.com.optus.mcas.sdp.bizservice.larsuid.batch.domain.dao.LarsBatchConfigDaoIF;
import au.com.optus.mcas.sdp.bizservice.larsuid.batch.domain.model.LarBatchConfig;

public class ItemReaderHandler implements ItemReadListener<ControlFileInfo> {
	
	/**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemReaderHandler.class);
	
	private String incomingDirectory;
	
	@Autowired
	private LarsBatchConfigDaoIF larsBatchConfigDao;

	@Override
	public void afterRead(ControlFileInfo ctlFile)  {
		
		if(ctlFile.getKey().equals("HEADER")){
			LOGGER.debug("Source file name: "+ctlFile.getSourceFileName());
			if(new File(incomingDirectory+ctlFile.getSourceFileName().replace(".CTL", ".EOT")).exists()){
				//Verify sequence number
				LarBatchConfig larBatchConfig =larsBatchConfigDao.getBatchConfig(CONTROL_TYPE.DAILY_SRV_LIST.toString());
				LOGGER.debug("Sequence number from DB: "+larBatchConfig.getFileCounter());
				int dbCounter =  larBatchConfig.getFileCounter();
						
				if(dbCounter > 1)
				{
					int fileCounter = Integer.valueOf(ctlFile.getSourceFileName().split("_")[2]);
					if(dbCounter != fileCounter)
					{
						LOGGER.debug("File sequence number is not the next sequence number");
						throw new LarsBatchValidationException("File Verification failed! File sequence number is not the next sequence number.");
					}
				}
				else if(dbCounter == 1){
					//Initial load file expected
					LOGGER.debug("Initial load file expected");
					throw new InitialLoadFileExpectedException("File Verification failed! Initial load file expected.");
				}
			}
			else
			{
				LOGGER.debug("EOT file does not exist");
				throw new LarsBatchValidationException("File Verification failed! EOT file does not exist.");
			}
		}
	}

	@Override
	public void beforeRead() {
		
		
	}

	@Override
	public void onReadError(Exception arg0) {
		
	}

	/**
	 * @param incomingDirectory the incomingDirectory to set
	 */
	public void setIncomingDirectory(String incomingDirectory) {
		this.incomingDirectory = incomingDirectory;
	}
	
	

}

