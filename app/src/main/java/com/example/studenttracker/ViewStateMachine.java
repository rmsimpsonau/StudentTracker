package com.example.studenttracker;

public class ViewStateMachine {

    public enum ViewState {
        HOME,
        TERM,
        COURSE,
        ASSESSMENT,
        EDIT_TERM,
        EDIT_COURSE,
        EDIT_ASSESSMENT
    }

    public static ViewState m_state = ViewState.HOME;

    public static void setState(ViewState newState)
    {
        m_state = newState;
    }

    public static ViewState getState()
    {
        return m_state;
    }
}
