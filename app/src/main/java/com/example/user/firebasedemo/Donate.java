package com.example.user.firebasedemo;

/**
 * Created by User on 9/22/2017.
 */

public class Donate {
    public String donatorname,donatorcontact,donatoradd,count,dtype,dmethod,donatoramount;

    public Donate()
    {

    }

    public Donate(String donatorname, String donatorcontact, String donatoradd, String count, String dtype, String dmethod, String donatoramount) {
        this.donatorname = donatorname;
        this.donatorcontact = donatorcontact;
        this.donatoradd = donatoradd;
        this.count = count;
        this.dtype = dtype;
        this.dmethod = dmethod;
        this.donatoramount = donatoramount;
    }

    public String getDonatorname() {
        return donatorname;
    }

    public void setDonatorname(String donatorname) {
        this.donatorname = donatorname;
    }

    public String getDonatorcontact() {
        return donatorcontact;
    }

    public void setDonatorcontact(String donatorcontact) {
        this.donatorcontact = donatorcontact;
    }

    public String getDonatoradd() {
        return donatoradd;
    }

    public void setDonatoradd(String donatoradd) {
        this.donatoradd = donatoradd;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    public String getDmethod() {
        return dmethod;
    }

    public void setDmethod(String dmethod) {
        this.dmethod = dmethod;
    }

    public String getDonatoramount() {
        return donatoramount;
    }

    public void setDonatoramount(String donatoramount) {
        this.donatoramount = donatoramount;
    }
}
