package com.scheduler.batch.task.reader;

import org.springframework.batch.item.ItemReader;

import java.util.Iterator;
import java.util.List;

public abstract class BaseItemReader<T> implements ItemReader<T> {

    private Iterator<T> itemList;

    protected abstract List<T> loadItems();

    @Override
    public T read() {
        if (itemList == null) {
            itemList = loadItems().iterator();
        }
        return itemList.hasNext() ? itemList.next() : null;
    }
}
