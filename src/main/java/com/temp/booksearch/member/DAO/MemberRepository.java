package com.temp.booksearch.member.DAO;

import com.temp.booksearch.member.VO.MemberVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberVO, Long> {
    // 사용자 이름으로 사용자를 검색하는 메서드
    MemberVO findByUserName(String userName);
}