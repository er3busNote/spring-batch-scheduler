package com.scheduler.batch.task.reader.certificate;

import com.scheduler.batch.domain.certificate.dto.Item;
import com.scheduler.batch.domain.certificate.service.CertificateService;
import com.scheduler.batch.task.reader.BaseItemReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NationalTechXmlItemReader extends BaseItemReader<Item> {

    private final CertificateService certificateService;

    @Override
    protected List<Item> loadItems() {
        return certificateService.getNationalTechnical();
    }
}
