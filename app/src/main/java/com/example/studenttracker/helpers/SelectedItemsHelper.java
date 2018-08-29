package com.example.studenttracker.helpers;

import android.content.Context;

import com.example.studenttracker.data.StudentDbHelper;
import com.example.studenttracker.models.Assessment;
import com.example.studenttracker.models.Course;
import com.example.studenttracker.models.Term;

import java.text.ParseException;

import static com.example.studenttracker.helpers.NOTIFICATION_ACTION.ADD;
import static com.example.studenttracker.helpers.NOTIFICATION_ACTION.CANCEL;

enum NOTIFICATION_TYPE {
    START,
    STOP
}

public class SelectedItemsHelper {

    public enum ITEM_TYPE {
        TERM,
        COURSE,
        ASSESSMENT
    }

    private static Term selectedTerm = new Term();
    private static Course selectedCourse = new Course();
    private static Assessment selectedAssessment = new Assessment();

    // Selected Term getter / setter
    public static Term getSelectedTerm()
    {
        return selectedTerm;
    }

    public static void setSelectedTerm(Term newTerm)
    {
        selectedTerm = newTerm;
    }

    public static void updateSelectedTerm(Context context) throws ParseException{
        StudentDbHelper studentDbHelper = new StudentDbHelper(context);
        Term updatedTerm = studentDbHelper.getTerm(selectedTerm.getId());
        setSelectedTerm(updatedTerm);
    }

    public static int getStatusIntFromString(String statusString)
    {
        int statusInt = -1;
        switch(statusString)
        {
            case "Plan To Take":
                statusInt = 0;
                break;
            case "Registered":
                statusInt = 1;
                break;
            case "In Progress":
                statusInt = 2;
                break;
            case "Dropped":
                statusInt = 3;
                break;
            case "Completed":
                statusInt = 4;
                break;
        }
        return statusInt;
    }

    // Selected Course getter / setter
    public static Course getSelectedCourse()
    {
        return selectedCourse;
    }

    public static void setSelectedCourse(Course newCourse)
    {
        selectedCourse = newCourse;
    }

    public static void updateSelectedCourse(Context context) throws ParseException{
        StudentDbHelper studentDbHelper = new StudentDbHelper(context);
        Course updatedCourse = studentDbHelper.getCourse(selectedCourse.getId());
        setSelectedCourse(updatedCourse);
    }

    // Selected Assessment getter / setter
    public static Assessment getSelectedAssessment()
    {
        return selectedAssessment;
    }

    public static void setSelectedAssessment(Assessment newAssessment)
    {
        selectedAssessment = newAssessment;
    }

    public static void updateSelectedAssessment(Context context) throws ParseException{
        StudentDbHelper studentDbHelper = new StudentDbHelper(context);
        Assessment updatedAssessment = studentDbHelper.getAssessment(selectedAssessment.getId());
        setSelectedAssessment(updatedAssessment);
    }

    static void addNotification(ITEM_TYPE itemType, NOTIFICATION_TYPE notificationType, Context context)
    {
        modifyNotification(itemType, notificationType, ADD, context);
    }

    static void removeNotification(ITEM_TYPE itemType, NOTIFICATION_TYPE notificationType, Context context)
    {
        modifyNotification(itemType, notificationType, CANCEL, context);
    }

    private static void modifyNotification(ITEM_TYPE itemType, NOTIFICATION_TYPE notificationType, NOTIFICATION_ACTION action, Context context) {
        StudentDbHelper studentDbHelper = new StudentDbHelper(context);
        int notificationId;
        switch(itemType)
        {
            case COURSE:
                Course selectedCourse = getSelectedCourse();
                boolean notificationAlreadySet;
                switch(notificationType)
                {
                    case START:
                        notificationId = (action == NOTIFICATION_ACTION.ADD) ? NotificationHelper.generateNotificationId() : selectedCourse.getStartNotificationId();

                        notificationAlreadySet = getSelectedCourse().getStartNotificationId() > 0;

                        if (action == NOTIFICATION_ACTION.ADD && !notificationAlreadySet)
                        {
                            NotificationHelper.toggleNotification(context, getSelectedCourse().getStartDate(), action, notificationId, selectedCourse.getTitle(), "Course is starting today");
                            selectedCourse.setStartNotificationId(notificationId);
                        }
                        else if (action == NOTIFICATION_ACTION.CANCEL && notificationAlreadySet)
                        {
                            NotificationHelper.toggleNotification(context, getSelectedCourse().getStartDate(), action, notificationId, selectedCourse.getTitle(), "Course is starting today");
                            selectedCourse.setStartNotificationId(-1);
                        }
                        break;
                    case STOP:
                        notificationId = (action == NOTIFICATION_ACTION.ADD) ? NotificationHelper.generateNotificationId() : selectedCourse.getStopNotificationId();

                        notificationAlreadySet = getSelectedCourse().getStopNotificationId() > 0;

                        if (action == NOTIFICATION_ACTION.ADD && !notificationAlreadySet)
                        {
                            NotificationHelper.toggleNotification(context, getSelectedCourse().getEndDate(), action, notificationId, selectedCourse.getTitle(), "Course is ending today");
                            selectedCourse.setStopNotificationId(notificationId);
                        }
                        else if (action == NOTIFICATION_ACTION.CANCEL && notificationAlreadySet)
                        {
                            NotificationHelper.toggleNotification(context, getSelectedCourse().getEndDate(), action, notificationId, selectedCourse.getTitle(), "Course is ending today");
                            selectedCourse.setStopNotificationId(-1);
                        }
                        break;
                }
                studentDbHelper.updateCourseHandler(selectedCourse);
                break;
            case ASSESSMENT:
                Assessment selectedAssessment = getSelectedAssessment();
                boolean assessmentNotificationAlreadySet;
                notificationId = (action == NOTIFICATION_ACTION.ADD) ? NotificationHelper.generateNotificationId() : selectedAssessment.getDueDateNotificationId();

                assessmentNotificationAlreadySet = selectedAssessment.getDueDateNotificationId() > 0;

                if (action == NOTIFICATION_ACTION.ADD && !assessmentNotificationAlreadySet)
                {
                    NotificationHelper.toggleNotification(context, getSelectedAssessment().getDueDate(), action, notificationId, selectedAssessment.getTitle(), "Assessment is due today");
                    selectedAssessment.setDueDateNotificationId(notificationId);
                }
                else if (action == NOTIFICATION_ACTION.CANCEL && assessmentNotificationAlreadySet)
                {
                    NotificationHelper.toggleNotification(context, getSelectedAssessment().getDueDate(), action, notificationId, selectedAssessment.getTitle(), "Assessment is due today");
                    selectedAssessment.setDueDateNotificationId(-1);
                }
                studentDbHelper.updateAssessmentHandler(selectedAssessment);
                break;
        }
    }

}
