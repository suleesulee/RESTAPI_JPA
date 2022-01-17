package com.example.jpa.user.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.example.jpa.board.entity.Board;
import com.example.jpa.board.entity.BoardComment;
import com.example.jpa.board.model.ServiceResult;
import com.example.jpa.board.service.BoardService;
import com.example.jpa.common.exception.BizException;
import com.example.jpa.common.model.ResponseResult;
import com.example.jpa.notice.entity.Notice;
import com.example.jpa.notice.entity.NoticeLike;
import com.example.jpa.notice.model.NoticeResponse;
import com.example.jpa.notice.model.ResponseError;
import com.example.jpa.notice.repository.NoticeLikeRepository;
import com.example.jpa.notice.repository.NoticeRepository;
import com.example.jpa.user.entity.User;
import com.example.jpa.user.exception.ExistsEmailException;
import com.example.jpa.user.exception.PasswordNotMatchException;
import com.example.jpa.user.exception.UserNotFoundException;
import com.example.jpa.user.model.*;
import com.example.jpa.user.repository.UserRepository;
import com.example.jpa.user.service.PointService;
import com.example.jpa.user.service.UserService;
import com.example.jpa.util.JWTUtils;
import com.example.jpa.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ApiUserController {

    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;
    private final NoticeLikeRepository noticeLikeRepository;
    private final BoardService boardService;

    private final PointService pointService;
    private final UserService userService;

    /*Valid를 이용해서 Userinput에서 잘못된 값들이 들어오는 부분에 대한 에러 처리*/
    /*@PostMapping("/api/user")
    public ResponseEntity addUser(@RequestBody @Valid UserInput userInput, Errors errors) {

        List<ResponseError> responseErrorList = new ArrayList<>();

        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(e->{
                responseErrorList.add(ResponseError.of((FieldError) e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().build();
    }*/


    /*User를 추가하는 로직, 에러 로직을 타지 않게 되면 UserInput의 내용을 데이터베이스에 저장한다.*/
    /*@PostMapping("/api/user")
    public ResponseEntity addUser(@RequestBody @Valid UserInput userInput, Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(e -> {
                responseErrorList.add(ResponseError.of((FieldError) e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        User user = User.builder()
                .email(userInput.getEmail())
                .userName(userInput.getUserName())
                .password(userInput.getPassword())
                .phone(userInput.getPhone())
                .regDate(LocalDateTime.now())
                .build();

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }*/

    /* Put Method를 통한 일부 정보의 업데이트, InputUser가 아닌 UpdateUser를 통한 정보 업데이트 => 필요없는 정보를 받아야하기에 새로만듬
    * UserNotFoundException에 대한 예외발생, 처리 함수 작성
    * Valid를 이용한 기존의 예외 발생시 ResponseError에서 에러 처리
    * (이 부분에 대해서 Error에 대한 Exception을 하나의 구조로 모아서 처리가 될 것 같음)
    * */
    @PutMapping("/api/user/{id}")
    public ResponseEntity updateUser(@PathVariable long id,
                                     @RequestBody @Valid UserUpdate userUpdate,
                                     Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(e -> {
                responseErrorList.add(ResponseError.of((FieldError) e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        User existUser = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("사용자 정보가 없습니다."));

        existUser.setPassword(userUpdate.getPhone());
        existUser.setUpdateDate(LocalDateTime.now());
        userRepository.save(existUser);

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity UserNotFoundExceptionHandler(UserNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /*사용자 정보를 조회하는 api 조회할수있는 모든 정보를 내리는것 보다는 필요한 정보만 따로 빼서 내리는 것이 좋다.*/
    @GetMapping("/api/user/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("사용자 정보가 없습니다."));
        UserResponse userResponse = UserResponse.of(user);

        return userResponse;
    }


    @GetMapping("/api/user/{id}/notice/")
    public List<NoticeResponse> userNotice(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("사용자 정보가 없습니다."));

        List<Notice> noticeList = noticeRepository.findByUser(user);

        List<NoticeResponse> noticeResponsesList = new ArrayList<>();
        noticeList.stream().forEach((e) ->{
            noticeResponsesList.add(NoticeResponse.of(e));
        });

        return noticeResponsesList;
    }

    /*
    @PostMapping("/api/user")
    public ResponseEntity<?> addUser(@RequestBody @Valid UserInput userInput, Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if(errors.hasErrors()){
            errors.getAllErrors().stream().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError)e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        if(userRepository.countByEmail(userInput.getEmail()) > 0 ) {
            throw new ExistsEmailException("이미 중복된 이메일이 있습니다.");
        }

        User user = User.builder()
                .email(userInput.getEmail())
                .userName(userInput.getUserName())
                .phone(userInput.getPhone())
                .password((userInput.getPassword()))
                .regDate(LocalDateTime.now())
                .build();

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
    */

    @ExceptionHandler(value = {ExistsEmailException.class, PasswordNotMatchException.class})
    public ResponseEntity<?> ExistEmailExceptionHandler(RuntimeException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**/
    @PatchMapping("/api/user/{id}/password")
    public ResponseEntity<?> updateUserPassword(@PathVariable Long id,
                                                @Valid @RequestBody UserInputPassword userInputPassword,
                                                Errors errors){
        List<ResponseError> responseErrorList = new ArrayList<>();
        if(errors.hasErrors()){
            errors.getAllErrors().stream().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError)e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByIdAndPassword(id, userInputPassword.getPassword())
                .orElseThrow(() -> new PasswordNotMatchException("비밀번호가 일치하지 않습니다."));

        user.setPassword(userInputPassword.getNewPassword());
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    /*회원 가입시 비밀번호를 암호화하여 저장*/
    @PostMapping("/api/user")
    public ResponseEntity<?> addUser(@RequestBody @Valid UserInput userInput, Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if(errors.hasErrors()){
            errors.getAllErrors().stream().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError)e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        if(userRepository.countByEmail(userInput.getEmail()) > 0 ) {
            throw new ExistsEmailException("이미 중복된 이메일이 있습니다.");
        }

        String encryptPassword = getEncryptPassword(userInput.getPassword());

        User user = User.builder()
                .email(userInput.getEmail())
                .userName(userInput.getUserName())
                .phone(userInput.getPhone())
                .password(encryptPassword)
                .regDate(LocalDateTime.now())
                .build();

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    private String getEncryptPassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encPassword = bCryptPasswordEncoder.encode(password);

        return encPassword;
    }

    /*회원 탈퇴 기능 */
    @DeleteMapping("/api/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("사용자 정보가 없습니다."));

        try {
            userRepository.delete(user);
        } catch(DataIntegrityViolationException e){
            String message = "제약조건에 문제가 발생하였습니다.";
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } catch ( Exception e){
            String message = "회원 탈퇴 중 문제가 발생하였습니다.";
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().build();
    }

    /*사용자 아이디를 찾는 API */
    @GetMapping("/api/user")
    public ResponseEntity<?> findUser(@RequestBody UserInputFind userInputFind){
        User user = userRepository.findByUserNameAndPhone(userInputFind.getUserName(), userInputFind.getPhone())
                .orElseThrow(()->new UserNotFoundException("사용자 정보가 없습니다."));

        UserResponse userResponse = UserResponse.of(user);

        return ResponseEntity.ok().body(userResponse);
    }


    @GetMapping("/api/user/{id}/password/reset")
    public ResponseEntity<?> resetUserPassword(@PathVariable Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("사용자 정보가 없습니다."));

        //비밀번호 초기화
        String resetPassword = getResetPassword();
        String resetEncryptPassword = getEncryptPassword((getResetPassword()));
        user.setPassword(resetEncryptPassword);
        userRepository.save(user);
        String message = String.format("[%s]님의 임시비밀번호가 [%s]로 초기화 되었습니다.", user.getUserName(), resetPassword);
        sendSMS(message);

        return ResponseEntity.ok().build();
    }

    private String getResetPassword() {
        return UUID.randomUUID().toString().replaceAll("-","").substring(0, 10);
    }

    void sendSMS(String message) {
        System.out.println("[문자메시지 전송]");
        System.out.println(message);
    }


    /*내가 좋아요한 공지사항을 보는 API
     * NoticeLike Entity 추가
     *
     * */
    @GetMapping("/api/user/{id}/notice/like")
    public List<NoticeLike> likeNotice(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        List<NoticeLike> noticeLikeList = noticeLikeRepository.findByUser(user);
        return noticeLikeList;
    }

    /*
    *
    *
    * */
    /*@PostMapping("/api/user/login")
    public ResponseEntity<?> createToken(@RequestBody @Valid UserLogin userLogin, Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if(errors.hasErrors()) {
            errors.getAllErrors().stream().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError)e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(userLogin.getEmail())
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        if(PasswordUtils.equalPassword(userLogin.getPassword(), user.getPassword())){
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }

        return ResponseEntity.ok().build();
    }*/

    /*
    * JWT토큰 발행 1달 후 expired
    * */
    @PostMapping("/api/user/login")
    public ResponseEntity<?> createToken(@RequestBody @Valid UserLogin userLogin, Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if(errors.hasErrors()) {
            errors.getAllErrors().stream().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError)e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(userLogin.getEmail())
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        if(PasswordUtils.equalPassword(userLogin.getPassword(), user.getPassword())){
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }

        LocalDateTime expiredDateTime = LocalDateTime.now().plusMonths(1);
        Date expiredDate = java.sql.Timestamp.valueOf(expiredDateTime);

        //토큰 발행
        String token = JWT.create()
                    .withExpiresAt(expiredDate)
                    .withClaim("user_id", user.getId())
                    .withSubject(user.getUserName())
                    .withIssuer(user.getEmail())
                    .sign(Algorithm.HMAC512("fastcampus".getBytes()));

        return ResponseEntity.ok().body(UserLoginToken.builder().token(token).build());
    }

    @PatchMapping("/api/user/login")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String token = request.getHeader("F-Token");
        String email = "";

        try {
            email = JWT.require(Algorithm.HMAC512("fastcampus".getBytes()))
                    .build()
                    .verify(token)
                    .getIssuer();
        }catch (SignatureVerificationException e){
            throw new PasswordNotMatchException("!");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UserNotFoundException("사용자 정보가 없습니다."));
        LocalDateTime expiredDateTime = LocalDateTime.now().plusMonths(1);
        Date expiredDate = java.sql.Timestamp.valueOf(expiredDateTime);

        String newToken = JWT.create()
                .withExpiresAt(expiredDate)
                .withClaim("user_id", user.getId())
                .withSubject(user.getUserName())
                .withIssuer(user.getEmail())
                .sign(Algorithm.HMAC512("fastcampus".getBytes()));

        return ResponseEntity.ok().body(UserLoginToken.builder().token(newToken).build());
    }

    /**?
     *
     */
    @DeleteMapping("/api/user/login")
    public ResponseEntity<?> removeToken(@RequestHeader("F-Token") String token) {
        String email = "";

        try {
            email = JWTUtils.getIssuer(token);
        } catch (SignatureVerificationException e) {
            return new ResponseEntity<>("토큰 정보가 정확하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        //세션, 쿠키 삭제
        //클라이언트 쿠키/로컬스토리지/세션스토리지
        //블랙리스트 작성
        return ResponseEntity.ok().build();
    }


    //내가 작성한 게시글 목록을 가져오는 API
    @GetMapping("/api/user/board/post")
    public ResponseEntity<?> myPost(@RequestHeader("F-TOKEN") String token){
        String email = "";

        try {
            email = JWTUtils.getIssuer(token);
        } catch (SignatureVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        List<Board> list= boardService.postList(email);
        return ResponseResult.success(list);
    }

    //내가 작성한 댓글 목록을 리턴하는 API

    @GetMapping("/api/user/board/comment")
    public ResponseEntity<?> myComments(@RequestHeader("F-TOKEN") String token){

        String email = "";
        try {
            email = JWTUtils.getIssuer(token);
        } catch (SignatureVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        List<BoardComment> list = boardService.commentList(email);
        return ResponseResult.success(list);
    }

    //사용자의 포인트 정보를 만들고 게시글을 작성할 경우 포인트를 누적하는 API
    @PostMapping("/api/user/point")
    public ResponseEntity<?> userPoint(@RequestHeader("F-TOKEN") String token,
                          @RequestBody UserPointInput userPointInput){

        String email = "";

        try {
            email = JWTUtils.getIssuer(token);
        } catch (SignatureVerificationException e) {
            return ResponseResult.fail("토큰 정보가 정확하지 않습니다.");
        }

        ServiceResult result = pointService.addPoint(email, userPointInput);
        return ResponseResult.result(result);

    }

    //사용자 추가하는 API
    @PostMapping("/api/public/user")
    public ResponseEntity<?> addUser(@RequestBody UserInput userInput) {
        ServiceResult result = userService.addUser(userInput);
        return ResponseResult.result(result);
    }

    //비밀번호 초기화를 위한 이메일 인증코드를 전송하는 API
    @PostMapping("/api/public/user/password/reset")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid UserPasswordResetInput userPasswordResetInput,
                                           Errors errors) {
        if(errors.hasErrors()){
            return ResponseResult.fail("입력이 잘못되었습니다.", ResponseError.of(errors.getAllErrors()));
        }

        ServiceResult result = null;
        try {
            result = userService.resetPassword(userPasswordResetInput);
            return ResponseResult.result(result);
        } catch(BizException e) {
            return ResponseResult.fail(e.getMessage());
        }



    }
}
