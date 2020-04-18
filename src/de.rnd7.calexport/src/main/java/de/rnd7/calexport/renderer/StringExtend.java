package de.rnd7.calexport.renderer;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

public final class StringExtend {

	private StringExtend() {
	}

	public static int getWidth(final String text) {
		final AffineTransform affinetransform = new AffineTransform();
		final FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
		final Font font = new Font("Calibri", Font.PLAIN, 10);
		return (int)font.getStringBounds(text, frc).getWidth();
	}

	public static int getWidthEm(final String text) {
		return (int) Math.round(12d / 96d * getWidth(text) + 0.4d);
	}
}
