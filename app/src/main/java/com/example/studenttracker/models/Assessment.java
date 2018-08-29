package com.example.studenttracker.models;

import java.util.Date;

import static com.example.studenttracker.helpers.DateHelper.MONTH_DAY_YEAR_FORMATTER;

public class Assessment {

    public enum ASSESSMENT_TYPE {
        PERFORMANCE {
            public String toString() {
                return "Performance";
            }
        },
        OBJECTIVE {
            public String toString() {
                return "Objective";
            }
        }
    }

    private int id;
    private String title;
    private ASSESSMENT_TYPE assessmentType;
    private Date dueDate;
    private int courseId;
    private int dueDateNotificationId;

    public Assessment(
            String title,
            ASSESSMENT_TYPE assessmentType,
            Date dueDate,
            int courseId
    )
    {
        this.title = title;
        this.assessmentType = assessmentType;
        this.dueDate = dueDate;
        this.courseId = courseId;
    }

    public Assessment(){}

    // Id getters / setters
    public int getId()
    {
        return id;
    }

    public void setId(int newId)
    {
        id = newId;
    }

    // Title getters / setters
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String newTitle)
    {
        title = newTitle;
    }
    // Assessment Type getters / setters
    public ASSESSMENT_TYPE getAssessmentType()
    {
        return assessmentType;
    }

    public void setAssessmentType(ASSESSMENT_TYPE newAssessmentType)
    {
        assessmentType = newAssessmentType;
    }

    public void setAssessmentTypeByString(String newAssessmentTypeString)
    {
        switch(newAssessmentTypeString)
        {
            case "Performance":
                assessmentType = ASSESSMENT_TYPE.PERFORMANCE;
                break;
            case "Objective":
                assessmentType = ASSESSMENT_TYPE.OBJECTIVE;
                break;
        }
    }

    // Due Date getters / setters
    public Date getDueDate()
    {
        return dueDate;
    }

    public String getDueDateFormatted()
    {
        return MONTH_DAY_YEAR_FORMATTER.format(dueDate);
    }

    public void setDueDate(Date newDueDate)
    {
        dueDate = newDueDate;
    }

    // Course ID
    public int getCourseId()
    {
        return courseId;
    }

    public void setCourseId(int newCourseId)
    {
        courseId =  newCourseId;
    }

    // Start Notification ID
    public int getDueDateNotificationId()
    {
        System.out.println("Getting duedate notification id: " + dueDateNotificationId);
        return dueDateNotificationId;
    }
    public void setDueDateNotificationId(int newNotificationId)
    {
        System.out.println("Setting duedate notification id: " + newNotificationId);
        dueDateNotificationId = newNotificationId;
    }

}
