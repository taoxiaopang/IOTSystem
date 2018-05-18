package io.qcheng.zuulserver.filter;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Component;

import com.netflix.zuul.context.RequestContext;

@Component
public class FilterUtils {
	public static final String CORRELATION_ID = "tmx-correlation-id";
	public static final String AUTH_TOKEN = "tmx-auth-token";
	public static final String USER_ID = "tmx-user-id";

	public static final String PRE_FILTER_TYPE = "pre";
	public static final String POST_FILTER_TYPE = "post";
	public static final String ROUTE_FILTER_TYPE = "route";

	/*
	 * Zuul doesn’t allow you to directly add or modify the HTTP request headers on
	 * an incoming request. If we add the tmx-correlation-id and then try to access
	 * it again later in the filter, it won’t be available as part of the
	 * ctx.getRequestHeader() call.
	 */
	public String getCorreclationId() {
		RequestContext ctx = RequestContext.getCurrentContext();

		String correlationId = ctx.getRequest().getHeader(CORRELATION_ID);
		if (correlationId != null) {
			return correlationId;
		} else {
			return ctx.getZuulRequestHeaders().get(CORRELATION_ID);
		}
	}

	public void setCorrelation(String correlationId) {
		RequestContext ctx = RequestContext.getCurrentContext();
		ctx.addZuulRequestHeader(CORRELATION_ID, correlationId);
	}

	public static String generateCorrelationId() {
		int length = 20;
		boolean useLetters = true;
		boolean useNumbers = true;

		return RandomStringUtils.random(length, useLetters, useNumbers);
	}

}
