package com.example.jpa.common.schedule;

import com.example.jpa.logs.service.LogsService;
import com.example.jpa.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final LogsService logsService;
    private UserService userService;

    //스프링 스케쥴러를 이용하여 매일 새벽 4시에 로그정보를 삭제하는 기능
    //@Scheduled(cron= "0 0 4 * * *")
    @Scheduled(fixedDelay = 1000 * 60)
    public void deleteLog(){
        log.info("스케쥴 실행 !");
        logsService.deleteLog();
    }

    //스프링 스케쥴러를 사용하여 회원중 가입일이 1년이 된 회원들에 대해서 서비스 이용내역 통지메일 발송 기능
    @Scheduled(fixedDelay = 1000 * 60)
    public void sendServiceNotice(){
        log.info("스케쥴 실행 !");

        userService.sendServiceNotice();
    }

}
