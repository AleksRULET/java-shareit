package ru.practicum.shareit.utils.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

public class PageRequestWithOffset extends PageRequest {

    private final int offset;

    public PageRequestWithOffset(int offset, int size, Sort sort) {
        super(offset / size, size, sort);
        this.offset = offset;
    }

    public static PageRequest of(int offset, int size, @NonNull Sort sort) {
        if (offset < 0) {
            throw new IllegalArgumentException("Не может быть ниже нуля");
        }
        return new PageRequestWithOffset(offset, size, sort);
    }

    public static PageRequest of(int offset, int size) {
        return of(offset, size, Sort.unsorted());
    }

    @Override
    public long getOffset() {
        return offset;
    }
}
