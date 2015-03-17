package com.gooddata;

import static com.gooddata.util.Validate.notNull;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.Configurable;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.AbstractClientHttpRequest;
import org.springframework.http.client.AbstractClientHttpResponse;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * TODO
 */
final class GoodDataClientHttpRequestFactory implements ClientHttpRequestFactory {

    private final HttpClient httpClient;

    public GoodDataClientHttpRequestFactory(HttpClient client) {
        this.httpClient = notNull(client, "client");
    }

    @Override
    public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {

        HttpUriRequest httpRequest = createHttpUriRequest(httpMethod, uri);
        HttpContext context = HttpClientContext.create();
        if (httpRequest instanceof Configurable) {
            context.setAttribute(HttpClientContext.REQUEST_CONFIG, ((Configurable) httpRequest).getConfig());
        }

        return new GoodDataClientHttpRequest(httpClient, httpRequest, context);
    }

    // copied from spring4 HttpComponentsClientHttpRequestFactory
    private HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri) {
        switch (httpMethod) {
            case GET:
                return new HttpGet(uri);
            case DELETE:
                return new HttpDelete(uri);
            case HEAD:
                return new HttpHead(uri);
            case OPTIONS:
                return new HttpOptions(uri);
            case POST:
                return new HttpPost(uri);
            case PUT:
                return new HttpPut(uri);
            case TRACE:
                return new HttpTrace(uri);
            case PATCH:
                return new HttpPatch(uri);
            default:
                throw new IllegalArgumentException("Invalid HTTP method: " + httpMethod);
        }
    }

    // copied from spring4 HttpComponentsClientHttpRequestFactory
    /**
     * An alternative to {@link org.apache.http.client.methods.HttpDelete} that
     * extends {@link org.apache.http.client.methods.HttpEntityEnclosingRequestBase}
     * rather than {@link org.apache.http.client.methods.HttpRequestBase} and
     * hence allows HTTP delete with a request body. For use with the RestTemplate
     * exchange methods which allow the combination of HTTP DELETE with entity.
     * @since 4.1.2
     */
    private static class HttpDelete extends HttpEntityEnclosingRequestBase {

        public HttpDelete(URI uri) {
            super();
            setURI(uri);
        }

        @Override
        public String getMethod() {
            return "DELETE";
        }
    }

    private static class GoodDataClientHttpRequest extends AbstractClientHttpRequest {

        private final HttpClient httpClient;
        private final HttpUriRequest httpRequest;
        private final HttpContext httpContext;

        private ByteArrayOutputStream bufferedOutput = new ByteArrayOutputStream();

        private GoodDataClientHttpRequest(HttpClient httpClient, HttpUriRequest httpRequest, HttpContext httpContext) {
            this.httpClient = httpClient;
            this.httpRequest = httpRequest;
            this.httpContext = httpContext;
        }

        @Override
        protected OutputStream getBodyInternal(HttpHeaders headers) throws IOException {
            return bufferedOutput;
        }

        @Override
        protected ClientHttpResponse executeInternal(HttpHeaders headers) throws IOException {
            byte[] bytes = this.bufferedOutput.toByteArray();
            if (headers.getContentLength() == -1) {
                headers.setContentLength(bytes.length);
            }

            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                String headerName = entry.getKey();
                if ("Cookie".equalsIgnoreCase(headerName)) {  // RFC 6265
                    String headerValue = StringUtils.collectionToDelimitedString(entry.getValue(), "; ");
                    httpRequest.addHeader(headerName, headerValue);
                }
                if (!headerName.equalsIgnoreCase(HTTP.CONTENT_LEN) &&
                        !headerName.equalsIgnoreCase(HTTP.TRANSFER_ENCODING)) {
                    for (String headerValue : entry.getValue()) {
                        httpRequest.addHeader(headerName, headerValue);
                    }
                }
            }

            if (httpRequest instanceof HttpEntityEnclosingRequest) {
                HttpEntityEnclosingRequest entityEnclosingRequest = (HttpEntityEnclosingRequest) httpRequest;
                HttpEntity requestEntity = new ByteArrayEntity(bytes);
                entityEnclosingRequest.setEntity(requestEntity);
            }
            final GoodDataClientHttpResponse httpResponse = new GoodDataClientHttpResponse(httpClient.execute(httpRequest, httpContext));
            this.bufferedOutput = null;
            return httpResponse;
        }

        @Override
        public HttpMethod getMethod() {
            return HttpMethod.valueOf(httpRequest.getMethod());
        }

        @Override
        public URI getURI() {
            return httpRequest.getURI();
        }

    }

    private static class GoodDataClientHttpResponse extends AbstractClientHttpResponse {

        private final HttpResponse httpResponse;
        private HttpHeaders headers;

        public GoodDataClientHttpResponse(HttpResponse httpResponse) {

            this.httpResponse = httpResponse;
        }

        @Override
        public int getRawStatusCode() throws IOException {
            return httpResponse.getStatusLine().getStatusCode();
        }

        @Override
        public String getStatusText() throws IOException {
            return httpResponse.getStatusLine().getReasonPhrase();
        }

        @Override
        public void close() {
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                try {
                    // Release underlying connection back to the connection manager
                    EntityUtils.consume(entity);
                }
                catch (IOException e) {
                    // ignore
                }
            }
        }

        @Override
        public InputStream getBody() throws IOException {
            HttpEntity entity = httpResponse.getEntity();
            return entity != null ? entity.getContent() : null;
        }

        @Override
        public HttpHeaders getHeaders() {
            if (headers == null) {
                headers = new HttpHeaders();
                for (Header header : httpResponse.getAllHeaders()) {
                    headers.add(header.getName(), header.getValue());
                }
            }
            return headers;
        }
    }
}
