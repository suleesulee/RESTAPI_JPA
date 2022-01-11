package com.example.jpa.board.entity;

import com.example.jpa.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class BoardBadReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private long userId;
    @Column
    private String userName;
    @Column
    private String userEmail;


    @Column
    private long boardId;
    @Column
    private String boardTitle;
    @Column
    private long boardUserId;
    @Column
    private String boardContents;
    @Column
    private LocalDateTime boardRegDate;


    @Column
    private String comments;
    @Column
    private LocalDateTime regDate;
}
