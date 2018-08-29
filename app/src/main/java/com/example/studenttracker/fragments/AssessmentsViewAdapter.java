package com.example.studenttracker.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.studenttracker.R;
import com.example.studenttracker.activities.AssessmentDetailActivity;
import com.example.studenttracker.fragments.AssessmentsFragment.OnListFragmentInteractionListener;
import com.example.studenttracker.fragments.dummy.DummyContent.DummyItem;
import com.example.studenttracker.helpers.SelectedItemsHelper;
import com.example.studenttracker.models.Assessment;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AssessmentsViewAdapter extends RecyclerView.Adapter<AssessmentsViewAdapter.ViewHolder> {

    private final ArrayList<Assessment> m_values;
    private final OnListFragmentInteractionListener m_listener;
    private Context m_context;

    public AssessmentsViewAdapter(ArrayList<Assessment> items, OnListFragmentInteractionListener listener, Context context) {
        m_values = items;
        m_listener = listener;
        m_context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_assessments, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.m_item = m_values.get(position);
        holder.m_title.setText(m_values.get(position).getTitle());
        holder.m_dueDate.setText(m_values.get(position).getDueDateFormatted());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != m_listener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    m_listener.onListFragmentInteraction(holder.m_item);
                    SelectedItemsHelper.setSelectedAssessment(holder.m_item);
                    Intent intent = new Intent(m_context, AssessmentDetailActivity.class);
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
        public final View mView;
        public final TextView m_title;
        public final TextView m_dueDate;
        public Assessment m_item;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            m_title = (TextView) view.findViewById(R.id.assessment_title);
            m_dueDate = (TextView) view.findViewById(R.id.assessment_due_date);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + m_title.getText() + "'";
        }
    }
}
