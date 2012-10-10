package model;

import java.util.Date;


public class Deadline {
   
    private Date mDate;
	
    public Deadline(Date pDate)
    {
        mDate = pDate;
    }

    public long TimeUntil()
    {
        return mDate.getTime()-(new Date()).getTime();
    }     
}