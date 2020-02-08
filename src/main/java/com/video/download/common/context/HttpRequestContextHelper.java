package com.video.download.common.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * Global session manage
 * @author Xiaoming
 */
@Slf4j
public class HttpRequestContextHelper {

    /**
     *
     * Get current thread httpServletRequest
     */
    @NonNull
    public static HttpServletRequest getContextRequest(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        Assert.notNull(requestAttributes,"Get current thread requestAttributes example fail");

        return ((ServletRequestAttributes)requestAttributes).getRequest();
    }

    /**
     *
     * Get current thread httpServletRequest
     */
    @NonNull
    public static HttpServletResponse getContextResponse(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        Assert.notNull(requestAttributes,"Get current thread requestAttributes example fail");

        return ((ServletRequestAttributes)requestAttributes).getResponse();
    }

    /**
     * Get current thread request session id
     */
    @NonNull
    public static String getContextRequestId(){
        HttpServletRequest request = getContextRequest();
        return request.getSession().getId();
    }

    /**
     * Get current thread request header value by headerKey.
     */
    public static Optional<String> getRequestHeader(@NonNull String headerKey){
        HttpServletRequest request = getContextRequest();
        return Optional.ofNullable(request.getHeader(headerKey));
    }
}
