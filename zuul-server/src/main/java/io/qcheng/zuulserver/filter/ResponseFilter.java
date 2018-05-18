package io.qcheng.zuulserver.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class ResponseFilter extends ZuulFilter {
	private static final Logger logger = LogManager.getLogger(ResponseFilter.class);
	private static final int FILTER_ORDER = 1;
	private static final boolean SHOULD_FILTER = true;
	
	@Autowired
	FilterUtils filterUtils;

	@Override
	public boolean shouldFilter() {
		return SHOULD_FILTER;
	}

	@Override
	public String filterType() {
		return FilterUtils.POST_FILTER_TYPE;
	}

	@Override
	public int filterOrder() {
		return FILTER_ORDER;
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();

		ctx.getResponse().setHeader(FilterUtils.CORRELATION_ID, FilterUtils.generateCorrelationId());

		logger.debug("Completing outgoing request for {}", ctx.getRequest().getRequestURI());

		return null;
	}

}
