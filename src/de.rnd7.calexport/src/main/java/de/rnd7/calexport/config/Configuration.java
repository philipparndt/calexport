package de.rnd7.calexport.config;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class Configuration {
	private static final JAXBContext jaxbContext;

	static {
		try {
			jaxbContext = JAXBContext.newInstance(Calconfig.class);
		} catch (final JAXBException e) {
			throw new ConfigurationRuntimeException(e);
		}
	}

	private Configuration() {
	}

	public static Calconfig loadFrom(final InputStream in) {
		try {
			final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			return (Calconfig) unmarshaller.unmarshal(in);
		} catch (final JAXBException e) {
			throw new ConfigurationRuntimeException(e);
		}
	}

	public static void writeTo(final Calconfig config, final OutputStream out) {
		try {
			final Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(config, out);
		} catch (final JAXBException e) {
			throw new ConfigurationRuntimeException(e);
		}
	}
}
