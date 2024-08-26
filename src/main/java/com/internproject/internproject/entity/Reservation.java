package com.internproject.internproject.entity;

import jakarta.persistence.*;

@Entity
@Table(name="reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

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

    @Column(name = "status")
    private String status;

    @Column(name = "first_price")
    private int firstPrice;

    @Column(name = "second_price")
    private int secondPrice;

    @Column(name = "creator")
    private String creator;

    public Reservation(String pnrCode, int flightNumberOne, int flightNumberTwo, String firstType, String secondType, String status, int firstPrice, int secondPrice, String creator) {
        this.pnrCode = pnrCode;
        this.flightNumberOne = flightNumberOne;
        this.flightNumberTwo = flightNumberTwo;
        this.firstType = firstType;
        this.secondType = secondType;
        this.status = status;
        this.firstPrice = firstPrice;
        this.secondPrice = secondPrice;
        this.creator = creator;
    }

    public Reservation() {
    }

    public String getPnrCode() {
        return pnrCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(int firstPrice) {
        this.firstPrice = firstPrice;
    }

    public int getSecondPrice() {
        return secondPrice;
    }

    public void setSecondPrice(int secondPrice) {
        this.secondPrice = secondPrice;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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
