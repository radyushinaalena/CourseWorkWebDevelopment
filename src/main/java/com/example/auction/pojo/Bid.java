package com.example.auction.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bids")
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
    @CreationTimestamp
    @Column(name = "date_time", nullable = false, updatable = false)
    private OffsetDateTime dateTime;
    @ManyToOne
    @JoinColumn(name = "lot_id")
    private Lot lot;
}