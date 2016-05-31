package com.lv.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

/**
 * 通过重写exposeHelpers方法，
 * 在spring里配置自己的freemarker的视图解析器，
 * 在模板中就可以通过${contextPath}获取项目绝对路径
 * @author lvliang
 *
 */
public class MyFreeMarkerView extends FreeMarkerView{

	private static final String CONTEXT_PATH = "contextPath";
	
	@Override
    protected void exposeHelpers(Map<String, Object> model,
            HttpServletRequest request) throws Exception {
        model.put(CONTEXT_PATH, request.getContextPath());
        super.exposeHelpers(model, request);
    }
	
}
