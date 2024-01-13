package com.enterprise.fleet.transaction;

import com.enterprise.fleet.exception.CustomException;
import com.enterprise.fleet.transaction.dto.CreateTransactionDTO;
import com.enterprise.fleet.transaction.dto.UpdateTransactionDTO;
import com.enterprise.fleet.transaction.type.TransactionStatusType;
import com.enterprise.fleet.util.CustomResponse;
import com.enterprise.fleet.vehicle_rental.VehicleRental;
import com.enterprise.fleet.vehicle_rental.VehicleRentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final VehicleRentalRepository vehicleRentalRepository;

    public ResponseEntity<CustomResponse> fetchTransactions(){
        var transactions = transactionRepository.findAll();

        return ResponseEntity.ok(
                new CustomResponse(true, "Transactions listed successfully", transactions)
        );
    }

    public ResponseEntity<CustomResponse> createTransaction(CreateTransactionDTO request){
        VehicleRental vehicleRental = vehicleRentalRepository.findById(request.getVehicleRentalId()).orElseThrow(
                () -> new CustomException("Vehicle Rental not found", HttpStatus.NOT_FOUND)
        );

        TransactionStatusType transactionStatus = TransactionStatusType.PENDING;

        if (request.getPaymentStatus() != null) {
            transactionStatus = request.getPaymentStatus();
        }

        var transaction = Transaction.builder()
                .vehicleRental(vehicleRental)
                .amount(request.getAmount())
                .paymentStatus(transactionStatus)
                .build();

        var savedTransaction = transactionRepository.save(transaction);

        String uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTransaction.getId())
                .toUriString();

        return ResponseEntity.created(java.net.URI.create(uri))
                .body(
                        new CustomResponse(true, "Transaction created successfully", savedTransaction)
                );
    }

    public ResponseEntity<CustomResponse> fetchTransaction(int transactionId){
        Transaction transaction =  transactionRepository.findById(transactionId).orElseThrow(
                () -> new CustomException("Transaction not found", HttpStatus.NOT_FOUND)
        );

        return ResponseEntity.ok(
                new CustomResponse(true, "Transaction fetched successfully", transaction)
        );
    }

    public ResponseEntity<CustomResponse> updateTransaction(int transactionId, UpdateTransactionDTO request){
        Transaction transaction =  transactionRepository.findById(transactionId).orElseThrow(
                () -> new CustomException("Transaction not found", HttpStatus.NOT_FOUND)
        );

        if (request.getVehicleRentalId() != null) {
            VehicleRental vehicleRental = vehicleRentalRepository.findById(request.getVehicleRentalId()).orElseThrow();

            transaction.setVehicleRental(vehicleRental);
        }

        if (request.getAmount() != null) {
            transaction.setAmount(request.getAmount());
        }

        if (request.getPaymentStatus() != null) {
            transaction.setPaymentStatus(request.getPaymentStatus());
        }

        transactionRepository.save(transaction);

        return ResponseEntity.ok(
                new CustomResponse(true, "Transaction updated successfully", transaction)
        );
    }

    public ResponseEntity<CustomResponse> deleteTransaction(int transactionId){
         transactionRepository.findById(transactionId).orElseThrow(
                 () -> new CustomException("Transaction not found", HttpStatus.NOT_FOUND)
         );

        transactionRepository.deleteById(transactionId);

        return ResponseEntity.ok().body(
                new CustomResponse(true, "Transaction deleted successfully", null)
        );
    }
}
