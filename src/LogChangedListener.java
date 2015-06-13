import java.awt.*;
import java.util.EventListener;

public interface LogChangedListener
        extends EventListener
{
    void logChanged(Event e);
}