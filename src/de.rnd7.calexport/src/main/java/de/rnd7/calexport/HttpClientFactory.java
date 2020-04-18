package de.rnd7.calexport;

import org.apache.http.impl.client.CloseableHttpClient;

public interface HttpClientFactory {
	CloseableHttpClient create();
}
