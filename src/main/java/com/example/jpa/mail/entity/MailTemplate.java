package com.example.jpa.mail.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class MailTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String templateId;

    private String title;
    private String contents;

    private String sendEmail;
    private String sendUserName;

    private LocalDateTime regDate;

}
