package au.com.optus.batch.larsuid.tasklets;

import au.com.optus.batch.larsuid.util.CONTROL_TYPE;
import au.com.optus.mcas.sdp.bizservice.larsuid.batch.domain.dao.LarsBatchConfigDaoIF;
import au.com.optus.mcas.sdp.bizservice.larsuid.batch.domain.model.LarBatchConfig;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * @author optus
 *
 */
public class FileCountTasklet implements Tasklet, InitializingBean {
    
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileCountTasklet.class);
    
    /**
     * Dao reference.
     */
    @Autowired
    private LarsBatchConfigDaoIF larsBatchConfigDaoIF;
    
    private Resource directory;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(directory, "directory must be set");
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        
        LarBatchConfig larBatchConfig =larsBatchConfigDaoIF.getBatchConfig(CONTROL_TYPE.DAILY_SRV_LIST.toString());
        
        File dir = directory.getFile();
        
        Assert.state(dir.isDirectory());

        File[] files = dir.listFiles();
        
        boolean eotfound = false;
        boolean ctlfound = false;
        List<String> datFileList = new ArrayList<String>();
        
        for (int i = 0; i < files.length; i++) {
            LOGGER.info(files[i].getName());
            if (files[i].getName().endsWith("EOT")){
            eotfound = files[i].getName().endsWith("EOT");
            break;
            }
        }
        for (int i = 0; i < files.length; i++) {
            LOGGER.info(files[i].getName());
            if (files[i].getName().endsWith("CTL")){
            ctlfound = files[i].getName().endsWith("CTL");
            break;
            }
        }
        for (int i = 0; i < files.length; i++) {
            LOGGER.info(files[i].getName());
            if (files[i].getName().endsWith("DAT")){
                datFileList.add(files[i].getName());
            }
        }
        
        LOGGER.info(eotfound+":"+ctlfound);
        if (!eotfound) {
            LOGGER.info(" eot files not found");
            throw new UnexpectedJobExecutionException("files not found ");
        } 
        if (!ctlfound) {
            LOGGER.info("ctl files not found");
            throw new UnexpectedJobExecutionException("files not found ");
        } 
        
        if (eotfound & ctlfound) {
            LOGGER.info("found hurray");
            if(datFileList.size()>0) {
                int dbCounter =  larBatchConfig.getFileCounter();
                int fileCounter = Integer.valueOf(datFileList.get(0).split("_")[2]);
                LOGGER.info("Sequence number from DB: "+dbCounter+":filecounter"+fileCounter);
                if(fileCounter == dbCounter) {
                    return RepeatStatus.FINISHED;
                } else {
                    throw new UnexpectedJobExecutionException("File Sequence number passing is incorrect ");  
                }
            }
        }
        return null;
    }

    public Resource getDirectory() {
        return directory;
    }

    public void setDirectory(Resource directory) {
        this.directory = directory;
    }
    
}
