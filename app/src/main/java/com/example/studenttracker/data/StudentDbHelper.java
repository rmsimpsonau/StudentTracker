package com.example.studenttracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.studenttracker.helpers.SelectedItemsHelper;
import com.example.studenttracker.helpers.Toaster;
import com.example.studenttracker.models.Assessment;
import com.example.studenttracker.models.Course;
import com.example.studenttracker.models.Term;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.studenttracker.helpers.DateHelper.TIMESTAMP_FORMATTER;

public class StudentDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "student.db";
    public Context context;

    private static final String SQL_CREATE_TERMS_ENTRIES =
            "CREATE TABLE " + StudentContract.TermsEntry.TABLE_NAME + " (" +
                    StudentContract.TermsEntry._ID + " INTEGER PRIMARY KEY," +
                    StudentContract.TermsEntry.COLUMN_NAME_TITLE + " TEXT," +
                    StudentContract.TermsEntry.COLUMN_NAME_START_DATE + " DATETIME," +
                    StudentContract.TermsEntry.COLUMN_NAME_END_DATE + " DATETIME);";

    private static final String SQL_CREATE_COURSES_ENTRIES =
            "CREATE TABLE " + StudentContract.CoursesEntry.TABLE_NAME + " (" +
                    StudentContract.CoursesEntry._ID + " INTEGER PRIMARY KEY," +
                    StudentContract.CoursesEntry.COLUMN_NAME_TITLE + " TEXT," +
                    StudentContract.CoursesEntry.COLUMN_NAME_START_DATE + " DATETIME," +
                    StudentContract.CoursesEntry.COLUMN_NAME_ANTICIPATED_END_DATE + " DATETIME," +
                    StudentContract.CoursesEntry.COLUMN_NAME_STATUS + " TEXT," +
                    StudentContract.CoursesEntry.COLUMN_NAME_TERM_ID + " INTEGER," +
                    StudentContract.CoursesEntry.COLUMN_NAME_MENTOR_NAME + " TEXT," +
                    StudentContract.CoursesEntry.COLUMN_NAME_MENTOR_PHONE + " TEXT," +
                    StudentContract.CoursesEntry.COLUMN_NAME_MENTOR_EMAIL + " TEXT," +
                    StudentContract.CoursesEntry.COLUMN_NAME_NOTES + " TEXT," +
                    StudentContract.CoursesEntry.COLUMN_NAME_START_NOTIFICATION_ID + " BIGINTEGER," +
                    StudentContract.CoursesEntry.COLUMN_NAME_STOP_NOTIFICATION_ID + " BIGINTEGER);";

    private static final String SQL_CREATE_ASSESSMENTS_ENTRIES =
            "CREATE TABLE " + StudentContract.AssessmentsEntry.TABLE_NAME + " (" +
                    StudentContract.AssessmentsEntry._ID + " INTEGER PRIMARY KEY," +
                    StudentContract.AssessmentsEntry.COLUMN_NAME_TITLE + " TEXT," +
                    StudentContract.AssessmentsEntry.COLUMN_NAME_TYPE + " TEXT," +
                    StudentContract.AssessmentsEntry.COLUMN_NAME_DUE_DATE + " DATETIME," +
                    StudentContract.AssessmentsEntry.COLUMN_NAME_COURSE_ID + " INTEGER," +
                    StudentContract.AssessmentsEntry.COLUMN_NAME_DUE_DATE_NOTIFICATION_ID + " BIGINTEGER);";

    private static final String SQL_DELETE_TERMS_ENTRIES =
            "DROP TABLE IF EXISTS " + StudentContract.TermsEntry.TABLE_NAME;
    private static final String SQL_DELETE_COURSES_ENTRIES =
            "DROP TABLE IF EXISTS " + StudentContract.CoursesEntry.TABLE_NAME;
    private static final String SQL_DELETE_ASSESSMENTS_ENTRIES =
            "DROP TABLE IF EXISTS " + StudentContract.AssessmentsEntry.TABLE_NAME;

    public StudentDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        // Create different tables
        db.execSQL(SQL_CREATE_TERMS_ENTRIES);
        db.execSQL(SQL_CREATE_COURSES_ENTRIES);
        db.execSQL(SQL_CREATE_ASSESSMENTS_ENTRIES);
    }

    private void clearDb(SQLiteDatabase db) {
        // Delete different tables
        db.execSQL(SQL_DELETE_ASSESSMENTS_ENTRIES);
//        db.execSQL(SQL_DELETE_COURSES_ENTRIES);
//        db.execSQL(SQL_DELETE_TERMS_ENTRIES);
        // Create different tables
//        db.execSQL(SQL_CREATE_TERMS_ENTRIES);
//        db.execSQL(SQL_CREATE_COURSES_ENTRIES);
        db.execSQL(SQL_CREATE_ASSESSMENTS_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ASSESSMENTS_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }

    private SQLiteDatabase getDatabase()
    {
        StudentDbHelper studentDbHelper = new StudentDbHelper(context);
        return studentDbHelper.getWritableDatabase();
    }

    public ArrayList<Term> getTerms() throws ParseException {
        ArrayList<Term> terms = new ArrayList<>();
        String query = "SELECT * FROM terms";
        SQLiteDatabase db = getDatabase();

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Term term = new Term();
            term.setId(cursor.getInt(0));
            term.setName(cursor.getString(1));
            term.setStartDate(TIMESTAMP_FORMATTER.parse(cursor.getString(2)));
            term.setEndDate(TIMESTAMP_FORMATTER.parse(cursor.getString(3)));

            terms.add(term);
        }
        cursor.close();
        db.close();
        return terms;
    }

    public Term getTerm(int termId) throws ParseException {
        Term requestedTerm = new Term();
        String query = "SELECT * FROM terms WHERE _id=" + termId;
        SQLiteDatabase db = getDatabase();

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            if (termId == id)
            {
                requestedTerm.setId(cursor.getInt(0));
                requestedTerm.setName(cursor.getString(1));
                requestedTerm.setStartDate(TIMESTAMP_FORMATTER.parse(cursor.getString(2)));
                requestedTerm.setEndDate(TIMESTAMP_FORMATTER.parse(cursor.getString(3)));
                break;
            }
        }
        cursor.close();
        db.close();
        return requestedTerm;
    }

    public ArrayList<Course> getCourses() throws ParseException {
        String result = "";
        ArrayList<Course> terms = new ArrayList<>();
        String query = "SELECT * FROM courses";
        SQLiteDatabase db = getDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Course course = new Course();
            course.setId(cursor.getInt(0));
            course.setTitle(cursor.getString(1));
            course.setStartDate(TIMESTAMP_FORMATTER.parse(cursor.getString(2)));
            course.setEndDate(TIMESTAMP_FORMATTER.parse(cursor.getString(3)));
            course.setStatus(cursor.getInt(4));
            course.setTermId(cursor.getInt(5));
            course.setMentorName(cursor.getString(6));
            course.setMentorPhoneNumber(cursor.getString(7));
            course.setMentorEmail(cursor.getString(8));
            course.setNotes(cursor.getString(9));
            course.setStartNotificationId(cursor.getInt(10));
            course.setStopNotificationId(cursor.getInt(11));

            terms.add(course);
        }
        cursor.close();
        db.close();
        return terms;
    }

    public Course getCourse(int courseId) throws ParseException {
        Course requestedCourse = new Course();
        String query = "SELECT * FROM courses WHERE _id=" + courseId;
        SQLiteDatabase db = getDatabase();

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            if (courseId == id) {
                requestedCourse.setId(cursor.getInt(0));
                requestedCourse.setTitle(cursor.getString(1));
                requestedCourse.setStartDate(TIMESTAMP_FORMATTER.parse(cursor.getString(2)));
                requestedCourse.setEndDate(TIMESTAMP_FORMATTER.parse(cursor.getString(3)));
                requestedCourse.setStatus(cursor.getInt(4));
                requestedCourse.setTermId(cursor.getInt(5));
                requestedCourse.setMentorName(cursor.getString(6));
                requestedCourse.setMentorPhoneNumber(cursor.getString(7));
                requestedCourse.setMentorEmail(cursor.getString(8));
                requestedCourse.setNotes(cursor.getString(9));
                requestedCourse.setStartNotificationId(cursor.getInt(10));
                requestedCourse.setStopNotificationId(cursor.getInt(11));
            }
        }
        cursor.close();
        db.close();
        return requestedCourse;
    }

    public ArrayList<Course> getTermCourses() throws ParseException {
        ArrayList<Course> terms = new ArrayList<>();
        String query = "SELECT * FROM courses WHERE term_id=" + SelectedItemsHelper.getSelectedTerm().getId();
        SQLiteDatabase db = getDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Course course = new Course();
            course.setId(cursor.getInt(0));
            course.setTitle(cursor.getString(1));
            course.setStartDate(TIMESTAMP_FORMATTER.parse(cursor.getString(2)));
            course.setEndDate(TIMESTAMP_FORMATTER.parse(cursor.getString(3)));
            course.setStatus(cursor.getInt(4));
            course.setTermId(cursor.getInt(5));
            course.setMentorName(cursor.getString(6));
            course.setMentorPhoneNumber(cursor.getString(7));
            course.setMentorEmail(cursor.getString(8));
            course.setNotes(cursor.getString(9));
            course.setStartNotificationId(cursor.getInt(10));
            course.setStopNotificationId(cursor.getInt(11));

            terms.add(course);
        }
        cursor.close();
        db.close();
        return terms;
    }

    public ArrayList<Assessment> getCourseAssessments() throws ParseException {
        ArrayList<Assessment> assessments = new ArrayList<>();
        String query = "SELECT * FROM assessments WHERE course_id=" + SelectedItemsHelper.getSelectedCourse().getId();
        SQLiteDatabase db = getDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Assessment assessment = new Assessment();
            assessment.setId(cursor.getInt(0));
            assessment.setTitle(cursor.getString(1));
            assessment.setAssessmentTypeByString(cursor.getString(2));
            assessment.setDueDate(TIMESTAMP_FORMATTER.parse(cursor.getString(3)));
            assessment.setCourseId(cursor.getInt(4));
            assessment.setDueDateNotificationId(cursor.getInt(5));

            assessments.add(assessment);
        }
        cursor.close();
        db.close();
        return assessments;
    }

    public Assessment getAssessment(int assessmentId) throws ParseException {
        Assessment requestedAssessment = new Assessment();
        String query = "SELECT * FROM assessments WHERE _id=" + assessmentId;
        SQLiteDatabase db = getDatabase();

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            if (assessmentId == id) {
                requestedAssessment.setId(cursor.getInt(0));
                requestedAssessment.setTitle(cursor.getString(1));
                requestedAssessment.setAssessmentTypeByString(cursor.getString(2));
                requestedAssessment.setDueDate(TIMESTAMP_FORMATTER.parse(cursor.getString(3)));
                requestedAssessment.setCourseId(cursor.getInt(4));
                requestedAssessment.setDueDateNotificationId(cursor.getInt(5));
            }
        }
        cursor.close();
        db.close();
        return requestedAssessment;
    }

    public void addTermHandler(Term newTerm)
    {
        // Gets the data repository in write mode
        SQLiteDatabase db = getDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(StudentContract.TermsEntry.COLUMN_NAME_TITLE, newTerm.getName());
        values.put(StudentContract.TermsEntry.COLUMN_NAME_START_DATE, newTerm.getStartDate().toString());
        values.put(StudentContract.TermsEntry.COLUMN_NAME_END_DATE, newTerm.getEndDate().toString());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(StudentContract.TermsEntry.TABLE_NAME, null, values);

        if (newRowId == -1) Toaster.makeToast("Error Adding Term", context);
        else Toaster.makeToast("Term Added", context);
        db.close();
    }

    public void addCourseHandler(Course newCourse)
    {
        // Gets the data repository in write mode
        SQLiteDatabase db = getDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(StudentContract.CoursesEntry.COLUMN_NAME_TITLE, newCourse.getTitle());
        values.put(StudentContract.CoursesEntry.COLUMN_NAME_START_DATE, newCourse.getStartDate().toString());
        values.put(StudentContract.CoursesEntry.COLUMN_NAME_ANTICIPATED_END_DATE, newCourse.getEndDate().toString());
        values.put(StudentContract.CoursesEntry.COLUMN_NAME_STATUS, newCourse.getStatus());
        values.put(StudentContract.CoursesEntry.COLUMN_NAME_TERM_ID, SelectedItemsHelper.getSelectedTerm().getId());
        values.put(StudentContract.CoursesEntry.COLUMN_NAME_MENTOR_NAME, newCourse.getMentorName());
        values.put(StudentContract.CoursesEntry.COLUMN_NAME_MENTOR_PHONE, newCourse.getMentorPhoneNumber());
        values.put(StudentContract.CoursesEntry.COLUMN_NAME_MENTOR_EMAIL, newCourse.getMentorEmail());
        values.put(StudentContract.CoursesEntry.COLUMN_NAME_NOTES, newCourse.getNotes());
        values.put(StudentContract.CoursesEntry.COLUMN_NAME_START_NOTIFICATION_ID, newCourse.getStartNotificationId());
        values.put(StudentContract.CoursesEntry.COLUMN_NAME_STOP_NOTIFICATION_ID, newCourse.getStopNotificationId());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(StudentContract.CoursesEntry.TABLE_NAME, null, values);

        if (newRowId == -1) Toaster.makeToast("Error Adding Course", context);
        else Toaster.makeToast("Course Added", context);
        db.close();
    }

    public void addAssessmentHandler(Assessment newAssessment)
    {
        // Gets the data repository in write mode
        SQLiteDatabase db = getDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(StudentContract.AssessmentsEntry.COLUMN_NAME_TITLE, newAssessment.getTitle());
        values.put(StudentContract.AssessmentsEntry.COLUMN_NAME_TYPE, newAssessment.getAssessmentType().toString());
        values.put(StudentContract.AssessmentsEntry.COLUMN_NAME_DUE_DATE, newAssessment.getDueDate().toString());
        values.put(StudentContract.AssessmentsEntry.COLUMN_NAME_COURSE_ID, SelectedItemsHelper.getSelectedCourse().getId());
        values.put(StudentContract.AssessmentsEntry.COLUMN_NAME_DUE_DATE_NOTIFICATION_ID, newAssessment.getDueDateNotificationId());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(StudentContract.AssessmentsEntry.TABLE_NAME, null, values);

        if (newRowId == -1) Toaster.makeToast("Error Adding Assessment", context);
        else Toaster.makeToast("Assessment Added", context);
        db.close();
    }

    public void updateTermHandler(Term updatedTerm)
    {
        int termId = SelectedItemsHelper.getSelectedTerm().getId();
        // Gets the data repository in write mode
        SQLiteDatabase db = getDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(StudentContract.TermsEntry.COLUMN_NAME_TITLE, updatedTerm.getName());
        values.put(StudentContract.TermsEntry.COLUMN_NAME_START_DATE, updatedTerm.getStartDate().toString());
        values.put(StudentContract.TermsEntry.COLUMN_NAME_END_DATE, updatedTerm.getEndDate().toString());

        // Update term, returning the primary key value of the updated row
        int rowsAffected = db.update(StudentContract.TermsEntry.TABLE_NAME, values, "_id=" + termId, null);

        if (rowsAffected == 1) Toaster.makeToast("Term Updated", context);
        else if (rowsAffected > 1) Toaster.makeToast("Multiple Terms Updated", context);
        else Toaster.makeToast("No Terms Updated", context);
        db.close();
    }

    public void deleteTermHandler(Term updatedTerm)
    {
        int termId = updatedTerm.getId();
        SQLiteDatabase db = getDatabase();

        int rowsAffected = db.delete(StudentContract.TermsEntry.TABLE_NAME, "_id=" + termId, null);

        // Toast
        if (rowsAffected == 1) Toaster.makeToast("Term Deleted", context);
        else if (rowsAffected > 1) Toaster.makeToast("Multiple Terms Deleted", context);
        else Toaster.makeToast("No Terms Deleted", context);
        db.close();
    }

    public void updateCourseHandler(Course updatedCourse)
    {
        int courseId = SelectedItemsHelper.getSelectedCourse().getId();
        SQLiteDatabase db = getDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(StudentContract.CoursesEntry.COLUMN_NAME_TITLE, updatedCourse.getTitle());
        values.put(StudentContract.CoursesEntry.COLUMN_NAME_START_DATE, updatedCourse.getStartDate().toString());
        values.put(StudentContract.CoursesEntry.COLUMN_NAME_ANTICIPATED_END_DATE, updatedCourse.getEndDate().toString());
        values.put(StudentContract.CoursesEntry.COLUMN_NAME_STATUS, updatedCourse.getStatus());
        values.put(StudentContract.CoursesEntry.COLUMN_NAME_TERM_ID, SelectedItemsHelper.getSelectedTerm().getId());
        values.put(StudentContract.CoursesEntry.COLUMN_NAME_MENTOR_NAME, updatedCourse.getMentorName());
        values.put(StudentContract.CoursesEntry.COLUMN_NAME_MENTOR_PHONE, updatedCourse.getMentorPhoneNumber());
        values.put(StudentContract.CoursesEntry.COLUMN_NAME_MENTOR_EMAIL, updatedCourse.getMentorEmail());
        values.put(StudentContract.CoursesEntry.COLUMN_NAME_NOTES, updatedCourse.getNotes());
        values.put(StudentContract.CoursesEntry.COLUMN_NAME_START_NOTIFICATION_ID, updatedCourse.getStartNotificationId());
        values.put(StudentContract.CoursesEntry.COLUMN_NAME_STOP_NOTIFICATION_ID, updatedCourse.getStopNotificationId());

        // Insert the new row, returning the primary key value of the new row
        int rowsAffected = db.update(StudentContract.CoursesEntry.TABLE_NAME, values, "_id=" + courseId, null);

        if (rowsAffected == 1) Toaster.makeToast("Course Updated", context);
        else if (rowsAffected > 1) Toaster.makeToast("Multiple Courses Updated", context);
        else Toaster.makeToast("No Courses Updated", context);
        db.close();
    }

    public void deleteCourseHandler(Course updatedCourse)
    {
        int courseId = updatedCourse.getId();
        SQLiteDatabase db = getDatabase();

        int rowsAffected = db.delete(StudentContract.CoursesEntry.TABLE_NAME, "_id=" + courseId, null);

        // Toast
        if (rowsAffected == 1) Toaster.makeToast("Course Deleted", context);
        else if (rowsAffected > 1) Toaster.makeToast("Multiple Courses Deleted", context);
        else Toaster.makeToast("No Courses Deleted", context);
        db.close();
    }

    public void updateAssessmentHandler(Assessment updatedAssessment)
    {
        int assessmentId = SelectedItemsHelper.getSelectedAssessment().getId();
        SQLiteDatabase db = getDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(StudentContract.AssessmentsEntry.COLUMN_NAME_TITLE, updatedAssessment.getTitle());
        values.put(StudentContract.AssessmentsEntry.COLUMN_NAME_TYPE, updatedAssessment.getAssessmentType().toString());
        values.put(StudentContract.AssessmentsEntry.COLUMN_NAME_DUE_DATE, updatedAssessment.getDueDate().toString());
        values.put(StudentContract.AssessmentsEntry.COLUMN_NAME_COURSE_ID, SelectedItemsHelper.getSelectedCourse().getId());
        values.put(StudentContract.AssessmentsEntry.COLUMN_NAME_DUE_DATE_NOTIFICATION_ID, updatedAssessment.getDueDateNotificationId());
        System.out.println("StudentDbHelper 427 - " + updatedAssessment.getDueDateNotificationId());

        // Insert the new row, returning the primary key value of the new row
        int rowsAffected = db.update(StudentContract.AssessmentsEntry.TABLE_NAME, values, "_id=" + assessmentId, null);

        // Toast
        if (rowsAffected == 1) Toaster.makeToast("Assessment Updated", context);
        else if (rowsAffected > 1) Toaster.makeToast("Multiple Assessments Updated", context);
        else Toaster.makeToast("No Assessments Updated", context);
        db.close();
    }

    public void deleteAssessmentHandler(Assessment updatedAssessment)
    {
        int assessmentId = updatedAssessment.getId();
        SQLiteDatabase db = getDatabase();

        int rowsAffected = db.delete(StudentContract.AssessmentsEntry.TABLE_NAME, "_id=" + assessmentId, null);

        // Toast
        if (rowsAffected == 1) Toaster.makeToast("Assessment Deleted", context);
        else if (rowsAffected > 1) Toaster.makeToast("Multiple Assessments Deleted", context);
        else Toaster.makeToast("No Assessments Deleted", context);
        db.close();
    }

    public Map<String,Integer> getProgresses()
    {
        SQLiteDatabase db = getDatabase();

        Map<String,Integer> progressesMap = new HashMap<>();

        int totalTerms = 0;
        int totalCourses = 0;
        int totalAssessments = 0;

        int completedTerms = 0;
        int completedCourses = 0;
        int completedAssessments = 0;

        // TERMS
        String termsQuery = "SELECT end_date FROM terms";

        try (Cursor cursor = db.rawQuery(termsQuery, null)) {

            while (cursor.moveToNext()) {
                Date endDate = TIMESTAMP_FORMATTER.parse(cursor.getString(0));
                Date currentDate = TIMESTAMP_FORMATTER.parse(new Date().toString());
                if (endDate.before(currentDate)) completedTerms++;
                totalTerms++;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // COURSES
        String coursesQuery = "SELECT anticipated_end_date FROM courses";

        try (Cursor cursor = db.rawQuery(coursesQuery, null)) {

            while (cursor.moveToNext()) {
                Date endDate = TIMESTAMP_FORMATTER.parse(cursor.getString(0));
                Date currentDate = TIMESTAMP_FORMATTER.parse(new Date().toString());
                if (endDate.before(currentDate)) completedCourses++;
                totalCourses++;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // COURSES
        String assessmentsQuery = "SELECT due_date FROM assessments";

        try (Cursor cursor = db.rawQuery(assessmentsQuery, null)) {

            while (cursor.moveToNext()) {
                Date dueDate = TIMESTAMP_FORMATTER.parse(cursor.getString(0));
                Date currentDate = TIMESTAMP_FORMATTER.parse(new Date().toString());
                if (dueDate.before(currentDate)) completedAssessments++;
                totalAssessments++;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Populate Map
        progressesMap.put("totalTerms", totalTerms);
        progressesMap.put("totalCourses", totalCourses);
        progressesMap.put("totalAssessments", totalAssessments);
        progressesMap.put("completedTerms", completedTerms);
        progressesMap.put("completedCourses", completedCourses);
        progressesMap.put("completedAssessments", completedAssessments);

        db.close();
        return progressesMap;
    }
}