package com.example.jpa.extra.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpenApiResultResponseBodyItems {
    private List<OpenApiResultResponseBodyItemsItem> item;
    private int numberOfRows;
    private int pageNo;
    private int totalCount;
}
