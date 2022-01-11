package com.example.jpa.user.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSummary {

    private long stopUserCount;
    private long usingUserCount;
    private long totalUserCount;

}
