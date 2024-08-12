package com.internproject.internproject.dao;

import com.internproject.internproject.entity.Reservation;

import java.util.List;

public interface ReservationDAO {

    public void save(Reservation reservation);

    public Reservation findById(String id);

    public List<Reservation> findAll();
}
