/*
 * A base of classes used for informing various events.
 */
public abstract class Event {
}

/*
 * The following classes represent various events.
 * These events are created in stateCheck() method
 * and passed to processEvent() method.
 */

/*
 * This event is used for informing that one car
 * sent a message to another car. And this event
 * contains the string of the message.
 */
class MessageEvent extends Event {
    // The text of message.
    public final String message;

    /*
     * Constructs MessageEvent with given message.
     */
    public MessageEvent(String message) {
        this.message = message;
    }
}

/*
 * This event is used for informing that all jewels are
 * collected successfully.
 */
class ClearedEvent extends Event {
}
