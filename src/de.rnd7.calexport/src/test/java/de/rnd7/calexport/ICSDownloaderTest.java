package de.rnd7.calexport;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ICSDownloaderTest {
	@Test
	public void test_download() throws Exception {
		final CloseableHttpClient client = mock(CloseableHttpClient.class);

		Mockito.when(client.execute(Mockito.any(), Mockito.eq(ICSDownloader.RESPONSE_HANDLER))).then(new Answer<String>() {
			@Override
			public String answer(final InvocationOnMock invocation) throws Throwable {
				return ((HttpGet) invocation.getArguments()[0]).getURI().toString();
			}
		});

		assertEquals("https://example.org/test", IOUtils.toString(ICSDownloader.download("webcal://example.org/test", () -> client), CharEncoding.UTF_8));
	}

	@Test
	public void test_response_handler() throws Exception {
		final HttpResponse response = mock(HttpResponse.class, RETURNS_DEEP_STUBS);
		when(response.getStatusLine().getStatusCode()).thenReturn(200);
		when(response.getEntity()).thenReturn(new StringEntity("foobar"));
		assertEquals("foobar", ICSDownloader.RESPONSE_HANDLER.handleResponse(response));
	}

	@Test
	public void test_null_entity() throws Exception {
		final HttpResponse response = mock(HttpResponse.class, RETURNS_DEEP_STUBS);
		when(response.getStatusLine().getStatusCode()).thenReturn(200);
		when(response.getEntity()).thenReturn(null);
		assertNull(ICSDownloader.RESPONSE_HANDLER.handleResponse(response));

	}

	@Test(expected=IOException.class)
	public void test_response_error1() throws Exception {
		final HttpResponse response = mock(HttpResponse.class, RETURNS_DEEP_STUBS);
		when(response.getStatusLine().getStatusCode()).thenReturn(400);
		ICSDownloader.RESPONSE_HANDLER.handleResponse(response);
	}

	@Test(expected=IOException.class)
	public void test_response_error2() throws Exception {
		final HttpResponse response = mock(HttpResponse.class, RETURNS_DEEP_STUBS);
		when(response.getStatusLine().getStatusCode()).thenReturn(100);
		ICSDownloader.RESPONSE_HANDLER.handleResponse(response);
	}
}
