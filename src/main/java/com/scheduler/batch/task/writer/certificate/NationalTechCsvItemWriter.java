package com.scheduler.batch.task.writer.certificate;

import com.scheduler.batch.domain.certificate.dto.NationalTechInfo;
import com.scheduler.batch.domain.certificate.service.CertificateService;
import com.scheduler.batch.task.writer.BaseItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NationalTechCsvItemWriter extends BaseItemWriter<NationalTechInfo> {

    private final CertificateService certificateService;

    @Override
    protected void saveItem(NationalTechInfo item) {
        this.certificateService.saveNationalTechnical(item);
    }
}
