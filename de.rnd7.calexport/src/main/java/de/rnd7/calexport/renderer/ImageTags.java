package de.rnd7.calexport.renderer;

import static j2html.TagCreator.img;

import java.util.HashMap;
import java.util.Map;

import de.rnd7.calexport.config.Calconfig;
import j2html.tags.DomContent;

public class ImageTags {

	private static final Map<String, DomContent> IMAGE_TAGS = new HashMap<>();

	private ImageTags() {
	}

	public static void initTags(final Calconfig config) {
		config.getHashtag().forEach(tag -> IMAGE_TAGS.put("#" + tag.getName().toLowerCase(), img()
				.attr("width", tag.getWidth())
				.attr("height", tag.getHeight())
				.attr("alt", tag.getName())
				.attr("src", tag.getImage())));
	}

	public static Map<String, DomContent> getImageTags() {
		return IMAGE_TAGS;
	}

}
