package com.bidsphere.controller;
import com.bidsphere.entity.*;
import com.bidsphere.service.AuctionService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.*;

@RestController @RequestMapping("/api/v1/auctions")
public class AuctionController {
 private final AuctionService service;
 public AuctionController(AuctionService s){service=s;}
 @GetMapping public List<Auction> all(){return service.all();}
 @GetMapping("/{id}") public Auction get(@PathVariable Long id){return service.get(id);}
 @PostMapping public Auction create(@RequestBody Map<String,String> body,Authentication auth){return service.create(body,auth.getName());}
 @GetMapping("/{id}/bids") public List<Bid> bids(@PathVariable Long id){return service.bidHistory(id);}
 @PostMapping("/{id}/bids") public Bid bid(@PathVariable Long id,@RequestBody Map<String,String> body,Authentication auth){
   try{return service.bid(id,new BigDecimal(body.get("amount")),auth.getName());}
   catch(NumberFormatException|NullPointerException e){throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,"A valid bid amount is required");}
 }
}
