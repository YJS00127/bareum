package com.example.bareum.user;

// DB 테이블 만들 때 사용
import jakarta.persistence.*;
// getter, setter등 어노테이션으로 생성가능
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name =  "users")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    private String userName;

    private String skinType;

}
