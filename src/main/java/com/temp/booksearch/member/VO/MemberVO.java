package com.temp.booksearch.member.VO;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(unique = true, nullable = false)
    private String userName;
    @Column(unique = true, nullable = false)
    private String email;
}
