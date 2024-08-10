package hu.muzso.fileslauncher;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.car.app.CarContext;
import androidx.car.app.Screen;
import androidx.car.app.model.Action;
import androidx.car.app.model.CarColor;
import androidx.car.app.model.MessageTemplate;
import androidx.car.app.model.Template;

/**
 * A screen that shows a simple "Launching Files app" message.
 *
 * <p>See {@link FilesLauncherService} for the app's entry point to the car host.
 */
public class FilesLauncherScreen extends Screen {
    private static final String TAG = "FilesLauncherScreen";
    private static final String[][] PACKAGES = new String[][] {
            new String[]{ "com.android.documentsui", "com.android.documentsui.LauncherActivity" },
            new String[]{ "com.google.android.documentsui", "com.android.documentsui.LauncherActivity" }
    };

    private final CarContext mCarContext;

    public FilesLauncherScreen(@NonNull CarContext carContext)
    {
        super(carContext);
        mCarContext = carContext;
    }

    private void onClickListener() {
        Log.i(TAG, "onClickListener is starting");
        PackageManager pm = mCarContext.getPackageManager();
        StringBuilder errorMsgBuilder = new StringBuilder(100);
        if (pm != null) {
            boolean success = false;
            for (int i = 0; i < PACKAGES.length; i++) {
                String packageName = PACKAGES[i][0];
                String activityClassName = PACKAGES[i][1];
                Log.i(TAG, "invoking getLaunchIntentForPackage() for \"" + packageName + "\" package");
                Intent intent = null;
                try {
                    intent = pm.getLaunchIntentForPackage(packageName);
                    if (intent == null) {
                        Log.i(TAG, "invoking getLaunchIntentForPackage() for \"" + packageName + "\" package returned null");
                    }
                } catch (Exception e) {
                    errorMsgBuilder.append(mCarContext.getString(R.string.error_exception_for_package, i+1, packageName, "PackageManager.getLaunchIntentForPackage()", e.getMessage()));
                }
                if (intent != null) {
                    try {
                        ComponentName componentName = intent.getComponent();
                        activityClassName = componentName.getClassName();
                    } catch (Exception e) {
                        errorMsgBuilder.append(mCarContext.getString(R.string.error_exception_get_activity_class, i+1, packageName, e.getMessage()));
                    }
                } else {
                    Log.i(TAG, "getLaunchIntentForPackage() did not return intent for \"" + packageName + "\" package, adding \"" + activityClassName + "\" activity");
                    intent = new Intent();
                    try {
                        intent.setClassName(packageName, activityClassName);
                        try {
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        } catch (Exception e) {
                            errorMsgBuilder.append(mCarContext.getString(R.string.error_exception_for_package_and_activity, i+1, packageName, activityClassName, "Intent.addFlags()", e.getMessage()));
                        }
                    } catch (Exception e) {
                        errorMsgBuilder.append(mCarContext.getString(R.string.error_exception_for_package_and_activity, i+1, packageName, activityClassName, "Intent.setClassName()", e.getMessage()));
                    }
                }
                Log.i(TAG, "before startActivity()");
                try {
                    mCarContext.startActivity(intent);
                    success = true;
                    Log.i(TAG, "startActivity() was successful");
                    break;
                } catch (ActivityNotFoundException e) {
                    Log.i(TAG, "no activity was found for \"" + packageName + "\" package and \"" + activityClassName + "\" activity");
                } catch (Exception e) {
                    errorMsgBuilder.append(mCarContext.getString(R.string.error_exception_for_package_and_activity, i+1, packageName, activityClassName, "CarContext.startActivity()", e.getMessage()));
                }
                Log.i(TAG, "after startActivity()");
            }
            if (!success) {
                errorMsgBuilder.append("\n");
                errorMsgBuilder.append(mCarContext.getString(R.string.error_failure_lead));
                for (int i = 0; i < PACKAGES.length; i++) {
                    String[] packageSpec = PACKAGES[i];
                    errorMsgBuilder.append("\n\n");
                    errorMsgBuilder.append(mCarContext.getString(R.string.error_failure_package, i+1, packageSpec[0], packageSpec[1]));
                }
            }
        } else {
            errorMsgBuilder.append("\n");
            errorMsgBuilder.append(mCarContext.getString(R.string.error_package_manager));
        }
        if (errorMsgBuilder.length() > 0) {
            Log.e(TAG, "Error:" + errorMsgBuilder);
            getScreenManager().push(new ResultScreen(mCarContext, errorMsgBuilder.toString()));
        }
        Log.i(TAG, "onClickListener is finished");
    }

    @NonNull
    @Override
    public Template onGetTemplate() {
        Action action = new Action.Builder()
                .setTitle(mCarContext.getString(R.string.launcher_button_label))
                .setBackgroundColor(CarColor.BLUE)
                .setOnClickListener(() -> onClickListener())
                .build();

        return new MessageTemplate.Builder(mCarContext.getString(R.string.launcher_message))
                .addAction(action)
                .setHeaderAction(Action.APP_ICON)
                .build();
    }
}
