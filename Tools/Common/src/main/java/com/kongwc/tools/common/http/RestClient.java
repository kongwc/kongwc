package com.kongwc.tools.common.http;

import com.alibaba.fastjson.JSON;
import com.kongwc.tools.common.exception.DevelopmentException;
import com.kongwc.tools.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class RestClient {

    private ViewDepotRestTemplate rest;
    private HttpHeaders jsonHeader;
    private HttpHeaders formHeader;
    private HttpHeaders mutilPartHeader;
    private HttpStatus status;


    public RestClient() {
        this.rest = new ViewDepotRestTemplate();
        init(rest);

        this.jsonHeader = new HttpHeaders();
        jsonHeader.setContentType(MediaType.APPLICATION_JSON);

        this.formHeader = new HttpHeaders();
        formHeader.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        this.mutilPartHeader = new HttpHeaders();
        mutilPartHeader.setContentType(MediaType.MULTIPART_FORM_DATA);

    }

    private void init(ViewDepotRestTemplate viewDepotRestTemplate) {
        List<HttpMessageConverter<?>> converters = viewDepotRestTemplate.getMessageConverters();
        for (HttpMessageConverter converter : converters) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setDefaultCharset(Charset.forName("UTF-8"));
                ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
                break;
            }
        }
        converters.add(new TextHtmlGsonHttpMessageConverter());

    }

    public <T> T get(String baseUrl, String uri, Class<T> type, Map<String, Object> args) {

        return get(baseUrl, uri, type, args, null);
    }

    public <T> T get(String baseUrl, String uri, Class<T> type, Map<String, Object> args, Map<String, String> externalHeader) {
        if (externalHeader != null) {
            for (Map.Entry<String, String> entry : externalHeader.entrySet()) {
                if (jsonHeader.containsKey(entry.getKey())) {
                    jsonHeader.replace(entry.getKey(), Arrays.asList(entry.getValue()));
                } else {
                    jsonHeader.add(entry.getKey(), entry.getValue());
                }
            }
        }
        HttpEntity<String> requestEntity = new HttpEntity<>("", jsonHeader);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + uri);
        if (args != null) {
            for (Map.Entry<String, Object> entry : args.entrySet()) {
                builder.queryParam(entry.getKey(), entry.getValue());
            }
        }
        log.debug("request--[{}]", builder.build().encode().toUri());
        ResponseEntity<T> responseEntity = rest.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity, type);
//        } else {
//            responseEntity = rest.exchange(baseUrl + uri, HttpMethod.GET, requestEntity, type);
//        }
        this.setStatus(responseEntity.getStatusCode());
        T response = responseEntity.getBody();
        log.debug("response--[{}]", response);
        return response;
    }

    public <T> T get(String baseUrl, String uri, Class<T> type) {
        return get(baseUrl, uri, type, null, null);
    }

//    public <T> T get(String baseUrl, String uri, Class<T> type, Map<String, String> externalHeader) {
//        return get(baseUrl, uri, type, null, externalHeader);
//    }

//    public void download(String baseUrl, String uri, File file) {
//
//        HttpEntity<String> entity = new HttpEntity<>(formHeader);
//        ResponseEntity<byte[]> response = rest.exchange(
//                baseUrl + uri, HttpMethod.GET, entity, byte[].class);
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            try {
//                Files.write(file.toPath(), response.getBody());
//            } catch (IOException e) {
//                throw new HttpException(String.format("Download failed. %s", baseUrl + uri));
//            }
//        }
//    }

    public <T> T download(String baseUrl, String uri, DownloadExtractor<T> extractor) {

        // Optional Accept header
        RequestCallback requestCallback = request -> request.getHeaders()
                .setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));

        // Streams the response instead of loading it all in memory
        ResponseExtractor<T> responseExtractor = response -> {
            // Here I write the response to a file but do what you like
            if (extractor != null) {
                return extractor.extract(response.getBody());
//                FileUtil.toFile(response.getBody(), "D:\\test.dat");
            }
            return null;
        };
        return rest.execute(baseUrl + StringUtil.nvl(uri), HttpMethod.GET, requestCallback, responseExtractor);
    }

    public InputStream downloadByStream(String baseUrl, String uri) {

        HttpEntity<String> entity = new HttpEntity<>(formHeader);
        ResponseEntity<Resource> response = rest.exchange(
                baseUrl + uri, HttpMethod.GET, entity, Resource.class);

        try {
            return response.getBody().getInputStream();
        } catch (IOException e) {
            throw new DevelopmentException(e);
        }
    }

    public interface DownloadExtractor<T> {
        T extract(InputStream is);
    }


    public <T> T post(String baseUrl, String uri, MultiValueMap<String, ?> args, Class<T> responseType) {

        ViewDepotRestTemplate rest = new ViewDepotRestTemplate();
        init(rest);
//        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//        requestFactory.setBufferRequestBody(false);
//        rest.setRequestFactory(requestFactory);

        HttpEntity<MultiValueMap<String, ?>> requestEntity = new HttpEntity<>(args, mutilPartHeader);
        ResponseEntity<T> responseEntity = rest.exchange(baseUrl + StringUtil.nvl(uri), HttpMethod.POST, requestEntity, responseType);

        this.setStatus(responseEntity.getStatusCode());
        log.debug("request--[{}][{}][{}]", baseUrl, uri);
        T response = responseEntity.getBody();
        log.debug("response--[{}]", JSON.toJSONString(response));
        return response;
    }

    public <T> T post(String baseUrl, String uri, Object param, Class<T> responseType, Map<String, String> externalHeader) {

        return post(baseUrl, uri, JSON.toJSONString(param), responseType, externalHeader);
    }

    public <T> T put(String baseUrl, String uri, Object param, Class<T> responseType, Map<String, String> externalHeader) {

        return put(baseUrl, uri, JSON.toJSONString(param), responseType, externalHeader);
    }

    public <T> T post(String baseUrl, String uri, String param, Class<T> responseType) {

        return post(baseUrl, uri, param, responseType, null);
    }

    public <T> T post(String baseUrl, String uri, Object param, Class<T> responseType) {

        return post(baseUrl, uri, JSON.toJSONString(param), responseType, null);
    }

//    public <T> T post(String baseUrl, String uri, InputStream is, Class<T> responseType) {
//
////        RequestCallback requestCallback =  request -> {
////                request.getHeaders().add("Content-type", "application/octet-stream");
////                IOUtils.copy(is, request.getBody());
////        };
//        ViewDepotRestTemplate rest = new ViewDepotRestTemplate();
//        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//        requestFactory.setBufferRequestBody(false);
//        rest.setRequestFactory(requestFactory);
//
//        HttpEntity<MultiValueMap<String,Object>> request = new HttpEntity<>(parts, headers);
//
////        final HttpMessageConverterExtractor<String> responseExtractor =
////                new HttpMessageConverterExtractor<>(String.class, rest.getMessageConverters());
//        String result = rest.execute(baseUrl.concat(uri), HttpMethod.POST, requestCallback, responseExtractor);
//        return JSON.parseObject(result, responseType);
//    }

    public <T> T post(String baseUrl, String uri, String json, Class<T> responseType, Map<String, String> externalHeader) {
        if (externalHeader != null) {
            for (Map.Entry<String, String> entry : externalHeader.entrySet()) {
                if (jsonHeader.containsKey(entry.getKey())) {
                    jsonHeader.replace(entry.getKey(), Arrays.asList(entry.getValue()));
                } else {
                    jsonHeader.add(entry.getKey(), entry.getValue());
                }
            }
        }

        HttpEntity<String> requestEntity = new HttpEntity<>(json, jsonHeader);
        ResponseEntity<T> responseEntity = rest.exchange(baseUrl + StringUtil.nvl(uri), HttpMethod.POST, requestEntity, responseType);

        this.setStatus(responseEntity.getStatusCode());
        // 控制log输出
        if (StringUtils.length(json) <= 1024 * 2) {
            log.debug("request--[{}][{}][{}]", baseUrl, uri, json);
        } else {
            log.debug("request--[{}][{}][********]", baseUrl, uri);
        }
        T response = responseEntity.getBody();
        log.debug("response--[{}]", JSON.toJSONString(response));
        return response;
    }

    public <T> T put(String baseUrl, String uri, String json, Class<T> responseType, Map<String, String> externalHeader) {
        if (externalHeader != null) {
            for (Map.Entry<String, String> entry : externalHeader.entrySet()) {
                if (jsonHeader.containsKey(entry.getKey())) {
                    jsonHeader.replace(entry.getKey(), Arrays.asList(entry.getValue()));
                } else {
                    jsonHeader.add(entry.getKey(), entry.getValue());
                }
            }
        }
        HttpEntity<String> requestEntity = new HttpEntity<>(json, jsonHeader);
        ResponseEntity<T> responseEntity = rest.exchange(baseUrl + StringUtil.nvl(uri), HttpMethod.PUT, requestEntity, responseType);

        this.setStatus(responseEntity.getStatusCode());
        // 控制log输出
        if (StringUtils.length(json) <= 1024 * 2) {
            log.debug("request--[{}][{}][{}]", baseUrl, uri, json);
        } else {
            log.debug("request--[{}][{}][********]", baseUrl, uri);
        }
        T response = responseEntity.getBody();
        log.debug("response--[{}]", JSON.toJSONString(response));
        return response;
    }

    public <T> T post(String baseUrl, String uri, Class<T> responseType) {
        return post(baseUrl, uri, (String) null, responseType);
    }

    public <T> T delete(String baseUrl, String uri, Class<T> type, Map<String, Object> args, Map<String, String> externalHeader) {

        if (externalHeader != null) {
            for (Map.Entry<String, String> entry : externalHeader.entrySet()) {
                if (jsonHeader.containsKey(entry.getKey())) {
                    jsonHeader.replace(entry.getKey(), Arrays.asList(entry.getValue()));
                } else {
                    jsonHeader.add(entry.getKey(), entry.getValue());
                }
            }
        }
        HttpEntity<String> requestEntity = new HttpEntity<>("", jsonHeader);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + uri);
        if (args != null) {
            for (Map.Entry<String, Object> entry : args.entrySet()) {
                builder.queryParam(entry.getKey(), entry.getValue());
            }
        }
        ResponseEntity<T> responseEntity = rest.exchange(builder.build().encode().toUri(), HttpMethod.DELETE, requestEntity, type);
        this.setStatus(responseEntity.getStatusCode());
        T response = responseEntity.getBody();
        log.debug("response--[{}]", JSON.toJSONString(response));
        return response;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setContentTypeReplacer(Map<String, String> replacer) {
        this.rest.setContentTypeReplacer(replacer);
    }

    private class TextHtmlGsonHttpMessageConverter extends GsonHttpMessageConverter {
        public TextHtmlGsonHttpMessageConverter() {
            List<MediaType> types = Arrays.asList(
                    new MediaType(MediaType.TEXT_HTML, DEFAULT_CHARSET),
                    new MediaType(MediaType.TEXT_PLAIN, DEFAULT_CHARSET),
                    new MediaType(MediaType.TEXT_XML, DEFAULT_CHARSET));
            super.setSupportedMediaTypes(types);
        }
    }

}
