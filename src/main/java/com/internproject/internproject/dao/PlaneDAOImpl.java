package com.internproject.internproject.dao;

import com.internproject.internproject.entity.Plane;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.sql.Date;
import java.util.List;


@Repository
public class PlaneDAOImpl implements PlaneDAO{

    EntityManager em;

    public PlaneDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Transactional
    @Override
    public void save(Plane plane) {
        em.persist(plane);
    }

    @Override
    public List<Plane> getAllPlane() {
        TypedQuery<Plane> query = em.createQuery("FROM Plane ", Plane.class);
        return query.getResultList();
    }

    @Override
    public Plane getPlaneById(int id) {
        return em.find(Plane.class,id);
    }

    @Override
    public List<Plane> getPlaneByDepartureLanding(String departure, String landing, Date departureDate) {
        TypedQuery<Plane> planes = em.createQuery("FROM Plane where departureLocation = :departure and landingLocation = :landing and dateDeparture =: date", Plane.class);
        planes.setParameter("departure", departure);
        planes.setParameter("landing", landing);
        planes.setParameter("date", departureDate);
        return planes.getResultList();
    }

    @Override
    public void deletePlaneById(int id) {

    }
}
