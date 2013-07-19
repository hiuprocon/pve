package samples;

/*
 * This event is used for informing that one car
 * sent a message to another car. And this event
 * contains the string of the message.
 */
public class MessageEvent extends Event {
    // The text of message.
    public final String message;

    /*
     * Constructs MessageEvent with given message.
     */
    public MessageEvent(String message) {
        this.message = message;
    }
}
