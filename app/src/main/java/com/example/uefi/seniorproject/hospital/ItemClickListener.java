package com.example.uefi.seniorproject.hospital;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by UEFI on 25/12/2560.
 */

public interface ItemClickListener {
    void onClick (View view, int position, boolean isLongClick, MotionEvent motionEvent);
}
