package com.homeautomation.homehub.callbacks;

import android.widget.Switch;

/**
 * Created by Control & Inst. LAB on 29-Jul-16.
 */
public interface OnCheckChangeListener {
    void checkChange(Switch _switch, boolean b, int position);
}
