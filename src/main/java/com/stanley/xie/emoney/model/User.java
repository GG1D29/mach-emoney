package com.stanley.xie.emoney.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    @Setter
    private int balance;

    private String userToken;

    public User(String username, String userToken) {
        this.username = username;
        this.userToken = userToken;
        this.balance = 0;
    }
}
