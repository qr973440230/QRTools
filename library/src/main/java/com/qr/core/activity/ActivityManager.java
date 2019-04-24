package com.qr.core.activity;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

public class ActivityManager {
    private static List<Activity> activities = new LinkedList<>();

    public static void addActivity(Activity activity){
        if(activity != null && !activities.contains(activity)){
            activities.add(activity);
        }
    }
    public static void removeActivity(Activity activity){
        if(activity != null){
            activities.remove(activity);
        }
    }

    public static void finishActivity(Activity activity){
        if(activity != null){
            activities.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    public static void finishActivity(Class<?> cls){
        for (Activity activity : activities) {
            if(activity.getClass().equals(cls)){
                finishActivity(activity);
            }
        }
    }

    public static void finishAllActivity(){
        for (Activity activity : activities) {
            activity.finish();
        }
        activities.clear();
    }
}
