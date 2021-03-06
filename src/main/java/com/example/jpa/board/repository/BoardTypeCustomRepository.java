package com.example.jpa.board.repository;

import com.example.jpa.board.entity.BoardType;
import com.example.jpa.board.model.BoardTypeCount;
import com.example.jpa.user.model.UserLogCount;
import com.example.jpa.user.model.UserNoticeCount;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BoardTypeCustomRepository {

    private final EntityManager entityManager;

    public List<BoardTypeCount> getBoardTypeCount() {
        String sql = " select bt.id, bt.board_name, bt.reg_date, bt.using_yn"
                + ", (select count(*) from board b where b.board_type_id) as board_count"
                + " from board_type bt ";
        
        //JSON 매칭이 잘안됨
        //List<BoardTypeCount> list = entityManager.createNativeQuery(sql).getResultList();
        

        //Object 매칭으로 한땀 수작업
        List<Object[]> result = entityManager.createNativeQuery(sql).getResultList();
        List<BoardTypeCount> resultList = result.stream().map(e-> new BoardTypeCount(e))
                .collect(Collectors.toList());


        //외부 라이브러리 사용
        Query nativeQuery = entityManager.createNativeQuery(sql);
        JpaResultMapper jpaResultMapper = new JpaResultMapper();

        List<BoardTypeCount> resultMapper = jpaResultMapper.list(nativeQuery, BoardTypeCount.class);

        return resultList;
    }


}
