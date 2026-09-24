package com.repairconnect.controller;

import com.repairconnect.entity.*;
import com.repairconnect.repository.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.*;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    final BookingRepository bookings; final CustomerRepository customers; final ProviderRepository providers; final ServiceRepository services;
    BookingController(BookingRepository b, CustomerRepository c, ProviderRepository p, ServiceRepository s) { bookings=b; customers=c; providers=p; services=s; }
    record Request(Long serviceId, String problemTitle, String problemDescription, String serviceAddress, String city, LocalDate preferredDate, LocalTime preferredTime, Double estimatedBudget, String additionalNotes) {}

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    Booking create(@RequestBody Request r, Authentication auth) {
        var service = services.findById(r.serviceId()).orElseThrow(() -> new RuntimeException("Selected service does not exist."));
        var provider = service.getProvider();
        if (provider.getVerificationStatus() != VerificationStatus.APPROVED) {
            throw new RuntimeException("Service provider is not approved yet.");
        }
        boolean isDoubleBooked = bookings.existsByProviderIdAndPreferredDateAndPreferredTimeAndStatusIn(
            provider.getId(),
            r.preferredDate(),
            r.preferredTime(),
            List.of(BookingStatus.PENDING, BookingStatus.ACCEPTED, BookingStatus.IN_PROGRESS)
        );
        if (isDoubleBooked) {
            throw new RuntimeException("That time slot is no longer available. Please select another time.");
        }
        var b = new Booking(); b.setCustomer(customers.findByUserEmail(auth.getName()).orElseThrow(() -> new RuntimeException("Customer profile not found.")));
        b.setService(service); b.setProvider(provider);
        b.setProblemTitle(r.problemTitle()); b.setProblemDescription(r.problemDescription()); b.setServiceAddress(r.serviceAddress()); b.setCity(r.city());
        b.setPreferredDate(r.preferredDate()); b.setPreferredTime(r.preferredTime()); b.setEstimatedBudget(r.estimatedBudget()); b.setAdditionalNotes(r.additionalNotes());
        return bookings.save(b);
    }
    @GetMapping("/my") List<Booking> mine(Authentication auth) { return bookings.findByCustomerUserEmailOrderByCreatedAtDesc(auth.getName()); }
    @GetMapping("/provider") List<Booking> provider(Authentication auth) { return bookings.findByProviderUserEmailOrderByCreatedAtDesc(auth.getName()); }
    @PutMapping("/{id}/{action}") Booking action(@PathVariable Long id, @PathVariable String action, Authentication auth) {
        var b=bookings.findById(id).orElseThrow(); if(!b.getProvider().getUser().getEmail().equals(auth.getName())) throw new RuntimeException("Not your booking");
        var next=switch(action){case "accept"->BookingStatus.ACCEPTED;case "reject"->BookingStatus.REJECTED;case "start"->BookingStatus.IN_PROGRESS;case "complete"->BookingStatus.COMPLETED;default->throw new RuntimeException("Unsupported action");};
        b.setStatus(next); return bookings.save(b);
    }
    @PutMapping("/{id}/cancel") Booking cancel(@PathVariable Long id, Authentication auth) {
        var b=bookings.findById(id).orElseThrow(); if(!b.getCustomer().getUser().getEmail().equals(auth.getName()) || b.getStatus()!=BookingStatus.PENDING) throw new RuntimeException("Booking cannot be cancelled");
        b.setStatus(BookingStatus.CANCELLED); return bookings.save(b);
    }
}
