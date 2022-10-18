package universalDiscord.notifiers;

public interface Notifier {
    boolean shouldNotify();

    void handleNotify();
}
