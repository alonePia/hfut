package com.lv.commons;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;


public class LoginFilter implements Filter {

	protected final transient Log log = LogFactory
			.getLog(LoginFilter.class);

	private String encoding;

	public LoginFilter() {

	}

	public void destroy() {
		log.info("LoginFilter---------销毁");
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		log.info("LoginFilter---------执行登录拦截");
		request.setCharacterEncoding(encoding);
		response.setCharacterEncoding(encoding);
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		HttpServletResponse resp = (HttpServletResponse) response;

		String url = req.getRequestURI();
		log.info("LoginFilter---------url="+url);
		
		if (url.indexOf("login.do") == -1) {//未登录
			if (session.getAttribute(Commons.currentUser) == null) {
				boolean isAjaxRequest = isAjaxRequest(req);
				if (isAjaxRequest) {
					resp.setCharacterEncoding(encoding);
					resp.sendError(HttpStatus.UNAUTHORIZED.value(),
							"您已经太长时间没有操作,请刷新页面");
					return;
				}else{
					session.removeAttribute(Commons.currentUser);
					log.info("session----------------"+session.getAttribute(Commons.currentUser));
					req.setCharacterEncoding("UTF-8");
					resp.setContentType("text/html; charset=UTF-8"); // 转码
					resp
							.getWriter()
							.println(
									"<script language=\"javascript\">if(window.opener==null){window.top.location.href=\""
											+ "login.html"
											+ "\";}else{window.opener.top.location.href=\""
											+ "login.html"
											+ "\";window.close();}</script>");
					return;
				}
			}
		}
		log.info("session----------------"+session.getAttribute(Commons.currentUser));
		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("LoginFilter---------初始化");
		encoding = "UTF-8";
	}

	/**
	 * 判断是否为Ajax请求
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return 是true, 否false
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		return request.getRequestURI().endsWith("ajax");
		// String requestType = request.getHeader("X-Requested-With");
		// return requestType != null && requestType.equals("XMLHttpRequest");
	}

}
