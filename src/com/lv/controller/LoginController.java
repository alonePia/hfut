package com.lv.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.lv.commons.Commons;
import com.lv.entity.Users;
import com.lv.service.UsersService;
import com.lv.utils.JsonObj;

@Controller
@RequestMapping("/login.do")
public class LoginController {

	protected static final transient Log log = LogFactory
			.getLog(LoginController.class);

	Gson gson = new Gson();

	public LoginController() {
	}

	@Autowired
	private UsersService usersService;

	@RequestMapping
	public String load(ModelMap modelMap) {

		return "main";
	}

	/*
	 * @RequestMapping(params="method=dologin2") public String
	 * login2(HttpServletRequest request, ModelMap modelMap) { String
	 * userName=request.getParameter("userName"); String
	 * password=request.getParameter("password"); try{ Users
	 * user=usersService.get(userName, password); if(user!=null){
	 * modelMap.put("state", 1); modelMap.put("user", user);
	 * request.getSession().setAttribute(Commons.currentUser, user); return
	 * "main"; }else{ modelMap.put("state", 0); return "relogin"; }
	 * }catch(Exception e){ modelMap.put("state", 0); return "relogin"; } }
	 */

	@RequestMapping(params = "method=dologinOut")
	public String dologinOut(HttpServletRequest request, ModelMap modelMap) {
		request.getSession().removeAttribute(Commons.currentUser);
		return "login";
	}

	@RequestMapping(params = "method=dologin")
	@ResponseBody
	// 若要返回json数据 必须添加此注解
	public String login(HttpServletRequest request, ModelMap modelMap) {
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		JsonObj jsonObj = new JsonObj();
		try {
			Users user = usersService.get(userName, password);
			if (user != null) {
				jsonObj.setState(1);
				jsonObj.setMsg("登录成功！");
				jsonObj.setObj(user);
				request.getSession().setAttribute(Commons.currentUser, user);
			} else {
				jsonObj.setState(0);
				jsonObj.setMsg("登录失败！");
				jsonObj.setObj(null);
			}
		} catch (Exception e) {
			jsonObj.setState(0);
			jsonObj.setMsg("系统异常！");
			jsonObj.setObj(null);
		}
		// System.out.println(gson.toJson(jsonObj));
		return gson.toJson(jsonObj);
	}

}
