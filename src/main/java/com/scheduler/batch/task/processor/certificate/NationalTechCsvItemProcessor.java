package com.scheduler.batch.task.processor.certificate;

import com.scheduler.batch.domain.certificate.dto.NationalTechInfo;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class NationalTechCsvItemProcessor implements ItemProcessor<NationalTechInfo, NationalTechInfo> {

    @Override
    public NationalTechInfo process(NationalTechInfo nationalTechInfo) {
        return nationalTechInfo;
    }
}
