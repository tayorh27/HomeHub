package com.homeautomation.homehub.callbacks;

import android.widget.CheckBox;
import android.widget.CompoundButton;

/**
 * Created by Control & Inst. LAB on 02-Aug-16.
 */
public interface SettingsCheckListener {
    void onSettingsCheckListener(CompoundButton compoundButton,boolean checked, int position);
    void onSettingsRelativeCheckListener(CheckBox checkBox, boolean checked, int position);
}
