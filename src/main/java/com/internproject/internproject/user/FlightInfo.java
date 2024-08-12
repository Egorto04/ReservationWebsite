package com.internproject.internproject.user;

import com.internproject.internproject.error.ValidDateRange;
import jakarta.validation.constraints.NotNull;

import java.sql.Date;


@ValidDateRange
public class FlightInfo {
    private String departure;
    private String landing;

    @NotNull(message = "is required")
    private Date departureDate;

    @NotNull(message = "is required")
    private Date arrivalDate;

    private int adult = 0;
    private int child = 0;
    private int infant = 0;
    private String direction;
    private int total = adult + child + infant;

    public FlightInfo(String departure, String landing, Date departureDate, Date arrivalDate, int adult, int child, int infant, String direction, int total) {
        this.departure = departure;
        this.landing = landing;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.adult = adult;
        this.child = child;
        this.infant = infant;
        this.direction = direction;
        this.total = total;
    }

    public int getAdult() {
        return adult;
    }

    public void setAdult(int adult) {
        this.adult = adult;
    }

    public int getChild() {
        return child;
    }

    public void setChild(int child) {
        this.child = child;
    }

    public int getInfant() {
        return infant;
    }

    public void setInfant(int infant) {
        this.infant = infant;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public FlightInfo(String departure, String landing, Date departureDate) {
        this.departure = departure;
        this.landing = landing;
        this.departureDate = departureDate;
    }
    public FlightInfo(){}

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public String getLanding() {
        return landing;
    }

    public void setLanding(String landing) {
        this.landing = landing;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "FlightInfo{" +
                "departure='" + departure + '\'' +
                ", landing='" + landing + '\'' +
                ", departureDate=" + departureDate +
                ", arrivalDate=" + arrivalDate +
                ", adult=" + adult +
                ", child=" + child +
                ", infant=" + infant +
                ", direction='" + direction + '\'' +
                ", total=" + total +
                '}';
    }
}
