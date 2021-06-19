package com.example.blogkimscafe.model.comment;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface commentdao extends JpaRepository<commentvo,Integer> {
    
    @Query(value = "select *from blogcomment  where bid=?1 order by cid desc limit ?2,?3",nativeQuery = true)
    List<commentvo> getCommentNative(int bid,int first,int end);

    List<commentvo> findByBidOrderByCid(int bid);
    
    int countByBid(int bid);

    commentvo findByCid(int cid);
    
    @Modifying 
    @Transactional
    @Query(value = "DELETE FROM comment c WHERE c.bid=?1",nativeQuery = true) // 딜리트 네거티브 쿼리 쓰는법!!!!!!!!!!!!20210531 대박이다 인서트도 이렇게 가능하지 않을까..? 테스트 해봐야겠다!
    void deleteBybidNative(int bid); 


}
