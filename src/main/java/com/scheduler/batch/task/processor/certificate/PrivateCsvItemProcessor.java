package com.scheduler.batch.task.processor.certificate;

import com.scheduler.batch.domain.certificate.dto.PrivateInfo;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class PrivateCsvItemProcessor implements ItemProcessor<PrivateInfo, PrivateInfo> {

    @Override
    public PrivateInfo process(PrivateInfo privateInfo) {
        return privateInfo;
    }
}
