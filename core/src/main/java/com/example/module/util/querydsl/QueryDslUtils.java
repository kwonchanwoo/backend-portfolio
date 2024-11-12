package com.example.module.util.querydsl;

import java.util.Map;

public class QueryDslUtils {
    public static void filterSetting(Map<String, Object> filters) {
        filters.remove("page");
        filters.remove("size");
        filters.remove("sort");
    }
}
