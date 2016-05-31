package com.lv.controller;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.google.gson.Gson;
import com.lv.commons.Commons;

@Controller
@RequestMapping("/main.do")
public class MainController {

	protected static final transient Log log = LogFactory
			.getLog(MainController.class);
	
	Gson gson=new Gson();

	public MainController() {
	}

	@RequestMapping
	public String load(HttpServletRequest request,ModelMap modelMap) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		return "main";
	}
	
	@RequestMapping(params="method=leftmain")
	public String leftmain(HttpServletRequest request, ModelMap modelMap) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		return "leftmain";
	}

}
