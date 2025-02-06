package armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape;

import android.app.Application;

import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.util.SharePrefUtils;


public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        SharePrefUtils.init(this);

    }

}

