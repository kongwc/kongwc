package com.kongwc.tools.common.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.client.*;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定制HTTP Rest访问工具类
 */
public class ViewDepotRestTemplate extends RestTemplate {

//    public ViewDepotRestTemplate() {
//        super(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
//        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
//        interceptors.add(new LoggingRequestInterceptor());
//        setInterceptors(interceptors);
//    }

    private Map<String, String> contentTypeReplacer = new HashMap<>();

    public void setContentTypeReplacer(Map<String, String> replacer) {
        this.contentTypeReplacer = replacer;
    }

    @Override
    protected <T> T doExecute(final URI url, final HttpMethod method, final RequestCallback requestCallback, final ResponseExtractor<T> responseExtractor)
            throws RestClientException {

        Assert.notNull(url, "'url' must not be null");
        Assert.notNull(method, "'method' must not be null");
        ClientHttpResponse response = null;
        try {
            ClientHttpRequest request = createRequest(url, method);
            if (requestCallback != null) {
                requestCallback.doWithRequest(request);
            }
            response = request.execute();
            handleResponse(url, method, response);
            if (responseExtractor != null) {

                // Customize for Invalid mime type.
                if (contentTypeReplacer != null) {
                    HttpHeaders headers = response.getHeaders();
                    List<String> contentTypes = headers.get("Content-Type");
                    if (contentTypes != null) {
                        for (int i = 0, size = contentTypes.size(); i < size; i++) {
                            String type = contentTypes.get(i);
                            if (contentTypeReplacer.containsKey(type)) {
                                contentTypes.set(i, contentTypeReplacer.get(type));
                            }
                        }
                    }
                }

                return responseExtractor.extractData(response);
            } else {
                return null;
            }
        } catch (IOException ex) {
            String resource = url.toString();
            String query = url.getRawQuery();
            resource = (query != null ? resource.substring(0, resource.indexOf(query) - 1) : resource);
            throw new ResourceAccessException("I/O error on " + method.name() +
                    " request for \"" + resource + "\": " + ex.getMessage(), ex);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}
