package palladin.lab32;

import android.view.View;

/**
 * Created by Palladin on 13.12.2016.
 */
public class Handlers
{
    public interface CheckedChangedHandler
    {
        void onCheckedChange(boolean isChecked, Object data);
    }

    public interface ClickHandler
    {
        void onClick(View v, Object data);
    }

    public interface LongClickHandler
    {
        boolean onLongClick(View v, Object data);
    }

    public interface Handler
    {
        void action();
    }
}
