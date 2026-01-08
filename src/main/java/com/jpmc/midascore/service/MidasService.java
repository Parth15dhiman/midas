package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class MidasService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final TransactionRecordRepository transactionRecordRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String API = "http://localhost:8080/incentive" ;

    @Transactional
    public void processTransaction(Transaction transaction){

        HttpEntity<Transaction> requestEntity = new HttpEntity<>(transaction);

        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());
        float amount = transaction.getAmount();

        if( sender != null && recipient != null && sender.getBalance() >= amount ) {

            ResponseEntity<Incentive> response = restTemplate.exchange(API, HttpMethod.POST, requestEntity, Incentive.class);

            float incentive = (response.getBody() != null) ? response.getBody().getAmount()  : 0.0f ;
            float senderCurrentBalance = sender.getBalance() ;

            float recipientCurrentBalance = recipient.getBalance() ;
            sender.setBalance(senderCurrentBalance - amount) ;
            recipient.setBalance(recipientCurrentBalance + amount + incentive) ;

            userRepository.save(sender) ;
            userRepository.save(recipient) ;
            TransactionRecord transactionRecord = new TransactionRecord(sender, recipient, amount, incentive) ;
            transactionRecordRepository.save(transactionRecord) ;
        }
    }
}
