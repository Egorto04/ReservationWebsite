package com.internproject.internproject.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.sql.Date;
import java.util.Comparator;

@Entity
@Table(name ="planes")
public class Plane {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flight_id")
    private Integer flightId;

    @Column(name = "departure_location")
    private String departureLocation;

    @Column(name = "landing_location")
    private String landingLocation;

    @Column(name = "date_departure")
    private Date dateDeparture;

    @Column(name = "time_departure")
    private String timeDeparture;

    @Column(name="availabe_seats")
    private int availabeSeats;

    @Column(name = "economy_price")
    private int economyPrice;

    @Column(name = "bussiness_price")
    private int bussinessPrice;

    @Column(name = "economy_availability")
    private int economySeat;

    @Column(name = "business_availability")
    private int businessSeat;

    public Plane() {

    }

    public Plane(String departureLocation, String landingLocation, Date dateDeparture, String timeDeparture, int availabeSeats, int economyPrice, int bussinessPrice, int economySeat, int businessSeat) {
        this.departureLocation = departureLocation;
        this.landingLocation = landingLocation;
        this.dateDeparture = dateDeparture;
        this.timeDeparture = timeDeparture;
        this.availabeSeats = availabeSeats;
        this.economyPrice = economyPrice;
        this.bussinessPrice = bussinessPrice;
        this.economySeat = economySeat;
        this.businessSeat = businessSeat;
    }

    public int getEconomyPrice() {
        return economyPrice;
    }

    public void setEconomyPrice(int economyPrice) {
        this.economyPrice = economyPrice;
    }

    public int getBussinessPrice() {
        return bussinessPrice;
    }

    public void setBussinessPrice(int bussinessPrice) {
        this.bussinessPrice = bussinessPrice;
    }

    public int getEconomySeat() {
        return economySeat;
    }

    public void setEconomySeat(int economySeat) {
        this.economySeat = economySeat;
    }

    public int getBusinessSeat() {
        return businessSeat;
    }

    public void setBusinessSeat(int businessSeat) {
        this.businessSeat = businessSeat;
    }

    public Integer getFlightId() {
        return flightId;
    }

    public void setFlightId(Integer flightId) {
        this.flightId = flightId;
    }

    public String getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
    }

    public String getLandingLocation() {
        return landingLocation;
    }

    public void setLandingLocation(String landingLocation) {
        this.landingLocation = landingLocation;
    }

    public Date getDateDeparture() {
        return dateDeparture;
    }

    public void setDateDeparture(Date dateDeparture) {
        this.dateDeparture = dateDeparture;
    }

    public String getTimeDeparture() {
        return timeDeparture;
    }

    public void setTimeDeparture(String timeDeparture) {
        this.timeDeparture = timeDeparture;
    }

    public int getAvailabeSeats() {
        return availabeSeats;
    }

    public void setAvailabeSeats(int availabeSeats) {
        this.availabeSeats = availabeSeats;
    }

    @Override
    public String toString() {
        return "Plane{" +
                "flightId=" + flightId +
                ", departureLocation='" + departureLocation + '\'' +
                ", landingLocation='" + landingLocation + '\'' +
                ", dateDeparture=" + dateDeparture +
                ", timeDeparture='" + timeDeparture + '\'' +
                ", availabeSeats=" + availabeSeats +
                ", economyPrice=" + economyPrice +
                ", bussinessPrice=" + bussinessPrice +
                ", economySeat=" + economySeat +
                ", businessSeat=" + businessSeat +
                '}';
    }
}
