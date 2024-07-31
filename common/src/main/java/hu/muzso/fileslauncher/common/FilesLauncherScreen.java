/*
 * Copyright (C) 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package hu.muzso.fileslauncher.common;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentSender;
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
    private final CarContext mCarContext;

    public FilesLauncherScreen(@NonNull CarContext carContext)
    {
        super(carContext);
        mCarContext = carContext;
    }

    @NonNull
    @Override
    public Template onGetTemplate() {
        String message = "This app can launch the \"Files\" builtin application.";
        String[][] packages = new String[][] {
                new String[]{ "com.android.documentsui", "com.android.documentsui.LauncherActivity" },
                new String[]{ "com.google.android.documentsui", "com.android.documentsui.LauncherActivity" }
        };

        Action action = new Action.Builder()
                .setTitle("Launch")
                .setBackgroundColor(CarColor.BLUE)
                .setOnClickListener(() -> {
                    Log.i(TAG, "onClickListener is starting");
                    PackageManager pm = mCarContext.getPackageManager();
                    if (pm != null) {
                        boolean success = false;
                        for (int i = 0; i < packages.length; i++) {
                            String packageName = packages[i][0];
                            String activityName = packages[i][1];
                            Log.i(TAG, "invoking getLaunchIntentForPackage() for \"" + packageName + "\" package");
                            Intent intent = pm.getLaunchIntentForPackage(packageName);
                            if (intent == null) {
                                Log.i(TAG, "getLaunchIntentForPackage() did not return intent for \"" + packageName + "\" package, adding \"" + activityName + "\" activity");
                                intent = new Intent();
                                intent.setClassName(packageName, activityName);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            }
                            Log.i(TAG, "before startActivity()");
                            try {
                                mCarContext.startActivity(intent);
                                success = true;
                                Log.i(TAG, "startActivity() was successful");
                                break;
                            } catch (ActivityNotFoundException e) {
                                Log.i(TAG, "no activity was found for \"" + packageName + "\" package and \"" + activityName + "\" activity");
                            }
                            Log.i(TAG, "after startActivity()");
                        }
                        if (!success) {
                            Log.e(TAG, "failed to launch the \"Files\" app");
                        }
                    } else {
                        Log.e(TAG, "failed to get the package manager from the context");
                    }
                    Log.i(TAG, "onClickListener is finished");
                })
                .build();

        return new MessageTemplate.Builder(message)
                .addAction(action)
                .setHeaderAction(Action.APP_ICON)
                .build();
    }
}
