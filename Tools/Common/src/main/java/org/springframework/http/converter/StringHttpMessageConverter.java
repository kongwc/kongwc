//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.http.converter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;

public class StringHttpMessageConverter extends AbstractHttpMessageConverter<String> {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private final List<Charset> availableCharsets;
    private boolean writeAcceptCharset;

    public StringHttpMessageConverter() {
        this(DEFAULT_CHARSET);
    }

    public StringHttpMessageConverter(Charset defaultCharset) {
        super(defaultCharset, new MediaType[]{MediaType.TEXT_PLAIN, MediaType.ALL});
        this.writeAcceptCharset = false;
        this.availableCharsets = new ArrayList(Charset.availableCharsets().values());
    }

    public void setWriteAcceptCharset(boolean writeAcceptCharset) {
        this.writeAcceptCharset = writeAcceptCharset;
    }

    public boolean supports(Class<?> clazz) {
        return String.class == clazz;
    }

    protected String readInternal(Class<? extends String> clazz, HttpInputMessage inputMessage) throws IOException {
        Charset charset = this.getContentTypeCharset(inputMessage.getHeaders().getContentType());
        return StreamUtils.copyToString(inputMessage.getBody(), charset);
    }

    protected Long getContentLength(String str, MediaType contentType) {
        Charset charset = this.getContentTypeCharset(contentType);

        try {
            return Long.valueOf((long)str.getBytes(charset.name()).length);
        } catch (UnsupportedEncodingException var5) {
            throw new IllegalStateException(var5);
        }
    }

    protected void writeInternal(String str, HttpOutputMessage outputMessage) throws IOException {
        if(this.writeAcceptCharset) {
            outputMessage.getHeaders().setAcceptCharset(this.getAcceptedCharsets());
        }

        Charset charset = this.getContentTypeCharset(outputMessage.getHeaders().getContentType());
        StreamUtils.copy(str, charset, outputMessage.getBody());
    }

    protected List<Charset> getAcceptedCharsets() {
        return this.availableCharsets;
    }

    private Charset getContentTypeCharset(MediaType contentType) {
        return contentType != null && contentType.getCharset() != null?contentType.getCharset():this.getDefaultCharset();
    }
}
