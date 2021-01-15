package com.gr15.messaging.models;

/**
 * @author Wassim
 */

public class Event {

    private String eventType;
    private Object eventInfo = null;

    public Event() {
    };

    public Event(String eventType, Object eventInfo) {
        this.eventType = eventType;
        this.eventInfo = eventInfo;
    }

    public Event(String type) {
        this.eventType = type;
    }

    public String getEventType() {
        return eventType;
    }

    public Object getEventInfo() {
        return eventInfo;
    }

    public boolean equals(Object o) {
        if (!this.getClass().equals(o.getClass())) {
            return false;
        }
        Event other = (Event) o;
        return this.eventType.equals(other.eventType)
                && (this.getEventInfo() != null && this.getEventInfo().equals(other.getEventInfo()))
                || (this.getEventInfo() == null && other.getEventInfo() == null);
    }

    public int hashCode() {
        return eventType.hashCode();
    }

    public String toString() {
        return String.format("event(%s,%s)", eventType, eventInfo);
    }
}
