package universalDiscord.notifiers.onevent;

import net.runelite.api.events.WidgetLoaded;
import universalDiscord.notifiers.Notifier;

public interface WidgetLoadHandler extends Notifier {
    void handleWidgetLoaded(WidgetLoaded widgetLoaded) ;
}
