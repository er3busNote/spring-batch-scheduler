package com.scheduler.batch.task.writer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@Slf4j
public abstract class BaseItemWriter<T> implements ItemWriter<T> {

    @Override
    public void write(List<? extends T> items) {
        for (T item : items) {
            try {
                saveItem(item);
            } catch (Exception e) {
                log.error("DB 에러 : " + e.getMessage());
            }
        }
    }

    protected abstract void saveItem(T item) throws Exception;
}
