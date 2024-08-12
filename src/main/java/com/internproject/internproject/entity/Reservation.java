package com.internproject.internproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="reservations")
public class Reservation {

    @Id
    @Column(name = "pnr_code")
    private String pnrCode;

    @Column(name = "flight_number_one")
    private int flightNumberOne;

    @Column(name = "flight_number_two")
    private int flightNumberTwo;

    @Column(name = "first_type")
    private String firstType;

    @Column(name = "second_type")
    private String secondType;

    public Reservation(String pnrCode, int flightNumberOne, int flightNumberTwo, String firstType, String secondType) {
        this.pnrCode = pnrCode;
        this.flightNumberOne = flightNumberOne;
        this.flightNumberTwo = flightNumberTwo;
        this.firstType = firstType;
        this.secondType = secondType;
    }

    public Reservation() {
    }

    public String getPnrCode() {
        return pnrCode;
    }

    public void setPnrCode(String pnrCode) {
        this.pnrCode = pnrCode;
    }

    public int getFlightNumberOne() {
        return flightNumberOne;
    }

    public void setFlightNumberOne(int flightNumberOne) {
        this.flightNumberOne = flightNumberOne;
    }

    public int getFlightNumberTwo() {
        return flightNumberTwo;
    }

    public void setFlightNumberTwo(int flightNumberTwo) {
        this.flightNumberTwo = flightNumberTwo;
    }

    public String getFirstType() {
        return firstType;
    }

    public void setFirstType(String firstType) {
        this.firstType = firstType;
    }

    public String getSecondType() {
        return secondType;
    }

    public void setSecondType(String secondType) {
        this.secondType = secondType;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "pnrCode='" + pnrCode + '\'' +
                ", flightNumberOne=" + flightNumberOne +
                ", flightNumberTwo=" + flightNumberTwo +
                ", firstType='" + firstType + '\'' +
                ", secondType='" + secondType + '\'' +
                '}';
    }
}
