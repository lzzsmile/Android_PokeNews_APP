package com.example.android.pokenews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

/**
 * Created by zhuangzhili on 2017-12-15.
 */

public class DateSelection extends DialogPreference {

    private static final String LOG_TAG = DateSelection.class.getSimpleName();

    private String lastDate;
    private String lastMonth;
    private String lastYear;
    private String dateval;
    private CharSequence mSummary;
    private DatePicker picker = null;

    public DateSelection(Context context, AttributeSet attrs) {
        super(context, attrs);

        setPositiveButtonText("Set");
        setNegativeButtonText("Cancel");
    }

    @Override
    protected View onCreateDialogView() {
        picker = new DatePicker(getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            picker.setCalendarViewShown(false);
        }
        return picker;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        picker.updateDate(Integer.parseInt(lastYear), Integer.parseInt(lastMonth) + 1, Integer.parseInt(lastDate));
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            lastYear = String.valueOf(picker.getYear());
            lastMonth = String.valueOf(picker.getMonth() + 1);
            lastDate = String.valueOf(picker.getDayOfMonth());
            dateval = lastYear + "-" + lastMonth + "-" + lastDate;
            if (callChangeListener(dateval)) {
                persistString(dateval);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        dateval = null;
        if (restorePersistedValue) {
            if (defaultValue == null) {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String formatted = format.format(cal.getTime());
                dateval = getPersistedString(formatted);
            } else {
                dateval = getPersistedString(defaultValue.toString());
            }
        } else {
            dateval = defaultValue.toString();
        }
        lastYear = getYear(dateval);
        lastMonth = getMonth(dateval);
        lastDate = getDate(dateval);
    }

    private String getYear(String dateValue) {
        return dateValue.split("-")[0];
    }
    private String getMonth(String dateValue) {
        return dateValue.split("-")[1];
    }
    private String getDate(String dateValue) {
        return dateValue.split("-")[2];
    }

    public void setText(String text) {
        final boolean wasBlocking = shouldDisableDependents();
        dateval = text;
        persistString(text);
        final boolean isBlocking = shouldDisableDependents();
        if (isBlocking != wasBlocking) {
            notifyDependencyChange(isBlocking);
        }
    }

}
