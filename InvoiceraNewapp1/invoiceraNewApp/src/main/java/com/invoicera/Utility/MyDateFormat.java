package com.invoicera.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDateFormat {

    public static String GetDate(String time) {
/*
        if (date == null)
            return "";
        if (date.isEmpty())
            return "";

        // String date = "2011/11/12 16:05:06";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:MM:SS");
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd");
        String newFormat = formatter.format(testDate);
        // System.out.println(".....Date..."+newFormat);
        return newFormat;


*/

        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String GetTime(String time) {
   /*     if (date == null)
            return "";
        if (date.isEmpty())
            return "";
        SimpleDateFormat sdf= null;
        if (date.length() < 11)
            sdf=new SimpleDateFormat ("yyyy-mm-dd");
        else
        // String date = "2011/11/12 16:05:06";
        sdf = new SimpleDateFormat("yyyy-mm-dd HH:MM:SS");
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat(
                "hh:mm a");
        String newFormat = formatter.format(testDate);
        // System.out.println(".....Date..."+newFormat);
        return newFormat;*/
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        if (time.length() < 12)
            inputPattern = "yyyy-mm-dd";
        else

            inputPattern = "yyyy-mm-dd HH:mm:ss";


        String outputPattern = "hh:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;


    }
}
