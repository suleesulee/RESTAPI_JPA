package com.example.jpa.extra.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpenApiResultResponseBodyItemsItem {
    private String dutyAddr;
    private String dutyEtc;
    //엄청많은 변수들.. 생략함
}
