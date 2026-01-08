package com.jpmc.midascore.contoller;

import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService
            ;
    @GetMapping("/balance")
    public Balance getUserBalanceByUserId(@RequestParam Long userId){
        return userService.getUserBalanceByUserId(userId) ;
    }
}
