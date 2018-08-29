package com.example.studenttracker.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.studenttracker.R;
import com.example.studenttracker.activities.AssessmentsActivity;
import com.example.studenttracker.fragments.CoursesFragment.OnListFragmentInteractionListener;
import com.example.studenttracker.helpers.SelectedItemsHelper;
import com.example.studenttracker.models.Course;

import java.util.ArrayList;

public class CoursesViewAdapter extends RecyclerView.Adapter<CoursesViewAdapter.ViewHolder> {

    private final ArrayList<Course> m_values;
    private final OnListFragmentInteractionListener mListener;
    private Context m_context;

    CoursesViewAdapter(ArrayList<Course> items, OnListFragmentInteractionListener listener, Context context) {
        m_values = items;
        mListener = listener;
        m_context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.m_item = m_values.get(position);
        holder.m_title.setText(m_values.get(position).getTitle());
        String startDate = m_values.get(position).getStartDateFormatted();
        String endDate = m_values.get(position).getEndDateFormatted();
        holder.m_courseDate.setText(startDate + " - " + endDate);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.m_item);
                    SelectedItemsHelper.setSelectedCourse(holder.m_item);
                    Intent intent = new Intent(m_context, AssessmentsActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    m_context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return m_values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView m_title;
        final TextView m_courseDate;
        private Course m_item;

        ViewHolder(View view) {
            super(view);
            mView = view;
            m_title = view.findViewById(R.id.course_name);
            m_courseDate = view.findViewById(R.id.course_date);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + m_title.getText() + "'";
        }
    }
}
