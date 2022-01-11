package com.example.jpa.notice.controller;


import com.example.jpa.notice.entity.UserLoginHistory;
import com.example.jpa.notice.model.ResponseMessage;
import com.example.jpa.notice.model.UserSearch;
import com.example.jpa.notice.model.UserStatusInput;
import com.example.jpa.notice.repository.NoticeRepository;
import com.example.jpa.user.entity.User;
import com.example.jpa.user.model.UserLogCount;
import com.example.jpa.user.model.UserSummary;
import com.example.jpa.user.repository.UserLoginHistoryRepository;
import com.example.jpa.user.repository.UserRepository;
import com.example.jpa.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class ApiAdminUserController {

    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;
    private final UserLoginHistoryRepository userLoginHistoryRepository;

    private final UserService userService;


    /*@GetMapping("/api/admin/user")
    public ResponseMessage userList(){
        List<User> userList = userRepository.findAll();
        long totalUserCount = userRepository.count();

        return ResponseMessage.builder()
                .totalCount(totalUserCount)
                .data(userList)
                .build();
    }*/


    @GetMapping("/api/admin/user/{id}")
    public ResponseEntity<?> userDetail(@PathVariable Long id) {

        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()){
            return new ResponseEntity<>(ResponseMessage.fail("사용자 정보가 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().body(ResponseMessage.success(user));
    }


    /**/

    @GetMapping("/api/admin/user/search")
    public ResponseEntity<?> findUser(@RequestBody UserSearch userSearch) {
        List<User> userList =
                userRepository.findByEmailContainsOrPhoneContainsOrUserNameContains(userSearch.getEmail(),
                        userSearch.getPhone(), userSearch.getUserName());

        return ResponseEntity.ok(ResponseMessage.success(userList));
    }

    /* 사용자의 상태를 변경*/
    @PatchMapping("/api/admin/user/{id}/status")
    public ResponseEntity<?> userStatus(@PathVariable Long id, @RequestBody UserStatusInput userStatusInput) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(!optionalUser.isPresent()){
            return new ResponseEntity<>(ResponseMessage.fail("사용자 정보가 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }

        User user = optionalUser.get();
        user.setStatus(userStatusInput.getStatus());
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    /*사용자 정보를 삭제하는 API 게시글이 존재한다면 삭제할 수 없다.*/
    @DeleteMapping("/api/admin/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        Optional<User> optionalUser = userRepository.findById(id);
        if(!optionalUser.isPresent()){
            return new ResponseEntity<>(ResponseMessage.fail("사용자 정보가 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }

        User user = optionalUser.get();
        if (noticeRepository.countByUser(user) > 0 ){
            return new ResponseEntity<>(ResponseMessage.fail("사용자가 작성한 공지사항이 있습니다."), HttpStatus.BAD_REQUEST);
        }
        
        userRepository.delete(user);

        return ResponseEntity.ok().build();
    }

    /* 사용자의 접속 이력이 저장될때, 접속이력을 조회하는 api*/

    @GetMapping("/api/admin/user/history/login")
    public ResponseEntity<?> userLoginHistory() {
        List<UserLoginHistory> userLoginHistory = userLoginHistoryRepository.findAll();

        return ResponseEntity.ok().body(userLoginHistory);
    }

    /* 사용자의 접속을 제한하는 API*/
    @PatchMapping("/api/admin/user/{id}/lock")
    public ResponseEntity<?> userLock(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(!optionalUser.isPresent()){
            return new ResponseEntity<>(ResponseMessage.fail("사용자 정보가 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }

        User user = optionalUser.get();

        if(user.isLockYn()){
            return new ResponseEntity<>(ResponseMessage.fail("이미 접속제한 된 사용자 입니다."), HttpStatus.BAD_REQUEST);
        }

        user.setLockYn(true);
        userRepository.save(user);

        return ResponseEntity.ok().body(ResponseMessage.success());
    }

    /* 사용자의 접속을 제한하는 해제하는 API*/
    @PatchMapping("/api/admin/user/{id}/unlock")
    public ResponseEntity<?> userUnlock(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(!optionalUser.isPresent()){
            return new ResponseEntity<>(ResponseMessage.fail("사용자 정보가 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }

        User user = optionalUser.get();

        if(user.isLockYn()){
            return new ResponseEntity<>(ResponseMessage.fail("이미 접속제한 해제된 사용자 입니다."), HttpStatus.BAD_REQUEST);
        }

        user.setLockYn(false);
        userRepository.save(user);

        return ResponseEntity.ok().body(ResponseMessage.success());
    }


    /*
    *  전체회원수와, 회원 상태별 회원수에 대한 정보를 리턴하는 API
    * */
    @GetMapping("/api/admin/user/status")
    public ResponseEntity<?> userStatusCount(){

        UserSummary userSummary = userService.getUserStatusCount();

        return ResponseEntity.ok().body(ResponseMessage.success(userSummary));
    }


    @GetMapping("/api/admin/user/today")
    public ResponseEntity<?> todayRegistration(){
        List<User> users = userService.getTodayUsers();

        return ResponseEntity.ok().body(ResponseMessage.success(users));
    }

//    @GetMapping("/api/admin/user/notice/count")
//    public ResponseEntity<?> userNoticeCount(){
//        List<UserNoticeCount> userNoticeCountList = userService.getUserNoticeCount();
//
//        return ResponseEntity.ok().body(ResponseMessage.success(userNoticeCountList));
//    }

    @GetMapping("/api/admin/user/log/count")
    public ResponseEntity<?> userLogCount(){
        List<UserLogCount> userLogCounts = userService.getUserLogCount();

        return ResponseEntity.ok().body(ResponseMessage.success(userLogCounts));
    }

    @GetMapping("/api/admin/user/like/best")
    public ResponseEntity<?> bestLikeCount(){
        List<UserLogCount> userLogCounts = userService.getUserBestLike();

        return ResponseEntity.ok().body(ResponseMessage.success(userLogCounts));
    }

}
