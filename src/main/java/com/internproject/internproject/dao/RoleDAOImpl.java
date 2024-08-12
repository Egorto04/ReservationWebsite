package com.internproject.internproject.dao;

import com.internproject.internproject.entity.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;


@Repository
public class RoleDAOImpl implements RoleDAO{

    private EntityManager em;

    public RoleDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Role findRoleByName(String roleName) {
        TypedQuery<Role> query = em.createQuery("FROM Role where name=:data", Role.class);
        query.setParameter("data", roleName);
        Role theRole = null;
        try {
            theRole = query.getSingleResult();
        } catch (Exception e) {
            theRole = null;
        }
        return theRole;
    }
}
