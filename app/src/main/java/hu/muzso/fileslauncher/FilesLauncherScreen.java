package hu.muzso.fileslauncher;

import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Display;

import androidx.annotation.NonNull;
import androidx.car.app.CarContext;
import androidx.car.app.Screen;
import androidx.car.app.model.Action;
import androidx.car.app.model.CarColor;
import androidx.car.app.model.Header;
import androidx.car.app.model.MessageTemplate;
import androidx.car.app.model.Template;

import java.util.ArrayList;
import java.util.List;

/**
 * A screen that shows a simple "Launching Files app" message.
 *
 * <p>See {@link FilesLauncherService} for the app's entry point to the car host.
 */
public class FilesLauncherScreen extends Screen {
    private static final String TAG = "FilesLauncherScreen";
    private static final List<String> PACKAGES = List.of(
            "com.android.documentsui",
            "com.android.car.documentsui", // AAOS 15
            "com.google.android.documentsui"
    );

    private static final List<String> ACTIVITIES = List.of(
            "com.android.documentsui.files.FilesActivity",
            "com.android.documentsui.LauncherActivity",
            "com.android.documentsui.files.LauncherActivity", // seen first in AAOS 15
            "com.android.documentsui.ViewDownloadsActivity"
    );

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
        boolean success = false;
        if (pm != null) {
            int i = 0;
            for (String packageName : PACKAGES) {
                ArrayList<String> activityClassNames = new ArrayList<>();
                try {
                    Intent intent = pm.getLaunchIntentForPackage(packageName);
                    if (intent != null) {
                        try {
                            ComponentName componentName = intent.getComponent();
                            activityClassNames.add(componentName.getClassName());
                        } catch (Exception e) {
                            errorMsgBuilder.append(mCarContext.getString(R.string.error_exception_get_activity_class, i, packageName, e.getMessage()));
                        }
                    } else {
                        Log.i(TAG, "invoking getLaunchIntentForPackage() for \"" + packageName + "\" package returned null");
                    }
                } catch (Exception e) {
                    errorMsgBuilder.append(mCarContext.getString(R.string.error_exception_for_package, i, packageName, "PackageManager.getLaunchIntentForPackage()", e.getMessage()));
                }
                if (activityClassNames.isEmpty()) {
                    activityClassNames.addAll(ACTIVITIES);
                }
                for (String activityClassName : activityClassNames) {
                    i++;
                    Log.i(TAG, "invoking getLaunchIntentForPackage() for \"" + packageName + "\" package");
                    Intent intent = new Intent();
                    try {
                        intent.setClassName(packageName, activityClassName);
                        try {
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        } catch (Exception e) {
                            errorMsgBuilder.append(mCarContext.getString(R.string.error_exception_for_package_and_activity, i, packageName, activityClassName, "Intent.addFlags()", e.getMessage()));
                        }
                    } catch (Exception e) {
                        errorMsgBuilder.append(mCarContext.getString(R.string.error_exception_for_package_and_activity, i, packageName, activityClassName, "Intent.setClassName()", e.getMessage()));
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
                        ActivityOptions activityOptions = ActivityOptions.makeBasic();
                        activityOptions.setLaunchDisplayId(Display.DEFAULT_DISPLAY);
                        try {
                            mCarContext.startActivity(intent, activityOptions.toBundle());
                            success = true;
                            Log.i(TAG, "startActivity() was successful");
                            break;
                        } catch (Exception e2) {
                            errorMsgBuilder.append(mCarContext.getString(R.string.error_exception_for_package_and_activity, i, packageName, activityClassName, "CarContext.startActivity()", e.getMessage()));
                        }
                    }
                    Log.i(TAG, "after startActivity()");
                }
            }
            if (!success) {
                errorMsgBuilder.append("\n");
                errorMsgBuilder.append(mCarContext.getString(R.string.error_failure_lead));
                i = 0;
                for (String packageName : PACKAGES) {
                    for (String activityClassName : ACTIVITIES) {
                        i++;
                        errorMsgBuilder.append("\n\n");
                        errorMsgBuilder.append(mCarContext.getString(R.string.error_failure_package, i, packageName, activityClassName));
                    }
                }
            }
        } else {
            errorMsgBuilder.append("\n");
            errorMsgBuilder.append(mCarContext.getString(R.string.error_package_manager));
        }
        if (!success && errorMsgBuilder.length() > 0) {
            Log.e(TAG, "Error(s):" + errorMsgBuilder);

            String finalMsg = mCarContext.getString(R.string.failure_with_error) +
                "\n" +
                mCarContext.getString(R.string.issue_reporting_notice, mCarContext.getString(R.string.github_url), mCarContext.getString(R.string.reddit_url)) +
                "\n\n" +
                errorMsgBuilder
            ;
            getScreenManager().push(new ResultScreen(mCarContext, finalMsg));
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

        Header header = new Header.Builder()
                .setStartHeaderAction(Action.APP_ICON)
                .build();

        return new MessageTemplate.Builder(mCarContext.getString(R.string.launcher_message))
                .addAction(action)
                .setHeader(header)
                .build();
    }
}
