package com.internproject.internproject.dao;

import com.internproject.internproject.entity.User;
import com.internproject.internproject.entity.UserPNR;

import java.util.List;

public interface UserPNRDAO {
    public void save(UserPNR userPNR);

    public UserPNR findById(int id);

    public List<User> findAll();

    public void delete(int id);

    public List<UserPNR> findByPNR(String pnr);
}
