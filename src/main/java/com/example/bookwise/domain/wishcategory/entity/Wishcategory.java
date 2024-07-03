package com.example.bookwise.domain.wishcategory.entity;


import com.example.bookwise.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Wishcategory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishId;
    private String category;
    private Long count;

    @ManyToOne
  //  @JoinColumn(name="userId", nullable=false,insertable=false,updatable=false)
    @JoinColumn(name="userId", nullable=false)
    private User user;



    public Wishcategory(String category,User user) {
        this.category =category;
        this.count = 0L;
        this.user = user;
    }


    // 카테고리 count 증가
    public void increase() {
        this.count += 1;
    }

    // 카테고리 count 감소
    public void decrease() {
        if(this.count >0) this.count -= 1;
    }

}





