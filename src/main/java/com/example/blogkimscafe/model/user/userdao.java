package com.example.blogkimscafe.model.user;





import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface userdao extends JpaRepository<uservo,Integer> {///얘는 인터페이스로
    
    public uservo findByEmail(String username);

    @Query(value = "select emailcheck from blogusers where email=?1",nativeQuery = true)
    public String getEmailCheckfindByEmailNative(String email); 

    public boolean existsByEmail(String email);
    
    /*@Modifying 
    @Transactional
    @Query(value = "update blogusers u set u.pwd=?1 where u.email=?2",nativeQuery = true)
    public void updatePwdNative(String npwd2,String email);*/

}
