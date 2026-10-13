package com.bidsphere.controller;
import com.bidsphere.service.AuthService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/api/v1/auth")
public class AuthController {
 private final AuthService auth;
 public AuthController(AuthService a){auth=a;}
 @PostMapping("/register") public Map<String,Object> register(@RequestBody Map<String,String> body){return auth.register(body);}
 @PostMapping("/login") public Map<String,Object> login(@RequestBody Map<String,String> body){return auth.login(body);}
}
