package samples;

public class MessageEvent extends Event {
    public final String message;
    public MessageEvent(String message) {
        this.message = message;
    }
}
