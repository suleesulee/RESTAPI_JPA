package com.example.jpa.board.entity;

import com.example.jpa.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class BoardBookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn
    private User user;

    //북마크 내용
    @Column
    private long boardId;
    @Column
    private long boardType;
    @Column
    private String boardTitle;
    @Column
    private String boardUrl;
    @Column
    private LocalDateTime boardRegDate;

    @Column
    private LocalDateTime regDate;
}
