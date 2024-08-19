package com.internproject.internproject.service;


import com.internproject.internproject.dao.*;
import com.internproject.internproject.entity.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.*;

@Service
public class CompanyService implements UserDetailsService {

    UserDAO userDAO;
    RoleDAO roleDAO;
    PlaneDAO planeDAO;
    ReservationDAO reservationDAO;
    UserPNRDAO userPNRDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CompanyService(UserDAO userDAO, RoleDAO roleDAO, PlaneDAO planeDAO, ReservationDAO reservationDAO, UserPNRDAO userPNRDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.planeDAO = planeDAO;
        this.reservationDAO = reservationDAO;
        this.userPNRDAO = userPNRDAO;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }


    @Transactional
    public void saveUser(User user) {
        userDAO.save(user);
    }

    @Transactional
    public void savePNR(UserPNR userPNR) {userPNRDAO.save(userPNR);}

    @Transactional
    public void savePlane(Plane plane)
    {
        planeDAO.save(plane);
    }

    public List<UserPNR> getFlyersPNR(String pnr)
    {
        return userPNRDAO.findByPNR(pnr);
    }

    @Transactional
    public void deleteUser(String username)
    {
        userDAO.deleteByUsername(username);
    }

    @Transactional
    public void saveReservation(Reservation reservation)
    {
        reservationDAO.save(reservation);
    }

    public Reservation findReservation(String reservationId)
    {
        return reservationDAO.findById(reservationId);
    }
    public List<Plane> findPlane(String departure, String landing, Date departureDate)
    {
        List<Plane> planes = planeDAO.getPlaneByDepartureLanding(departure,landing,departureDate);
        planes.sort(new Comparator<Plane>() {
            @Override
            public int compare(Plane o1, Plane o2) {
                String t1 = o1.getTimeDeparture();
                String t2 = o2.getTimeDeparture();
                return t1.compareTo(t2);
            }
        });
        return planes;
    }

    @Transactional
    public void deleteUserPNR(int id)
    {
        userPNRDAO.delete(id);
    }
    public Plane findById(int id)
    {
        return planeDAO.getPlaneById(id);
    }


    @Transactional
    public void deleteReservation(String id)
    {
        reservationDAO.delete(id);
    }

    public void reduceSeat(Plane p, int seatAmount, String seatType)
    {
        if (seatType.equals("Business"))
        {
            p.setBusinessSeat(p.getBusinessSeat()-seatAmount);
        }else if (seatType.equals("Economy"))
        {
            p.setEconomySeat(p.getEconomySeat()-seatAmount);
        }
        p.setAvailabeSeats(p.getAvailabeSeats()-seatAmount);
        planeDAO.save(p);
    }

    public void increaseSeat(Plane p, int seatAmount, String seatType)
    {
        if (seatType.equals("Business"))
        {
            p.setBusinessSeat(p.getBusinessSeat()+seatAmount);
        }else if (seatType.equals("Economy"))
        {
            p.setEconomySeat(p.getEconomySeat()+seatAmount);
        }
        p.setAvailabeSeats(p.getAvailabeSeats()+seatAmount);
        planeDAO.save(p);
    }

    @Transactional
    public void changeReservationStatus(String id)
    {
        reservationDAO.changeRes(id);
    }

//    @PostConstruct
//    public void createPlanes(){
//        Random rand = new Random();
//        String[] locations = new String[]{"Ankara","Istanbul","Izmir","Antalya","Adana","Trabzon","Samsun","Erzurum","Van","Konya"};
//        for (int i = 0; i < 10000; i++) {
//            Plane p = new Plane();
//            int randNum = rand.nextInt(locations.length);
//            p.setDepartureLocation(locations[randNum]);
//            int randNum2;
//            do {
//                randNum2 = rand.nextInt(locations.length);
//            }while (randNum == randNum2);
//            p.setLandingLocation(locations[randNum2]);
//            int randMonth = rand.nextInt(7,12);
//            int randDay = rand.nextInt(30);
//            Date d = new Date(124, randMonth, randDay);
//            p.setDateDeparture(d);
//            p.setAvailabeSeats(rand.nextInt(4,8)*50);
//            p.setBussinessPrice(rand.nextInt(1,9)*1000);
//            p.setEconomyPrice(rand.nextInt(1,9)*100);
//            int businessSeatAmount = rand.nextInt(0,10)*5;
//            int economySeatAmount = p.getAvailabeSeats()-businessSeatAmount;
//            p.setBusinessSeat(businessSeatAmount);
//            p.setEconomySeat(economySeatAmount);
//            if (i%10000 == 0)
//            {
//                System.out.println(i);
//            }
//            int randHour = rand.nextInt(24);
//            int randMinute = rand.nextInt(12)*5;
//            String hour = Integer.toString(randHour);
//            String minute = Integer.toString(randMinute);
//            if (randHour == 0)
//            {
//                hour = "00";
//            }else if (randHour < 10)
//            {
//                hour = "0"+randHour;
//            }
//            if (randMinute == 0)
//            {
//                minute = "00";
//            }else if (randMinute < 10)
//            {
//                minute = "0"+randMinute;
//            }
//            p.setTimeDeparture(hour+":"+minute);
//            planeDAO.save(p);
//        }
//    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        Collection<SimpleGrantedAuthority> authorities = mapRolestoAuthorities(user.getRoles());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    private Collection<SimpleGrantedAuthority> mapRolestoAuthorities(Collection<Role> roles) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    public User findUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }
}
