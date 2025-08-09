package com.example.fitmybody;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.annotation.NonNull;

public class Spor implements Parcelable {
    private String ad;
    private int resimId;

    public Spor(String ad,int resimId){
        this.ad = ad;
        this.resimId = resimId;
    }

    protected Spor(Parcel in){
        ad = in.readString();
        resimId = in.readInt();
    }

    public static final Creator<Spor> CREATOR = new Creator<Spor>() {
        @Override
        public Spor createFromParcel(Parcel source) {
            return new Spor(source);
        }

        @Override
        public Spor[] newArray(int size) {
            return new Spor[size];
        }
    };

    @Override
    public int describeContents() {
            return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(ad);
        dest.writeInt(resimId);
    }
    public String getAd(){
        return ad;
    }
    public int getResimId(){
        return resimId;
    }
}
