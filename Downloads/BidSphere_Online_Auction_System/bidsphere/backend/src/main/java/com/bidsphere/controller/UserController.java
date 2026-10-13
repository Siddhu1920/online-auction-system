package com.bidsphere.controller;
import com.bidsphere.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.Map;
@RestController @RequestMapping("/api/v1/users")
public class UserController {
 private final UserRepository users;
 public UserController(UserRepository u){users=u;}
 @GetMapping("/me") public Map<String,Object> me(Authentication a){
   var u=users.findByEmailIgnoreCase(a.getName()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
   return Map.of("id",u.id,"name",u.name,"email",u.email,"role",u.role);
 }
}
