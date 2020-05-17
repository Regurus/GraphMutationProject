import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class ProgressEvent extends Event {
    public ProgressEvent(Object source, EventTarget target, EventType<? extends Event> eventType) {
        super(source, target, eventType);
    }
}
