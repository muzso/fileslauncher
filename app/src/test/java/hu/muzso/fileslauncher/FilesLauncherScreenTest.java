package hu.muzso.fileslauncher;

import static com.google.common.truth.Truth.assertThat;

import androidx.car.app.model.MessageTemplate;
import androidx.car.app.testing.TestCarContext;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.internal.DoNotInstrument;

/**
 * A sample test on {@link FilesLauncherScreen}.
 *
 * <p>Demonstrating the usage of {@link TestCarContext} and validating that the returned
 * {@link androidx.car.app.model.Template} has the expected contents.
 */
@RunWith(RobolectricTestRunner.class)
@DoNotInstrument
public class FilesLauncherScreenTest {
    private final TestCarContext mTestCarContext =
            TestCarContext.createCarContext(ApplicationProvider.getApplicationContext());

    @Test
    public void getTemplate_containsExpectedRow() {
        FilesLauncherScreen screen = new FilesLauncherScreen(mTestCarContext);
        MessageTemplate template = (MessageTemplate) screen.onGetTemplate();

        String msg = template.getMessage().toString();
        assertThat(msg).isEqualTo("This app can launch the \"Files\" builtin application.");
    }
}
