package com.internproject.internproject.entity;


import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "user_and_pnrs")
public class UserPNR {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "pnr")
    private String pnr;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "passport_no")
    private String passportNo;

    @Column(name = "nationality_no")
    private String nationalityNo;

    @Column(name = "tel_no")
    private String telNo;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    private String gender;

    @Column(name = "person_type")
    private String personType;

    public UserPNR(String pnr, String name, String surname, Date birthDate, String passportNo, String nationalityNo, String telNo, String email, String gender, String personType) {
        this.pnr = pnr;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.passportNo = passportNo;
        this.nationalityNo = nationalityNo;
        this.telNo = telNo;
        this.email = email;
        this.gender = gender;
        this.personType = personType;
    }

    public UserPNR() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getNationalityNo() {
        return nationalityNo;
    }

    public void setNationalityNo(String nationalityNo) {
        this.nationalityNo = nationalityNo;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    @Override
    public String toString() {
        return "UserPNR{" +
                "id=" + id +
                ", pnr='" + pnr + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birthDate=" + birthDate +
                ", passportNo='" + passportNo + '\'' +
                ", nationalityNo='" + nationalityNo + '\'' +
                ", telNo='" + telNo + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", personType='" + personType + '\'' +
                '}';
    }
}
