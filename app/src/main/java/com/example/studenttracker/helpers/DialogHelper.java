package com.example.studenttracker.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ImageButton;

import com.example.studenttracker.models.Assessment;
import com.example.studenttracker.models.Course;

import static com.example.studenttracker.helpers.SelectedItemsHelper.ITEM_TYPE;
import static com.example.studenttracker.helpers.SelectedItemsHelper.addNotification;
import static com.example.studenttracker.helpers.SelectedItemsHelper.removeNotification;

public class DialogHelper {

    public enum SEVERITY
    {
        INFO {
            public String toString() {
                return "Info";
            }
        },
        WARNING {
            public String toString() {
                return "Warning";
            }
        },
        ERROR {
            public String toString() {
                return "Error";
            }
        },
    }

    public static AlertDialog showDialog(String message, SEVERITY severity, Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message)
                .setTitle(severity.toString());

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });

        return builder.create();
    }

    public static void showChecklistDialog(String title, final Activity activity, final ITEM_TYPE itemType, final int alertIconId)
    {
        AlertDialog dialog;

        CharSequence[] items = new CharSequence[0];

        // Notifications already enabled
        boolean[] checkedValues = new boolean[0];
        switch (itemType)
        {
            case COURSE:
                items = new CharSequence[]{" Start (9 AM)", " End (9 AM)"};
                checkedValues = new boolean[2];
                Course selectedCourse = SelectedItemsHelper.getSelectedCourse();
                checkedValues[0] = selectedCourse.getStartNotificationId() > 0;
                checkedValues[1] = selectedCourse.getStopNotificationId() > 0;
                break;
            case ASSESSMENT:
                items = new CharSequence[]{" Start (9 AM)"};
                checkedValues = new boolean[1];
                Assessment selectedAssessment = SelectedItemsHelper.getSelectedAssessment();
                checkedValues[0] = selectedAssessment.getDueDateNotificationId() > 0;
                break;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        final boolean[] finalCheckedValues = checkedValues;
        builder.setMultiChoiceItems(items, checkedValues,
                new DialogInterface.OnMultiChoiceClickListener() {
                    // indexSelected contains the index of item (of which checkbox checked)
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected,
                                        boolean isChecked) {
                    }
                })
                // Action buttons
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicks OK
                        // Start notifications
                        if (finalCheckedValues[0]) {
                            addNotification(itemType, NOTIFICATION_TYPE.START, activity.getApplicationContext());
                        }
                        else {
                            removeNotification(itemType, NOTIFICATION_TYPE.START, activity.getApplicationContext());
                        }

                        if (itemType == ITEM_TYPE.COURSE) {
                            // Stop notifications
                            if (finalCheckedValues[1]) {
                                addNotification(itemType, NOTIFICATION_TYPE.STOP, activity.getApplicationContext());
                            } else {
                                removeNotification(itemType, NOTIFICATION_TYPE.STOP, activity.getApplicationContext());
                            }
                        }

                        // Change the Notification bell icon to show notifications as enabled or disabled
                        ImageButton btn = activity.findViewById(alertIconId);

                        boolean enabled;
                        if (itemType == ITEM_TYPE.COURSE) {
                            enabled = finalCheckedValues[0] || finalCheckedValues[1];
                        }
                        else
                        {
                            enabled = finalCheckedValues[0];
                        }
                        if (enabled)
                        {
                            NotificationHelper.setAlertIconState(btn, NotificationHelper.ALERT_STATE.ENABLED);
                        }
                        else
                        {
                            NotificationHelper.setAlertIconState(btn, NotificationHelper.ALERT_STATE.DISABLED);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicks Cancel
                    }
                });

        dialog = builder.create();//AlertDialog dialog; create like this outside onClick
        dialog.show();
    }
}
