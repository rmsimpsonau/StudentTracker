package com.example.studenttracker.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.studenttracker.R;
import com.example.studenttracker.ViewStateMachine;
import com.example.studenttracker.data.StudentDbHelper;
import com.example.studenttracker.fragments.MainFragment;
import com.example.studenttracker.fragments.TermsFragment;
import com.example.studenttracker.helpers.FragmentsHelper;
import com.example.studenttracker.models.Term;

import java.util.Map;

public class AppBaseActivity extends AppCompatActivity
        implements MainFragment.OnFragmentInteractionListener,
        TermsFragment.OnListFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener {

    ProgressBar m_overallProgressBar;
    ProgressBar m_termsProgressBar;
    ProgressBar m_coursesProgressBar;
    ProgressBar m_assessmentsProgressBar;

    FloatingActionButton m_fab;
    int m_cancelIconId;
    int m_addIconId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_base_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        m_fab = findViewById(R.id.add_fab);
        m_cancelIconId = getResources().getIdentifier(String.valueOf(R.drawable.ic_cancel_icon), "drawable", getPackageName());
        m_addIconId = getResources().getIdentifier(String.valueOf(R.drawable.ic_add_icon), "drawable", getPackageName());

        // Action bar listener
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Set progress
        m_overallProgressBar = findViewById(R.id.overallProgressBar);
        m_termsProgressBar = findViewById(R.id.termsProgressBar);
        m_coursesProgressBar = findViewById(R.id.coursesProgressBar);
        m_assessmentsProgressBar = findViewById(R.id.assessmentsProgressBar);

        StudentDbHelper studentDbHelper = new StudentDbHelper(this);
        Map<String,Integer> progressesMap = studentDbHelper.getProgresses();

        int totalTerms = progressesMap.get("totalTerms");
        int completedTerms = progressesMap.get("completedTerms");
        int totalCourses = progressesMap.get("totalCourses");
        int completedCourses = progressesMap.get("completedCourses");
        int totalAssessments = progressesMap.get("totalAssessments");
        int completedAssessments = progressesMap.get("completedAssessments");

        // Overall Progress Bar
        m_overallProgressBar.setMax(
                totalTerms +
                        totalCourses +
                        totalAssessments
        );
        m_overallProgressBar.setProgress(
                completedTerms +
                        completedCourses +
                        completedAssessments
        );

        // Terms Progress Bar
        m_termsProgressBar.setMax(totalTerms);
        m_termsProgressBar.setProgress(completedTerms);

        // Courses Progress Bar
        m_coursesProgressBar.setMax(totalCourses);
        m_coursesProgressBar.setProgress(completedCourses);

        // Assessments Progress Bar
        m_assessmentsProgressBar.setMax(totalAssessments);
        m_assessmentsProgressBar.setProgress(completedAssessments);

        // Navigation view listener
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (ViewStateMachine.getState() == ViewStateMachine.ViewState.HOME)
        {
            // Set Home to be selected nav item by default
            navigationView.getMenu().getItem(0).setChecked(true);
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
        }
        else
        {
            // Set Home to be selected nav item by default
            navigationView.getMenu().getItem(1).setChecked(true);
            onNavigationItemSelected(navigationView.getMenu().getItem(1));
        }

        // Floating action button listener
        m_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppBaseActivity.this, FormActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        // Set floating action button listener
        changeHomeState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Create new fragment manager / transaction to replace the existing
        // fragment with the fragment associated with the menu item selected
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new Fragment();

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        ViewStateMachine.ViewState viewState = ViewStateMachine.ViewState.HOME;

        if (id == R.id.home) {
            this.setTitle("Student Tracker");
            fragment = new MainFragment();
            viewState = ViewStateMachine.ViewState.HOME;
        } else if (id == R.id.nav_terms) {
            this.setTitle("Terms");
            fragment = new TermsFragment();
            viewState = ViewStateMachine.ViewState.TERM;
        }

        // Change view state in view state machine
        ViewStateMachine.setState(viewState);
        changeHomeState();

        // Replace fragment
        FragmentsHelper.replaceFragment(fragmentManager, R.id.fragment_container, fragment);
//        FragmentsHelper.replaceFragment(formActivityFragmentManager, R.id.form_fragment_container, formActivityFragment);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }

    private void changeHomeState()
    {
        ViewStateMachine.ViewState state = ViewStateMachine.getState();
        boolean visible = true;

        switch (state)
        {
            case HOME:
                visible = false;
                break;
            case TERM:
                break;
            case COURSE:
                break;
            case ASSESSMENT:
                break;
        }

        final boolean finalVisible = visible;

        // Hide or show the button
        if (finalVisible)
        {
            m_fab.show();

            m_overallProgressBar.setVisibility(View.INVISIBLE);
            m_termsProgressBar.setVisibility(View.INVISIBLE);
            m_coursesProgressBar.setVisibility(View.INVISIBLE);
            m_assessmentsProgressBar.setVisibility(View.INVISIBLE);
            findViewById(R.id.overallProgressLabel).setVisibility(View.INVISIBLE);
            findViewById(R.id.termsProgressLabel).setVisibility(View.INVISIBLE);
            findViewById(R.id.coursesProgressLabel).setVisibility(View.INVISIBLE);
            findViewById(R.id.assessmentsProgressLabel).setVisibility(View.INVISIBLE);
        }
        else
        {
            m_fab.hide();

            m_overallProgressBar.setVisibility(View.VISIBLE);
            m_termsProgressBar.setVisibility(View.VISIBLE);
            m_coursesProgressBar.setVisibility(View.VISIBLE);
            m_assessmentsProgressBar.setVisibility(View.VISIBLE);
            findViewById(R.id.overallProgressLabel).setVisibility(View.VISIBLE);
            findViewById(R.id.termsProgressLabel).setVisibility(View.VISIBLE);
            findViewById(R.id.coursesProgressLabel).setVisibility(View.VISIBLE);
            findViewById(R.id.assessmentsProgressLabel).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onListFragmentInteraction(Term item) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
