package com.gooddata;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.Locale;

/**
 * TODO
 */
public class GoodDataCloseableHttpClientWrapper extends CloseableHttpClient {

    private final HttpClient wrapped;

    public GoodDataCloseableHttpClientWrapper(HttpClient wrapped) {
        this.wrapped = wrapped;
    }


    @Override
    public HttpParams getParams() {
        return wrapped.getParams();
    }

    @Override
    public ClientConnectionManager getConnectionManager() {
        return wrapped.getConnectionManager();
    }

    @Override
    public CloseableHttpResponse execute(HttpUriRequest request) throws IOException, ClientProtocolException {
        return new CloseableHttpResponseWrapper(wrapped.execute(request));
    }

    @Override
    public CloseableHttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException, ClientProtocolException {
        return new CloseableHttpResponseWrapper(wrapped.execute(request, context));
    }

    @Override
    public CloseableHttpResponse execute(HttpHost target, HttpRequest request) throws IOException, ClientProtocolException {
        return new CloseableHttpResponseWrapper(wrapped.execute(target, request));
    }

    @Override
    protected CloseableHttpResponse doExecute(HttpHost target, HttpRequest request, HttpContext context) throws IOException, ClientProtocolException {
        throw new UnsupportedOperationException("Not willing to implement");
    }

    @Override
    public CloseableHttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException, ClientProtocolException {
        return new CloseableHttpResponseWrapper(wrapped.execute(target, request, context));
    }

    @Override
    public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        return wrapped.execute(request, responseHandler);
    }

    @Override
    public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
        return wrapped.execute(request, responseHandler, context);
    }

    @Override
    public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        return wrapped.execute(target, request, responseHandler);
    }

    @Override
    public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
        return wrapped.execute(target, request, responseHandler, context);
    }

    @Override
    public void close() throws IOException {
        getConnectionManager().shutdown();
    }

    private static class CloseableHttpResponseWrapper implements CloseableHttpResponse {

        private final HttpResponse wrapped;

        private CloseableHttpResponseWrapper(HttpResponse wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public void close() throws IOException {
            // TODO nothing??
        }

        @Override
        public StatusLine getStatusLine() {
            return wrapped.getStatusLine();
        }

        @Override
        public void setStatusLine(StatusLine statusline) {
            wrapped.setStatusLine(statusline);
        }

        @Override
        public void setStatusLine(ProtocolVersion ver, int code) {
            wrapped.setStatusLine(ver, code);
        }

        @Override
        public void setStatusLine(ProtocolVersion ver, int code, String reason) {
            wrapped.setStatusLine(ver, code, reason);
        }

        @Override
        public void setStatusCode(int code) throws IllegalStateException {
            wrapped.setStatusCode(code);
        }

        @Override
        public void setReasonPhrase(String reason) throws IllegalStateException {
            wrapped.setReasonPhrase(reason);
        }

        @Override
        public HttpEntity getEntity() {
            return wrapped.getEntity();
        }

        @Override
        public void setEntity(HttpEntity entity) {
            wrapped.setEntity(entity);
        }

        @Override
        public Locale getLocale() {
            return wrapped.getLocale();
        }

        @Override
        public void setLocale(Locale loc) {
            wrapped.setLocale(loc);
        }

        @Override
        public ProtocolVersion getProtocolVersion() {
            return wrapped.getProtocolVersion();
        }

        @Override
        public boolean containsHeader(String name) {
            return wrapped.containsHeader(name);
        }

        @Override
        public Header[] getHeaders(String name) {
            return wrapped.getHeaders(name);
        }

        @Override
        public Header getFirstHeader(String name) {
            return wrapped.getFirstHeader(name);
        }

        @Override
        public Header getLastHeader(String name) {
            return wrapped.getLastHeader(name);
        }

        @Override
        public Header[] getAllHeaders() {
            return wrapped.getAllHeaders();
        }

        @Override
        public void addHeader(Header header) {
            wrapped.addHeader(header);
        }

        @Override
        public void addHeader(String name, String value) {
            wrapped.addHeader(name, value);
        }

        @Override
        public void setHeader(Header header) {
            wrapped.setHeader(header);
        }

        @Override
        public void setHeader(String name, String value) {
            wrapped.setHeader(name, value);
        }

        @Override
        public void setHeaders(Header[] headers) {
            wrapped.setHeaders(headers);
        }

        @Override
        public void removeHeader(Header header) {
            wrapped.removeHeader(header);
        }

        @Override
        public void removeHeaders(String name) {
            wrapped.removeHeaders(name);
        }

        @Override
        public HeaderIterator headerIterator() {
            return wrapped.headerIterator();
        }

        @Override
        public HeaderIterator headerIterator(String name) {
            return wrapped.headerIterator(name);
        }

        @Override
        public HttpParams getParams() {
            return wrapped.getParams();
        }

        @Override
        public void setParams(HttpParams params) {
            wrapped.setParams(params);
        }
    }
}
