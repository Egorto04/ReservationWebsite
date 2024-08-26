package com.internproject.internproject.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.internproject.internproject.entity.Plane;
import com.internproject.internproject.entity.Reservation;
import com.internproject.internproject.entity.User;
import com.internproject.internproject.entity.UserPNR;
import com.internproject.internproject.service.CompanyService;
import com.internproject.internproject.user.FlightInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LENGTH = 8;
    private static final SecureRandom RANDOM = new SecureRandom();
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

    private Reservation editReservation;

    private boolean isRoundTrip;

    private boolean editing;

    private boolean changedPassengerCount;

    private List<UserPNR> users;

    private int newAdult;
    private int newChild;
    private int newInfant;


    @RequestMapping("/home")
    public String home(Model model, @ModelAttribute("error") String error) {
        editing = false;
        changedPassengerCount = false;
        if (reservation == null)
        {
            reservation = new Reservation();
            String pnr;
            do {
                pnr = generatePNR();
            }while (companyService.findReservation(pnr) != null);
            reservation.setPnrCode(pnr);
        }
        model.addAttribute("countries", countries);
        model.addAttribute("numbers", numbers);
        model.addAttribute("flight", new FlightInfo());
        if (error != null)
        {
            model.addAttribute("error", error);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User u = companyService.findUserByUsername(username);
        model.addAttribute("user", u);
        return "main-page";
    }
    @RequestMapping("/edit-reservation")
    public String editReservation(@RequestParam("pnrCode") String pnr, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (!companyService.findReservation(pnr).getCreator().equals(username)) {
            return "redirect:/main-page/home";
        }
        if(companyService.findReservation(pnr).getStatus().equals("TICKETED"))
        {
            return "redirect:/main-page/home";
        }
        editing = true;
        Reservation reservation = companyService.findReservation(pnr);
        editReservation = reservation;
        List<UserPNR> users = companyService.getFlyersPNR(pnr);
        flightInfo = new FlightInfo();
        int adult = 0;
        int child = 0;
        int infant = 0;
        for (UserPNR user: users)
        {
            if (user.getPersonType().equals("Adult"))
            {
                adult++;
            }else if (user.getPersonType().equals("Child")) {
                child++;
            }else {
                infant++;
            }
        }
        flightInfo.setAdult(adult);
        flightInfo.setChild(child);
        flightInfo.setInfant(infant);
        Plane p = companyService.findById(editReservation.getFlightNumberOne());
        flightInfo.setDeparture(p.getDepartureLocation());
        flightInfo.setLanding(p.getLandingLocation());
        flightInfo.setDepartureDate(p.getDateDeparture());
        if (reservation.getFlightNumberTwo() != 0)
        {
            flightInfo.setDirection("Roundtrip");
            Plane p2 = companyService.findById(editReservation.getFlightNumberTwo());
            flightInfo.setArrivalDate(p2.getDateDeparture());
        }else{
            flightInfo.setDirection("One-Way");
        }
        model.addAttribute("countries", countries);
        model.addAttribute("numbers", numbers);
        model.addAttribute("reservation", reservation);
        model.addAttribute("flight", flightInfo);
        return "edit-reservation";
    }

    @RequestMapping("/find-user")
    public String getUser(@RequestParam("memberNo") String memberNo, Model model, RedirectAttributes redirectAttributes)
    {
        User user = companyService.getByMemberNo(memberNo);
        if (user == null)
        {
            redirectAttributes.addFlashAttribute("error2", "No user found with that Member No");
            return "redirect:/main-page/user-management";
        }
        model.addAttribute("user", user);
        return "show-user";
    }


    @RequestMapping("/searchplane")
    public String searchPlane( Model model,  @ModelAttribute("error") String error) {
        List<Plane> planes = companyService.findPlane(flightInfo.getDeparture(),flightInfo.getLanding(),flightInfo.getDepartureDate());
        model.addAttribute("planes", planes);
        model.addAttribute("error", error);
        model.addAttribute("flight", flightInfo);
        return "departure-plane-show";
    }

    @RequestMapping("/processdeparture")
    public String processDeparture(@RequestParam("planeId") int planeId,
                                   Model model, RedirectAttributes redirectAttributes)  {
        Plane p = companyService.findById(planeId);
        if (editing)
        {
            editReservation.setFlightNumberOne(planeId);
            editReservation.setFlightNumberTwo(0);
        }else{
            reservation.setFlightNumberOne(planeId);
            reservation.setFlightNumberTwo(0);
        }

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
        if (editing)
        {
            Plane p1 = companyService.findById(editReservation.getFlightNumberOne());
            Plane p2;
            if (editReservation.getFlightNumberTwo() != 0)
            {
                p2 = companyService.findById(editReservation.getFlightNumberTwo());
            }else{
                p2 = new Plane();
                p2.setEconomyPrice(0);
                p2.setBussinessPrice(0);
            }
            model.addAttribute("flightTwo", p2);
            model.addAttribute("flightOne", p1);
            model.addAttribute("isRoundTrip", isRoundTrip);
            model.addAttribute("reservation", editReservation);
            model.addAttribute("address", "/main-page/save-res");
            if (changedPassengerCount)
            {
                model.addAttribute("address",   "/main-page/edit-passenger-info?pnrCode=" +editReservation.getPnrCode());
            }
            return "seat-selection-show";
        }else{
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
        }
        if (editing)
        {
            editing = false;
            model.addAttribute("address", "/main-page/save-res");
        }else {
            model.addAttribute("address", "/main-page/passenger-info");
        }
        return "seat-selection-show";
    }
    @RequestMapping("/save-res")
    public String saveRes(@ModelAttribute("Reservation") Reservation res)
    {

        editReservation.setFirstType(res.getFirstType());
        editReservation.setSecondType(res.getSecondType());
        Plane p = companyService.findById(editReservation.getFlightNumberOne());
        Plane p1 = companyService.findById(editReservation.getFlightNumberTwo());
        int firstPrice = 0;
        int secondPrice = 0;
        if (editReservation.getFirstType().equals("Economy"))
        {
            firstPrice = p.getEconomyPrice();
        }else{
            firstPrice = p.getBussinessPrice();
        }
        if (editReservation.getFlightNumberTwo() != 0)
        {
            if (editReservation.getSecondType().equals("Economy"))
            {
                secondPrice = p1.getEconomyPrice();
            }else{
                secondPrice = p1.getBussinessPrice();
            }
        }

        int totalPas = flightInfo.getAdult() + flightInfo.getInfant() + flightInfo.getChild();

        firstPrice = firstPrice*totalPas;

        secondPrice = secondPrice*totalPas;

        editReservation.setFirstPrice(firstPrice);

        editReservation.setSecondPrice(secondPrice);

        companyService.saveReservation(editReservation);

        return "redirect:/main-page/find-reservation-info?pnrCode="+editReservation.getPnrCode();
    }
    @PostMapping("/checkValid")
    public String checkvalid(@Valid @ModelAttribute("flightInfo") FlightInfo flight, BindingResult theBindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (flightInfo != null)
        {
            if (flight.getAdult() != flightInfo.getAdult() || flight.getChild() != flightInfo.getChild() || flight.getInfant() != flightInfo.getInfant())
            {
                newAdult = flight.getAdult();
                newChild = flight.getChild();
                newInfant = flight.getInfant();
                if (newAdult == 0 && newChild == 0 && newInfant == 0)
                {
                    redirectAttributes.addFlashAttribute("error", "Please select at least one passenger");
                    return "redirect:/main-page/home";
                }
                if (newAdult + newChild + newInfant < flightInfo.getAdult() + flightInfo.getChild() + flightInfo.getInfant())
                {
                    companyService.deleteUserPNR(companyService.getFlyersPNR(editReservation.getPnrCode()).get(0).getId());
                }
                changedPassengerCount = true;
            }
        }
        flightInfo = flight;

        List<Plane> planes = companyService.findPlane(flightInfo.getDeparture(),flightInfo.getLanding(),flightInfo.getDepartureDate());

        if (planes.isEmpty())
        {
            redirectAttributes.addFlashAttribute("countries", countries);
            redirectAttributes.addFlashAttribute("numbers", numbers);
            redirectAttributes.addFlashAttribute("flight", new FlightInfo());
            redirectAttributes.addFlashAttribute("error", "NO FLIGHTS FOR DEPARTURE");
            if (editing)
            {
                return "redirect:/main-page/edit-reservation?pnrCode="+editReservation.getPnrCode();
            }
            return "redirect:/main-page/home";
        }
        if (flightInfo.getDirection().equals("Roundtrip"))
        {
            List<Plane> planes2 = companyService.findPlane(flightInfo.getLanding(),flightInfo.getDeparture(),flightInfo.getArrivalDate());
            if (planes2.isEmpty())
            {
                redirectAttributes.addFlashAttribute("countries", countries);
                redirectAttributes.addFlashAttribute("numbers", numbers);
                redirectAttributes.addFlashAttribute("flight", new FlightInfo());
                redirectAttributes.addFlashAttribute("error", "NO FLIGHTS FOR ARRIVAL");
                if (editing)
                {
                    return "redirect:/main-page/edit-reservation?pnrCode="+editReservation.getPnrCode();
                }
                return "redirect:/main-page/home";
            }
        }

        return "redirect:/main-page/searchplane";
    }
    @RequestMapping("/help")
    public String help(Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User u = companyService.findUserByUsername(username);
        model.addAttribute("user", u);
        return "help";
    }
    @RequestMapping("/payment-search")
    public String paymentSearch(@ModelAttribute("error") String error, Model model)
    {
        if (error != null)
        {
            model.addAttribute("error", error);
        }
        return "payment-search";
    }

    @RequestMapping("/look-for-reservation")
    public String lookForReservation(@RequestParam("paymentId") String pnr, RedirectAttributes redirectAttributes)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (companyService.findReservation(pnr) != null)
        {
            if (companyService.findReservation(pnr).getStatus().equals("TICKETED")) {
                redirectAttributes.addFlashAttribute("error", "Reservation already ticketed");
                return "redirect:/main-page/payment-search";
            }
            if (!companyService.findReservation(pnr).getCreator().equals(username))
            {
                redirectAttributes.addFlashAttribute("error", "Reservation is not yours!");
                return "redirect:/main-page/payment-search";
            }
            return "redirect:/main-page/make-payment?pnrCode="+pnr;
        }else{
            redirectAttributes.addFlashAttribute("error", "No reservation found");
            return "redirect:/main-page/payment-search";
        }
    }
    @RequestMapping("/reservation-confirmed")
    public String reservationConfirmed(@RequestParam("paymentType") String payment, @RequestParam("pnrCode") String pnr, Model model, RedirectAttributes redirectAttributes)
    {
        Reservation reservation1 = companyService.findReservation(pnr);
        System.out.println("1. " +reservation1.getStatus());
        List<UserPNR> users = companyService.getFlyersPNR(pnr);
        User u = companyService.findUserByUsername(reservation1.getCreator());
        String memberNo = u.getMemberNo();
        int pointsSpend = (users.size()*(reservation1.getFirstPrice() + reservation1.getSecondPrice()))/2;
        int pointsGained = (users.size()*(reservation1.getFirstPrice() + reservation1.getSecondPrice()))/10;
        if (payment.equals("Points"))
        {
            System.out.println("Points");
            if (u.getPoints() <  pointsSpend)
            {
                redirectAttributes.addFlashAttribute("error", "Not enough points");
                return "redirect:/main-page/make-payment?pnrCode="+pnr;
            }else{
                companyService.decreasePoints(memberNo, pointsSpend);
                companyService.changeReservationStatus(pnr, "TICKETED");
            }
        }else{
            companyService.increasePoints(memberNo, pointsGained);
            companyService.changeReservationStatus(pnr, "TICKETED");
        }

        Plane p1 = companyService.findById(reservation1.getFlightNumberOne());

        companyService.reduceSeat(p1, users.size(), reservation1.getFirstType());

        if (reservation1.getFlightNumberTwo() != 0) {

            Plane p2 = companyService.findById(reservation1.getFlightNumberTwo());

            companyService.reduceSeat(p2, users.size(), reservation1.getSecondType());

        }

        companyService.checkMembership(memberNo);

        reservation1.setStatus("TICKETED");

        model.addAttribute("reservation", reservation1);
        model.addAttribute("users", users);
        model.addAttribute("planeOne", companyService.findById(reservation1.getFlightNumberOne()));
        model.addAttribute("planeTwo", companyService.findById(reservation1.getFlightNumberTwo()));
        model.addAttribute("address", "/main-page/home");
        return "show-reservation";
    }
    @RequestMapping("/make-payment")
    public String makePayment(@RequestParam("pnrCode") String pnr, @ModelAttribute("error") String error, Model model) {
        if(companyService.findReservation(pnr).getStatus().equals("TICKETED"))
        {
            return "redirect:/main-page/home";
        }
        Reservation reservation = companyService.findReservation(pnr);
        List<UserPNR> users = companyService.getFlyersPNR(pnr);
        Plane p1 = companyService.findById(reservation.getFlightNumberOne());
        Plane p2 = companyService.findById(reservation.getFlightNumberTwo());
        model.addAttribute("reservation", reservation);
        model.addAttribute("users", users);
        model.addAttribute("planeOne", p1);
        model.addAttribute("planeTwo", p2);
        model.addAttribute("error",error);
        int totalPrice = (reservation.getFirstPrice() + reservation.getSecondPrice())*users.size();
        model.addAttribute("totalPrice", totalPrice);
        return "payment";
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
        if (editing)
        {
            editReservation.setFlightNumberTwo(planeId);
        }else{
            reservation.setFlightNumberTwo(planeId);
        }
        return "redirect:/main-page/checkPlaneValidity";
    }

    @RequestMapping ("/checkPlaneValidity")
    public String checkPlaneValidity(RedirectAttributes redirectAttributes)
    {
        Plane p1;
        Plane p2;
        if (editing)
        {
            p1 = companyService.findById(editReservation.getFlightNumberOne());
            p2 = companyService.findById(editReservation.getFlightNumberTwo());
        }else{
            p1 = companyService.findById(reservation.getFlightNumberOne());
            p2 = companyService.findById(reservation.getFlightNumberTwo());
        }
        redirectAttributes.addFlashAttribute("error", "Please select a valid plane time");
        if (flightInfo.getDepartureDate().getTime() == flightInfo.getArrivalDate().getTime())
        {
            if (p1.getTimeDeparture().compareTo(p2.getTimeDeparture()) < 0)
            {
                return "redirect:/main-page/seatSelection";
            }else{
                return "redirect:/main-page/searchplane";
            }
        }
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

        companyService.getFlyersPNR(reservation.getPnrCode()).forEach(userPNR -> {
            companyService.deleteUserPNR(userPNR.getId());
        });

        reservation.setFirstPrice(firstPrice);

        reservation.setSecondPrice(secondPrice);

        int count = 0;

        List<UserPNR> users = new ArrayList<>();
        String[] types = new String[30];



        for (int i = 0; i < flightInfo.getAdult(); i++) {
            users.add(new UserPNR());
        }

        for (int i = 0; i < flightInfo.getChild(); i++) {
            users.add(new UserPNR());
        }

        for (int i = 0; i < flightInfo.getInfant(); i++) {
            users.add(new UserPNR());
        }

        model.addAttribute("adult", flightInfo.getAdult());
        model.addAttribute("child", flightInfo.getChild());
        model.addAttribute("infant", flightInfo.getInfant());
        model.addAttribute("users", users);

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        for (UserPNR user: users)
        {
            user.setPnr(reservation.getPnrCode());
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
        reservation.setCreator(username);
        companyService.saveReservation(reservation);
        companyService.changeReservationStatus(reservation.getPnrCode(), "NOT TICKETED");
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

        int totalPrice = (reservation.getFirstPrice()+ reservation.getSecondPrice())*users.size();

        model.addAttribute("totalPrice", totalPrice);

        return "payment";
    }
    @RequestMapping("/add-user")
    public String addUser(@RequestParam("username") String username,@RequestParam("firstName") String firstName ,@RequestParam("lastName") String lastName,@RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword,  RedirectAttributes redirectAttributes,Model model)
    {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!password.equals(confirmPassword))
        {
            redirectAttributes.addFlashAttribute("error", "Password does not match");
        }else if (companyService.findUserByUsername(username) != null)
        {
            redirectAttributes.addFlashAttribute("error", "Username already exists");
        }else{
            User user = new User();
            String randomMember;
            do {
                randomMember = generateRandomValue();
            }while(companyService.getByMemberNo(randomMember) != null);
            user.setMemberNo(randomMember);
            user.setMembership("Regular");
            user.setPoints(0);
            user.setUsername(username);
            user.setLastName(lastName);
            user.setFirstName(firstName);
            user.setEmail(email);
            user.setEnabled(true);
            password = passwordEncoder.encode(password);
            user.setPassword(password);
            companyService.saveUser(user);
            redirectAttributes.addFlashAttribute("error", "User added successfully");
        }
        return "redirect:/main-page/user-management";
    }
    public String generateRandomValue() {
        StringBuilder randomValue = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            randomValue.append(CHARACTERS.charAt(randomIndex));
        }
        return randomValue.toString();
    }
    @RequestMapping("/delete-profile")
    public String deleteProfile(HttpServletRequest request, HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null){
            String username = authentication.getName();
            companyService.deleteUser(username);
            List<Reservation> reservations = companyService.getPNRs(username);
            for (Reservation reservation : reservations)
            {
                companyService.deleteReservation(reservation.getPnrCode());
                Plane p1 = companyService.findById(reservation.getFlightNumberOne());
                companyService.increaseSeat(p1, companyService.getFlyersPNR(reservation.getPnrCode()).size(), reservation.getFirstType());
                if (reservation.getFlightNumberTwo() != 0) {
                    Plane p2 = companyService.findById(reservation.getFlightNumberTwo());
                    companyService.increaseSeat(p2, companyService.getFlyersPNR(reservation.getPnrCode()).size(), reservation.getSecondType());
                }
                List<UserPNR> users = companyService.getFlyersPNR(reservation.getPnrCode());
                for (UserPNR userPNR: users)
                {
                    companyService.deleteUserPNR(userPNR.getId());
                }
            }
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/showLoginPage?logout"; // Redirect to login page after logout
    }
    @RequestMapping("/delete-user")
    public String delete(@RequestParam("delUsername") String username, RedirectAttributes redirectAttributes)
    {
        User user = companyService.findUserByUsername(username);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username1 = authentication.getName();
        if (username1.equals(username)) {
            redirectAttributes.addFlashAttribute("error1", "You cannot delete yourself");
        }else if (user == null)
        {
            redirectAttributes.addFlashAttribute("error1", "User not found");
        }else{
            companyService.deleteUser(username);
            redirectAttributes.addFlashAttribute("error1", "User deleted successfully");
        }
        List<Reservation> pnrs = companyService.getPNRs(username);
        for (Reservation pnr: pnrs)
        {
            companyService.deleteReservation(pnr.getPnrCode());
            List<UserPNR> users = companyService.getFlyersPNR(pnr.getPnrCode());
            for (UserPNR userPNR: users)
            {
                companyService.deleteUserPNR(userPNR.getId());
            }
        }
        return "redirect:/main-page/user-management";
    }


    @RequestMapping("/user-management")
    public String userManagement(Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User u = companyService.findUserByUsername(username);
        model.addAttribute("user", u);
        return "user-management";
    }

    @RequestMapping("/find-reservation")
    public String findReservation(Model model) {
        if (model.containsAttribute("error")) {
            model.addAttribute("error", model.getAttribute("error"));
        }
        model.addAttribute("reservation", new Reservation());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (companyService.getPNRs(username) != null)
        {
            model.addAttribute("pnrCodes", companyService.getPNRs(username));
        }
        User u = companyService.findUserByUsername(username);
        model.addAttribute("user", u);
        return "find-reservation";
    }

    @RequestMapping("/find-reservation-info")
    public String findPNRReservation(@RequestParam("pnrCode") String pnr, RedirectAttributes redirectAttributes, Model model) {
        if (companyService.findReservation(pnr) == null ) {
            redirectAttributes.addFlashAttribute("error", "No reservation found");
            return "redirect:/main-page/find-reservation";
        }
        if (companyService.findReservation(pnr).getStatus().equals("CANCELLED"))
        {
            redirectAttributes.addFlashAttribute("error", "This reservation is cancelled");
            return "redirect:/main-page/find-reservation";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (!companyService.findReservation(pnr).getCreator().equals(username))
        {
            redirectAttributes.addFlashAttribute("error", "Reservation is not yours!");
            return "redirect:/main-page/find-reservation";
        }
        Reservation reservation = companyService.findReservation(pnr);
        List<UserPNR> users = companyService.getFlyersPNR(pnr);
        model.addAttribute("reservation", reservation);
        model.addAttribute("users", users);
        model.addAttribute("planeOne", companyService.findById(reservation.getFlightNumberOne()));
        model.addAttribute("planeTwo", companyService.findById(reservation.getFlightNumberTwo()));
        model.addAttribute("address", "/main-page/find-reservation");
        return "show-reservation";
    }
    @RequestMapping("/cancel-reservation")
    public String cancelReservation(@RequestParam("pnrCode") String pnr, @RequestParam("address") String address, RedirectAttributes redirectAttributes) {
        Reservation reservation = companyService.findReservation(pnr);
        List<UserPNR> users = companyService.getFlyersPNR(pnr);
        Plane p1 = companyService.findById(reservation.getFlightNumberOne());
        companyService.increaseSeat(p1, users.size(), reservation.getFirstType());
        if(reservation.getFlightNumberTwo() != 0)
        {
            Plane p2 = companyService.findById(reservation.getFlightNumberTwo());
            companyService.increaseSeat(p2, users.size(), reservation.getSecondType());
        }
        companyService.changeReservationStatus(pnr, "CANCELLED");
        return "redirect:/main-page/reset-everything?address=" + address;
    }

    @RequestMapping("/reset-everything")
    public String resetEverything(@RequestParam("address") String address)
    {
        reservation = null;
        flightInfo = null;
        users = null;
        return "redirect:" + address;
    }

    @RequestMapping("/edit-passenger-info")
    public String editPassenger(@RequestParam("pnrCode") String pnr, Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (!companyService.findReservation(pnr).getCreator().equals(username)) {
            return "redirect:/main-page/home";
        }
        if(companyService.findReservation(pnr).getStatus().equals("TICKETED"))
        {
            return "redirect:/main-page/home";
        }
        List<UserPNR> passengers = companyService.getFlyersPNR(pnr);
        List<UserPNR> dataSend = new ArrayList<>();
        if (changedPassengerCount)
        {
            changedPassengerCount = false;
            int passenger = flightInfo.getAdult() + flightInfo.getChild() + flightInfo.getInfant();
            for (int i = 0; i < passenger; i++) {
                if (i < passengers.size())
                {
                    dataSend.add(passengers.get(i));
                }else {
                    UserPNR user = new UserPNR();
                    user.setName("");
                    user.setSurname("");
                    user.setBirthDate(null);
                    user.setPassportNo("");
                    user.setNationalityNo("");
                    user.setTelNo("");
                    user.setEmail("");
                    dataSend.add(user);
                }
            }
        }else{
            dataSend = passengers;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String passengerDataJson = objectMapper.writeValueAsString(dataSend);
            model.addAttribute("pnrCode", pnr);
            model.addAttribute("passengerData", passengerDataJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "edit-passenger-info";
    }

    @RequestMapping("/update-passengers")
    public String updatePassengers(@RequestBody List<UserPNR> passengers) {
        if (newAdult != 0 ||newChild != 0 || newInfant != 0)
        {
            for (UserPNR passenger : passengers) {
                if (newAdult != 0)
                {
                    passenger.setPersonType("Adult");
                } else if (newChild != 0) {
                    passenger.setPersonType("Child");
                }else {
                    passenger.setPersonType("Infant");
                }
                passenger.setPnr(passengers.get(0).getPnr());
                companyService.savePNR(passenger);

                if (passenger.getPersonType().equals("Adult"))
                {
                    newAdult--;
                } else if (passenger.getPersonType().equals("Child")) {
                    newChild--;
                } else {
                    newInfant--;
                }
            }
            newAdult = 0;
            newChild = 0;
            newInfant = 0;
        }else {
            for (UserPNR passenger : passengers) {
                passenger.setPnr(passengers.get(0).getPnr());
                companyService.savePNR(passenger);
            }
        }

        return "redirect:/main-page/find-reservation-info?pnrCode=" + passengers.get(0).getPnr();
    }



}
