package com.example.madiba.venualpha.models;

/**
 * Created by Madiba on 1/30/2017.
 */

public class ModelEventFeature {
    public static final String FEATURE_WIFI="Wifi";
    public static final String FEATURE_BUS="Bus";
    public static final String FEATURE_PHOTOSHOT="Photoshot";
    public static final String FEATURE_FOOD_AND_DRINKS="Food and Drinks";

    private String title;
    private String resourceId;

    public ModelEventFeature(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
}
