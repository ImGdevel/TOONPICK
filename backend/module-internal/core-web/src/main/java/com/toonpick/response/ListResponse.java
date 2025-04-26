package com.toonpick.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ListResponse<T> {
    private final List<T> items;
    private final int size;

    public static <T> ListResponse<T> from(List<T> list) {
        return new ListResponse<>(list, list.size());
    }
}
