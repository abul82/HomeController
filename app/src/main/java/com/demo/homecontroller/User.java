package com.demo.homecontroller;

public class User {
    String fname;
    String lname;
    String contact;
    String userid;
public User(){}

    public User(String fname, String lname, String contact, String userid) {
        this.fname = fname;
        this.lname = lname;
        this.contact = contact;
        this.userid = userid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
