package com.dsole.controldellamadas;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

/**
 * Created by dsole on 29/01/2015.
 */
public class NumberPickerLimiteMinutosPreference extends DialogPreference {
    private static final int MAX = 500;
    private static final int MIN= 1;
    private static final String KEY = "PREF_LIMITE_MINUTOS";
    private static final String DEFAULT= "100";

    private NumberPicker mNumberPicker;

    public NumberPickerLimiteMinutosPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPositiveButtonText("OK");
        setNegativeButtonText("CANCELAR");


/*
        int i = context.getResources().getIdentifier("DialogPreference", "id", "android");
        int [] id = new int[1];
        id[0] = i;
        TypedArray dialogType = context.obtainStyledAttributes(attrs,
                id, 0, 0);
        TypedArray numberPickerType = context.obtainStyledAttributes(attrs,
                R.styleable.NumberPickerPreference, 0, 0);

        mMaxExternalKey = numberPickerType.getString(R.styleable.NumberPickerPreference_maxExternal);
        mMinExternalKey = numberPickerType.getString(R.styleable.NumberPickerPreference_minExternal);

        mMax = numberPickerType.getInt(R.styleable.NumberPickerPreference_max, 5);
        mMin = numberPickerType.getInt(R.styleable.NumberPickerPreference_min, 0);

        int i2 = Resources.getSystem().getIdentifier("Preference_defaultValue", "id", "android");

        mDefault = dialogType.getInt(i2, mMin);
        dialogType.recycle();
        numberPickerType.recycle();
        */
    }

    @Override
    protected View onCreateDialogView() {

        LayoutInflater inflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.number_picker_dialog, null);

        mNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker);

        // Initialize state
        mNumberPicker.setMaxValue(MAX);
        mNumberPicker.setMinValue(MIN);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());

        String valor = sp.getString(KEY, DEFAULT);

        mNumberPicker.setValue(Integer.valueOf(valor));
        mNumberPicker.setWrapSelectorWheel(false);
/*
        // No keyboard popup
        int id = Resources.getSystem().getIdentifier("numberpicker_input", "id", "android");
        EditText textInput = (EditText) mNumberPicker.findViewById(id);
        textInput.setCursorVisible(false);
        textInput.setFocusable(false);
        textInput.setFocusableInTouchMode(false);
*/
        return view;
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);

        // mNumberPicker.setMaxValue(60);
        //mNumberPicker.setValue(Minute);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        //super.onDialogClosed(positiveResult);
        if (positiveResult) {
            //persistInt(mNumberPicker.getValue());
            persistString(String.valueOf(mNumberPicker.getValue()));
        }
    }
}