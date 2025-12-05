package com.comp2042.input;

/**
 * Represents a game movement event.
 * Encapsulates the type of event and its source.
 */
public final class MoveEvent {
    private final EventType eventType;
    private final EventSource eventSource;

    /**
     * Constructs a new MoveEvent.
     *
     * @param eventType   The type of the event.
     * @param eventSource The source of the event.
     */
    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    /**
     * Gets the event type.
     * 
     * @return The {@link EventType}.
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Gets the event source.
     * 
     * @return The {@link EventSource}.
     */
    public EventSource getEventSource() {
        return eventSource;
    }
}
