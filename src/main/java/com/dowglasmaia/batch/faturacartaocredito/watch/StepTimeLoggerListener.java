package com.dowglasmaia.batch.faturacartaocredito.watch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
public class StepTimeLoggerListener extends StepExecutionListenerSupport {

    private static final Logger logger = LoggerFactory.getLogger(StepTimeLoggerListener.class);
    private StopWatch stopWatch;

    @Override
    public void beforeStep(StepExecution stepExecution){
        stopWatch = new StopWatch();
        stopWatch.start();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution){
        stopWatch.stop();
        logger.info("Tempo de execução do Step " + stepExecution.getStepName() + ": " + stopWatch.getTotalTimeMillis() + " ms");
        return stepExecution.getExitStatus();
    }
}
