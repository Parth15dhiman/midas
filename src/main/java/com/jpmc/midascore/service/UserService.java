package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository ;

    public Balance getUserBalanceByUserId(Long userId){
        try{
            Optional<UserRecord> userRecord = userRepository.findById(userId);
            if(userRecord.isPresent() ) {
                float userBalance = userRecord.get().getBalance();
                return new Balance(userBalance) ;
            }
        }catch (Exception e){
            log.error("error in getting user's balance by getUserBalanceByUserId", e);
        }
        return new Balance(0f) ;
    }
}
