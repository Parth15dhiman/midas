package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MidasService {
    
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final TransactionRecordRepository transactionRecordRepository;

    @Transactional
    public void processTransaction(Transaction transaction){

        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());
        float amount = transaction.getAmount();

        if( sender != null && recipient != null && sender.getBalance() >= amount ) {
            float senderCurrentBalance = sender.getBalance() ;
            float recipientCurrentBalance = recipient.getBalance() ;
            sender.setBalance(senderCurrentBalance - amount) ;
            recipient.setBalance(recipientCurrentBalance + amount) ;

            userRepository.save(sender) ;
            userRepository.save(recipient) ;
            TransactionRecord transactionRecord = new TransactionRecord(sender, recipient, amount) ;
            transactionRecordRepository.save(transactionRecord) ;
        }
    }
}
