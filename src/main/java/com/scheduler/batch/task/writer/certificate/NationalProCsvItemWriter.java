package com.scheduler.batch.task.writer.certificate;

import com.scheduler.batch.domain.certificate.dto.NationalProInfo;
import com.scheduler.batch.domain.certificate.service.CertificateService;
import com.scheduler.batch.task.writer.BaseItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NationalProCsvItemWriter extends BaseItemWriter<NationalProInfo> {

    private final CertificateService certificateService;

    @Override
    protected void saveItem(NationalProInfo nationalProInfo) {
        this.certificateService.saveNationalProfessional(nationalProInfo);
    }
}
