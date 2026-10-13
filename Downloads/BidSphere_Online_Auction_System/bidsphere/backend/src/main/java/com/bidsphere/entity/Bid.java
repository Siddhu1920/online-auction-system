package com.bidsphere.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity @Table(name="bids")
public class Bid {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="auction_id", nullable=false) public Auction auction;
 @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="bidder_id", nullable=false) public User bidder;
 @Column(nullable=false, precision=12, scale=2) public BigDecimal amount;
 @Column(name="created_at", insertable=false, updatable=false) public Instant createdAt;
 public Bid() {}
}
