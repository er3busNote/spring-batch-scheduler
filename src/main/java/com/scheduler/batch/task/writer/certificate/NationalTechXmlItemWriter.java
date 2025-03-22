package com.scheduler.batch.task.writer.certificate;

import com.scheduler.batch.domain.certificate.dto.Item;
import com.scheduler.batch.domain.certificate.service.CertificateService;
import com.scheduler.batch.task.writer.BaseItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NationalTechXmlItemWriter extends BaseItemWriter<Item> {

    private final CertificateService certificateService;

    @Override
    protected void saveItem(Item item) {
        this.certificateService.saveNationalTechnical(item);
    }
}
