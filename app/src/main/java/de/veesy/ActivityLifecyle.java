package de.veesy;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import de.veesy.settings.IntroductionActivity;
import de.veesy.util.Constants;

/**
 * Created by vivienbardosi on 12.12.17.
 */
public class ActivityLifecyle implements Application.ActivityLifecycleCallbacks {

    private VeesyApp veesyApp;

    public ActivityLifecyle(VeesyApp veesyApp) {
        this.veesyApp = veesyApp;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
