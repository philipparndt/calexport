package de.rnd7.calexport.renderer;

public class RendererRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 5220664495222568315L;

	public RendererRuntimeException(final String message, final Throwable t) {
		super(message, t);
	}
}
