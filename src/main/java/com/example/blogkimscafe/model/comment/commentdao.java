package com.example.blogkimscafe.model.comment;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface commentdao extends JpaRepository<commentvo,Integer> {
    
    @Query(value = "select *from comment  where bid=?1 order by cid desc limit ?2,?3",nativeQuery = true)
    List<commentvo> getCommentNative(int num,int first,int end);
        ////이거다 네거티브 ㅁㅊ 몇시간쨰 뒤졌는데 진짜...
    ///이거같은데 table이름을 쓰는게 아니라 파일이름을 적어줘야하는거 같다 ㅁㅊ20210518
    //@Query(value = "select count(*)from comment  where bid=?1",nativeQuery = true)
    int countByBid(int bid);

    commentvo findByCid(int cid);
    
    @Modifying 
    @Transactional
    @Query(value = "DELETE FROM comment c WHERE c.bid=?1",nativeQuery = true) // 딜리트 네거티브 쿼리 쓰는법!!!!!!!!!!!!20210531 대박이다 인서트도 이렇게 가능하지 않을까..? 테스트 해봐야겠다!
    void deleteBybidNative(int bid); 


}
