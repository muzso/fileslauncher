package hu.muzso.fileslauncher;

import android.content.Intent;
import android.content.pm.ApplicationInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.car.app.CarAppService;
import androidx.car.app.Screen;
import androidx.car.app.Session;
import androidx.car.app.SessionInfo;
import androidx.car.app.validation.HostValidator;

/**
 * Entry point for the Files Launcher app.
 *
 * <p>{@link CarAppService} is the main interface between the app and the car host. For more
 * details, see the <a href="https://developer.android.com/training/cars/navigation">Android for
 * Cars Library developer guide</a>.
 */
public final class FilesLauncherService extends CarAppService {

    public FilesLauncherService() {
        // Exported services must have an empty public constructor.
    }

    @Override
    @NonNull
    public Session onCreateSession(@NonNull SessionInfo sessionInfo) {
        return new Session() {
            @Override
            @NonNull
            public Screen onCreateScreen(@Nullable Intent intent) {
                return new FilesLauncherScreen(getCarContext());
            }
        };
    }

    @NonNull
    @Override
    public HostValidator createHostValidator() {
        if ((getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            return HostValidator.ALLOW_ALL_HOSTS_VALIDATOR;
        } else {
            return new HostValidator.Builder(getApplicationContext())
                    .addAllowedHosts(androidx.car.app.R.array.hosts_allowlist_sample)
                    .build();
        }
    }
}
