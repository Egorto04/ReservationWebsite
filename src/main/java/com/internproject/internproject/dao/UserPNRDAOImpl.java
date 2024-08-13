package com.internproject.internproject.dao;


import com.internproject.internproject.entity.User;
import com.internproject.internproject.entity.UserPNR;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserPNRDAOImpl implements UserPNRDAO{

    EntityManager em;

    public UserPNRDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(UserPNR user) {
        em.merge(user);
    }

    @Override
    public UserPNR findById(int id) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public void delete(int id) {
        em.createQuery("delete from UserPNR where id=:id").setParameter("id", id).executeUpdate();
    }

    @Override
    public List<UserPNR> findByPNR(String pnr) {
        TypedQuery<UserPNR> users = em.createQuery("FROM UserPNR where pnr=:pnr", UserPNR.class);
        users.setParameter("pnr", pnr);
        return users.getResultList();
    }
}
