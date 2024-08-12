package com.internproject.internproject.dao;

import com.internproject.internproject.entity.Plane;

import java.sql.Date;
import java.util.List;

public interface PlaneDAO {

    public void save(Plane plane);

    public List<Plane> getAllPlane();

    public Plane getPlaneById(int id);

    public List<Plane> getPlaneByDepartureLanding(String departure, String landing, Date departureDate);

    public void deletePlaneById(int id);
}
