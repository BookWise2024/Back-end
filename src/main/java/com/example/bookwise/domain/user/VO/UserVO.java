package com.example.bookwise.domain.user.VO;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="User")
public class UserVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userName;
    @Column(unique = true, nullable = false)
    private String userEmail;
}
