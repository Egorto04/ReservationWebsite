package com.internproject.internproject.dao;


import com.internproject.internproject.entity.Reservation;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReservationDAOImpl implements ReservationDAO{

    EntityManager em;
    public ReservationDAOImpl(EntityManager em) {
        this.em = em;
    }
    @Override
    public void save(Reservation reservation) {
        em.persist(reservation);
    }

    @Override
    public Reservation findById(String id) {
        em.find(Reservation.class, id);
        return null;
    }

    @Override
    public List<Reservation> findAll() {
        return List.of();
    }
}
