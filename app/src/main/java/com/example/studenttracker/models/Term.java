package com.example.studenttracker.models;

import java.util.Date;

import static com.example.studenttracker.helpers.DateHelper.MONTH_DAY_YEAR_FORMATTER;

public class Term {

    private int id;
    private String name;
    private Date startDate;
    private Date endDate;

    public Term(
        String name,
        Date startDate,
        Date endDate
    )
    {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Term(){}

    // Id getters and setters
    public int getId()
    {
        return id;
    }

    public void setId(int newId)
    {
        id = newId;
    }

    // Name getters and setters
    public String getName()
    {
        return name;
    }

    public void setName(String newName)
    {
        name = newName;
    }

    // Start Date getters and setters
    public Date getStartDate()
    {
        return startDate;
    }

    public String getStartDateFormatted()
    {
        return MONTH_DAY_YEAR_FORMATTER.format(startDate);
    }

    public void setStartDate(Date newStartDate)
    {
        startDate = newStartDate;
    }

    // End Date getters and setters
    public Date getEndDate()
    {
        return endDate;
    }

    public String getEndDateFormatted()
    {
        return MONTH_DAY_YEAR_FORMATTER.format(endDate);
    }

    public void setEndDate(Date newEndDate)
    {
        endDate = newEndDate;
    }

}
