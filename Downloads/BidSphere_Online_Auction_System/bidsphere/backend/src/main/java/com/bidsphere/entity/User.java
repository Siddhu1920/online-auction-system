package com.bidsphere.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name="users")
public class User {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(nullable=false) public String name;
 @Column(nullable=false, unique=true) public String email;
 @Column(name="password_hash", nullable=false) public String passwordHash;
 @Column(nullable=false) public String role;
 @Column(nullable=false) public boolean active=true;
 @Column(name="created_at", insertable=false, updatable=false) public Instant createdAt;
 public User() {}
}
