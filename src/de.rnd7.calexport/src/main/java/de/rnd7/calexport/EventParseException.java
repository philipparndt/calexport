package de.rnd7.calexport;

public class EventParseException extends Exception {

	private static final long serialVersionUID = -2706640222572376605L;

	public EventParseException(final Throwable t) {
		super(t);
	}

	public EventParseException(final String msg) {
		super(msg);
	}
}
