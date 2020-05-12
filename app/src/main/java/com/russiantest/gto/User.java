package com.russiantest.gto;

import java.util.Date;

public class User
{
    String name;
    String email;
    String password;
    Date birthday;
    String address;
    String occupation;
    String employer;

    public User(String name, String email, String password, Date birthday, String address, String occupation, String employer) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.address = address;
        this.occupation = occupation;
        this.employer = employer;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getAddress() {
        return address;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getEmployer() {
        return employer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }
}
