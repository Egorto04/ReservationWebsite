package com.internproject.internproject.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.internproject.internproject.entity.Plane;
import com.internproject.internproject.entity.Reservation;
import com.internproject.internproject.entity.User;
import com.internproject.internproject.entity.UserPNR;
import com.internproject.internproject.service.CompanyService;
import com.internproject.internproject.user.FlightInfo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/main-page")
public class MainPageController {

    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    CompanyService companyService;

    @Autowired
    public MainPageController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Value("${countries}")
    private List<String> countries;

    @Value("${numbers}")
    private List<Integer> numbers;

    private FlightInfo flightInfo;

    private Reservation reservation;

    private boolean isRoundTrip;

    private List<UserPNR> users;

    @RequestMapping("/home")
    public String home(Model model, @ModelAttribute("error") String error) {
        if (reservation == null)
        {
            reservation = new Reservation();
            reservation.setPnrCode(generatePNR());
        }
        model.addAttribute("countries", countries);
        model.addAttribute("numbers", numbers);
        model.addAttribute("flight", new FlightInfo());
        System.out.println(error);
        if (error != null)
        {
            model.addAttribute("error", error);
        }
        return "main-page";
    }
    @RequestMapping("/searchplane")
    public String searchPlane( Model model) {
        List<Plane> planes = companyService.findPlane(flightInfo.getDeparture(),flightInfo.getLanding(),flightInfo.getDepartureDate());
        model.addAttribute("planes", planes);
        System.out.println(flightInfo);

        int adult = flightInfo.getInfant();
        System.out.println(adult);
        model.addAttribute("flight", flightInfo);
        return "departure-plane-show";
    }

    @PostMapping("/processdeparture")
    public String processDeparture(@RequestParam("planeId") int planeId,
                                   Model model)  {
        Plane p = companyService.findById(planeId);
        reservation.setFlightNumberOne(planeId);
        reservation.setFlightNumberTwo(0);
        System.out.println(flightInfo);
        System.out.println(p);
        System.out.println(reservation);

        if (flightInfo.getDirection().equals("Roundtrip"))
        {
            List<Plane> planes = companyService.findPlane(flightInfo.getLanding(),flightInfo.getDeparture(),flightInfo.getArrivalDate());
            model.addAttribute("planes", planes);
            isRoundTrip = true;
            return "arrival-plane-show";
        }
        isRoundTrip = false;
        return "redirect:/main-page/seatSelection";
    }

    @GetMapping("/seatSelection")
    public String seatSelection(Model model)
    {
        Plane p1 = companyService.findById(reservation.getFlightNumberOne());
        if (reservation.getFlightNumberTwo() != 0)
        {
            Plane p2 = companyService.findById(reservation.getFlightNumberTwo());
            model.addAttribute("flightTwo", p2);
        }else{
            Plane p2 = new Plane();
            p2.setEconomyPrice(0);
            p2.setBussinessPrice(0);
            model.addAttribute("flightTwo", p2);
        }
        model.addAttribute("flightOne", p1);
        model.addAttribute("isRoundTrip", isRoundTrip);
        model.addAttribute("reservation", reservation);

        return "seat-selection-show";
    }

    @PostMapping("/checkValid")
    public String checkvalid(
            @Valid @ModelAttribute("flightInfo") FlightInfo flight, BindingResult theBindingResult, Model model) {

        flightInfo = flight;
        System.out.println(flightInfo);

        List<Plane> planes = companyService.findPlane(flightInfo.getDeparture(),flightInfo.getLanding(),flightInfo.getDepartureDate());
        System.out.println(planes);
        if (planes.isEmpty())
        {
            model.addAttribute("countries", countries);
            model.addAttribute("numbers", numbers);
            model.addAttribute("flight", new FlightInfo());
            model.addAttribute("error", "No flight found");
            System.out.println("No flight found");
            return "main-page";
        }

       if (theBindingResult.hasErrors())
       {
           return "redirect:/main-page/home";
       }else{
           return "redirect:/main-page/searchplane";
       }
    }


    public String generatePNR()
    {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(ALPHANUMERIC_STRING.length());
            sb.append(ALPHANUMERIC_STRING.charAt(index));
        }
        return sb.toString();
    }

    @PostMapping("/processarrival")
    public String processArrival(@RequestParam("planeId") int planeId, Model model) {
        reservation.setFlightNumberTwo(planeId);
        System.out.println(reservation);
        return "redirect:/main-page/seatSelection";
    }

    @PostMapping("/passenger-info")
    public String passengerInfo(@ModelAttribute("reservation") Reservation res, Model model) {
        reservation.setFirstType(res.getFirstType());
        reservation.setSecondType(res.getSecondType());
        Plane p = companyService.findById(reservation.getFlightNumberOne());
        Plane p1 = companyService.findById(reservation.getFlightNumberTwo());
        int firstPrice = 0;
        int secondPrice = 0;
        if (reservation.getFirstType().equals("Economy"))
        {
            firstPrice = p.getEconomyPrice();
        }else{
            firstPrice = p.getBussinessPrice();
        }
        if (reservation.getFlightNumberTwo() != 0)
        {
            if (reservation.getSecondType().equals("Economy"))
            {
                secondPrice = p1.getEconomyPrice();
            }else{
                secondPrice = p1.getBussinessPrice();
            }
        }

        int totalPas = flightInfo.getAdult() + flightInfo.getInfant() + flightInfo.getChild();

        firstPrice = firstPrice*totalPas;

        secondPrice = secondPrice*totalPas;

        reservation.setFirstPrice(firstPrice);

        reservation.setSecondPrice(secondPrice);

        System.out.println(firstPrice);

        System.out.println(secondPrice);

        System.out.println(reservation);

        int count = 0;

        List<UserPNR> users = new ArrayList<>();
        String[] types = new String[30];

        for (int i = 0; i < flightInfo.getAdult(); i++) {
            count++;
            UserPNR user = new UserPNR();
            types[count] = "Adult";
            users.add(user);
        }

        for (int i = 0; i < flightInfo.getChild(); i++) {
            count++;
            types[count] = "Child";
            UserPNR user = new UserPNR();
            users.add(user);
        }

        for (int i = 0; i < flightInfo.getInfant(); i++) {
            count++;
            UserPNR user = new UserPNR();
            types[count] = "Infant";
            users.add(user);
        }
        model.addAttribute("adult", flightInfo.getAdult());
        model.addAttribute("child", flightInfo.getChild());
        model.addAttribute("infant", flightInfo.getInfant());
        model.addAttribute("users", users);
        for (UserPNR user : users)
        {
            System.out.println(user);
        }

        return "passenger-form";
    }

    @RequestMapping("/submit-passenger-data")
    public String submitPassengerData(@RequestBody List<UserPNR> users) throws JsonProcessingException {
        this.users = users;
        return "redirect:/main-page/reservation-info";
    }

    @RequestMapping("/reservation-info")
    public String reservationForm()
    {
        for (UserPNR user: users)
        {
            user.setPnr(reservation.getPnrCode());
        }
        int count = 0;
        for (UserPNR user: users)
        {
            count++;
            System.out.println(count);
            System.out.println(user);
        }
        for (int i = 0; i < flightInfo.getAdult(); i++)
        {
            users.get(i).setPersonType("Adult");
        }
        for (int i = flightInfo.getAdult(); i < flightInfo.getAdult()+flightInfo.getChild(); i++)
        {
            users.get(i).setPersonType("Child");
        }
        for (int i = flightInfo.getAdult()+flightInfo.getChild(); i < flightInfo.getAdult()+flightInfo.getChild()+flightInfo.getInfant(); i++)
        {
            users.get(i).setPersonType("Infant");
        }
        for (UserPNR user: users)
        {
            companyService.savePNR(user);
        }
        companyService.saveReservation(reservation);
        return "redirect:/main-page/payment";
    }

    @RequestMapping("/payment")
    public String payment(Model model)
    {
        model.addAttribute("reservation", reservation);

        Plane p1 = companyService.findById(reservation.getFlightNumberOne());

        Plane p2;
        if (reservation.getFlightNumberTwo() == 0)
        {
            p2 = new Plane();
            model.addAttribute("planeTwo", p2);
        }else{
            p2 = companyService.findById(reservation.getFlightNumberTwo());
        }


        model.addAttribute("planeOne", p1);

        model.addAttribute("planeTwo", p2);

        model.addAttribute("users", users);

        return "payment";
    }


    @RequestMapping("/reservation-confirmed")
    public String reservationConfirmed(@RequestParam("pnrCode") String pnr, Model model)
    {
        companyService.changeReservationStatus(pnr);
        model.addAttribute("reservation", reservation);
        model.addAttribute("users", users);
        model.addAttribute("planeOne", companyService.findById(reservation.getFlightNumberOne()));
        model.addAttribute("planeTwo", companyService.findById(reservation.getFlightNumberTwo()));
        return "reservation-confirmation";
    }
    @RequestMapping("/find-reservation")
    public String findReservation(Model model) {
        if (model.containsAttribute("error")) {
            model.addAttribute("error", model.getAttribute("error"));
        }
        model.addAttribute("reservation", new Reservation());
        return "find-reservation";
    }

    @RequestMapping("/find-reservation-info")
    public String findPNRReservation(@RequestParam("pnrCode") String pnr, RedirectAttributes redirectAttributes, Model model) {
        System.out.println(pnr);
        if (companyService.findReservation(pnr) == null) {
            // Add the error message to the redirect attributes
            redirectAttributes.addFlashAttribute("error", "No reservation found");
            return "redirect:/main-page/find-reservation";
        }
        Reservation reservation = companyService.findReservation(pnr);
        List<UserPNR> users = companyService.getFlyersPNR(pnr);
        model.addAttribute("reservation", reservation);
        model.addAttribute("users", users);
        model.addAttribute("planeOne", companyService.findById(reservation.getFlightNumberOne()));
        model.addAttribute("planeTwo", companyService.findById(reservation.getFlightNumberTwo()));
        return "show-reservation";
    }
    @RequestMapping("/cancel-reservation")
    public String cancelReservation(@RequestParam("pnrCode") String pnr, RedirectAttributes redirectAttributes) {
        Reservation reservation = companyService.findReservation(pnr);
        List<UserPNR> users = companyService.getFlyersPNR(pnr);
        for (UserPNR user: users)
        {
            companyService.deleteUserPNR(user.getId());
        }
        companyService.deleteReservation(pnr);
        return "redirect:/main-page/home";
    }

}
