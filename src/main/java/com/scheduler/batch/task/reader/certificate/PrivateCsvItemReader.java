package com.scheduler.batch.task.reader.certificate;

import com.scheduler.batch.domain.certificate.dto.PrivateInfo;
import com.scheduler.batch.task.reader.CsvFileItemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class PrivateCsvItemReader extends CsvFileItemReader<PrivateInfo> {
    public PrivateCsvItemReader() {
        super("nationalProCsvItemReader",
                new ClassPathResource("data/한국직업능력연구원_민간자격등록정보_20231231.csv"),
                PrivateInfo.class,
                "createDate", "status", "institution", "createNum", "name", "grade", "agency", "summary", "description", "authorizedStatus", "authorizedYn", "coOperation");
    }
}
