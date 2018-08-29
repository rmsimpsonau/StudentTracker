package com.example.studenttracker.activities;

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
import android.widget.TextView;

import com.example.studenttracker.R;
import com.example.studenttracker.ViewStateMachine;
import com.example.studenttracker.data.StudentDbHelper;
import com.example.studenttracker.fragments.CoursesFragment;
import com.example.studenttracker.fragments.TermInfoFragment;
import com.example.studenttracker.helpers.DialogHelper;
import com.example.studenttracker.helpers.FragmentsHelper;
import com.example.studenttracker.helpers.SelectedItemsHelper;
import com.example.studenttracker.models.Course;
import com.example.studenttracker.models.Term;

import java.text.ParseException;
import java.util.ArrayList;

public class CoursesActivity extends AppCompatActivity
        implements CoursesFragment.OnListFragmentInteractionListener,
        TermInfoFragment.OnFragmentInteractionListener {

    FloatingActionButton m_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new CoursesFragment();
        FragmentsHelper.replaceFragment(fragmentManager, R.id.activity_courses_fragment_container, fragment);

        updateUi();

        // Floating action button listener
        m_fab = findViewById(R.id.addCourseFab);
        m_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CoursesActivity.this, FormActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_delete_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                ViewStateMachine.setState(ViewStateMachine.ViewState.TERM);
                NavUtils.navigateUpFromSameTask(this);
//                return true;
                break;
            case R.id.edit_action:
                ViewStateMachine.setState(ViewStateMachine.ViewState.EDIT_TERM);
                Intent intent = new Intent(CoursesActivity.this, FormActivity.class);
                startActivity(intent);
                break;
            case R.id.delete_action:
                StudentDbHelper studentDbHelper = new StudentDbHelper(getApplicationContext());
                ArrayList<Course> courseArrayList = null;
                try {
                    courseArrayList = studentDbHelper.getTermCourses();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                assert courseArrayList != null;
                if (courseArrayList.isEmpty())
                {
                    // Delete course
                    final Term selectedTerm = SelectedItemsHelper.getSelectedTerm();
                    studentDbHelper.deleteTermHandler(selectedTerm);
                    ViewStateMachine.setState(ViewStateMachine.ViewState.ASSESSMENT);
                    intent = new Intent(CoursesActivity.this, AppBaseActivity.class);
                    startActivity(intent);
                }
                else
                {
                    DialogHelper.showDialog(
                            "Cannot delete term with courses. Delete courses first.",
                            DialogHelper.SEVERITY.ERROR,
                            CoursesActivity.this
                    ).show();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume()
    {
        updateUi();
        super.onResume();
    }

    @Override
    public void onBackPressed()
    {
        ViewStateMachine.setState(ViewStateMachine.ViewState.TERM);
        finish();
    }

    @Override
    public void onListFragmentInteraction(Course item)
    {

    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }

    private void updateUi()
    {
        ViewStateMachine.setState(ViewStateMachine.ViewState.COURSE);

        Term selectedTerm = SelectedItemsHelper.getSelectedTerm();

        this.setTitle(selectedTerm.getName() + " | Courses");

        // Term Info
        String termName = selectedTerm.getName();
        String termInfoStartDate = selectedTerm.getStartDateFormatted();
        String termInfoEndDate = selectedTerm.getEndDateFormatted();

        TextView termNameField = findViewById(R.id.termInfoTermName);
        TextView termInfoDate = findViewById(R.id.termInfoDate);

        termNameField.setText(termName);
        termInfoDate.setText(termInfoStartDate + " - " + termInfoEndDate);
    }
}
