package com.internproject.internproject.dao;


import com.internproject.internproject.entity.User;

import java.util.List;

public interface UserDAO {

    public void save(User user);

    public User findById(int id);

    public List<User> findAll();

    public void delete(int id);

    public User findByUsername(String username);
}
