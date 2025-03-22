package com.scheduler.batch.task.processor.certificate;

import com.scheduler.batch.domain.certificate.dto.NationalProInfo;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class NationalProCsvItemProcessor implements ItemProcessor<NationalProInfo, NationalProInfo> {

    @Override
    public NationalProInfo process(NationalProInfo nationalProInfo) {
        return nationalProInfo;
    }
}
