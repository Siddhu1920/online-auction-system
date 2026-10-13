package com.bidsphere.service;

import com.bidsphere.entity.*;
import com.bidsphere.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Service
public class AuctionService {
 private final AuctionRepository auctions; private final BidRepository bids; private final UserRepository users;
 public AuctionService(AuctionRepository a,BidRepository b,UserRepository u){auctions=a;bids=b;users=u;}
 public List<Auction> all(){return auctions.findAllByOrderByCreatedAtDesc();}
 public Auction get(Long id){return auctions.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Auction not found"));}
 @Transactional public Auction create(Map<String,String> in,String email){
   User seller=users.findByEmailIgnoreCase(email).orElseThrow();
   Auction a=new Auction();
   a.title=req(in,"title"); a.description=req(in,"description"); a.category=req(in,"category"); a.conditionLabel=in.getOrDefault("conditionLabel","GOOD");
   a.startingPrice=money(in,"startingPrice"); a.currentPrice=a.startingPrice; a.minIncrement=money(in,"minIncrement");
   a.startTime=Instant.parse(req(in,"startTime")); a.endTime=Instant.parse(req(in,"endTime"));
   if(a.startingPrice.signum()<0||a.minIncrement.signum()<=0||!a.endTime.isAfter(a.startTime)||!a.endTime.isAfter(Instant.now())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Prices or auction dates are invalid");
   a.status=a.startTime.isAfter(Instant.now())?"SCHEDULED":"ACTIVE"; a.seller=seller; return auctions.save(a);
 }
 @Transactional public Bid bid(Long id,BigDecimal amount,String email){
   Auction a=auctions.findLockedById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Auction not found"));
   User bidder=users.findByEmailIgnoreCase(email).orElseThrow();
   Instant now=Instant.now();
   if(!a.status.equals("ACTIVE")||now.isBefore(a.startTime)||!now.isBefore(a.endTime)) throw new ResponseStatusException(HttpStatus.CONFLICT,"Auction is not accepting bids");
   if(a.seller.id.equals(bidder.id)) throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Sellers cannot bid on their own auctions");
   if(amount==null||amount.compareTo(a.currentPrice.add(a.minIncrement))<0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Bid must be at least "+a.currentPrice.add(a.minIncrement));
   Bid b=new Bid();b.auction=a;b.bidder=bidder;b.amount=amount;
   a.currentPrice=amount; auctions.save(a); return bids.save(b);
 }
 public List<Bid> bidHistory(Long id){get(id);return bids.findByAuctionIdOrderByAmountDescCreatedAtAsc(id);}
 @Transactional public void activateScheduled(){
   for(Auction a:auctions.findByStatusAndStartTimeLessThanEqual("SCHEDULED",Instant.now())){
     Auction locked=auctions.findLockedById(a.id).orElse(null);
     if(locked!=null && locked.status.equals("SCHEDULED") && !locked.startTime.isAfter(Instant.now()) && locked.endTime.isAfter(Instant.now())){locked.status="ACTIVE";auctions.save(locked);}
   }
 }
 @Transactional public void finishExpired(){
   for(Auction a:auctions.findByStatusAndEndTimeLessThanEqual("ACTIVE",Instant.now())){
     Auction locked=auctions.findLockedById(a.id).orElse(null);
     if(locked==null||!locked.status.equals("ACTIVE")||locked.endTime.isAfter(Instant.now())) continue;
     List<Bid> history=bids.findByAuctionIdOrderByAmountDescCreatedAtAsc(locked.id);
     if(!history.isEmpty()){locked.winner=history.get(0).bidder;locked.status="ENDED";}else locked.status="UNSOLD";
     auctions.save(locked);
   }
 }
 private String req(Map<String,String> m,String k){String v=m.get(k);if(v==null||v.isBlank())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,k+" is required");return v.trim();}
 private BigDecimal money(Map<String,String> m,String k){try{return new BigDecimal(req(m,k));}catch(NumberFormatException e){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,k+" must be a valid amount");}}
}
