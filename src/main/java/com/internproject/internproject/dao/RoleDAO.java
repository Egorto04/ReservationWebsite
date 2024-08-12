package com.internproject.internproject.dao;

import com.internproject.internproject.entity.Role;

public interface RoleDAO {

    public Role findRoleByName(String roleName);
}
