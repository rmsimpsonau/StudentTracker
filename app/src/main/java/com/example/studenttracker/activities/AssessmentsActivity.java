package com.example.studenttracker.activities;

import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.studenttracker.R;
import com.example.studenttracker.ViewStateMachine;
import com.example.studenttracker.data.StudentDbHelper;
import com.example.studenttracker.fragments.AssessmentsFragment;
import com.example.studenttracker.fragments.CourseInfoFragment;
import com.example.studenttracker.helpers.DialogHelper;
import com.example.studenttracker.helpers.FragmentsHelper;
import com.example.studenttracker.helpers.NotificationHelper;
import com.example.studenttracker.helpers.SelectedItemsHelper;
import com.example.studenttracker.models.Assessment;
import com.example.studenttracker.models.Course;
import com.example.studenttracker.models.Term;

import java.text.ParseException;
import java.util.ArrayList;

public class AssessmentsActivity extends AppCompatActivity
        implements AssessmentsFragment.OnListFragmentInteractionListener,
        CourseInfoFragment.OnFragmentInteractionListener {

    FloatingActionButton m_fab;
    ImageButton m_notificationToggleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new AssessmentsFragment();
        FragmentsHelper.replaceFragment(fragmentManager, R.id.activity_assessments_fragment_container, fragment);

        updateUi();

        // Floating action button listener
        m_fab = findViewById(R.id.addAssessmentsFab);
        m_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AssessmentsActivity.this, FormActivity.class));
            }
        });

        m_notificationToggleBtn = findViewById(R.id.courseAlertIcon);
        m_notificationToggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHelper.showChecklistDialog("Enable Course Notifications", AssessmentsActivity.this, SelectedItemsHelper.ITEM_TYPE.COURSE, R.id.courseAlertIcon);
                updateUi();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_delete_share_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                ViewStateMachine.setState(ViewStateMachine.ViewState.COURSE);
                NavUtils.navigateUpFromSameTask(this);
                break;
            case R.id.edit_action:
                ViewStateMachine.setState(ViewStateMachine.ViewState.EDIT_COURSE);
                Intent intent = new Intent(AssessmentsActivity.this, FormActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.delete_action:
                StudentDbHelper studentDbHelper = new StudentDbHelper(getApplicationContext());
                ArrayList<Assessment> assessmentArrayList = null;
                try {
                    assessmentArrayList = studentDbHelper.getCourseAssessments();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                assert assessmentArrayList != null;
                if (assessmentArrayList.isEmpty())
                {
                    // Delete course
                    final Course selectedCourse = SelectedItemsHelper.getSelectedCourse();
                    studentDbHelper.deleteCourseHandler(selectedCourse);
                    ViewStateMachine.setState(ViewStateMachine.ViewState.ASSESSMENT);
                    intent = new Intent(AssessmentsActivity.this, CoursesActivity.class);
                    startActivity(intent);
                }
                else
                {
                    DialogHelper.showDialog(
                            "Cannot delete course with assessments. Delete assessments first.",
                            DialogHelper.SEVERITY.ERROR,
                            AssessmentsActivity.this
                    ).show();
                }

                break;
            case R.id.share_notes_action:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String courseName = SelectedItemsHelper.getSelectedCourse().getTitle();
                String courseNotes = SelectedItemsHelper.getSelectedCourse().getNotes();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, courseName + " notes");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, courseNotes);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        ViewStateMachine.setState(ViewStateMachine.ViewState.COURSE);
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public void onListFragmentInteraction(Assessment item) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onResume()
    {
        System.out.println("Resumed!!");
        updateUi();
        super.onResume();
    }

    public void updateUi()
    {
        ViewStateMachine.setState(ViewStateMachine.ViewState.ASSESSMENT);

        Term selectedTerm = SelectedItemsHelper.getSelectedTerm();
        Course selectedCourse = SelectedItemsHelper.getSelectedCourse();

        this.setTitle(selectedTerm.getName() + " | " + selectedCourse.getTitle() + " | Assessments");

        // Get Course Info from Selected Course Object
        String courseInfoName = selectedCourse.getTitle();
        String courseInfoStartDate = selectedCourse.getStartDateFormatted();
        String courseInfoEndDate = selectedCourse.getEndDateFormatted();
        String status = selectedCourse.getStatusStringFromInt();
        String mentorName = selectedCourse.getMentorName();
        String mentorPhoneNumber = selectedCourse.getMentorPhoneNumber();
        String mentorEmail = selectedCourse.getMentorEmail();
        String courseNotes = selectedCourse.getNotes();

        // Get TextView objects
        TextView courseInfoCourseName = findViewById(R.id.assessmentInfoType);
        TextView courseInfoDate = findViewById(R.id.courseInfoDate);
        TextView statusTextField = findViewById(R.id.assessmentInfoDueDate);
        TextView mentorNameField = findViewById(R.id.courseInfoMentorName);
        TextView mentorPhoneField = findViewById(R.id.courseInfoMentorPhoneNumber);
        TextView mentorEmailField = findViewById(R.id.courseInfoMentorEmail);
        TextView courseNotesField = findViewById(R.id.assessmentInfoCourseNotes);

        // Set TextView fields
        courseInfoCourseName.setText(courseInfoName);
        courseInfoDate.setText(courseInfoStartDate + " - " + courseInfoEndDate);
        statusTextField.setText(status);
        mentorNameField.setText(mentorName);
        mentorPhoneField.setText(mentorPhoneNumber);
        mentorEmailField.setText(mentorEmail);
        courseNotesField.setText(courseNotes);

        // Change the Notification bell icon to show notifications as enabled or disabled
        ImageButton btn = findViewById(R.id.courseAlertIcon);

        if (selectedCourse.getStartNotificationId() > 0 || selectedCourse.getStopNotificationId() > 0)
        {
            NotificationHelper.setAlertIconState(btn, NotificationHelper.ALERT_STATE.ENABLED);
        }
        else
        {
            NotificationHelper.setAlertIconState(btn, NotificationHelper.ALERT_STATE.DISABLED);
        }
    }

    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_cancel_icon);
        return builder.build();
    }
}
