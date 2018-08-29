package com.example.studenttracker.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.studenttracker.R;
import com.example.studenttracker.activities.CoursesActivity;
import com.example.studenttracker.fragments.TermsFragment.OnListFragmentInteractionListener;
import com.example.studenttracker.fragments.dummy.DummyContent.DummyItem;
import com.example.studenttracker.helpers.SelectedItemsHelper;
import com.example.studenttracker.models.Term;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TermsViewAdapter extends RecyclerView.Adapter<TermsViewAdapter.ViewHolder> {

    private final ArrayList<Term> m_values;
    private final OnListFragmentInteractionListener m_listener;
    private Context m_context;

    public TermsViewAdapter(ArrayList<Term> items, OnListFragmentInteractionListener listener, Context context) {
        m_values = items;
        m_listener = listener;
        m_context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_terms, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.m_item = m_values.get(position);
        holder.m_termName.setText(m_values.get(position).getName());
        holder.m_startDate.setText(m_values.get(position).getStartDateFormatted());
        holder.m_endDate.setText(new StringBuilder().append(" - ").append(m_values.get(position).getEndDateFormatted()).toString());

        holder.m_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != m_listener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    m_listener.onListFragmentInteraction(holder.m_item);
                    SelectedItemsHelper.setSelectedTerm(holder.m_item);
                    m_context.startActivity(new Intent(m_context, CoursesActivity.class));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return m_values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View m_View;
        public final TextView m_termName;
        public final TextView m_startDate;
        public final TextView m_endDate;
        public Term m_item;

        public ViewHolder(View view) {
            super(view);
            m_View = view;
            m_termName = view.findViewById(R.id.termview_name);
            m_startDate = view.findViewById(R.id.termview_startdate);
            m_endDate = view.findViewById(R.id.termview_enddate);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + m_termName.getText() + "'";
        }
    }
}
