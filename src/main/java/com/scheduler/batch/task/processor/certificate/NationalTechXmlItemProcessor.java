package com.scheduler.batch.task.processor.certificate;

import com.scheduler.batch.domain.certificate.dto.Item;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class NationalTechXmlItemProcessor implements ItemProcessor<Item, Item> {

    @Override
    public Item process(Item item) {
        return item;
    }
}
