package com.example.studenttracker.data;

import android.provider.BaseColumns;

public final class StudentContract {
    // Making constructor private to prevent someone from instantiating
    // the contract class.
    private StudentContract() {}

    public static class TermsEntry implements BaseColumns {
        public static final String TABLE_NAME = "terms";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_START_DATE = "start_date";
        public static final String COLUMN_NAME_END_DATE = "end_date";
    }

    public static class CoursesEntry implements BaseColumns {
        public static final String TABLE_NAME = "courses";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_START_DATE = "start_date";
        public static final String COLUMN_NAME_ANTICIPATED_END_DATE = "anticipated_end_date";
        public static final String COLUMN_NAME_STATUS = "status";
        public static final String COLUMN_NAME_TERM_ID = "term_id"; // Foreign key to terms table
        public static final String COLUMN_NAME_MENTOR_NAME = "mentor_name";
        public static final String COLUMN_NAME_MENTOR_PHONE = "mentor_phone";
        public static final String COLUMN_NAME_MENTOR_EMAIL = "mentor_email";
        public static final String COLUMN_NAME_NOTES = "notes";
        public static final String COLUMN_NAME_START_NOTIFICATION_ID = "start_notification_id";
        public static final String COLUMN_NAME_STOP_NOTIFICATION_ID = "stop_notification_id";
    }

    public static class AssessmentsEntry implements BaseColumns {
        public static final String TABLE_NAME = "assessments";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_DUE_DATE = "due_date";
        public static final String COLUMN_NAME_COURSE_ID = "course_id"; // Foreign key to courses table
        public static final String COLUMN_NAME_DUE_DATE_NOTIFICATION_ID = "due_date_notification_id";
    }
}
