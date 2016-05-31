package com.lv.controller;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lv.commons.Commons;
import com.lv.entity.Broke;
import com.lv.entity.Cars;
import com.lv.entity.OrderOthers;
import com.lv.entity.Orders;
import com.lv.entity.Repair;
import com.lv.entity.RepairOthers;
import com.lv.entity.Users;
import com.lv.service.BrokeService;
import com.lv.service.CarsService;
import com.lv.service.OrdersService;
import com.lv.service.RepairService;
import com.lv.service.UsersService;
import com.lv.utils.SQLHelper;
import com.mysql.jdbc.Connection;

@Controller
@RequestMapping("/iframe.do")
public class IframeController {
	protected final transient Log log = LogFactory
			.getLog(IframeController.class);

	@Autowired
	private UsersService usersService;
	@Autowired
	private CarsService carsService;
	@Autowired
	private OrdersService ordersService;
	@Autowired
	private RepairService repairService;
	@Autowired
	private BrokeService brokeService;
	
	public IframeController() {
	}
	
	@RequestMapping(params="method=addUsers")
	public String userlist(HttpServletRequest request,ModelMap modelMap,String firstResult1) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		return "addUsers";
	}
	
	@RequestMapping(params="method=editUsers")
	public String editUsers(HttpServletRequest request,ModelMap modelMap,String userbh) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		userbh=request.getParameter("userbh");
		if(userbh==null || "".equals(userbh)){
			modelMap.put("msg", "请选择一条记录进行操作！");
			return "userlist";
		}
		Users user=this.usersService.getObj(userbh);
		if(user==null){
			modelMap.put("msg", "当前用户已删除或不存在！");
			return "userlist";
		}
		modelMap.put("userInfo", user);
		return "editUsers";
	}
	
	@RequestMapping(params="method=usersDetail")
	public String usersDetail(HttpServletRequest request,ModelMap modelMap,String userbh) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		userbh=request.getParameter("userbh");
		if(userbh==null || "".equals(userbh)){
			modelMap.put("msg", "请选择一条记录进行操作！");
			return "userlist";
		}
		Users user=this.usersService.getObj(userbh);
		if(user==null){
			modelMap.put("msg", "当前用户已删除或不存在！");
			return "userlist";
		}
		modelMap.put("userInfo", user);
		return "usersDetail";
	}
	
	@RequestMapping(params="method=addCars")
	public String addCars(HttpServletRequest request,ModelMap modelMap,String firstResult1) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		return "addCars";
	}
	
	@RequestMapping(params="method=editCar")
	public String editCar(HttpServletRequest request,ModelMap modelMap,String carbh) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		carbh=request.getParameter("carbh");
		if(carbh==null || "".equals(carbh)){
			modelMap.put("msg", "请选择一条记录进行操作！");
			return "carslist";
		}
		Cars car=this.carsService.getObj(carbh);
		if(car==null){
			modelMap.put("msg", "当前记录已删除或不存在！");
			return "carslist";
		}
		modelMap.put("carInfo", car);
		return "editCars";
	}
	
	@RequestMapping(params="method=carDetail")
	public String carDetail(HttpServletRequest request,ModelMap modelMap,String carbh) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		carbh=request.getParameter("carbh");
		if(carbh==null || "".equals(carbh)){
			modelMap.put("msg", "请选择一条记录进行操作！");
			return "carslist";
		}
		Cars car=this.carsService.getObj(carbh);
		if(car==null){
			modelMap.put("msg", "当前记录已删除或不存在！");
			return "carslist";
		}
		modelMap.put("carInfo", car);
		return "carDetail";
	}
	
	@RequestMapping(params="method=addOrder")
	public String addOrder(HttpServletRequest request,ModelMap modelMap) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		Users user=(Users) request.getSession().getAttribute(Commons.currentUser);
		modelMap.put("currentUser", user);
		modelMap.put("carList", carsService.getCarsLists());
		return "addOrder";
	}
	
	@RequestMapping(params="method=editOrder")
	public String editOrder(HttpServletRequest request,ModelMap modelMap,String orderbh) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		orderbh=request.getParameter("orderbh");
		if(orderbh==null || "".equals(orderbh)){
			modelMap.put("msg", "请选择一条记录进行操作！");
			return "orderlist";
		}
		Orders order=this.ordersService.getObj(orderbh);
		if(order==null){
			modelMap.put("msg", "当前记录已删除或不存在！");
			return "orderlist";
		}
		Users user=(Users) request.getSession().getAttribute(Commons.currentUser);
		modelMap.put("currentUser", user);
		modelMap.put("carList", carsService.getCarsLists());
		modelMap.put("orderInfo", order);
		return "editOrder";
	}
	
	@RequestMapping(params="method=orderDetail")
	public String orderDetail(HttpServletRequest request,ModelMap modelMap,String orderbh) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		orderbh=request.getParameter("orderbh");
		if(orderbh==null || "".equals(orderbh)){
			modelMap.put("msg", "请选择一条记录进行操作！");
			return "orderlist";
		}
		
		String sql="select o.id,o.orderbh,u.userbh,u.username,u.accountNum,u.sex,u.age,u.phone,u.address,u.sfz as usersfz,u.czsj as applyDate," +
				"c.carbh,c.`owner`,c.carno,c.type,c.color,c.image,c.ownerphone,c.ownersfz," +
				"o.isback,o.startTime,o.endTime,o.czsj,o.price " +
				"from orders o LEFT JOIN users u on o.userbh=u.userbh " +
				"LEFT JOIN cars c on o.carbh=c.carbh where orderbh='"+orderbh+"'";
		Connection conn=(Connection) SQLHelper.getConnection();
		ResultSet rs=SQLHelper.getResultSet(conn,sql.toString(),null);
		OrderOthers orderOther=SQLHelper.getOrderObj(rs);
		if(orderOther==null){
			modelMap.put("msg", "当前记录已删除或不存在！");
			return "orderlist";
		}
		try {
			rs.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		Users user=(Users) request.getSession().getAttribute(Commons.currentUser);
		modelMap.put("currentUser", user);
		modelMap.put("carList", carsService.getCarsLists());
		modelMap.put("orderInfo", orderOther);
		return "orderDetail";
	}
	
	@RequestMapping(params="method=addRepair")
	public String addRepair(HttpServletRequest request,ModelMap modelMap) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		modelMap.put("carList", carsService.getCarsLists());
		return "addRepair";
	}
	
	@RequestMapping(params="method=editRepair")
	public String editRepair(HttpServletRequest request,ModelMap modelMap,String repairbh) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		repairbh=request.getParameter("repairbh");
		if(repairbh==null || "".equals(repairbh)){
			modelMap.put("msg", "请选择一条记录进行操作！");
			return "repairlist";
		}
		Repair repair=this.repairService.getUniqueObj(repairbh);
		if(repair==null){
			modelMap.put("msg", "当前记录已删除或不存在！");
			return "repairlist";
		}
		modelMap.put("carList", carsService.getCarsLists());
		modelMap.put("repairInfo", repair);
		return "editRepair";
	}
	
	@RequestMapping(params="method=repairDetail")
	public String repairDetail(HttpServletRequest request,ModelMap modelMap,String repairbh) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		repairbh=request.getParameter("repairbh");
		if(repairbh==null || "".equals(repairbh)){
			modelMap.put("msg", "请选择一条记录进行操作！");
			return "repairlist";
		}
		
		Connection conn=(Connection) SQLHelper.getConnection();
		StringBuffer sql=new StringBuffer("select r.id,r.repairbh,r.carbh,r.state,r.startTime,r.endTime," +
				"r.describes,r.czsj,c.carno,c.color,c.type,c.`owner`,c.image " +
				"from `repair` r LEFT JOIN cars c ON r.carbh=c.carbh where repairbh='"+repairbh+"'");
		ResultSet rs=SQLHelper.getResultSet(conn,sql.toString(),null);
		RepairOthers repair=SQLHelper.getRepairObj(rs);
		if(repair==null){
			modelMap.put("msg", "当前记录已删除或不存在！");
			return "repairlist";
		}
		try {
			rs.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		modelMap.put("carList", carsService.getCarsLists());
		modelMap.put("repairInfo", repair);
		return "repairDetail";
	}

	@RequestMapping(params="method=addBroke")
	public String addBroke(HttpServletRequest request,ModelMap modelMap) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		modelMap.put("carList", carsService.getCarsLists());
		return "addBroke";
	}
	
	@RequestMapping(params="method=editBroke")
	public String editBroke(HttpServletRequest request,ModelMap modelMap,String brokebh) {
		if(!Commons.isLogin(request, modelMap)){
			return "login";
		}
		brokebh=request.getParameter("brokebh");
		if(brokebh==null || "".equals(brokebh)){
			modelMap.put("msg", "请选择一条记录进行操作！");
			return "brokelist";
		}
		Broke broke=this.brokeService.getUniqueObj(brokebh);
		if(broke==null){
			modelMap.put("msg", "当前记录已删除或不存在！");
			return "brokelist";
		}
		modelMap.put("carList", carsService.getCarsLists());
		modelMap.put("brokeInfo", broke);
		return "editBroke";
	}
}
