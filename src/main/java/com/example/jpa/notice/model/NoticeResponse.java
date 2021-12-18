package com.example.jpa.notice.model;

import com.example.jpa.notice.entity.Notice;
import com.example.jpa.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NoticeResponse {

    private long id;
    private long regUserId;
    private String regUserName;
    private String title;
    private String contents;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private int hits;
    private int likes;

    public static NoticeResponse of(Notice notice) {
        return NoticeResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .contents(notice.getContents())
                .regDate(notice.getRegDate())
                .updateDate(notice.getUpdateDate())
                .regUserId(notice.getUser().getId())
                .regUserName(notice.getUser().getUserName())
                .hits(notice.getHits())
                .likes(notice.getLikes())
                .build();
    }
}
