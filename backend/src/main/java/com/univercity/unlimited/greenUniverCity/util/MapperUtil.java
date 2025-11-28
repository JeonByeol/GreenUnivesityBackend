package com.univercity.unlimited.greenUniverCity.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class MapperUtil {
    public static void updateFrom(Object source, Object target, List<String> excludeColumns) {
        Field[] targetFields = target.getClass().getDeclaredFields();
        Field[] sourceFields = source.getClass().getDeclaredFields();

        List<String> fieldNames = Arrays.stream(targetFields)
                .map(Field::getName)
                .toList();

        for (Field sourceField : sourceFields) {
            sourceField.setAccessible(true);
            try {
                String fieldName = sourceField.getName();

                // PK 같은 건 제외하고 싶으면 여기서 걸러줌
                if (excludeColumns.contains(fieldName)) {
                    continue;
                }

                Object value = sourceField.get(source);

                if (value != null && fieldNames.contains(fieldName)) {
                    Method setter = target.getClass().getMethod(
                            "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1),
                            sourceField.getType()
                    );
                    setter.invoke(target, value);
                }
            } catch (Exception e) {
                log.error("필드 매핑 중 오류: {}", e.getMessage());
            }
        }
    }

}
