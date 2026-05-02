package com.soccergame.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Estatísticas globais do usuário.
 * Campos: id, user (one-to-one), wins, losses
 */
@Entity
@Table(name = "stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private int wins = 0;

    @Column(nullable = false)
    private int losses = 0;
}
