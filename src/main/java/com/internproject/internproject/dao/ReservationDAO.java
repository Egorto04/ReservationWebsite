package com.internproject.internproject.dao;

import com.internproject.internproject.entity.Reservation;

import java.util.List;

public interface ReservationDAO {

    public void save(Reservation reservation);

    public Reservation findById(int id);

    public Reservation getReservation(String pnr);

    public List<Reservation> getReservations(String pnr);

    public List<Reservation> findAll();

    public void delete(String id);

    public void changeRes(String id, String status);

    public List<Reservation> findByCreator(String creator);
}
