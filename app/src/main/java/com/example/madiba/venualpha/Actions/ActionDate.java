package com.example.madiba.venualpha.Actions;

import java.util.Date;

/**
 * Created by Madiba on 10/31/2016.
 */

public class ActionDate {

    public Date date;


    public ActionDate(int year, int month, int day) {
        Date localDate = new Date(year, month, day);
        this.date =localDate;
    }
}
