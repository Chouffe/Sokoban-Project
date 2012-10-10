package model;

import java.util.Date;


public class Deadline {
   
    private Date mDate;
	
    Deadline(Date pDate)
    {
        mDate = pDate;
    }

    long TimeUntil()
    {
        return mDate.getTime()-(new Date()).getTime();
    }     
}