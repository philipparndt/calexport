package de.rnd7.calexport.renderer;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

public class StringExtend {
	public static int getWidth(final String text) {
		final AffineTransform affinetransform = new AffineTransform();
		final FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
		final Font font = new Font("Helvetica", Font.PLAIN, 8);
		final int textwidth = (int)font.getStringBounds(text, frc).getWidth();
		return textwidth;
	}

	public static int getWidthEm(final String text) {
		return (int) Math.round(12d / 96d * getWidth(text) + 0.4d);
	}
}
