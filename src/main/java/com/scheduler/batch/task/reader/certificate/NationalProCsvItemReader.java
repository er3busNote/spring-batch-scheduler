package com.scheduler.batch.task.reader.certificate;

import com.scheduler.batch.domain.certificate.dto.NationalProInfo;
import com.scheduler.batch.task.reader.CsvFileItemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class NationalProCsvItemReader extends CsvFileItemReader<NationalProInfo> {
    public NationalProCsvItemReader() {
        super("nationalProCsvItemReader",
                new ClassPathResource("data/한국직업능력연구원_국가전문자격정보_20230906.csv"),
                NationalProInfo.class,
                "name", "law", "institution", "institutionUrl", "agency", "agencyUrl", "purpose", "effect", "grade", "description", "requirements", "qualification", "receptionDetail", "process", "subject", "criteria", "career", "exemptions");
    }
}
