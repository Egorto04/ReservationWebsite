package com.internproject.internproject.dao;


import com.internproject.internproject.entity.Reservation;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ReservationDAOImpl implements ReservationDAO{

    EntityManager em;
    public ReservationDAOImpl(EntityManager em) {
        this.em = em;
    }
    @Override
    public void save(Reservation reservation) {
        em.merge(reservation);
    }

    @Override
    public Reservation findById(String id) {
        return em.find(Reservation.class, id);
    }

    @Override
    public List<Reservation> findAll() {
        return List.of();
    }

    @Override
    public void delete(String id) {
        em.createQuery("delete from Reservation where id=:id").setParameter("id", id).executeUpdate();
    }

    @Override
    public void changeRes(String id) {
        em.createQuery("update Reservation set status='TICKETED' where id=:id").setParameter("id", id).executeUpdate();
    }
}
