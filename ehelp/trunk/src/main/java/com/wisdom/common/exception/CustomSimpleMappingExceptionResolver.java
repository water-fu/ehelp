package com.wisdom.common.exception;

import com.wisdom.common.entity.ResultBean;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomSimpleMappingExceptionResolver extends SimpleMappingExceptionResolver {

	private static Logger logger = LoggerFactory
			.getLogger(CustomSimpleMappingExceptionResolver.class);

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {

		logger.error(ex.getMessage(), ex);

		String viewName = determineViewName(ex, request);

		if (viewName != null) {
			if (request.getHeader("x-requested-with") != null) {

				try {
					response.setContentType("text/html;charset=UTF-8");
					PrintWriter writer = response.getWriter();

					ResultBean resultBean = new ResultBean(false);

					if (ex instanceof ApplicationException) {
						resultBean.setMsg(ex.getMessage());
					} else {
						resultBean.setMsg("系统处理错误");
					}

					ObjectMapper objectMapper = new ObjectMapper();
					writer.write(objectMapper.writeValueAsString(resultBean));

					writer.flush();
				} catch (IOException e) {
					return getModelAndView(viewName, ex, request);
				}

				return null;
			} else {
				// Apply HTTP status code for error views, if specified.
				// Only apply it if we're processing a top-level request.
				Integer statusCode = determineStatusCode(request, viewName);
				if (statusCode != null) {
					applyStatusCodeIfPossible(request, response, statusCode);
				}

				ApplicationException exception = new ApplicationException(ex);

				return getModelAndView(viewName, exception, request);
			}
		} else {
			return null;
		}
	}
}
