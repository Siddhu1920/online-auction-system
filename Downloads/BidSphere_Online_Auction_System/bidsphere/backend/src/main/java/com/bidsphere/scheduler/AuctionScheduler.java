package com.bidsphere.scheduler;
import com.bidsphere.service.AuctionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class AuctionScheduler {
 private final AuctionService service;
 public AuctionScheduler(AuctionService s){service=s;}
 @Scheduled(fixedDelay=15000) public void updateAuctions(){service.activateScheduled();service.finishExpired();}
}
