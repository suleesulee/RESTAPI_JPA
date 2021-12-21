package com.example.jpa.notice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@Builder@AllArgsConstructor@NoArgsConstructor
public class UserSearch {
    private String email;
    private String userName;
    private String phone;
}
