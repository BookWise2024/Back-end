package com.temp.booksearch.member.VO;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name="Member")
public class MemberVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userName;
    @Column(unique = true, nullable = false)
    private String userEmail;
}
