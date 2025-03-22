package com.scheduler.batch.task.configs;

import com.scheduler.batch.domain.certificate.dto.NationalProInfo;
import com.scheduler.batch.domain.certificate.dto.NationalTechInfo;
import com.scheduler.batch.domain.certificate.dto.Item;
import com.scheduler.batch.domain.certificate.dto.PrivateInfo;
import com.scheduler.batch.task.processor.certificate.NationalProCsvItemProcessor;
import com.scheduler.batch.task.processor.certificate.NationalTechCsvItemProcessor;
import com.scheduler.batch.task.processor.certificate.NationalTechXmlItemProcessor;
import com.scheduler.batch.task.processor.certificate.PrivateCsvItemProcessor;
import com.scheduler.batch.task.reader.certificate.NationalProCsvItemReader;
import com.scheduler.batch.task.reader.certificate.NationalTechCsvItemReader;
import com.scheduler.batch.task.reader.certificate.NationalTechXmlItemReader;
import com.scheduler.batch.task.reader.certificate.PrivateCsvItemReader;
import com.scheduler.batch.task.writer.certificate.NationalProCsvItemWriter;
import com.scheduler.batch.task.writer.certificate.NationalTechCsvItemWriter;
import com.scheduler.batch.task.writer.certificate.NationalTechXmlItemWriter;
import com.scheduler.batch.task.writer.certificate.PrivateCsvItemWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CertificateConfig {

    public static final String JOB_NAME = "certificateJob";
    public static final String STEP_NAME = "certificateStep";
    private static final Integer CHUNK_SIZE = 10;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final NationalTechXmlItemReader nationalTechXmlItemReader;
    private final NationalTechCsvItemReader nationalTechCsvItemReader;
    private final NationalProCsvItemReader nationalProCsvItemReader;
    private final PrivateCsvItemReader privateCsvItemReader;
    private final NationalTechXmlItemProcessor nationalTechXmlItemProcessor;
    private final NationalTechCsvItemProcessor nationalTechCsvItemProcessor;
    private final NationalProCsvItemProcessor nationalProCsvItemProcessor;
    private final PrivateCsvItemProcessor privateCsvItemProcessor;
    private final NationalTechXmlItemWriter nationalTechXmlItemWriter;
    private final NationalTechCsvItemWriter nationalTechCsvItemWriter;
    private final NationalProCsvItemWriter nationalProCsvItemWriter;
    private final PrivateCsvItemWriter privateCsvItemWriter;

    @Bean
    public Job certificateJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(certificateReadyStep(null))
                .next(decider())
                .from(decider()).on("NATIONAL_TECHNICAL_API").to(nationalTechApiStep())
                .from(decider()).on("NATIONAL_TECHNICAL_CSV").to(nationalTechCsvStep())
                .from(decider()).on("NATIONAL_PROFESSIONAL_CSV").to(nationalProCsvStep())
                .from(decider()).on("PRIVATE_CSV").to(privateCsvStep())
                .end()
                .build();
    }

    @Bean
    @JobScope
    public Step certificateReadyStep(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get("certificateReadyStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("자격증 정보 수집 환경이 세팅되었습니다.");
                    log.info(">>>>> requestDate = {}", requestDate);
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step nationalTechApiStep() {
        return stepBuilderFactory.get(STEP_NAME)
                .<Item, Item>chunk(CHUNK_SIZE)
                .reader(nationalTechXmlItemReader)
                .processor(nationalTechXmlItemProcessor)
                .writer(nationalTechXmlItemWriter)
                .build();
    }

    @Bean
    public Step nationalTechCsvStep() {
        return stepBuilderFactory.get(STEP_NAME)
                .<NationalTechInfo, NationalTechInfo>chunk(CHUNK_SIZE)
                .reader(nationalTechCsvItemReader)
                .processor(nationalTechCsvItemProcessor)
                .writer(nationalTechCsvItemWriter)
                .build();
    }

    @Bean
    public Step nationalProCsvStep() {
        return stepBuilderFactory.get(STEP_NAME)
                .<NationalProInfo, NationalProInfo>chunk(CHUNK_SIZE)
                .reader(nationalProCsvItemReader)
                .processor(nationalProCsvItemProcessor)
                .writer(nationalProCsvItemWriter)
                .build();
    }

    @Bean
    public Step privateCsvStep() {
        return stepBuilderFactory.get(STEP_NAME)
                .<PrivateInfo, PrivateInfo>chunk(CHUNK_SIZE)
                .reader(privateCsvItemReader)
                .processor(privateCsvItemProcessor)
                .writer(privateCsvItemWriter)
                .build();
    }

    @Bean
    public JobExecutionDecider decider() {
        return (jobExecution, stepExecution) -> {
            String condition = jobExecution.getJobParameters().getString("code");

            if (condition != null && condition.equals("NT_API")) {
                return new FlowExecutionStatus("NATIONAL_TECHNICAL_API");
            } else if (condition != null && condition.equals("NT_CSV")) {
                return new FlowExecutionStatus("NATIONAL_TECHNICAL_CSV");
            } else if (condition != null && condition.equals("NP_CSV")) {
                return new FlowExecutionStatus("NATIONAL_PROFESSIONAL_CSV");
            } else if (condition != null && condition.equals("P_CSV")) {
                return new FlowExecutionStatus("PRIVATE_CSV");
            } else {
                throw new IllegalArgumentException("파라미터가 잘못 되었습니다.");
            }
        };
    }
}
