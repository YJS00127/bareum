package com.example.bareum.diary;

import com.example.bareum.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private Integer skinScore;
    private Integer stressScore;
    private Boolean hasTrouble;
    private String weather;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @ElementCollection
    private List<String> productsNames;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
