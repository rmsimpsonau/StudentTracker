package com.example.studenttracker.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.studenttracker.R;
import com.example.studenttracker.data.StudentDbHelper;
import com.example.studenttracker.helpers.DateHelper;
import com.example.studenttracker.helpers.SelectedItemsHelper;
import com.example.studenttracker.models.Assessment;
import com.example.studenttracker.models.Course;
import com.example.studenttracker.models.Term;

import java.text.ParseException;
import java.util.Calendar;

import static com.example.studenttracker.ViewStateMachine.ViewState;
import static com.example.studenttracker.ViewStateMachine.getState;

public class FormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch(getState())
        {
            case TERM:
                setContentView(R.layout.term_form_content);
                setTitle("Add New Term");
                break;
            case COURSE:
                setContentView(R.layout.course_form_content);
                setTitle("Add New Course");
                break;
            case ASSESSMENT:
                setContentView(R.layout.assessment_form_content);
                setTitle("Add New Assessment");
                break;
            case EDIT_TERM:
                setContentView(R.layout.term_form_content);
                setTitle("Edit Term");
                populateEditTermForm();
                break;
            case EDIT_COURSE:
                setContentView(R.layout.course_form_content);
                setTitle("Edit Course");
                populateEditCourseForm();
                break;
            case EDIT_ASSESSMENT:
                setContentView(R.layout.assessment_form_content);
                setTitle("Edit Assessment");
                populateEditAssessmentForm();
                break;
        }
    }

    public void showDatePickerDialog(final View v) {
        final Button datePickerButton = findViewById(v.getId());
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(FormActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month++; // add 1 to month index to display correct month value
                        datePickerButton.setText(month + "/" + day + "/" + year);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        returnToPreviousActivity();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                returnToPreviousActivity();
                finish();
//                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void returnToPreviousActivity() {
//        NavUtils.navigateUpFromSameTask(this);
        ViewState currentState = getState();
        Intent intent;
        switch(currentState)
        {
            case EDIT_ASSESSMENT:
                System.out.println("ASSESSMENT DETAIL");
                intent = new Intent(this, AssessmentDetailActivity.class);
                break;
            case ASSESSMENT:
                System.out.println("ASSESSMENT NEW");
                intent = new Intent(this, AssessmentsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            case EDIT_COURSE:
                System.out.println("ASSESSMENT");
                intent = new Intent(this, AssessmentsActivity.class);
                break;
            case COURSE:
                System.out.println("COURSE NEW");
                intent = new Intent(this, CoursesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            case EDIT_TERM:
                System.out.println("COURSE");
                intent = new Intent(this, CoursesActivity.class);
                break;
            case TERM:
                System.out.println("TERM");
                intent = new Intent(this, AppBaseActivity.class);
                break;
            default:
                System.out.println("DEFAULT");
                intent = new Intent(this, AppBaseActivity.class);
        }
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void populateEditTermForm()
    {
        String termName = SelectedItemsHelper.getSelectedTerm().getName();
        String termStartDate = SelectedItemsHelper.getSelectedTerm().getStartDateFormatted();
        String termEndDate = SelectedItemsHelper.getSelectedTerm().getEndDateFormatted();

        EditText nameTextField = findViewById(R.id.termFormName);
        Button startDateButton = findViewById(R.id.termFormStartDate);
        Button endDateButton = findViewById(R.id.termFormEndDate);

        nameTextField.setText(termName);
        startDateButton.setText(termStartDate);
        endDateButton.setText(termEndDate);
    }

    public void populateEditCourseForm()
    {
        Course selectedCourse = SelectedItemsHelper.getSelectedCourse();
        String title = selectedCourse.getTitle();
        String startDate = selectedCourse.getStartDateFormatted();
        String endDate = selectedCourse.getEndDateFormatted();
        int status = selectedCourse.getStatus();
        String mentorName = selectedCourse.getMentorName();
        String mentorPhone = selectedCourse.getMentorPhoneNumber();
        String mentorEmail = selectedCourse.getMentorEmail();
        String notes = selectedCourse.getNotes();

        EditText titleTextField = findViewById(R.id.courseFormName);
        Button startDateButton = findViewById(R.id.courseFormStartDate);
        Button endDateButton = findViewById(R.id.courseFormEndDate);
        Spinner statusSpinner = findViewById(R.id.courseStatusSpinner);
        EditText mentorNameField = findViewById(R.id.courseMentorName);
        EditText mentorPhoneField = findViewById(R.id.courseMentorPhone);
        EditText mentorEmailField = findViewById(R.id.courseMentorEmail);
        EditText notesField = findViewById(R.id.courseFormNotes);

        titleTextField.setText(title);
        startDateButton.setText(startDate);
        endDateButton.setText(endDate);
        statusSpinner.setSelection(status);
        mentorNameField.setText(mentorName);
        mentorPhoneField.setText(mentorPhone);
        mentorEmailField.setText(mentorEmail);
        notesField.setText(notes);
    }

    public void populateEditAssessmentForm()
    {
        Assessment selectedAssessment = SelectedItemsHelper.getSelectedAssessment();
        String title = selectedAssessment.getTitle();
        int type = selectedAssessment.getAssessmentType().ordinal();
        String dueDate = selectedAssessment.getDueDateFormatted();

        EditText titleField = findViewById(R.id.formAssessmentTitle);
        Spinner typeField = findViewById(R.id.assessmentTypeSpinner);
        TextView dueDateField = findViewById(R.id.assessmentFormDueDate);

        titleField.setText(title);
        typeField.setSelection(type);
        dueDateField.setText(dueDate);
    }

    public void saveForm(View v) throws ParseException {
        StudentDbHelper studentDbHelper = new StudentDbHelper(this);
        switch(getState())
        {
            case TERM:
                Term newTerm = generateTerm();
                studentDbHelper.addTermHandler(newTerm);
                break;
            case COURSE:
                Course newCourse = generateCourse();
                studentDbHelper.addCourseHandler(newCourse);
                break;
            case ASSESSMENT:
                Assessment newAssessment = generateAssessment();
                studentDbHelper.addAssessmentHandler(newAssessment);
                break;
            case EDIT_TERM:
                Term updatedTerm = generateTerm();
                studentDbHelper.updateTermHandler(updatedTerm);
                SelectedItemsHelper.updateSelectedTerm(getApplicationContext());
                break;
            case EDIT_COURSE:
                Course updatedCourse = generateCourse();
                studentDbHelper.updateCourseHandler(updatedCourse);
                SelectedItemsHelper.updateSelectedCourse(getApplicationContext());
                break;
            case EDIT_ASSESSMENT:
                Assessment updatedAssessment = generateAssessment();
                studentDbHelper.updateAssessmentHandler(updatedAssessment);
                SelectedItemsHelper.updateSelectedAssessment(getApplicationContext());
                break;
        }
        returnToPreviousActivity();
//        finish();
    }

    private Term generateTerm() {
        Term term = null;

        // Get objects
        EditText nameTextField = findViewById(R.id.termFormName);
        Button startDateButton = findViewById(R.id.termFormStartDate);
        Button endDateButton = findViewById(R.id.termFormEndDate);

        try {
            term = new Term(
                    nameTextField.getText().toString(),
                    DateHelper.MONTH_DAY_YEAR_FORMATTER.parse(startDateButton.getText().toString()),
                    DateHelper.MONTH_DAY_YEAR_FORMATTER.parse(endDateButton.getText().toString())
            );
        } catch (ParseException e) {
            System.out.println("ParseException - " + e.getMessage());
        }

        return term;
    }

    private Course generateCourse() {
        Course course = null;

        // Get objects
        EditText titleTextField = findViewById(R.id.courseFormName);
        Button startDateButton = findViewById(R.id.courseFormStartDate);
        Button endDateButton = findViewById(R.id.courseFormEndDate);
        Spinner statusSpinner = findViewById(R.id.courseStatusSpinner);
        EditText mentorName = findViewById(R.id.courseMentorName);
        EditText mentorPhone = findViewById(R.id.courseMentorPhone);
        EditText mentorEmail = findViewById(R.id.courseMentorEmail);
        EditText courseNotes = findViewById(R.id.courseFormNotes);

        try {
            course = new Course(
                    titleTextField.getText().toString(),
                    DateHelper.MONTH_DAY_YEAR_FORMATTER.parse(startDateButton.getText().toString()),
                    DateHelper.MONTH_DAY_YEAR_FORMATTER.parse(endDateButton.getText().toString()),
                    SelectedItemsHelper.getStatusIntFromString(statusSpinner.getSelectedItem().toString()),
                    SelectedItemsHelper.getSelectedTerm().getId(),
                    mentorName.getText().toString(),
                    mentorPhone.getText().toString(),
                    mentorEmail.getText().toString(),
                    courseNotes.getText().toString()
            );
        } catch (ParseException e) {
            System.out.println("ParseException - " + e.getMessage());
        }

        return course;
    }

    private Assessment generateAssessment() {
        Assessment assessment = null;

        // Get objects
        EditText title = findViewById(R.id.formAssessmentTitle);
        Spinner typeSpinner = findViewById(R.id.assessmentTypeSpinner);
        Button dueDate = findViewById(R.id.assessmentFormDueDate);

        try {
            assessment = new Assessment();
            assessment.setTitle(title.getText().toString());
            assessment.setAssessmentTypeByString(typeSpinner.getSelectedItem().toString());
            assessment.setDueDate(DateHelper.MONTH_DAY_YEAR_FORMATTER.parse(dueDate.getText().toString()));
        } catch (ParseException e) {
            System.out.println("ParseException - " + e.getMessage());
        }

        return assessment;
    }

    private void clearTermsForm()
    {
        // Get objects
        EditText nameTextField = findViewById(R.id.termFormName);
        Button startDateButton = findViewById(R.id.termFormStartDate);
        Button endDateButton = findViewById(R.id.termFormEndDate);

        // Set values
        nameTextField.setText("");
        startDateButton.setText("");
        endDateButton.setText("");
    }
}
