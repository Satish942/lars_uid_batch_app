package au.com.optus.batch.larsuid.listener;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang.Validate;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author optus
 *
 */
public class LarHeaderFooterListner implements 
	InitializingBean, FlatFileHeaderCallback, FlatFileFooterCallback, StepExecutionListener {
	
	private String filename;
	
	private String delimiter;
	
	private String recCountKey;
	
	private StepExecution stepExecution;
	
    /**
     * file name prefix.
     */
    private static final String HEADER_KEY = "HEADER";
    
    /**
     * Zero padding format.
     */
    private static final String FOOTER_KEY = "TRAILER";

	@Override
	public void writeHeader(Writer writer) throws IOException {
		writer.write(HEADER_KEY + delimiter + filename);

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Validate.notNull(filename, "File name should not be null.");
		Validate.notNull(delimiter, "Delimeter should not be null.");
		
	}

	@Override
	public void writeFooter(Writer writer) throws IOException {
		String recordCount = String.format("%010d", this.stepExecution.getExecutionContext().get(recCountKey));
		writer.write(FOOTER_KEY + "|" + recordCount);
		
	}

	@Override
	public void beforeStep(StepExecution pStepExecution) {
		this.stepExecution = pStepExecution;		
	}

	@Override
	public ExitStatus afterStep(StepExecution arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @param delimiter the delimiter to set
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	/**
	 * @param recCountKey the recCountKey to set
	 */
	public void setRecCountKey(String recCountKey) {
		this.recCountKey = recCountKey;
	}
	
	

}
