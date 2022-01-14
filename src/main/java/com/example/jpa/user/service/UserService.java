package com.example.jpa.user.service;

import com.example.jpa.board.model.ServiceResult;
import com.example.jpa.user.entity.User;
import com.example.jpa.user.model.*;

import java.util.List;

public interface UserService {

    UserSummary getUserStatusCount();

    List<User> getTodayUsers();

    List<UserNoticeCount> getUserNoticeCount();

    List<UserLogCount> getUserLogCount();


    //좋아요를 가장 많이 한 사용자 목록
    List<UserLogCount> getUserBestLike();

    ServiceResult addInterestUser(String email, Long id);

    ServiceResult deleteInterestUser(String email, Long interestId);

    User login(UserLogin userLogin);

    void sendServiceNotice();

    ServiceResult addUser(UserInput userInput);

}
