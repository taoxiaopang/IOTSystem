package io.qcheng.zuulserver.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

@Component
public class TrackingFilter extends ZuulFilter {
	private static final Logger logger = LogManager.getLogger(TrackingFilter.class);
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
		return FilterUtils.PRE_FILTER_TYPE;
	}

	@Override
	public int filterOrder() {
		return FILTER_ORDER;
	}

	@Override
	public Object run() {
		if (filterUtils.getCorreclationId() != null) {
			logger.debug("tmx-correlation-id : {}", filterUtils.getCorreclationId());
		} else {
			filterUtils.setCorrelation(FilterUtils.generateCorrelationId());
		}

		logger.debug("Processing incoming request for {}",
				RequestContext.getCurrentContext().getRequest().getRequestURI());

		return null;
	}

}
