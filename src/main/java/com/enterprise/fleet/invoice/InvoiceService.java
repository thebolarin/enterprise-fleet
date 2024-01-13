package com.enterprise.fleet.invoice;

import com.enterprise.fleet.exception.CustomException;
import com.enterprise.fleet.invoice.dto.*;
import com.enterprise.fleet.util.CustomResponse;
import com.enterprise.fleet.vehicle_rental.VehicleRental;
import com.enterprise.fleet.vehicle_rental.VehicleRentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final VehicleRentalRepository vehicleRentalRepository;
    private final InvoiceRepository invoiceRepository;

    public ResponseEntity<CustomResponse> fetchInvoices(){
        List<Invoice> invoices = invoiceRepository.findAll();

        return ResponseEntity.ok(
                new CustomResponse(true, "Invoices listed successfully", invoices)
        );
    }

    public ResponseEntity<CustomResponse> createInvoice(CreateInvoiceDTO request){

        VehicleRental vehicleRental = vehicleRentalRepository.findById(request.getVehicleRentalId()).orElseThrow(
                () -> new CustomException("Vehicle Rental not found", HttpStatus.NOT_FOUND)
        );

        var invoice = Invoice.builder()
                .vehicleRental(vehicleRental)
                .amount(request.getAmount())
                .dueDate(request.getDueDate())
                .build();

        var savedInvoice = invoiceRepository.save(invoice);

        String uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedInvoice.getId())
                .toUriString();

        return ResponseEntity.created(java.net.URI.create(uri))
                .body(
                        new CustomResponse(true, "Invoice created successfully", savedInvoice)
                );
    }

    public ResponseEntity<CustomResponse> fetchInvoice(int invoiceId){
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(()-> new CustomException( "Invoice not found", HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(
                new CustomResponse(true, "Vehicle Rental fetched successfully", invoice)
        );
    }

    public ResponseEntity<CustomResponse> updateInvoice(int invoiceId, UpdateInvoiceDTO request){
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(()-> new CustomException( "Invoice not found", HttpStatus.NOT_FOUND));

        if (request.getVehicleRentalId() != null) {
            VehicleRental vehicleRental = vehicleRentalRepository.findById(request.getVehicleRentalId()).orElseThrow(
                    () -> new CustomException("Vehicle Rental not found", HttpStatus.NOT_FOUND)
            );

            invoice.setVehicleRental(vehicleRental);
        }

        if (request.getAmount() != null) {
            invoice.setAmount(request.getAmount());
        }

        if (request.getDueDate() != null) {
            invoice.setDueDate(request.getDueDate());
        }

        invoiceRepository.save(invoice);

        return ResponseEntity.ok(
                new CustomResponse(true, "Invoice updated successfully", invoice)
        );
    }

    public ResponseEntity<CustomResponse> deleteInvoice(int invoiceId){
        invoiceRepository.findById(invoiceId).orElseThrow(()-> new CustomException( "Invoice not found", HttpStatus.NOT_FOUND));

        invoiceRepository.deleteById(invoiceId);

        return ResponseEntity.ok().body(
                new CustomResponse(true, "Invoice deleted successfully", null)
        );
    }
}

