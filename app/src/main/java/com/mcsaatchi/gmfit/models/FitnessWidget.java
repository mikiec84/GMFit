package com.mcsaatchi.gmfit.models;

import android.os.Parcel;
import android.os.Parcelable;

public class FitnessWidget implements Parcelable {
    public static final Creator<FitnessWidget> CREATOR = new Creator<FitnessWidget>() {
        @Override
        public FitnessWidget createFromParcel(Parcel source) {
            return new FitnessWidget(source);
        }

        @Override
        public FitnessWidget[] newArray(int size) {
            return new FitnessWidget[size];
        }
    };

    int id;
    String title;
    String measurementUnit;
    int value;
    int metricIconDrawableId;
    int position;
    int widget_id;

    public FitnessWidget() {
    }

    public FitnessWidget(String title, String measurementUnit, int value, int metricIconDrawableId, int position) {
        this.title = title;
        this.measurementUnit = measurementUnit;
        this.value = value;
        this.metricIconDrawableId = metricIconDrawableId;
        this.position = position;
    }

    protected FitnessWidget(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.measurementUnit = in.readString();
        this.value = in.readInt();
        this.metricIconDrawableId = in.readInt();
        this.position = in.readInt();
        this.widget_id = in.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getMetricIconDrawableId() {
        return metricIconDrawableId;
    }

    public void setMetricIconDrawableId(int metricIconDrawableId) {
        this.metricIconDrawableId = metricIconDrawableId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getWidget_id() {
        return widget_id;
    }

    public void setWidget_id(int widget_id) {
        this.widget_id = widget_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.measurementUnit);
        dest.writeInt(this.value);
        dest.writeInt(this.metricIconDrawableId);
        dest.writeInt(this.position);
        dest.writeInt(this.widget_id);
    }
}
