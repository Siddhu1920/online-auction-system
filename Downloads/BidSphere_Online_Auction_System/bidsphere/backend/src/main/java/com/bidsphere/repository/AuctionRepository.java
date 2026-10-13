package com.bidsphere.repository;
import com.bidsphere.entity.Auction;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.Instant;
import java.util.*;
public interface AuctionRepository extends JpaRepository<Auction,Long> {
 @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select a from Auction a where a.id=:id") Optional<Auction> findLockedById(@Param("id") Long id);
 List<Auction> findAllByOrderByCreatedAtDesc();
 List<Auction> findByStatusAndEndTimeLessThanEqual(String status, Instant now);
 List<Auction> findByStatusAndStartTimeLessThanEqual(String status, Instant now);
}
