package com.internproject.internproject.dao;


import com.internproject.internproject.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO{

    private EntityManager em;

    public UserDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(User user) {
        em.persist(user);
    }

    @Override
    public User findById(int id) {

        return em.find(User.class, id);
    }

    @Override
    public List<User> findAll() {
        return em.createQuery("from User", User.class).getResultList();
    }

    @Override
    public void delete(int id) {
        User u = em.find(User.class, id);
        if (u == null)
        {
            throw new RuntimeException("User not found");
        }else {
            em.remove(u);
        }
    }

    @Override
    public User findByUsername(String username) {
        TypedQuery<User> query = em.createQuery("from User where username = :username", User.class);
        query.setParameter("username", username);


        User theUser = null;

        try{
            theUser = query.getSingleResult();
        }catch (Exception e){
            theUser = null;
        }

        return theUser;

    }

    @Override
    public void deleteByUsername(String username) {
        em.createQuery("delete from User where username = :username").setParameter("username", username).executeUpdate();
    }

}
