package com.permissionnanny.demo.extra;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import com.permissionnanny.demo.R;

/**
 *
 */
public class BooleanExtra implements Extra<Boolean> {

    boolean mValue;

    public BooleanExtra() {
        this(false);
    }

    public BooleanExtra(boolean value) {
        mValue = value;
    }

    @Override
    public View getView(Context context, ViewGroup parent, String label) {
        View view = LayoutInflater.from(context).inflate(R.layout.extras_boolean, parent, false);
        ButterKnife.bind(this, view);
        ((TextView) view.findViewById(R.id.tvLabel)).setText(label);
        ((SwitchCompat) view.findViewById(R.id.sValue)).setChecked(mValue);
        return view;
    }

    @Override
    public Boolean getValue() {
        return mValue;
    }

    @OnCheckedChanged(R.id.sValue)
    void onCheckChanged(CompoundButton view, boolean isChecked) {
        mValue = isChecked;
    }
}
