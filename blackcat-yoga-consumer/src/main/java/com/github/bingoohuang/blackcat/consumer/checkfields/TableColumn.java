package com.github.bingoohuang.blackcat.consumer.checkfields;

import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor
public class TableColumn {
    String columnName;
    String columnDefault;
    String columnType;
    String isNullable;
}
