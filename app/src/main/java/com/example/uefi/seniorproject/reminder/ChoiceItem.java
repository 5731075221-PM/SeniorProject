package com.example.uefi.seniorproject.reminder;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by palida on 23-Mar-18.
 */

public class ChoiceItem implements Parcelable {
    public String text;
    public boolean check;

    public ChoiceItem(String text,Boolean check){
        this.text = text;
        this.check = check;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(text);
        out.writeInt(check ? 1:0);
    }

    public static final Parcelable.Creator<ChoiceItem> CREATOR = new Parcelable.Creator<ChoiceItem>() {
        public ChoiceItem createFromParcel(Parcel in) {
            return new ChoiceItem(in);
        }

        public ChoiceItem[] newArray(int size) {
            return new ChoiceItem[size];
        }
    };

    private ChoiceItem(Parcel in) {
        text = in.readString();
        check = in.readInt() !=0 ;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getText() {

        return text;
    }

    public boolean isCheck() {
        return check;
    }
}
