package com.lv.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.lv.commons.BhGenerator;
import com.lv.commons.Commons;
import com.lv.entity.Broke;
import com.lv.entity.Cars;
import com.lv.entity.Orders;
import com.lv.entity.OrderOthers;
import com.lv.entity.Repair;
import com.lv.entity.RepairOthers;
import com.lv.entity.Users;
import com.lv.service.BrokeService;
import com.lv.service.CarsService;
import com.lv.service.OrdersService;
import com.lv.service.RepairService;
import com.lv.service.UsersService;
import com.lv.utils.JsonObj;
import com.lv.utils.SQLHelper;
import com.mysql.jdbc.Connection;

@Controller
@RequestMapping("/sub.do")
public class SubController {
	protected final transient Log log = LogFactory
			.getLog(SubController.class);
	
	Gson gson = new Gson();

	@Autowired
	private UsersService usersService;
	@Autowired
	private CarsService carsService;
	@Autowired
	private OrdersService orderService;
	@Autowired
	private RepairService repairService;
	@Autowired
	private BrokeService brokeService;
	
	private int firstResult=Commons.firstResult;
	private int maxResults=Commons.maxResults;
	
	public SubController() {
	}

	/**--------------------------------------------------用户管理-----------------------------------------------------*/
	
	@RequestMapping
	public String load(HttpServletRequest request,ModelMap modelMap) {
		List<Object> list = usersService.getUsersLists();
		modelMap.put("userlist", list);
		return "userlist";
	}
	
	@RequestMapping(params="method=userlist")
	public String userlist(HttpServletRequest request,ModelMap modelMap) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		
		String username=request.getParameter("username");
		String address=request.getParameter("address");
		
		try{
			if(username!=null && !"".equals(username)){
				username=URLDecoder.decode(username,"utf-8");
				username=new String(username.getBytes("ISO-8859-1"),"UTF-8");
			}
			if(address!=null && !"".equals(address)){
				address=URLDecoder.decode(address,"utf-8");
				address=new String(address.getBytes("ISO-8859-1"),"UTF-8");
			}
		}catch (Exception e) { 
			log.error("userlist---->编码模式问题：", e);
		}
		
		
		
		//&firstResult=1000&maxResults=99999
		if(request.getParameter("firstResult")!=null && !"".equals(request.getParameter("firstResult"))){
			firstResult=Integer.parseInt(request.getParameter("firstResult"));
		}
		if(request.getParameter("maxResults")!=null && !"".equals(request.getParameter("maxResults"))){
			firstResult=Integer.parseInt(request.getParameter("maxResults"));
		}
		
		log.info("SubController----->userlist----->firstResult="+firstResult+".....maxResults="+maxResults);

		Map<String,String> map=null;
		if(username!=null && !username.equals("")){
			map=new HashMap<String,String>();
			map.put("accountNum", username);
		}
		if(address!=null && !address.equals("")){
			if(map==null){
				map=new HashMap<String,String>();
			}
			map.put("address", address);
		}
		
		List<Object> list=usersService.queryPageList("from "+Users.class.getSimpleName()+" a ",firstResult, maxResults,map);
		modelMap.put("userlist", list);
		// modelMap.put("totalCount", usersService.getTotalCount("from Users"));
		modelMap.put("pageSize", Commons.maxResults);
		return "userlist";
	}
	
	@RequestMapping(params = "method=saveUser")
	@ResponseBody
	// 若要返回json数据 必须添加此注解
	public String saveUser(HttpServletRequest request, ModelMap modelMap,Users user) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		JsonObj jsonObj = new JsonObj();
		try {
			if (user != null) {
				//判断用户账号是否存在
				Users onlyUser=usersService.getUniqueObj(user.getAccountNum());
				if(onlyUser!=null){
					jsonObj.setState(0);
					jsonObj.setMsg("用户已存在！");
					jsonObj.setObj(null);
					return gson.toJson(jsonObj);
				}
				user.setUserbh(BhGenerator.getBh().toString());
				user.setCzsj(new Date());
				usersService.save(user);
				jsonObj.setState(1);
				jsonObj.setMsg("保存成功！");
				jsonObj.setObj(user);
			} else {
				jsonObj.setState(0);
				jsonObj.setMsg("保存失败！");
				jsonObj.setObj(null);
			}
		} catch (Exception e) {
			jsonObj.setState(0);
			jsonObj.setMsg("保存失败！");
			jsonObj.setObj(null);
			log.error("saveUser", e);
		}
		return gson.toJson(jsonObj);
	}
	
	
	@RequestMapping(params = "method=editUser")
	@ResponseBody
	// 若要返回json数据 必须添加此注解
	public String editUser(HttpServletRequest request, ModelMap modelMap,Users user) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		JsonObj jsonObj = new JsonObj();
		try {
			if (user != null) {
				user.setCzsj(new Date());
				usersService.update(user);
				jsonObj.setState(1);
				jsonObj.setMsg("保存成功！");
				jsonObj.setObj(user);
			} else {
				jsonObj.setState(0);
				jsonObj.setMsg("保存失败！");
				jsonObj.setObj(null);
			}
		} catch (Exception e) {
			jsonObj.setState(0);
			jsonObj.setMsg("保存失败！");
			jsonObj.setObj(null);
			log.error("editUser", e);
		}
		return gson.toJson(jsonObj);
	}


	@RequestMapping(params = "method=delUser")
	@ResponseBody
	// 若要返回json数据 必须添加此注解
	public String delUser(HttpServletRequest request, ModelMap modelMap,String userbh) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		JsonObj jsonObj = new JsonObj();
		try {
			userbh=request.getParameter("userbh");
			if(userbh==null || "".equals(userbh)){
				jsonObj.setState(0);
				jsonObj.setMsg("请选择一条记录进行操作！");
				jsonObj.setObj(null);
				return gson.toJson(jsonObj);
			}
			Users user=this.usersService.getObj(userbh);
			if(user==null){
				jsonObj.setState(0);
				jsonObj.setMsg("当前用户已删除或不存在！");
				jsonObj.setObj(null);
				return gson.toJson(jsonObj);
			}
			usersService.delete(user);
			jsonObj.setState(1);
			jsonObj.setMsg("操作成功！");
			jsonObj.setObj(null);
		} catch (Exception e) {
			log.error("delUser", e);
			jsonObj.setMsg("操作失败！");
			jsonObj.setObj(null);
			jsonObj.setState(0);
		}
		return gson.toJson(jsonObj);
	}
	
	/**-----------------------------------------------车辆管理------------------------------------------------------*/
	
	@RequestMapping(params="method=carslist")
	public String carslist(HttpServletRequest request,ModelMap modelMap) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		
		String owner="";
		String carno="";
		try {
			request.setCharacterEncoding("UTF-8");
			
			carno=request.getParameter("carno");
			owner=request.getParameter("owner");
			if(carno!=null && !"".equals(carno)){
				carno=URLDecoder.decode(carno,"utf-8");
				carno=new String(carno.getBytes("ISO-8859-1"),"UTF-8");
			}
			if(owner!=null && !"".equals(owner)){
				owner=URLDecoder.decode(owner,"utf-8");
				owner=new String(owner.getBytes("ISO-8859-1"),"UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			log.error("carslist---->编码模式问题：", e);
		}
		
		if(request.getParameter("firstResult")!=null && !"".equals(request.getParameter("firstResult"))){
			firstResult=Integer.parseInt(request.getParameter("firstResult"));
		}
		if(request.getParameter("maxResults")!=null && !"".equals(request.getParameter("maxResults"))){
			firstResult=Integer.parseInt(request.getParameter("maxResults"));
		}

		Map<String,String> map=null;
		if(carno!=null && !carno.equals("")){
			map=new HashMap<String,String>();
			map.put("carno", carno);
		}
		if(owner!=null && !owner.equals("")){
			if(map==null){
				map=new HashMap<String,String>();
			}
			map.put("owner", owner);
		}
		
		List<Object> list=carsService.queryPageList("from "+Cars.class.getSimpleName()+" a ",firstResult, maxResults,map);
		modelMap.put("carslist", list);
		//modelMap.put("totalCount", carsService.getTotalCount("from Cars"));
		modelMap.put("pageSize", Commons.maxResults);
		return "carslist";
	}
	
	@RequestMapping(params = "method=saveCar")
	@ResponseBody
	// 若要返回json数据 必须添加此注解
	public String saveCar(HttpServletRequest request, ModelMap modelMap,Cars car) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		JsonObj jsonObj = new JsonObj();
		try {
			if (car != null) {
				car.setCarbh(BhGenerator.getBh().toString());
				car.setCzsj(new Date());
				carsService.save(car);
				jsonObj.setState(1);
				jsonObj.setMsg("保存成功！");
				jsonObj.setObj(car);
			} else {
				jsonObj.setState(0);
				jsonObj.setMsg("保存失败！");
				jsonObj.setObj(null);
			}
		} catch (Exception e) {
			jsonObj.setState(0);
			jsonObj.setMsg("保存失败！");
			jsonObj.setObj(null);
			log.error("saveCar", e);
		}
		return gson.toJson(jsonObj);
	}
	
	@RequestMapping(params = "method=editCar")
	@ResponseBody
	// 若要返回json数据 必须添加此注解
	public String editCar(HttpServletRequest request, ModelMap modelMap,Cars car) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		JsonObj jsonObj = new JsonObj();
		try {
			if (car != null) {
				car.setCzsj(new Date());
				carsService.update(car);
				jsonObj.setState(1);
				jsonObj.setMsg("保存成功！");
				jsonObj.setObj(car);
			} else {
				jsonObj.setState(0);
				jsonObj.setMsg("保存失败！");
				jsonObj.setObj(null);
			}
		} catch (Exception e) {
			jsonObj.setState(0);
			jsonObj.setMsg("保存失败！");
			jsonObj.setObj(null);
			log.error("editUser", e);
		}
		return gson.toJson(jsonObj);
	}
	
	
	@RequestMapping(params = "method=delCar")
	@ResponseBody
	// 若要返回json数据 必须添加此注解
	public String delCar(HttpServletRequest request, ModelMap modelMap,String carbh) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		JsonObj jsonObj = new JsonObj();
		try {
			carbh=request.getParameter("carbh");
			if(carbh==null || "".equals(carbh)){
				jsonObj.setState(0);
				jsonObj.setMsg("请选择一条记录进行操作！");
				jsonObj.setObj(null);
				return gson.toJson(jsonObj);
			}
			Cars car=this.carsService.getObj(carbh);
			if(car==null){
				jsonObj.setState(0);
				jsonObj.setMsg("当前用户已删除或不存在！");
				jsonObj.setObj(null);
				return gson.toJson(jsonObj);
			}
			carsService.delete(car);
			jsonObj.setState(1);
			jsonObj.setMsg("操作成功！");
			jsonObj.setObj(null);
		} catch (Exception e) {
			log.error("delcar", e);
			jsonObj.setMsg("操作失败！");
			jsonObj.setObj(null);
			jsonObj.setState(0);
		}
		return gson.toJson(jsonObj);
	}
	
	
	/**-----------------------------------------------申请信息------------------------------------------------------*/
	@RequestMapping(params="method=orderlist")
	public String orderlist(HttpServletRequest request,ModelMap modelMap) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		
		String owner=request.getParameter("owner");
		String carno=request.getParameter("carno");
		
		if(request.getParameter("firstResult")!=null && !"".equals(request.getParameter("firstResult"))){
			firstResult=Integer.parseInt(request.getParameter("firstResult"));
		}
		if(request.getParameter("maxResults")!=null && !"".equals(request.getParameter("maxResults"))){
			firstResult=Integer.parseInt(request.getParameter("maxResults"));
		}

		StringBuffer sql=new StringBuffer("select o.id,o.orderbh,u.username,c.`owner`,c.type,c.color,c.image,c.ownerphone,o.isback,o.startTime,o.endTime,o.czsj,c.carno from orders o LEFT JOIN users u on o.userbh=u.userbh LEFT JOIN cars c on o.carbh=c.carbh where 1=1 ");
		/*List<Object> list=orderService.queryPageList("from "+Orders.class.getSimpleName()+" o LEFT JOIN "+Users.class.getSimpleName()+" u ON o.userbh=u.userbh LEFT JOIN "+Cars.class.getSimpleName()+" c ON o.carbh=c.carbh ",firstResult, maxResults,map);*/
		List<Object> params=null;
		if(carno!=null && !carno.equals("")){
			try {
				carno=new String(carno.getBytes("ISO-8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			sql.append(" and carno like ?");
			if(params==null){
				params=new ArrayList<Object>();
			}
			params.add(carno);
		}
		if(owner!=null && !owner.equals("")){
			try {
				owner=new String(owner.getBytes("ISO-8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			sql.append(" and owner like ?");
			if(params==null){
				params=new ArrayList<Object>();
			}
			params.add(owner);
		}

		Connection conn=(Connection) SQLHelper.getConnection();
		ResultSet rs=SQLHelper.getResultSet(conn,sql.toString(),params);
		List<OrderOthers> list=SQLHelper.getOrderList(rs);
		try {
			rs.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		modelMap.put("orderslist", list);
		modelMap.put("pageSize", Commons.maxResults);
		return "orderlist";
	}
	
	@RequestMapping(params = "method=saveOrder")
	@ResponseBody
	// 若要返回json数据 必须添加此注解
	@Transactional
	public String saveOrder(HttpServletRequest request, ModelMap modelMap,Orders order) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		JsonObj jsonObj = new JsonObj();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		try {
			if (order != null) {
				order.setOrderbh(BhGenerator.getBh().toString());
				order.setCzsj(new Date());
				order.setStartTime(sdf.parse((request.getParameter("startTimes"))));
				order.setEndTime(sdf.parse((request.getParameter("endTimes"))));
				orderService.save(order);
				Cars car=carsService.getObj(order.getCarbh());
				car.setState(1);
				carsService.update(car);
				jsonObj.setState(1);
				jsonObj.setMsg("保存成功！");
				jsonObj.setObj(order);
			} else {
				jsonObj.setState(0);
				jsonObj.setMsg("保存失败！");
				jsonObj.setObj(null);
			}
		} catch (Exception e) {
			jsonObj.setState(0);
			jsonObj.setMsg("保存失败！");
			jsonObj.setObj(null);
			log.error("saveOrder", e);
		}
		return gson.toJson(jsonObj);
	}
	
	@RequestMapping(params = "method=editOrder")
	@ResponseBody
	// 若要返回json数据 必须添加此注解
	public String editOrder(HttpServletRequest request, ModelMap modelMap,Orders order) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		JsonObj jsonObj = new JsonObj();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		try {
			if (order != null) {
				order.setCzsj(new Date());
				order.setStartTime(sdf.parse((request.getParameter("startTimes"))));
				order.setEndTime(sdf.parse((request.getParameter("endTimes"))));
				orderService.update(order);
				if(order.getIsback()==0){
					Cars car=carsService.getObj(order.getCarbh());
					car.setState(0);
					carsService.update(car);
				}else if(order.getIsback()==1){
					Cars car=carsService.getObj(order.getCarbh());
					car.setState(1);
					carsService.update(car);
				}
				jsonObj.setState(1);
				jsonObj.setMsg("保存成功！");
				jsonObj.setObj(order);
			} else {
				jsonObj.setState(0);
				jsonObj.setMsg("保存失败！");
				jsonObj.setObj(null);
			}
		} catch (Exception e) {
			jsonObj.setState(0);
			jsonObj.setMsg("保存失败！");
			jsonObj.setObj(null);
			log.error("editOrder", e);
		}
		return gson.toJson(jsonObj);
	}
	
	
	@RequestMapping(params = "method=delOrder")
	@ResponseBody
	// 若要返回json数据 必须添加此注解
	public String delOrder(HttpServletRequest request, ModelMap modelMap,String orderbh) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		JsonObj jsonObj = new JsonObj();
		try {
			orderbh=request.getParameter("orderbh");
			if(orderbh==null || "".equals(orderbh)){
				jsonObj.setState(0);
				jsonObj.setMsg("请选择一条记录进行操作！");
				jsonObj.setObj(null);
				return gson.toJson(jsonObj);
			}
			Orders order=this.orderService.getObj(orderbh);
			if(order==null){
				jsonObj.setState(0);
				jsonObj.setMsg("当前记录已删除或不存在！");
				jsonObj.setObj(null);
				return gson.toJson(jsonObj);
			}
			orderService.delete(order);
			jsonObj.setState(1);
			jsonObj.setMsg("操作成功！");
			jsonObj.setObj(null);
		} catch (Exception e) {
			log.error("delcar", e);
			jsonObj.setMsg("操作失败！");
			jsonObj.setObj(null);
			jsonObj.setState(0);
		}
		return gson.toJson(jsonObj);
	}
	

	/**-----------------------------------------------事故查询------------------------------------------------------*/
	@RequestMapping(params="method=repairlist")
	public String repairlist(HttpServletRequest request,ModelMap modelMap) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		
		String carno=request.getParameter("carno");
		String owner=request.getParameter("owner");
		
		StringBuffer sql=new StringBuffer("select r.id,r.repairbh,r.carbh,r.state,r.startTime,r.endTime," +
				"r.describes,r.czsj,c.carno,c.color,c.type,c.`owner`,c.image " +
				"from `repair` r LEFT JOIN cars c ON r.carbh=c.carbh where 1=1 ");
		
		List<Object> params=null;
		if(carno!=null && !carno.equals("")){
			try {
				carno=new String(carno.getBytes("ISO-8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			sql.append(" and carno like ?");
			if(params==null){
				params=new ArrayList<Object>();
			}
			params.add(carno);
		}
		if(owner!=null && !owner.equals("")){
			try {
				owner=new String(owner.getBytes("ISO-8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			sql.append(" and owner like ?");
			if(params==null){
				params=new ArrayList<Object>();
			}
			params.add(owner);
		}

		Connection conn=(Connection) SQLHelper.getConnection();
		ResultSet rs=SQLHelper.getResultSet(conn,sql.toString(),params);
		List<RepairOthers> list=SQLHelper.getRepairList(rs);
		try {
			rs.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		modelMap.put("repairlist", list);
		modelMap.put("pageSize", Commons.maxResults);
		return "repairlist";
	}
	
	@RequestMapping(params = "method=saveRepair")
	@ResponseBody
	// 若要返回json数据 必须添加此注解
	@Transactional
	public String saveRepair(HttpServletRequest request, ModelMap modelMap,Repair repair) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		JsonObj jsonObj = new JsonObj();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		try {
			if (repair != null) {
				repair.setRepairbh(BhGenerator.getBh().toString());
				repair.setState(0);
				repair.setCzsj(new Date());
				repair.setStartTime(new Date());
				repair.setStartTime(sdf.parse((request.getParameter("startTimes"))));
				repair.setEndTime(sdf.parse((request.getParameter("endTimes"))));
				repairService.save(repair);
				jsonObj.setState(1);
				jsonObj.setMsg("保存成功！");
				jsonObj.setObj(repair);
			} else {
				jsonObj.setState(0);
				jsonObj.setMsg("保存失败！");
				jsonObj.setObj(null);
			}
		} catch (Exception e) {
			jsonObj.setState(0);
			jsonObj.setMsg("保存失败！");
			jsonObj.setObj(null);
			log.error("saveRepair", e);
		}
		return gson.toJson(jsonObj);
	}
	
	@RequestMapping(params = "method=editRepair")
	@ResponseBody
	// 若要返回json数据 必须添加此注解
	public String editRepair(HttpServletRequest request, ModelMap modelMap,Repair repair) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		JsonObj jsonObj = new JsonObj();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		try {
			if (repair != null) {
				repair.setCzsj(new Date());
				repair.setId(Integer.parseInt(request.getParameter("id")));
				repair.setRepairbh(request.getParameter("repairbh"));
				repair.setState(Integer.parseInt(request.getParameter("state")));
				repair.setCarbh(request.getParameter("carbh"));
				repair.setUserbh(request.getParameter("userbh"));
				repair.setStartTime(sdf.parse((request.getParameter("startTimes"))));
				repair.setEndTime(sdf.parse((request.getParameter("endTimes"))));
				repairService.update(repair);
				jsonObj.setState(1);
				jsonObj.setMsg("保存成功！");
				jsonObj.setObj(repair);
			} else {
				jsonObj.setState(0);
				jsonObj.setMsg("保存失败！");
				jsonObj.setObj(null);
			}
		} catch (Exception e) {
			jsonObj.setState(0);
			jsonObj.setMsg("保存失败！");
			jsonObj.setObj(null);
			log.error("editRepair", e);
		}
		return gson.toJson(jsonObj);
	}
	
	
	@RequestMapping(params = "method=delRepair")
	@ResponseBody
	// 若要返回json数据 必须添加此注解
	public String delRepair(HttpServletRequest request, ModelMap modelMap,String repairbh) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		JsonObj jsonObj = new JsonObj();
		try {
			repairbh=request.getParameter("repairbh");
			if(repairbh==null || "".equals(repairbh)){
				jsonObj.setState(0);
				jsonObj.setMsg("请选择一条记录进行操作！");
				jsonObj.setObj(null);
				return gson.toJson(jsonObj);
			}
			Repair repair=this.repairService.getUniqueObj(repairbh);
			if(repair==null){
				jsonObj.setState(0);
				jsonObj.setMsg("当前记录已删除或不存在！");
				jsonObj.setObj(null);
				return gson.toJson(jsonObj);
			}
			repairService.delete(repair);
			jsonObj.setState(1);
			jsonObj.setMsg("操作成功！");
			jsonObj.setObj(null);
		} catch (Exception e) {
			log.error("delRepair", e);
			jsonObj.setMsg("操作失败！");
			jsonObj.setObj(null);
			jsonObj.setState(0);
		}
		return gson.toJson(jsonObj);
	}
	

	/**-----------------------------------------------违章查询------------------------------------------------------*/
	
	@RequestMapping(params="method=brokelist")
	public String brokelist(HttpServletRequest request,ModelMap modelMap) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		
		String carno=request.getParameter("carno");
		String owner=request.getParameter("owner");
		
		if(carno!=null && !carno.equals("")){
			try {
				carno=new String(carno.getBytes("ISO-8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if(owner!=null && !owner.equals("")){
			try {
				owner=new String(owner.getBytes("ISO-8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		if(request.getParameter("firstResult")!=null && !"".equals(request.getParameter("firstResult"))){
			firstResult=Integer.parseInt(request.getParameter("firstResult"));
		}
		if(request.getParameter("maxResults")!=null && !"".equals(request.getParameter("maxResults"))){
			firstResult=Integer.parseInt(request.getParameter("maxResults"));
		}

		Map<String,String> map=null;
		if(carno!=null && !carno.equals("")){
			map=new HashMap<String,String>();
			map.put("carno", carno);
		}
		if(owner!=null && !owner.equals("")){
			if(map==null){
				map=new HashMap<String,String>();
			}
			map.put("owner", owner);
		}
		
		List<Object> list=brokeService.queryPageList("from "+Broke.class.getSimpleName()+" a ",firstResult, maxResults,map);
		modelMap.put("brokelist", list);
		modelMap.put("pageSize", Commons.maxResults);
		return "brokelist";
	}
	

	@RequestMapping(params = "method=saveBroke")
	@ResponseBody
	// 若要返回json数据 必须添加此注解
	@Transactional
	public String saveBroke(HttpServletRequest request, ModelMap modelMap,Broke broke) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		JsonObj jsonObj = new JsonObj();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (broke != null) {
				broke.setBrokebh(BhGenerator.getBh().toString());
				broke.setCzsj(new Date());
				broke.setBrokeTime(sdf.parse((request.getParameter("times"))));
				brokeService.save(broke);
				jsonObj.setState(1);
				jsonObj.setMsg("保存成功！");
				jsonObj.setObj(broke);
			} else {
				jsonObj.setState(0);
				jsonObj.setMsg("保存失败！");
				jsonObj.setObj(null);
			}
		} catch (Exception e) {
			jsonObj.setState(0);
			jsonObj.setMsg("保存失败！");
			jsonObj.setObj(null);
			log.error("saveBroke", e);
		}
		return gson.toJson(jsonObj);
	}
	
	@RequestMapping(params = "method=editBroke")
	@ResponseBody
	// 若要返回json数据 必须添加此注解
	public String editBroke(HttpServletRequest request, ModelMap modelMap,Broke broke) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		JsonObj jsonObj = new JsonObj();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		try {
			if (broke != null) {
				broke.setCzsj(new Date());
				broke.setBrokeTime(sdf.parse((request.getParameter("times"))));
				brokeService.update(broke);
				jsonObj.setState(1);
				jsonObj.setMsg("保存成功！");
				jsonObj.setObj(broke);
			} else {
				jsonObj.setState(0);
				jsonObj.setMsg("保存失败！");
				jsonObj.setObj(null);
			}
		} catch (Exception e) {
			jsonObj.setState(0);
			jsonObj.setMsg("保存失败！");
			jsonObj.setObj(null);
			log.error("editBroke", e);
		}
		return gson.toJson(jsonObj);
	}
	
	
	@RequestMapping(params = "method=delBroke")
	@ResponseBody
	// 若要返回json数据 必须添加此注解
	public String delBroke(HttpServletRequest request, ModelMap modelMap,String brokebh) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		JsonObj jsonObj = new JsonObj();
		try {
			brokebh=request.getParameter("repairbh");
			if(brokebh==null || "".equals(brokebh)){
				jsonObj.setState(0);
				jsonObj.setMsg("请选择一条记录进行操作！");
				jsonObj.setObj(null);
				return gson.toJson(jsonObj);
			}
			Broke broke=this.brokeService.getUniqueObj(brokebh);
			if(broke==null){
				jsonObj.setState(0);
				jsonObj.setMsg("当前记录已删除或不存在！");
				jsonObj.setObj(null);
				return gson.toJson(jsonObj);
			}
			brokeService.delete(broke);
			jsonObj.setState(1);
			jsonObj.setMsg("操作成功！");
			jsonObj.setObj(null);
		} catch (Exception e) {
			log.error("delBroke", e);
			jsonObj.setMsg("操作失败！");
			jsonObj.setObj(null);
			jsonObj.setState(0);
		}
		return gson.toJson(jsonObj);
	}
}
