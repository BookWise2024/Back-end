package com.temp.booksearch.member.repository;

import com.temp.booksearch.member.VO.MemberVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberVO, Long> {
    MemberVO findByUserName(String userName);
}