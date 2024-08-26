package com.internproject.internproject.dao;


import com.internproject.internproject.entity.Reservation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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
    public Reservation findById(int id) {
        return em.find(Reservation.class, id);
    }

    @Override
    public Reservation getReservation(String pnr) {
        TypedQuery<Reservation> query = em.createQuery("FROM Reservation where pnrCode=:pnr", Reservation.class);
        query.setParameter("pnr", pnr);
        List<Reservation> res = query.getResultList();
        if(res.size() > 0) {
            return res.get(0);
        }
        return null;
    }

    @Override
    public List<Reservation> getReservations(String pnr) {
        TypedQuery<Reservation> query = em.createQuery("FROM Reservation where pnrCode=:pnr", Reservation.class);
        query.setParameter("pnr", pnr);
        return query.getResultList();
    }

    @Override
    public List<Reservation> findAll() {
        return List.of();
    }

    @Override
    public void delete(String id) {
        em.createQuery("delete from Reservation where pnrCode=:id").setParameter("id", id).executeUpdate();
    }

    @Override
    public void changeRes(String id, String stat) {
        em.createQuery("UPDATE Reservation r SET r.status = :status WHERE r.pnrCode = :id")
                .setParameter("status", stat)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public List<Reservation> findByCreator(String creator) {
        TypedQuery<Reservation> query = em.createQuery("FROM Reservation where creator=:creator", Reservation.class);
        query.setParameter("creator", creator);
        return query.getResultList();
    }
}
