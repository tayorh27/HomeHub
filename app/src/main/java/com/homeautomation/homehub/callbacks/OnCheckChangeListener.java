package com.homeautomation.homehub.callbacks;

import android.widget.CompoundButton;

/**
 * Created by Control & Inst. LAB on 29-Jul-16.
 */
public interface OnCheckChangeListener {
    void checkChange(CompoundButton compoundButton, boolean b, int position);
}
