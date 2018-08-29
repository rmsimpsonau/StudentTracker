package com.example.studenttracker.models;

import java.util.Date;

import static com.example.studenttracker.helpers.DateHelper.MONTH_DAY_YEAR_FORMATTER;

public class Course {

    public enum Status {
        PLAN_TO_TAKE,
        REGISTERED,
        IN_PROGRESS,
        DROPPED,
        COMPLETED
    }

    private int id;
    private String title;
    private Date startDate;
    private Date endDate;
    private int status;
    private int termId;
    private String mentorName;
    private String mentorPhoneNumber;
    private String mentorEmail;
    private String notes;
    private int startNotificationId;
    private int stopNotificationId;

    public Course(
        String title,
        Date startDate,
        Date endDate,
        int status,
        int termId,
        String mentorName,
        String mentorPhoneNumber,
        String mentorEmail,
        String notes
    )
    {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.termId = termId;
        this.mentorName = mentorName;
        this.mentorPhoneNumber = mentorPhoneNumber;
        this.mentorEmail = mentorEmail;
        this.notes = notes;
    }

    public Course(){}

    // Id getters and setters
    public int getId()
    {
        return id;
    }

    public void setId(int newId)
    {
        id = newId;
    }

    // Title getters and setters
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String newTitle)
    {
        title = newTitle;
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

    // Status getters and setters
    public int getStatus()
    {
        return status;
    }

    public String getStatusStringFromInt()
    {
        String statusString = "";
        switch(status)
        {
            case 0:
                statusString = "Plan To Take";
                break;
            case 1:
                statusString = "Registered";
                break;
            case 2:
                statusString = "In Progress";
                break;
            case 3:
                statusString = "Dropped";
                break;
            case 4:
                statusString = "Completed";
                break;
        }
        return statusString;
    }

    public void setStatus(int newStatus)
    {
        status = newStatus;
    }

    public int getTermId()
    {
        return termId;
    }

    public void setTermId(int newTermId)
    {
        termId = newTermId;
    }

    // Mentor Name getters and setters
    public String getMentorName()
    {
        return mentorName;
    }

    public void setMentorName(String newMentorName)
    {
        mentorName = newMentorName;
    }

    // Mentor Phone Number getters and setters
    public String getMentorPhoneNumber()
    {
        return mentorPhoneNumber;
    }

    public void setMentorPhoneNumber(String newMentorPhoneNumber)
    {
        mentorPhoneNumber = newMentorPhoneNumber;
    }

    // Mentor Email getters and setters
    public String getMentorEmail()
    {
        return mentorEmail;
    }

    public void setMentorEmail(String newMentorEmail)
    {
        mentorEmail = newMentorEmail;
    }

    // Notes
    public String getNotes() {
        return notes;
    }

    public void setNotes(String newNotes)
    {
        notes = newNotes;
    }

    // Start Notification ID
    public int getStartNotificationId()
    {
        return startNotificationId;
    }
    public void setStartNotificationId(int newNotificationId)
    {
        startNotificationId = newNotificationId;
    }

    // Stop Notification ID
    public int getStopNotificationId()
    {
        return stopNotificationId;
    }
    public void setStopNotificationId(int newNotificationId)
    {
        stopNotificationId = newNotificationId;
    }
}
