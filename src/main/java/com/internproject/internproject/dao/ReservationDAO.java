package com.internproject.internproject.dao;

import com.internproject.internproject.entity.Reservation;

import java.util.List;

public interface ReservationDAO {

    public void save(Reservation reservation);

    public Reservation findById(String id);

    public String getCreator(String id);

    public List<Reservation> findAll();

    public void delete(String id);

    public void changeRes(String id, String status);

    public List<Reservation> findByCreator(String creator);
}
