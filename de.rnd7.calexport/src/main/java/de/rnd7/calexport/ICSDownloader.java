package de.rnd7.calexport;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ICSDownloader {
	private ICSDownloader() {
	}

	public static InputStream download(final String surl) throws Exception {
		final String replaced = surl.replace("webcal://", "https://");
		final HttpGet httpget = new HttpGet(replaced);
		final CloseableHttpClient httpclient = HttpClients.createDefault();

		// Create a custom response handler
		final ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

			@Override
			public String handleResponse(
					final HttpResponse response) throws ClientProtocolException, IOException {
				final int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					final HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					throw new ClientProtocolException("Unexpected response status: " + status);
				}
			}

		};

		final String ics = httpclient.execute(httpget, responseHandler);
		return new ByteArrayInputStream(ics.getBytes("utf-8"));
	}
}
