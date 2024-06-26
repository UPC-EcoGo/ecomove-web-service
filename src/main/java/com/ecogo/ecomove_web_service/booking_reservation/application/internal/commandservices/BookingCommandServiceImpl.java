package com.ecogo.ecomove_web_service.booking_reservation.application.internal.commandservices;

import com.ecogo.ecomove_web_service.booking_reservation.application.internal.outboundservices.acl.ExternalEcoVehicleService;
import com.ecogo.ecomove_web_service.booking_reservation.domain.model.aggregates.Booking;
import com.ecogo.ecomove_web_service.booking_reservation.domain.model.commands.CancelBookingCommand;
import com.ecogo.ecomove_web_service.booking_reservation.domain.model.commands.CompleteBookingCommand;
import com.ecogo.ecomove_web_service.booking_reservation.domain.model.commands.CreateBookingCommand;
import com.ecogo.ecomove_web_service.booking_reservation.domain.services.BookingCommandService;
import com.ecogo.ecomove_web_service.booking_reservation.infrastructure.persistance.jpa.BookingRepository;
import com.ecogo.ecomove_web_service.shared.application.internal.outboundservices.acl.ExternalUserService;
import com.ecogo.ecomove_web_service.vehicle_management.domain.model.aggregates.EcoVehicle;
import org.springframework.stereotype.Service;

import java.util.Optional;
/**
 * Service to handle booking commands and interact with external services
 */
@Service
public class BookingCommandServiceImpl implements BookingCommandService { // Service for handling booking commands
    private final BookingRepository bookingRepository;
    private final ExternalUserService externalUserService;
    private final ExternalEcoVehicleService externalEcoVehicleService;

    public BookingCommandServiceImpl(BookingRepository bookingRepository, ExternalUserService externalUserService, ExternalEcoVehicleService externalEcoVehicleService){
        this.bookingRepository = bookingRepository;
        this.externalUserService = externalUserService;
        this.externalEcoVehicleService = externalEcoVehicleService;
    }

    @Override
    public Optional<Booking> handle(CreateBookingCommand command){
        return externalUserService.fetchUserById(command.userId()).map(user -> {
            EcoVehicle vehicle = externalEcoVehicleService.fetchEcoVehicleById(command.vehicleId())
                    .orElseThrow(()-> new RuntimeException("Vehicle with id: " + command.vehicleId() + " not found"));
            Booking booking = new Booking(user, vehicle, command.startTime(), command.endTime());
            booking = bookingRepository.save(booking);
            return Optional.of(booking);
        }).orElseThrow(() -> new RuntimeException("User with id: " + command.userId() + " not found"));
    }

    @Override
    public Optional<Booking> handle(CancelBookingCommand command){
        bookingRepository.findById(command.bookingId()).map(booking -> {
            booking.cancel();
            bookingRepository.save(booking);
            return Optional.of(booking);
        }).orElseThrow(() -> new RuntimeException("Booking with id: " + command.bookingId() + " not found"));
        return Optional.empty();
    }

    @Override
    public Optional<Booking> handle(CompleteBookingCommand command){
        bookingRepository.findById(command.bookingId()).map(booking -> {
            booking.complete();
            bookingRepository.save(booking);
            return booking;
        }).orElseThrow(() -> new RuntimeException("Booking with id: " + command.bookingId() + " not found"));
        return Optional.empty();
    }
}
