package com.ecogo.ecomove_web_service.booking_reservation.domain.model.aggregates;

import com.ecogo.ecomove_web_service.booking_reservation.domain.model.valueobjects.BookingStatus;
import com.ecogo.ecomove_web_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.ecogo.ecomove_web_service.user_management.domain.model.aggregates.User;
import com.ecogo.ecomove_web_service.vehicle_management.domain.model.aggregates.EcoVehicle;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class Booking extends AuditableAbstractAggregateRoot<Booking> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    @Getter
    private User user;

    @ManyToOne
    @JoinColumn(name="vehicle_id")
    @Getter
    private EcoVehicle vehicle;

    @Column(nullable = false, updatable = false)
    @Getter
    private Date startTime;

    @Column(nullable = false)
    @Getter
    private Date endTime;


    private BookingStatus status;

    public Booking(){

    }

    public Booking(User user, EcoVehicle ecoVehicle,Date startTime, Date endTime){
        this.user = user;
        this.vehicle = ecoVehicle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = BookingStatus.BOOKED;
    }

    public void complete(){
        this.status = BookingStatus.COMPLETED;
    }

    public void cancel(){
        this.status = BookingStatus.CANCELLED;
    }

    public String getStatus() {
        return this.status.name().toLowerCase();
    }

}
