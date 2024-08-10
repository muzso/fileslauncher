package hu.muzso.fileslauncher;

import androidx.annotation.NonNull;
import androidx.car.app.CarContext;
import androidx.car.app.Screen;
import androidx.car.app.model.Action;
import androidx.car.app.model.LongMessageTemplate;
import androidx.car.app.model.Template;

/**
 * A screen that shows a long text message.
 */
public class ResultScreen extends Screen {
    private final CarContext mCarContext;
    private final String mMessage;

    public ResultScreen(@NonNull CarContext carContext, String message)
    {
        super(carContext);
        mCarContext = carContext;
        mMessage = message;
    }

    @NonNull
    @Override
    public Template onGetTemplate() {
        return new LongMessageTemplate.Builder(mMessage)
                .setTitle(mCarContext.getString(R.string.result_title))
                .setHeaderAction(Action.BACK)
                .build();
    }
}
