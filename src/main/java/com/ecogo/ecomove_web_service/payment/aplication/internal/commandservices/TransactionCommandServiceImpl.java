package com.ecogo.ecomove_web_service.payment.application.internal.commandservices;

import com.ecogo.ecomove_web_service.payment.application.internal.outboundservices.acl.ExternalUserServiceToPayment;
import com.ecogo.ecomove_web_service.payment.domain.model.aggregates.Transaction;
import com.ecogo.ecomove_web_service.payment.domain.model.commands.CreateTransactionCommand;
import com.ecogo.ecomove_web_service.payment.domain.services.TransactionCommandService;
import com.ecogo.ecomove_web_service.payment.infrastructure.persistance.jpa.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransactionCommandServiceImpl implements TransactionCommandService {

    private final TransactionRepository transactionRepository;
    private final ExternalUserServiceToPayment externalUserServiceToPayment;

    public TransactionCommandServiceImpl(TransactionRepository transactionRepository, ExternalUserServiceToPayment externalUserServiceToPayment){
        this.transactionRepository = transactionRepository;
        this.externalUserServiceToPayment = externalUserServiceToPayment;
    }

    public Optional<Transaction> handle(CreateTransactionCommand command){
        externalUserServiceToPayment.fetchUserById(command.userId()).map(user -> {
            var transaction = new Transaction(user, command.amount(), command.date());
            transactionRepository.save(transaction);
            return Optional.of(transaction);
        }).orElseThrow(()->new RuntimeException("User not found for specified id: " + command.userId()));
        return Optional.empty();
    }
}
