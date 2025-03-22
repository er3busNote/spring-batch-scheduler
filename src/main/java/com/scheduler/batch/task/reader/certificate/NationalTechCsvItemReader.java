package com.scheduler.batch.task.reader.certificate;

import com.scheduler.batch.domain.certificate.dto.NationalTechInfo;
import com.scheduler.batch.task.reader.CsvFileItemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class NationalTechCsvItemReader extends CsvFileItemReader<NationalTechInfo> {
    public NationalTechCsvItemReader() {
        super("nationalTechCsvItemReader",
                new ClassPathResource("data/한국산업인력공단_국가자격종목별 주무부처_20230316.csv"),
                NationalTechInfo.class,
                "name", "type", "agency");
    }
}
