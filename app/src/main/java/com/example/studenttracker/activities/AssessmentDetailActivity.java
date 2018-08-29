package com.example.studenttracker.activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.studenttracker.helpers.DialogHelper;
import com.example.studenttracker.helpers.NotificationHelper;
import com.example.studenttracker.helpers.SelectedItemsHelper;
import com.example.studenttracker.models.Assessment;
import com.example.studenttracker.models.Course;
import com.example.studenttracker.models.Term;

public class AssessmentDetailActivity extends AppCompatActivity {

    ImageButton m_notificationToggleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);

        updateUi();

        m_notificationToggleBtn = findViewById(R.id.assessmentAlertIcon);
        m_notificationToggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogHelper.showChecklistDialog("Enable Assessment Notifications", AssessmentDetailActivity.this, SelectedItemsHelper.ITEM_TYPE.ASSESSMENT, R.id.assessmentAlertIcon);
                updateUi();
            }
        });
    }

    @Override
    protected void onResume()
    {
        updateUi();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_delete_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                ViewStateMachine.setState(ViewStateMachine.ViewState.ASSESSMENT);
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.edit_action:
                // Edit assessment
                ViewStateMachine.setState(ViewStateMachine.ViewState.EDIT_ASSESSMENT);
                intent = new Intent(AssessmentDetailActivity.this, FormActivity.class);
                startActivity(intent);
                break;
            case R.id.delete_action:
                // Delete assessment
                StudentDbHelper studentDbHelper = new StudentDbHelper(AssessmentDetailActivity.this);
                final Assessment selectedAssessment = SelectedItemsHelper.getSelectedAssessment();
                studentDbHelper.deleteAssessmentHandler(selectedAssessment);
                ViewStateMachine.setState(ViewStateMachine.ViewState.ASSESSMENT);
                intent = new Intent(AssessmentDetailActivity.this, AssessmentsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        ViewStateMachine.setState(ViewStateMachine.ViewState.ASSESSMENT);
        finish();
    }

    private void updateUi()
    {
        // Get Assessment Info
        final Assessment selectedAssessment = SelectedItemsHelper.getSelectedAssessment();
        final Course selectedCourse = SelectedItemsHelper.getSelectedCourse();
        final Term selectedTerm = SelectedItemsHelper.getSelectedTerm();

        String title = selectedAssessment.getTitle();
        String type = selectedAssessment.getAssessmentType().toString();
        String dueDate = selectedAssessment.getDueDateFormatted();
        String notes = selectedCourse.getNotes();

        TextView titleField = findViewById(R.id.assessmentInfoTitle);
        TextView typeField = findViewById(R.id.assessmentInfoType);
        TextView dueDateField = findViewById(R.id.assessmentInfoDueDate);
        TextView notesField = findViewById(R.id.assessmentInfoCourseNotes);

        titleField.setText(title);
        typeField.setText(type);
        dueDateField.setText(dueDate);
        notesField.setText(notes);

        // Change the Notification bell icon to show notifications as enabled or disabled
        ImageButton btn = findViewById(R.id.assessmentAlertIcon);

        if (selectedAssessment.getDueDateNotificationId() > 0)
        {
            NotificationHelper.setAlertIconState(btn, NotificationHelper.ALERT_STATE.ENABLED);
        }
        else
        {
            NotificationHelper.setAlertIconState(btn, NotificationHelper.ALERT_STATE.DISABLED);
        }

        this.setTitle(selectedCourse.getTitle() + " | " + selectedAssessment.getTitle());
    }
}
