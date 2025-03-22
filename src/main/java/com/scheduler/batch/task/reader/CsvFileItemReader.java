package com.scheduler.batch.task.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.core.io.Resource;

public class CsvFileItemReader<T> implements ItemReader<T> {

    private final FlatFileItemReader<T> flatFileItemReader;

    protected CsvFileItemReader(String name, Resource resource, Class<T> type, String... fieldNames) {
        this.flatFileItemReader = new FlatFileItemReaderBuilder<T>()
                .name(name)
                .encoding("EUC-KR")
                .resource(resource)
                .delimited()
                .delimiter(",")
                .names(fieldNames)
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<T>() {{
                    setTargetType(type);
                }})
                .build();

        try {
            this.flatFileItemReader.afterPropertiesSet(); // 설정 완료
        } catch (Exception e) {
            throw new RuntimeException("FlatFileItemReader 설정 오류", e);
        }
    }

    @Override
    public T read() throws Exception {
        return this.flatFileItemReader.read();
    }
}
