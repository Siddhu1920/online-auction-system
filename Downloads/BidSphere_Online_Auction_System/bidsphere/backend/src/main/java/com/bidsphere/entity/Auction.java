package com.bidsphere.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity @Table(name="auctions")
public class Auction {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(nullable=false) public String title;
 @Column(nullable=false, columnDefinition="TEXT") public String description;
 @Column(nullable=false) public String category;
 @Column(name="condition_label", nullable=false) public String conditionLabel;
 @Column(name="starting_price", nullable=false, precision=12, scale=2) public BigDecimal startingPrice;
 @Column(name="current_price", nullable=false, precision=12, scale=2) public BigDecimal currentPrice;
 @Column(name="min_increment", nullable=false, precision=12, scale=2) public BigDecimal minIncrement;
 @Column(name="start_time", nullable=false) public Instant startTime;
 @Column(name="end_time", nullable=false) public Instant endTime;
 @Column(nullable=false) public String status;
 @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="seller_id", nullable=false) public User seller;
 @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="winner_id") public User winner;
 @Column(name="created_at", insertable=false, updatable=false) public Instant createdAt;
 public Auction() {}
}
