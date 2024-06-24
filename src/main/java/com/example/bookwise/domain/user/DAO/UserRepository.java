package com.example.bookwise.domain.user.DAO;

import com.example.bookwise.domain.user.VO.UserVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserVO, Long> {
    // 사용자 이름으로 사용자를 검색하는 메서드
    UserVO findByUserName(String userName);
}
