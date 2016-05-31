package com.lv.commons;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;

public class Commons {
	
	/**
	 * 系统当前登录用户
	 * 用于保存session
	 */
	public static final String currentUser="currentUser";
	
	/**
	 * 系统当前登录用户
	 * 用于freemarker显示于前台页面
	 */
	public static final String user="user";
	
	/**
	 * 系统默认分页索引
	 */
	public static int firstResult=0;//从当前数据往后查询
	
	/**
	 * 系统默认分页大小
	 */
	public static int maxResults=10;

	/**
	 * 判断用户是否登录
	 * 未登录将返回false 用于跳转login页面
	 * 已登录则保存session
	 * @param request
	 * @param modelMap
	 * @return
	 */
	public static final boolean isLogin(HttpServletRequest request,ModelMap modelMap){
		if(request.getSession().getAttribute(currentUser)==null){
			return false;
		}else{
			modelMap.put(user, request.getSession().getAttribute(currentUser));//获得当前登录用户
		}
		return true;
	}
	
}
