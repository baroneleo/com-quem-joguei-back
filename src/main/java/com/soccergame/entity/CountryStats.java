package main.java.com.soccergame.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Estatísticas por país para um usuário.
 */
@Entity
@Table(name = "country_stats",
       indexes = {@Index(name = "idx_country_stats_country", columnList = "country")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CountryStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private int wins = 0;

    @Column(nullable = false)
    private int losses = 0;
}
