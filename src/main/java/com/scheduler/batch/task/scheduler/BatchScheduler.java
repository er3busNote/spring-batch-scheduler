package com.scheduler.batch.task.scheduler;

import com.scheduler.batch.common.utils.DateUtil;
import com.scheduler.batch.task.configs.CertificateConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final CertificateConfig certificateConfig;

    @Scheduled(cron = "0/40 * * * * ?")
    public void runCertificate() {

        String requestDate = DateUtil.getCurrentDateTime();
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("code", "NT_API")
                .addString("requestDate", requestDate)
                .toJobParameters();
        try {
            jobLauncher.run(certificateConfig.certificateJob(), jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException | JobRestartException e) {
            log.error(e.getMessage());
        }
    }
}
