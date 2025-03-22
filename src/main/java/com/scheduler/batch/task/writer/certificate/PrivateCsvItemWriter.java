package com.scheduler.batch.task.writer.certificate;

import com.scheduler.batch.domain.certificate.dto.PrivateInfo;
import com.scheduler.batch.domain.certificate.service.CertificateService;
import com.scheduler.batch.task.writer.BaseItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrivateCsvItemWriter extends BaseItemWriter<PrivateInfo> {

    private final CertificateService certificateService;

    @Override
    protected void saveItem(PrivateInfo privateInfo) {
        this.certificateService.savePrivate(privateInfo);
    }
}
