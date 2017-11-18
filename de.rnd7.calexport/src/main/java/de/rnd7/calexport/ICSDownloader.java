package de.rnd7.calexport;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

public class ICSDownloader {
	private ICSDownloader() {
	}

	static final ResponseHandler<? extends String> RESPONSE_HANDLER = response -> {
		final int status = response.getStatusLine().getStatusCode();
		if (status >= 200 && status < 300) {
			final HttpEntity entity = response.getEntity();
			return entity != null ? EntityUtils.toString(entity) : null;
		} else {
			throw new ClientProtocolException("Unexpected response status: " + status);
		}
	};

	public static InputStream download(final String surl, final HttpClientFactory clientFactory) throws IOException {
		final String replaced = surl.replace("webcal://", "https://");
		final HttpGet httpget = new HttpGet(replaced);

		try (final CloseableHttpClient httpclient = clientFactory.create()) {
			final String ics = httpclient.execute(httpget, RESPONSE_HANDLER);
			return new ByteArrayInputStream(ics.getBytes("utf-8"));
		}

	}
}
