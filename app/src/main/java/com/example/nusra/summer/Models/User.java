package com.example.nusra.summer.Models;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class User implements Serializable, Parcelable {
    public String Email;
    public String Name;
    public String PhoneNumber;
    public String DateOfBirth;
    public String UID;
    public PatientBGRecord P_Record;
// need empty constractor

    public User() {
    }

    public User(String email) {
        Email = email;
    }

    public User(String name, String phoneNumber, String dateOfBirth) {
        Name = name;
        PhoneNumber = phoneNumber;
        DateOfBirth = dateOfBirth;
    }
    public User(String uid, PatientBGRecord PR) {
        String UID = uid;
        P_Record = PR;
    }

    protected User(Parcel in) {
        Email = in.readString();
        Name = in.readString();
        PhoneNumber = in.readString();
        DateOfBirth = in.readString();
        UID = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Email);
        dest.writeString(Name);
        dest.writeString(PhoneNumber);
        dest.writeString(DateOfBirth);
        dest.writeString(UID);
    }


//    public String getEmail() {
//        return Email;
//    }

}
