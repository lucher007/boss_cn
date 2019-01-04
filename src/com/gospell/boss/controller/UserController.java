package com.gospell.boss.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gospell.boss.common.AesSecret;
import com.gospell.boss.common.ConfigUtil;
import com.gospell.boss.common.Tools;
import com.gospell.boss.dao.IAreaDao;
import com.gospell.boss.dao.IBusinesstypeDao;
import com.gospell.boss.dao.ICardDao;
import com.gospell.boss.dao.IDispatchDao;
import com.gospell.boss.dao.IGiftcardDao;
import com.gospell.boss.dao.INetworkDao;
import com.gospell.boss.dao.IPrinttemplateDao;
import com.gospell.boss.dao.IProblemcomplaintDao;
import com.gospell.boss.dao.IProductDao;
import com.gospell.boss.dao.IProviderDao;
import com.gospell.boss.dao.IServerDao;
import com.gospell.boss.dao.IStbDao;
import com.gospell.boss.dao.ISystemparaDao;
import com.gospell.boss.dao.IUserDao;
import com.gospell.boss.dao.IUseraccountlogDao;
import com.gospell.boss.dao.IUserbusinessDao;
import com.gospell.boss.dao.IUserbusinessdetailDao;
import com.gospell.boss.dao.IUsercardDao;
import com.gospell.boss.dao.IUserlevelDao;
import com.gospell.boss.dao.IUserlevelproductDao;
import com.gospell.boss.dao.IUserproductDao;
import com.gospell.boss.dao.IUserstbDao;
import com.gospell.boss.po.Area;
import com.gospell.boss.po.Businesstype;
import com.gospell.boss.po.Card;
import com.gospell.boss.po.Constant;
import com.gospell.boss.po.Dispatch;
import com.gospell.boss.po.Giftcard;
import com.gospell.boss.po.Network;
import com.gospell.boss.po.Operator;
import com.gospell.boss.po.Printtemplate;
import com.gospell.boss.po.Problemcomplaint;
import com.gospell.boss.po.Product;
import com.gospell.boss.po.Server;
import com.gospell.boss.po.Stb;
import com.gospell.boss.po.Systempara;
import com.gospell.boss.po.User;
import com.gospell.boss.po.Useraccountlog;
import com.gospell.boss.po.Userbusiness;
import com.gospell.boss.po.Userbusinessdetail;
import com.gospell.boss.po.Usercard;
import com.gospell.boss.po.Userlevel;
import com.gospell.boss.po.Userlevelproduct;
import com.gospell.boss.po.Userproduct;
import com.gospell.boss.po.Userstb;
import com.gospell.boss.service.IAuthorizeService;
import com.gospell.boss.service.ICardService;
import com.gospell.boss.service.IStbService;
import com.gospell.boss.service.ISystemparaService;
import com.gospell.boss.service.IUserproductService;


/**
 * 用户控制层
 */
@Controller
@Scope("prototype")
@RequestMapping("/user")
@Transactional
public class UserController extends BaseController{
	
	@Autowired 
	private ServletContext servletContext;
	@Autowired
	private IUserDao userDao; 
	@Autowired
	private INetworkDao networkDao; 
	@Autowired
	private IServerDao serverDao; 
	@Autowired
	private IAreaDao areaDao; 
	@Autowired
	private IUserstbDao userstbDao; 
	@Autowired
	private IUsercardDao usercardDao; 
	@Autowired
	private IUserproductDao userproductDao; 
	@Autowired
	private IStbDao stbDao; 
	@Autowired
	private IStbService stbService; 
	@Autowired
	private ICardDao cardDao;
	@Autowired
	private ICardService cardService;
	@Autowired
	private IProductDao productDao;
	@Autowired
	private IProviderDao providerDao; 
	@Autowired
	private IUserbusinessDao userbusinessDao; 
	@Autowired
	private IUserbusinessdetailDao userbusinessdetailDao; 
	@Autowired
	private IBusinesstypeDao businesstypeDao; 
	@Autowired
	private IProblemcomplaintDao problemcomplaintDao; 
	@Autowired
	private ISystemparaDao systemparaDao;
	@Autowired
	private IAuthorizeService authorizeService;
	@Autowired
	private IPrinttemplateDao printtemplateDao;
	@Autowired
	private IDispatchDao dispatchDao;
	@Autowired
	private IUseraccountlogDao useraccountlogDao;
	@Autowired
	private IGiftcardDao giftcardDao;
	@Autowired
	private ISystemparaService systemparaService;
	@Autowired
	private IUserlevelDao userlevelDao;
	@Autowired
	private IUserlevelproductDao userlevelproductDao;
	@Autowired
	private IUserproductService userproductService;
	/*
     * 特殊的instance变量
     * 注：零长度的byte数组对象创建起来将比任何对象都经济――查看编译后的字节码：
     * 生成零长度的byte[]对象只需3条操作码，而Object lock = new Object()则需要7行操作码。
     */
    private static byte[] lock = new byte[0];    
	
	/**
	 * 查询用户信息
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/findByList")
	public String findByList(User form) {
		form.setPager_openset(Integer.valueOf(servletContext.getInitParameter("pager_openset")));
		form.setPager_count(userDao.findByCount(form));
		List<User> userlist = userDao.findByList(form);
		for (User user : userlist) {
			user.setNetwork(networkDao.findById(user.getNetid()));
		}
		form.setUserlist(userlist);
		// 构建上级网络Map对象
		List<Network> list = networkDao.queryByList(new Network());
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Network network = (Network) iterator.next();
			map.put(network.getId(), network.getNetname());
		}
		form.setNetworkmap(map);
		return "user/findUserList";
	}
	
	@ResponseBody
	@RequestMapping(value = "/queryUserlist")
	public Map<String, Object> queryUserlist(User form,int rows,int page) {
		form.setPager_offset(rows*(page-1));
		form.setPager_openset(rows);
		Integer total = 0;
		List<User> users=new ArrayList<User>();
		
		if(StringUtils.isEmpty(form.getQuerycardid())){
			total = userDao.countByUserinfo(form);
			users= userDao.findByUserinfo(form);
		}else{
			Usercard usercard_finder = new Usercard();
			usercard_finder.setQuerynetid(form.getQuerynetid());
			usercard_finder.setQueryareacode(form.getQueryareacode());
			usercard_finder.setQuerycardid(form.getQuerycardid());
			usercard_finder.setQueryusertype(form.getQueryusertype());
			usercard_finder.setPager_offset(rows*(page-1));
			usercard_finder.setPager_openset(rows);
			total = usercardDao.findByCountForUserquery(usercard_finder);
			List<Usercard> usercards = usercardDao.findByListForUserquery(usercard_finder);
			for(Usercard usercard : usercards){
				User user = userDao.findById(usercard.getUserid());
				if(user !=null){
					users.add(user);
				}
			}
		}
	/*	Integer total = userDao.findByCount(form);
		List<User> users = userDao.findByList(form);*/
		
		
		Map<String, Object> json = new HashMap<String, Object>(); 
		List<HashMap<String, Object>> userlist = new ArrayList<HashMap<String, Object>>();
		for (User user : users) {
			HashMap<String, Object> userMap = new HashMap<String, Object>();
			userMap.put("id", user.getId());
			Network network = networkDao.findById(user.getNetid());
			userMap.put("netname", network!=null ? network.getNetname() : "");
			Area areaForm = new Area();
			areaForm.setNetid(user.getNetid());
			areaForm.setAreacode(user.getAreacode());
			Area area = areaDao.findByAreacode(areaForm);
			userMap.put("areacode", user.getAreacode());
			userMap.put("areaname", area!=null ? area.getAreaname() : "");
			userMap.put("usercode", user.getUsercode());  
			userMap.put("username", user.getUsername());
			userMap.put("telephone", user.getTelephone());
			userMap.put("state", user.getState());
			userMap.put("address", user.getAddress());
			userlist.add(userMap);
		}
		json.put("total", total);
		json.put("rows", userlist);
		return json;
	}

	
	
	/**
	 * 添加用户信息初始化
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/addInit")
	public String addInit(User form) {
		// 构建上级网络Map对象
		List<Network> netlist = networkDao.queryByList(new Network());
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (Iterator iterator = netlist.iterator(); iterator.hasNext();) {
			Network network = (Network) iterator.next();
			map.put(network.getId(), network.getNetname());
		}
		form.setNetworkmap(map);
		
		return "user/addUser";
	}
	
	/**
	 * 新增
	 */
	@RequestMapping(value="/save")
	public String save(User form){
		
		Operator operator = (Operator)getSession().getAttribute("Operator");
		

		
		//state：0：注销;1：正常;2-报停
		form.setState("1");
		form.setScore(new BigDecimal(0));
		form.setAccount(new BigDecimal(0));
		
		//如果页面没有输入订户登陆手机APP的密码，系统默认初始化密码
		if(form.getPassword() == null || "".equals(form.getPassword())){
			form.setPassword(ConfigUtil.getConfigFilePath("app_password","system.properties"));
		}
		
		//如果页面没有输入订户账户余额支付密码，系统默认初始化密码
		if(form.getPaypassword() == null || "".equals(form.getPaypassword())){
			form.setPaypassword(ConfigUtil.getConfigFilePath("app_paypassword","system.properties"));
		}
		
		Integer addFlag = userDao.save(form);
		
		//保存业务记录
		if(form.getId() != null){
			
			//取出开户费
			Businesstype typeform = new Businesstype();
			typeform.setTypekey("adduser");
			Businesstype businesstype = businesstypeDao.findByTypekey(typeform);
            if(businesstype == null ){
            	businesstype = typeform;
            }
            
			//封装业务主表
			Userbusiness userbusiness = new Userbusiness();
			userbusiness.setNetid(form.getNetid());
			userbusiness.setOperatorid(operator.getId());
			userbusiness.setUserid(form.getId());
			userbusiness.setStoreid(operator.getStoreid());
			userbusiness.setAreacode(form.getAreacode());
			
			userbusiness.setTotalmoney(businesstype.getPrice()==null?new BigDecimal(0):businesstype.getPrice());
			userbusiness.setShouldmoney(businesstype.getPrice()==null?new BigDecimal(0):businesstype.getPrice());
			userbusiness.setPaymoney(businesstype.getPrice()==null?new BigDecimal(0):businesstype.getPrice());
			
			userbusiness.setAccountmoney(new BigDecimal(0));
			
			userbusiness.setSource("0");
			userbusiness.setLogout("0");
			userbusiness.setPaytype("0");
			userbusiness.setAddtime(Tools.getCurrentTime());
			
			userbusinessDao.save(userbusiness);
			
			//封装业务明细
			Userbusinessdetail businessdetail = new Userbusinessdetail();
			businessdetail.setBusinessid(userbusiness.getId());
			businessdetail.setNetid(form.getNetid());
			businessdetail.setOperatorid(operator.getId());
			businessdetail.setUserid(form.getId());
			businessdetail.setAreacode(form.getAreacode());
			businessdetail.setStoreid(operator.getStoreid());
			businessdetail.setBusinesstypekey("adduser");
			businessdetail.setBusinesstypename(getMessage("business.type.adduser"));
			businessdetail.setContent(getMessage("user.usercode")+":"+form.getUsercode()+";"+getMessage("user.username")+":"+form.getUsername());
			businessdetail.setPrice(userbusiness.getTotalmoney()); 
			businessdetail.setTotalmoney(userbusiness.getTotalmoney());
			businessdetail.setAddtime(Tools.getCurrentTime());
			businessdetail.setLogout("0");
			businessdetail.setSource("0");
			userbusinessdetailDao.save(businessdetail);
		}
		
		
		
		//将新增加的订户信息封装到session中
		if(addFlag == 1 && form.getId() != null ){//将user放入到session当中
			operator.setUser(form);
			getRequest().getSession().setAttribute("Operator", operator);
		}
		
		form.setReturninfo(getMessage("page.execution.success"));
		return addInit(form);
	}
	
	/**
	 * 更新初始化
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value="/updateInit")
	public String updateInit(User form){
		
		form.setUser(userDao.findById(form.getId()));
		
		// 构建上级网络Map对象
		List<Network> netlist = networkDao.queryByList(new Network());
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (Iterator iterator = netlist.iterator(); iterator.hasNext();) {
			Network network = (Network) iterator.next();
			map.put(network.getId(), network.getNetname());
		}
		form.setNetworkmap(map);
		
		return "user/updateUser";
	}
	
	/**
	 * 更新
	 */
	@RequestMapping(value="/update")
	public String update(User form){
//		if ("".equals(form.getUsercode())) {
//			form.setReturninfo(getMessage("user.userid.empty"));
//			return updateInit(form);
//		} else {
//			User oldUser = userDao.findByUsercode(form);
//			if (oldUser != null && !oldUser.getId().equals(form.getId())) {
//				form.setReturninfo(getMessage("user.userid.existed"));
//				return updateInit(form);
//			}
//		}
		
		//如果页面没有输入订户登陆手机APP的密码，系统默认初始化密码
		if(form.getPassword() == null || "".equals(form.getPassword())){
			form.setPassword(ConfigUtil.getConfigFilePath("app_password","system.properties"));
		}
		
		//如果页面没有输入订户登陆手机APP的密码，系统默认初始化密码
		if(form.getPaypassword() == null || "".equals(form.getPaypassword())){
			form.setPaypassword(ConfigUtil.getConfigFilePath("app_paypassword","system.properties"));
		}
		
        //修改网络信息
		userDao.update(form);
		
		form.setReturninfo(getMessage("page.execution.success"));
		return businessUnit(form);
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value="/delete")
	public String delete(User form) {
        //删除user
		userDao.delete(form.getId());
		
		form.setReturninfo(getMessage("page.execution.success"));
		return findByList(form);
	}
	
	/**
	 * 批量激活
	 */
	@RequestMapping(value="/active")
	public String activeUser(User form,HttpServletRequest request)  {
		String[] idArray = request.getParameterValues("ids");
		
		if(idArray ==null || idArray.length < 1){
			form.setReturninfo(getMessage("page.select.empty"));
		}else{
			for (int i=0;i<idArray.length;i++) {
				User user = new User();
				user.setId(Integer.parseInt(idArray[i]));
				user.setState("1");//激活
				Integer deleteflag = userDao.activeUser(user);
			}
			form.setReturninfo(getMessage("page.execution.success"));
		}
		return findByList(form);
	}
	
	/**
	 * 查询用户信息
	 */
	@RequestMapping(value="/businessUnit")
	public String businessUnit(User form) {
		
		//从内存中读取用户信息
		Operator operator = (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		
		if(user == null || user.getId() == null || "1".equals(user.getUsertype())){//未初始化用户，进入查询界面
			// 构建上级网络Map对象
			List<Network> list = networkDao.queryByList(new Network());
			Map<Integer, String> map = new HashMap<Integer, String>();
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Network network = (Network) iterator.next();
				map.put(network.getId(), network.getNetname());
			}
			form.setNetworkmap(map);
			return "user/queryUser";
		} else {
			if("queryUser".equals(form.getBusinesstype())){
				// 构建上级网络Map对象
				List<Network> list = networkDao.queryByList(new Network());
				Map<Integer, String> map = new HashMap<Integer, String>();
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					Network network = (Network) iterator.next();
					map.put(network.getId(), network.getNetname());
				}
				form.setNetworkmap(map);
				return "user/queryUser";
			}else{
				//取session中的订户信息
				//form.setUser(user);
				//return userBusinessDispath(form);//进入到跳转页面
				
				//重新查询订户信息
				form.setId(user.getId());
				return lookUserBusinessInfo(form);
			}
		}
	}
	
	/**
	 * 查询个人业务信息
	 * @return
	 */
	@RequestMapping(value="/lookUserBusinessInfo")
	public String lookUserBusinessInfo(User form) {
		
		User user =  userDao.findById(form.getId());
		
		//封装网络信息
		Network network = networkDao.findById(user.getNetid());
		user.setNetwork(network);
		//封装区域信息
		Area area = new Area();
		area.setNetid(user.getNetid());
		area.setAreacode(user.getAreacode());
		area = areaDao.findByAreacode(area);
		user.setArea(area);
		//封装机顶盒信息
		Userstb userstb = new Userstb();
		userstb.setUserid(user.getId());
		List<Userstb> userstblist = userstbDao.queryByList(userstb);
		//赋值机顶盒是否购买了智能卡
		for (Userstb userstb2 : userstblist) {
			Usercard usercard = usercardDao.findByStbnoStr(userstb2.getStbno());
			if(usercard != null){
				userstb2.setBuycardflag("1");
			}else{
				userstb2.setBuycardflag("0");
			}
		}
		user.setUserstblist(userstblist);
		
		//封装智能卡信息
		Usercard usercard = new Usercard();
		usercard.setUserid(user.getId());
		List<Usercard> usercardlist = usercardDao.queryByList(usercard);
		user.setUsercardlist(usercardlist);
		
		//查询参数设置-是否发送机卡配对指令
		String send_stbcardpair_flag = systemparaService.findSystemParaByCodeStr("send_stbcardpair_flag");
		user.setStbcardpairflag(send_stbcardpair_flag);
		//是否打印纳税人识别号和纳税人名称:1 打印 
		String systempara = systemparaService.findSystemParaByCodeStr("print_taxpayer_flag");
		user.setPrinttaxpayerflag(systempara);
		//查询订户所有的授权的产品信息
		Userproduct userproduct = new Userproduct();
		userproduct.setUserid(user.getId());
		userproduct.setEndtime(Tools.getCurrentTimeByFormat("yyyy-MM-dd"));
		List<Userproduct> userproductlist = userproductDao.queryByList(userproduct);
		user.setUserproductlist(userproductlist);
		
		Userlevel userlevel = userlevelDao.findById(user.getUserlevelid());
		if(userlevel == null){
			userlevel = new Userlevel();
		}
		user.setUserlevel(userlevel);
		
		//封装订户信息
		form.setUser(user);
		
		Operator operator = (Operator)getSession().getAttribute("Operator");
		if(user != null ){//将user放入到session当中
			operator.setUser(user);
			getSession().setAttribute("Operator", operator);
		}
		
		
		return userBusinessDispath(form);
	}
	
	/**
	 * 查询用户信息
	 */
	@RequestMapping(value="/userBusinessDispath")
	public String userBusinessDispath(User form) {
		if("queryUser".equals(form.getBusinesstype())||"businessInfo".equals(form.getBusinesstype())){//用户查询
			return "user/userBusinessInfo";
		}else if("buyDevice".equals(form.getBusinesstype())){//购买设备
			return "user/buyDevice";
		}else if("buyProduct".equals(form.getBusinesstype())){//购买产品
			return "user/buyProduct";
		}else if("stopAndOn".equals(form.getBusinesstype())){//停开机
			return "user/stopAndOn";
		}else if("transferUser".equals(form.getBusinesstype())){//过户
			return "user/transferUser";
		}else if("cancelUser".equals(form.getBusinesstype())){//销户
			return "user/cancelUser";
		}else if("transferAddress".equals(form.getBusinesstype())){//迁移
			return "user/transferAddress";
		}else if("replaceStb".equals(form.getBusinesstype())){//更换机顶盒
			return "user/replaceStb";
		}else if("replaceCard".equals(form.getBusinesstype())){//更换智能卡
			return "user/replaceCard";
		}else if("fillcard".equals(form.getBusinesstype())){//订户补卡
			return "user/fillCard";
		}else if("rechargeAccount".equals(form.getBusinesstype())){//账户充值
			return "user/rechargeAccount";
		}else if("feecollection".equals(form.getBusinesstype())){//其他费用收取
			return "user/feecollection";
		}else if("giftcardrecharge".equals(form.getBusinesstype())){//礼品卡充值
			return "user/rechargeGiftcard";
		}else if("rechargeWallet".equals(form.getBusinesstype())){//电子钱包充值
			//查询货币转换点数值
			Systempara para = new Systempara();
			para.setCode("CURRENCY_CONVERSION_DENOMINATOR");
			para = systemparaDao.findByCode(para);
			if(para == null || StringUtils.isEmpty(para.getValue())){//如果该参数没设置
				form.setReturninfo(getMessage("systempara.code.currency_conversion_denominator.empty"));
				getRequest().setAttribute("CURRENCY_CONVERSION_DENOMINATOR", "0");
			}else{
				getRequest().setAttribute("CURRENCY_CONVERSION_DENOMINATOR", para.getValue());
			}
			return "user/rechargeWallet";
		}else if("updateUser".equals(form.getBusinesstype())){//订户资料修改
			return "user/updateUser";
		}else if("problemcomplaint".equals(form.getBusinesstype())){//问题投诉
			return "user/addProblemcomplaint";
		}else if("authorizeRefresh".equals(form.getBusinesstype())){//授权刷新
			return "user/authorizeRefresh";
		}else if("queryBusiness".equals(form.getBusinesstype())){//订户办理业务查询
			return "user/findBusinessList";
		}else if("invoicePrint".equals(form.getBusinesstype())){//发票打印
			return "user/findInvoicePrintList";
		}else if("cancelproduct".equals(form.getBusinesstype())){//产品退订
			return "user/cancelUserproduct";
		}else if("rebackdevice".equals(form.getBusinesstype())){//设备回收
			return "user/rebackDevice";
		}else if("resetbinding".equals(form.getBusinesstype())){//重置机卡绑定关系
			return "user/resetbinding";
		}else if("removebinding".equals(form.getBusinesstype())){//解除机卡绑定关系
			return "user/removebinding";
		}else if("accountlog".equals(form.getBusinesstype())){//账户出入账记录查询
			return "user/findAccountlogList";
		}else{
			return "user/queryUser";
		}
	}
	
	
	/**
	 * 封装业务信息初始化
	 */
	@RequestMapping(value="/unitBusiness")
	public String unitBusiness(Userbusiness form) {
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		
		//临时变量，存放总金额
		BigDecimal totalmoney = new BigDecimal(0);
		//业务主表
		Userbusiness userbusiness = new Userbusiness();
		//业务明细表
		List<Userbusinessdetail> buyingbusinessdetaillist = new ArrayList<Userbusinessdetail>();
		
		//购买设备或者购买产品业务
		if("buyDevice".equals(form.getBusinesstype()) || "buyProduct".equals(form.getBusinesstype())){
			//机顶盒费用
			List<Userstb> buyingstblist = user.getBuyingstblist();
			for (Userstb userstb : buyingstblist) {
				Userbusinessdetail businessdetail = new Userbusinessdetail();
				businessdetail.setNetid(user.getNetid());
				businessdetail.setOperatorid(operator.getId());
				businessdetail.setAreacode(user.getAreacode());
				businessdetail.setUserid(user.getId());
				businessdetail.setStoreid(operator.getStoreid());
				businessdetail.setTerminalid(userstb.getStbno());
				businessdetail.setTerminaltype("0");
				businessdetail.setStbno(userstb.getStbno());
				businessdetail.setCardid("");
				businessdetail.setBusinesstypekey("buystb");
				Businesstype businesstype  = businesstypeDao.findByTypekeyStr("buystb");
				if(businesstype == null){
					businesstype = new Businesstype();
				}
				businessdetail.setBusinesstypename(businesstype.getTypename());
				businessdetail.setFeename(businesstype.getFeename());
				businessdetail.setContent(userstb.getStbno());
				businessdetail.setPrice(userstb.getPrice());
				businessdetail.setTotalmoney(userstb.getPrice());
				totalmoney = totalmoney.add(businessdetail.getTotalmoney()==null?new BigDecimal("0"):businessdetail.getTotalmoney()); //累加机顶盒的费用
				businessdetail.setAddtime(Tools.getCurrentTime());
				businessdetail.setLogout("0");
				businessdetail.setSource("0");
				buyingbusinessdetaillist.add(businessdetail);//封装业务明细
			}
			
			//智能卡费用
			List<Usercard> buyingcardlist = user.getBuyingcardlist();
			for (Usercard usercard : buyingcardlist) {
				Userbusinessdetail businessdetail = new Userbusinessdetail();
				businessdetail.setNetid(user.getNetid());
				businessdetail.setOperatorid(operator.getId());
				businessdetail.setUserid(user.getId());
				businessdetail.setAreacode(user.getAreacode());
				businessdetail.setStoreid(operator.getStoreid());
				businessdetail.setTerminalid(usercard.getCardid());
				businessdetail.setTerminaltype("1");
				businessdetail.setStbno("");
				businessdetail.setCardid(usercard.getCardid());
				businessdetail.setBusinesstypekey("buycard");
				Businesstype businesstype  = businesstypeDao.findByTypekeyStr("buycard");
				if(businesstype == null){
					businesstype = new Businesstype();
				}
				businessdetail.setBusinesstypename(businesstype.getTypename());
				businessdetail.setFeename(businesstype.getFeename());
				businessdetail.setContent(usercard.getCardid());
				businessdetail.setContent(getMessage("card.cardid")+":"+usercard.getCardid());
				businessdetail.setPrice(usercard.getPrice());
				businessdetail.setTotalmoney(usercard.getPrice());
				totalmoney = totalmoney.add(businessdetail.getTotalmoney()==null?new BigDecimal("0"):businessdetail.getTotalmoney()); //累加智能卡的费用
				businessdetail.setAddtime(Tools.getCurrentTime());
				businessdetail.setLogout("0");
				businessdetail.setSource("0");
				buyingbusinessdetaillist.add(businessdetail);//封装业务明细
			}
			
			//产品费用
			List<Userproduct> buyingproductlist = user.getBuyingproductlist();
			for (Userproduct userproduct : buyingproductlist) {
				Userbusinessdetail businessdetail = new Userbusinessdetail();
				businessdetail.setNetid(user.getNetid());
				businessdetail.setOperatorid(operator.getId());
				businessdetail.setAreacode(user.getAreacode());
				businessdetail.setUserid(user.getId());
				businessdetail.setStoreid(operator.getStoreid());
				
				businessdetail.setTerminalid(userproduct.getTerminalid());
				businessdetail.setTerminaltype(userproduct.getTerminaltype());
				businessdetail.setCardid(userproduct.getCardid());
				businessdetail.setStbno(userproduct.getStbno());
				businessdetail.setProductid(userproduct.getProductid());
				businessdetail.setProductname(userproduct.getProductname());
				businessdetail.setType(userproduct.getType());
				businessdetail.setAddtime(Tools.getCurrentTime());
				businessdetail.setStarttime(userproduct.getStarttime());
				businessdetail.setEndtime(userproduct.getEndtime());
				businessdetail.setBusinesstypekey("buyproduct");
				Businesstype businesstype  = businesstypeDao.findByTypekeyStr("buyproduct");
				if(businesstype == null){
					businesstype = new Businesstype();
				}
				businessdetail.setBusinesstypename(businesstype.getTypename());
				businessdetail.setFeename(businesstype.getFeename());
				businessdetail.setContent(userproduct.getProductname()+":"+StringUtils.substring(userproduct.getStarttime(), 0,10)+"至"+StringUtils.substring(userproduct.getEndtime(),0,10));
				businessdetail.setRemark(userproduct.getProductname()+":"+StringUtils.substring(userproduct.getStarttime(), 0,10)+"至"+StringUtils.substring(userproduct.getEndtime(),0,10));
				businessdetail.setPrice(userproduct.getPrice()); 
				businessdetail.setTotalmoney(userproduct.getTotalmoney());
				totalmoney = totalmoney.add(businessdetail.getTotalmoney()==null?new BigDecimal("0"):businessdetail.getTotalmoney()); //累加产品的费用
				
				businessdetail.setLogout("0");
				businessdetail.setSource("0");
				buyingbusinessdetaillist.add(businessdetail);//封装业务明细
			}
		//停开机业务
		} else if ("stopAndOn".equals(form.getBusinesstype())){
			Userbusinessdetail businessdetail = new Userbusinessdetail();
			businessdetail.setNetid(user.getNetid());
			businessdetail.setOperatorid(operator.getId());
			businessdetail.setUserid(user.getId());
			businessdetail.setAreacode(user.getAreacode());
			businessdetail.setStoreid(operator.getStoreid());
			businessdetail.setTerminalid(form.getTerminalid());
			businessdetail.setTerminaltype(form.getTerminaltype());
			if("0".equals(form.getTerminaltype())){//无卡机顶盒
				businessdetail.setCardid("");
				businessdetail.setStbno(form.getTerminalid());
			}else if("1".equals(form.getTerminaltype())){//智能卡
				businessdetail.setCardid(form.getTerminalid());
				businessdetail.setStbno("");
			}
			
			businessdetail.setBusinesstypekey(form.getBusinesstypekey());
			Businesstype businesstype = businesstypeDao.findByTypekeyStr(form.getBusinesstypekey());
			 if(businesstype == null ){
	            	businesstype = new Businesstype();;
	            }
			businessdetail.setBusinesstypename(businesstype.getTypename());
			businessdetail.setFeename(businesstype.getFeename());
			businessdetail.setContent(form.getTerminalid()+":"+businesstype.getTypename());
			businessdetail.setPrice(businesstype.getPrice()); 
			businessdetail.setTotalmoney(businesstype.getPrice()!=null?businesstype.getPrice():new BigDecimal(0));
			totalmoney = totalmoney.add(businessdetail.getTotalmoney()==null?new BigDecimal("0"):businessdetail.getTotalmoney()); //累加停开机的费用
			businessdetail.setAddtime(Tools.getCurrentTime());
			businessdetail.setLogout("0");
			businessdetail.setSource("0");
			buyingbusinessdetaillist.add(businessdetail);//封装业务明细
		//过户业务	
		} else if("transferUser".equals(form.getBusinesstype())){//过户业务
			Userbusinessdetail businessdetail = new Userbusinessdetail();
			businessdetail.setNetid(user.getNetid());
			businessdetail.setOperatorid(operator.getId());
			businessdetail.setUserid(user.getId());
			businessdetail.setAreacode(user.getAreacode());
			businessdetail.setStoreid(operator.getStoreid());
			businessdetail.setBusinesstypekey("transferuser");
			Businesstype businesstype = businesstypeDao.findByTypekeyStr("transferuser");
			 if(businesstype == null ){
	            	businesstype = new Businesstype();;
	            }
			businessdetail.setBusinesstypename(businesstype.getTypename());
			businessdetail.setFeename(businesstype.getFeename());
			businessdetail.setContent("业务过户给新订户:"+user.getTransferuser().getId());
            
			businessdetail.setPrice(businesstype.getPrice()); 
			businessdetail.setTotalmoney(businesstype.getPrice()!=null?businesstype.getPrice():new BigDecimal(0));
			totalmoney = totalmoney.add(businessdetail.getTotalmoney()==null?new BigDecimal("0"):businessdetail.getTotalmoney()); //累加过户的费用
			businessdetail.setAddtime(Tools.getCurrentTime());
			businessdetail.setLogout("0");
			businessdetail.setSource("0");
			buyingbusinessdetaillist.add(businessdetail);//封装业务明细
		//销户业务		
		} else if("cancelUser".equals(form.getBusinesstype())){//销户业务
			Userbusinessdetail businessdetail = new Userbusinessdetail();
			businessdetail.setNetid(user.getNetid());
			businessdetail.setOperatorid(operator.getId());
			businessdetail.setUserid(user.getId());
			businessdetail.setAreacode(user.getAreacode());
			businessdetail.setStoreid(operator.getStoreid());
			businessdetail.setBusinesstypekey("canceluser");
			Businesstype businesstype = businesstypeDao.findByTypekeyStr("canceluser");
	        if(businesstype == null ){
	        	businesstype = new Businesstype();
	        }
	        businessdetail.setBusinesstypename(businesstype.getTypename());
	        businessdetail.setFeename(businesstype.getFeename());
			businessdetail.setContent(getMessage("menu.business.usercancel"));
			businessdetail.setPrice(businesstype.getPrice()); 
			businessdetail.setTotalmoney(businesstype.getPrice()!=null?businesstype.getPrice():new BigDecimal(0));
			totalmoney = totalmoney.add(businessdetail.getTotalmoney()==null?new BigDecimal("0"):businessdetail.getTotalmoney()); //累加过户的费用
			businessdetail.setAddtime(Tools.getCurrentTime());
			businessdetail.setLogout("0");
			businessdetail.setSource("0");
			buyingbusinessdetaillist.add(businessdetail);//封装业务明细
		//迁移业务		
		}else if("transferAddress".equals(form.getBusinesstype())){//迁移业务
			Userbusinessdetail businessdetail = new Userbusinessdetail();
			businessdetail.setNetid(user.getNetid());
			businessdetail.setOperatorid(operator.getId());
			businessdetail.setUserid(user.getId());
			businessdetail.setAreacode(user.getAreacode());
			businessdetail.setStoreid(operator.getStoreid());
			businessdetail.setBusinesstypekey("transferaddress");
			Businesstype businesstype = businesstypeDao.findByTypekeyStr("transferaddress");
	        if(businesstype == null ){
	        	businesstype = new Businesstype();
	        }
	        businessdetail.setBusinesstypename(businesstype.getTypename());
	        businessdetail.setFeename(businesstype.getFeename());
			businessdetail.setContent(getMessage("user.oldaddress")+":"+user.getAddress()+" "+getMessage("user.newaddress")+":"+form.getAddress());
			businessdetail.setPrice(businesstype.getPrice()); 
			businessdetail.setTotalmoney(businesstype.getPrice()!=null?businesstype.getPrice():new BigDecimal(0));
			totalmoney = totalmoney.add(businessdetail.getTotalmoney()==null?new BigDecimal("0"):businessdetail.getTotalmoney()); //累加过户的费用
			businessdetail.setAddtime(Tools.getCurrentTime());
			businessdetail.setLogout("0");
			businessdetail.setSource("0");
			buyingbusinessdetaillist.add(businessdetail);//封装业务明细
		//更换机顶盒
		}else if("replaceStb".equals(form.getBusinesstype())){//更换机顶盒
			Userbusinessdetail businessdetail = new Userbusinessdetail();
			businessdetail.setNetid(user.getNetid());
			businessdetail.setOperatorid(operator.getId());
			businessdetail.setUserid(user.getId());
			businessdetail.setAreacode(user.getAreacode());
			businessdetail.setStoreid(operator.getStoreid());
			businessdetail.setTerminalid(form.getStbno());
			businessdetail.setTerminaltype("0");
			businessdetail.setCardid("");
			businessdetail.setStbno(form.getStbno());
			businessdetail.setBusinesstypekey("replacestb");
			Businesstype businesstype = businesstypeDao.findByTypekeyStr("replacestb");
	        if(businesstype == null ){
	        	businesstype = new Businesstype();
	        }
	        businessdetail.setBusinesstypename(businesstype.getTypename());
	        businessdetail.setFeename(businesstype.getFeename());
			businessdetail.setContent(form.getStbno()+":"+getMessage("business.type.replacestb")+":"+user.getReplacestb().getStbno());
			
			//更新价格在型号上2017-08-23
			businessdetail.setPrice(businesstype.getPrice()); 
			businessdetail.setTotalmoney(businesstype.getPrice()!=null?businesstype.getPrice():new BigDecimal(0));
			totalmoney = totalmoney.add(businessdetail.getTotalmoney()==null?new BigDecimal("0"):businessdetail.getTotalmoney()); //累加过户的费用
			businessdetail.setAddtime(Tools.getCurrentTime());
			businessdetail.setLogout("0");
			businessdetail.setSource("0");
			buyingbusinessdetaillist.add(businessdetail);//封装业务明细
		//更换智能卡
		}else if("replaceCard".equals(form.getBusinesstype())){//更换智能卡
			Userbusinessdetail businessdetail = new Userbusinessdetail();
			businessdetail.setNetid(user.getNetid());
			businessdetail.setOperatorid(operator.getId());
			businessdetail.setUserid(user.getId());
			businessdetail.setAreacode(user.getAreacode());
			businessdetail.setStoreid(operator.getStoreid());
			businessdetail.setTerminalid(form.getCardid());
			businessdetail.setTerminaltype("1");
			businessdetail.setCardid(form.getCardid());
			businessdetail.setStbno("");
			
			businessdetail.setBusinesstypekey("replacecard");
			Businesstype businesstype = businesstypeDao.findByTypekeyStr("replacecard");
			if(businesstype == null){
				businesstype = new Businesstype();
			}
			businessdetail.setBusinesstypename(businesstype.getTypename());
			businessdetail.setFeename(businesstype.getFeename());
			businessdetail.setContent(form.getCardid()+":更换成:"+user.getReplacecard().getCardid());
			businessdetail.setPrice(businesstype.getPrice()!=null?businesstype.getPrice():new BigDecimal(0)); 
			businessdetail.setTotalmoney(businesstype.getPrice());
			totalmoney = totalmoney.add(businessdetail.getTotalmoney()==null?new BigDecimal("0"):businessdetail.getTotalmoney()); //累加更换的费用
			businessdetail.setAddtime(Tools.getCurrentTime());
			businessdetail.setLogout("0");
			businessdetail.setSource("0");
			buyingbusinessdetaillist.add(businessdetail);//封装业务明细
		//订户补卡	
		}else if("fillcard".equals(form.getBusinesstype())){//订户补卡
			Userbusinessdetail businessdetail = new Userbusinessdetail();
			businessdetail.setNetid(user.getNetid());
			businessdetail.setOperatorid(operator.getId());
			businessdetail.setUserid(user.getId());
			businessdetail.setAreacode(user.getAreacode());
			businessdetail.setStoreid(operator.getStoreid());
			businessdetail.setTerminalid(form.getCardid());
			businessdetail.setTerminaltype("1");
			businessdetail.setCardid(form.getCardid());
			businessdetail.setStbno("");
			businessdetail.setBusinesstypekey("fillcard");
			//取出补卡费
			Businesstype businesstype = businesstypeDao.findByTypekeyStr("fillcard");
			if(businesstype == null){
				businesstype = new Businesstype();
			}
			businessdetail.setBusinesstypename(businesstype.getTypename());
			businessdetail.setFeename(businesstype.getFeename());
			businessdetail.setContent(form.getCardid()+":补卡成:"+user.getReplacecard().getCardid());
			businessdetail.setPrice(businesstype.getPrice()!=null?businesstype.getPrice():new BigDecimal(0)); 
			businessdetail.setTotalmoney(businesstype.getPrice());
			totalmoney = totalmoney.add(businessdetail.getTotalmoney()==null?new BigDecimal("0"):businessdetail.getTotalmoney()); //累加补卡的费用
			businessdetail.setAddtime(Tools.getCurrentTime());
			businessdetail.setLogout("0");
			businessdetail.setSource("0");
			buyingbusinessdetaillist.add(businessdetail);//封装业务明细
		}
		
		//封装其他费用收取
		List<Businesstype> buyingotherfeelist = user.getBuyingotherfeelist();
		for (Businesstype businesstype : buyingotherfeelist) {
			Userbusinessdetail businessdetail = new Userbusinessdetail();
			businessdetail.setNetid(user.getNetid());
			businessdetail.setOperatorid(operator.getId());
			businessdetail.setUserid(user.getId());
			businessdetail.setAreacode(user.getAreacode());
			businessdetail.setStoreid(operator.getStoreid());
			businessdetail.setBusinesstypekey(businesstype.getTypekey());
			businessdetail.setBusinesstypename(businesstype.getTypename());
			businessdetail.setFeename(businesstype.getFeename());
			businessdetail.setContent(businesstype.getRemark());
			businessdetail.setRemark(businesstype.getRemark());
			businessdetail.setPrice(businesstype.getPrice()); 
			businessdetail.setTotalmoney(businesstype.getPrice());
			totalmoney = totalmoney.add(businessdetail.getTotalmoney()==null?new BigDecimal("0"):businessdetail.getTotalmoney()); //累加收取的其他费用
			businessdetail.setAddtime(Tools.getCurrentTime());
			businessdetail.setLogout("0");
			businessdetail.setSource("0");
			buyingbusinessdetaillist.add(businessdetail);//封装业务明细
		}
		
		//封装业务主表
		userbusiness.setNetid(user.getNetid());
		userbusiness.setOperatorid(operator.getId());
		userbusiness.setUserid(user.getId());
		userbusiness.setStoreid(operator.getStoreid());
		userbusiness.setAreacode(user.getAreacode());
		userbusiness.setTotalmoney(totalmoney);
		userbusiness.setShouldmoney(totalmoney);
		userbusiness.setSource("0");
		userbusiness.setLogout("0");
		
		//将业务主表保存到user中
		user.setBuyingbusiness(userbusiness);
		
		//将业务明细保存到user中
		user.setBuyingbusinessdetaillist(buyingbusinessdetaillist);
		
		operator.setUser(user);
		getSession().setAttribute("Operator", operator);
		
		form.setUserbusiness(userbusiness);
		
		return "user/feeCount";
	}
	
	/**
	 * 保存业务信息
	 */
	@RequestMapping(value="/saveBusiness")
	public String saveBusiness(Userbusiness form) {
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		//销户订户和未审核订户不能办理业务
		if("0".equals(user.getState())||"3".equals(user.getState())){
			form.setReturninfo(getMessage("page.execution.failure"));
			return "user/feeCount";
		}
		
		//购买设备或者购买产品业务
		if("buyDevice".equals(form.getBusinesstype()) || "buyProduct".equals(form.getBusinesstype())){
			//机顶盒费用
			List<Userstb> buyingstblist = user.getBuyingstblist();
			for (Userstb userstb : buyingstblist) {
				//保存订户机顶盒
				userstb.setAddtime(Tools.getCurrentTime());
				userstbDao.save(userstb);
				//改变机顶盒库存状态
				Stb stb = new Stb();
				stb.setStbno(userstb.getStbno());
				stb = stbDao.findByStbno(stb);
				stb.setState("1");
				stbDao.updateStbState(stb);
				
				//系统是否自动添加安装工单
				String systempara = systemparaService.findSystemParaByCodeStr("auto_add_installation_workorder");
				if("1".equals(systempara)){//0-否；1-是
					//新建工单
					Dispatch  dispatch = new Dispatch();
					dispatch.setNetid(user.getNetid());
					dispatch.setAreacode(user.getAreacode());
					dispatch.setProblemtype("0");
					dispatch.setUserid(user.getId());
					dispatch.setAdddate(Tools.getCurrentTime());
					dispatch.setOperatorid(operator.getId());
					dispatch.setState("0");
					dispatch.setContent(getMessage("dispatch.content.installstb")+";"+getMessage("stb.stbno")+":"+stb.getStbno());
					dispatchDao.save(dispatch);
				}
			}
			
			//全部保存完成之后，再来发送机顶盒授权,因为涉及到子母卡查询
			for (Userstb userstb : buyingstblist) {
				//发送授权指令
				if("2".equals(userstb.getIncardflag())){//无卡机顶盒
					//保存授权
					authorizeService.saveAuthorize_buyStb(userstb);
				}
			}
			
			//智能卡费用
			List<Usercard> buyingcardlist = user.getBuyingcardlist();
			for (Usercard usercard : buyingcardlist) {
				//保存订户智能卡
				usercard.setAddtime(Tools.getCurrentTime());
				usercardDao.save(usercard);
				//改变智能卡库存状态
				Card card = new Card();
				card.setCardid(usercard.getCardid());
				card.setState("1");
				cardDao.updateCardState(card);
				
				//重置机顶盒配对机顶盒
				card.setStbno(null);
				cardDao.updateStbno(card);
				
			}
			
			//全部保存完成之后，再来发送智能卡授权,因为涉及到子母卡查询
			for (Usercard usercard : buyingcardlist) {
				//发送授权指令
				authorizeService.saveAuthorize_buyCard(usercard);
			}
			
			//产品费用
			List<Userproduct> buyingproductlist = user.getBuyingproductlist();
			for (Userproduct userproduct : buyingproductlist) {
				//保存订户购买产品
				userproduct.setAddtime(Tools.getCurrentTime());
				userproductDao.save(userproduct);
				//发送授权指令
				authorizeService.saveAuthorize_buyProduct(userproduct);
				
			    //删除过期的产品
				userproductDao.deleteInvalidProduct(userproduct);
				
			}
			//清空购买中的机顶盒
			user.setBuyingstblist(null);
			//清空购买中的智能卡
			user.setBuyingcardlist(null);
			//清空购买中的产品
			user.setBuyingproductlist(null);
			
	    //停开机业务
		} else if("stopAndOn".equals(form.getBusinesstype())){
			if("pausecard".equals(form.getBusinesstypekey())){
				//1-把该终端下面所有的未到期的产品报停，同时保存授权剩余天数
				Userproduct userproductform= new Userproduct();
				userproductform.setTerminalid(form.getTerminalid());
				userproductform.setQuerystate("1");
				userproductform.setEndtime(Tools.getCurrentTimeByFormat("yyyy-MM-dd"));
				List<Userproduct> userproductlist = userproductDao.queryByList(userproductform);
				for (Userproduct userproduct : userproductlist) {
					Date startDate = Tools.getDateFromStr(userproduct.getStarttime(), "yyyy-MM-dd HH:mm:ss");
					Date endDate = Tools.getDateFromStr(userproduct.getEndtime(), "yyyy-MM-dd HH:mm:ss");
					Integer restday = 0;
					if(startDate.getTime() < new Date().getTime()){//产品开始日期少于当前时间，剩余授权天数从当天开始算
						restday = Tools.differentDays(new Date(), endDate);
					}else{//授权剩余天数从开始时间开始计算
						restday = Tools.differentDays(startDate, endDate);
					}
					if(restday >= 1){//授权剩余时间大于1，产品才进行报停操作
						userproduct.setRestday(new BigDecimal(restday));
						userproduct.setState("0");//报停
						userproductDao.update(userproduct);//保存修改
						
						//产品发送反授权
						userproduct.setStarttime(userproduct.getEndtime());//产品反授权的开始时间等于结束时间
						authorizeService.saveAuthorize_buyProduct(userproduct);
					}
				}
				if("0".equals(form.getTerminaltype())){//无卡机顶盒终端
					//修改机顶盒状态
					Userstb userstb =  new Userstb();
					userstb.setStbno(form.getTerminalid());
					userstb = userstbDao.findByStbno(userstb);
					userstb.setState("0");//报停
					userstbDao.update(userstb);//保存修改
					
					//发送授权指令
					authorizeService.saveAuthorize_stopAndOnStb(userstb,"0");
					
				}else if("1".equals(form.getTerminaltype())){//智能卡终端
					//2-修改卡的状态
					Usercard usercard =  new Usercard();
					usercard.setCardid(form.getTerminalid());
					usercard = usercardDao.findByCardid(usercard);
					usercard.setState("0");//报停
					usercardDao.update(usercard);//保存修改
					
					//发送授权指令
					authorizeService.saveAuthorize_stopAndOnCard(usercard,"0");
				}
			}else if("opencard".equals(form.getBusinesstypekey())){
//				//1-查询出该卡下所有报停的产品
//				Userproduct userproductform= new Userproduct();
//				userproductform.setTerminalid(form.getTerminalid());
//				userproductform.setQuerystate("0");
//				List<Userproduct> userproductlist = userproductDao.queryByList(userproductform);
//				for (Userproduct userproduct : userproductlist) {
//					Date startDate = Tools.getDateFromStr(userproduct.getStarttime(), "yyyy-MM-dd HH:mm:ss");
//					//Date endDate = Tools.getDateFromStr(userproduct.getEndtime(), "yyyy-MM-dd HH:mm:ss");
//					if(startDate.getTime() < new Date().getTime()){//产品开始日期少于当前时间，剩余授权天数从当天开始算
//						String starttime = Tools.getCurrentTimeByFormat("yyyy-MM-dd");
//						String endtime = Tools.getSpecifiedDayAfter(starttime,"yyyy-MM-dd",userproduct.getRestday().intValue());
//						userproduct.setStarttime(starttime+" 00:00:00");
//						userproduct.setEndtime(endtime+" 23:59:59");
//					}
//					userproduct.setRestday(new BigDecimal(0));
//					userproduct.setState("1");//正常
//					userproductDao.update(userproduct);//保存修改
//					
//					//产品发送授权
//					authorizeService.saveAuthorize_buyProduct(userproduct);
//					
//				}
				
				//1-查询出该卡下所有报停的产品
				Userproduct userproductform= new Userproduct();
				userproductform.setUserid(user.getId());
				userproductform.setTerminalid(form.getTerminalid());;
				userproductform.setQuerystate("0");
				//查询报停智能卡的每个产品剩余收视天数之和
				List<Userproduct> pauseProductRestdaylist = userproductDao.findPauseProductRestday(userproductform);
				
				//批量添加每一个产品授权
				if(pauseProductRestdaylist !=null && pauseProductRestdaylist.size() > 0){
					for (Userproduct userproduct : pauseProductRestdaylist) {
						userproduct.setNetid(user.getNetid());
						userproduct.setAreacode(user.getAreacode());
						userproduct.setTerminaltype("1");
						userproduct.setCardid(userproduct.getTerminalid());
						userproduct.setType("1");
						userproduct.setSource("0");
						userproduct.setAddtime(Tools.getCurrentTime());
						String starttime = Tools.getCurrentTimeByFormat("yyyy-MM-dd");
						String endtime = Tools.getSpecifiedDayAfter(starttime,"yyyy-MM-dd",userproduct.getRestday().intValue());
						userproduct.setStarttime(starttime+" 00:00:00");
						userproduct.setEndtime(endtime+" 23:59:59");
						userproduct.setState("1");
						userproduct.setUnit("day");
						BigDecimal restday = userproduct.getRestday();
						userproduct.setBuyamount(restday.intValue());
						userproduct.setTotalmoney(userproduct.getPrice().multiply(restday));
						userproduct.setRestday(new BigDecimal(0));
					}
					//批量添加产品
					userproductDao.saveBatch(pauseProductRestdaylist);
					//批量发送产品授权
					authorizeService.saveAuthorize_buyProduct_batch(pauseProductRestdaylist);
				}
				
				//删除报停智能卡的报停产品
				userproductDao.deletePauseProduct_batch(userproductform);
				
				if("0".equals(form.getTerminaltype())){//无卡机顶盒终端
					//修改机顶盒状态
					Userstb userstb =  new Userstb();
					userstb.setStbno(form.getTerminalid());
					userstb = userstbDao.findByStbno(userstb);
					userstb.setState("1");//正常
					userstbDao.update(userstb);//保存修改
					
					//发送授权指令
					authorizeService.saveAuthorize_stopAndOnStb(userstb,"1");
					
				}else if("1".equals(form.getTerminaltype())){//智能卡终端
					//2-修改卡的状态
					Usercard usercard =  new Usercard();
					usercard.setCardid(form.getTerminalid());
					usercard = usercardDao.findByCardid(usercard);
					usercard.setState("1");//正常
					usercardDao.update(usercard);//保存修改
					
					//发送授权指令
					authorizeService.saveAuthorize_stopAndOnCard(usercard,"1");
				}
			}
		//过户业务	
		}else if("transferUser".equals(form.getBusinesstype())){//过户业务	
			//订户的机顶盒信息过户给新订户
			List<Userstb> userstblist = user.getUserstblist();
			for (Userstb userstb : userstblist) {
				userstb.setNetid(user.getTransferuser().getNetid());
				userstb.setAreacode(user.getTransferuser().getAreacode());
				userstb.setUserid(user.getTransferuser().getId());
				userstbDao.update(userstb);
			}
			//订户的智能卡信息过户给新订户
			List<Usercard> usercardlist = user.getUsercardlist();
			for (Usercard usercard : usercardlist) {
				usercard.setNetid(user.getTransferuser().getNetid());
				usercard.setAreacode(user.getTransferuser().getAreacode());
				usercard.setUserid(user.getTransferuser().getId());
				usercardDao.update(usercard);
			}
			//订户的产品信息过户给新订户
			List<Userproduct> userproductlist = user.getUserproductlist();
			for (Userproduct userproduct : userproductlist) {
				userproduct.setNetid(user.getTransferuser().getNetid());
				userproduct.setAreacode(user.getTransferuser().getAreacode());
				userproduct.setUserid(user.getTransferuser().getId());
				userproductDao.update(userproduct);
			}
			
			//过户改变了区域，需要重新激活
			if(!user.getAreacode().equals(user.getTransferuser().getAreacode())){
				//迁移激活指令
				authorizeService.saveAuthorize_transferUser(user.getTransferuser());
			}
			
			//过户新订户增加一笔业务记录
			//封装新订户业务主表
			Userbusiness userbusiness = new Userbusiness();
			userbusiness.setNetid(user.getTransferuser().getNetid());
			userbusiness.setOperatorid(operator.getId());
			userbusiness.setUserid(user.getTransferuser().getId());
			userbusiness.setStoreid(operator.getStoreid());
			userbusiness.setAreacode(user.getTransferuser().getAreacode());
			userbusiness.setTotalmoney(new BigDecimal(0));
			userbusiness.setShouldmoney(new BigDecimal(0));
			userbusiness.setPaymoney(new BigDecimal(0));
			userbusiness.setAddtime(Tools.getCurrentTime());
			userbusiness.setSource("0");
			userbusiness.setLogout("0");
			userbusiness.setPaytype("0");
			userbusinessDao.save(userbusiness);
			//封装新订户业务明细表
			Userbusinessdetail businessdetail = new Userbusinessdetail();
			businessdetail.setBusinessid(userbusiness.getId());
			businessdetail.setNetid(user.getTransferuser().getNetid());
			businessdetail.setOperatorid(operator.getId());
			businessdetail.setUserid(user.getTransferuser().getId());
			businessdetail.setAreacode(user.getTransferuser().getAreacode());
			businessdetail.setStoreid(operator.getStoreid());
			businessdetail.setBusinesstypekey("transferusered");
			Businesstype businesstype  = businesstypeDao.findByTypekeyStr("transferusered");
			if(businesstype == null){
				businesstype = new Businesstype();
			}
			businessdetail.setBusinesstypename(businesstype.getTypename());
			businessdetail.setFeename(businesstype.getFeename());
			businessdetail.setContent("从订户ID"+":"+user.getId()+"过户进来的");
			
			businessdetail.setPrice(new BigDecimal(0)); 
			businessdetail.setTotalmoney(new BigDecimal(0));
			businessdetail.setAddtime(Tools.getCurrentTime());
			businessdetail.setLogout("0");
			businessdetail.setSource("0");
			userbusinessdetailDao.save(businessdetail);
			
			//清理过户旧订户的机顶盒信息
			user.setUserstblist(null);
			//清理过户旧订户的智能卡信息
			user.setUsercardlist(null);
			//清理过户旧订户的产品信息
			user.setUserproductlist(null);
			//清空过户新订户缓存信息
			user.setTransferuser(null);
		//销户业务		
		}else if("cancelUser".equals(form.getBusinesstype())){//销户业务	
			//1-把订户所有的未到期的产品停止，同时保存授权剩余天数
			Userproduct userproductform= new Userproduct();
			userproductform.setUserid(user.getId());
			userproductform.setEndtime(Tools.getCurrentTimeByFormat("yyyy-MM-dd"));
			List<Userproduct> userproductlist = userproductDao.queryByList(userproductform);
			for (Userproduct userproduct : userproductlist) {
				Date startDate = Tools.getDateFromStr(userproduct.getStarttime(), "yyyy-MM-dd HH:mm:ss");
				Date endDate = Tools.getDateFromStr(userproduct.getEndtime(), "yyyy-MM-dd HH:mm:ss");
				Integer restday = 0;
				
				if(startDate.getTime() < new Date().getTime()){//产品开始日期少于当前时间，剩余授权天数从当天开始算
					restday = Tools.differentDays(new Date(), endDate);
				}else{//授权剩余天数从开始时间开始计算
					restday = Tools.differentDays(startDate, endDate);
				}
				if(restday >= 1){//授权剩余时间大于1，产品才进行停止操作
					userproduct.setRestday(new BigDecimal(restday));
					userproduct.setState("4");//停止
					userproductDao.update(userproduct);//保存修改
					
					//产品发送反授权
					userproduct.setStarttime(userproduct.getEndtime());//产品反授权的开始时间等于结束时间
					authorizeService.saveAuthorize_buyProduct(userproduct);
				}
			}
			
			//2-修改机顶盒的销户状态
			Userstb userstbform = new Userstb();
			userstbform.setUserid(user.getId());
			List<Userstb> userstblist = userstbDao.queryByList(userstbform);
			for (Userstb userstb : userstblist) {
				userstb.setState("4");//销户状态
				userstbDao.update(userstb);
				//发送停机指令
				if("2".equals(userstb.getIncardflag())){//无卡机顶盒终端
					//发送停机指令
					authorizeService.saveAuthorize_cancelUser_userstb(userstb);
				}
			}
			//3-修改智能卡的销户状态
			Usercard usercardform = new Usercard();
			usercardform.setUserid(user.getId());
			List<Usercard> usercardlist = usercardDao.queryByList(usercardform);
			for (Usercard usercard : usercardlist) {
				usercard.setState("4");//销户状态
				usercardDao.update(usercard);
				//发送停机指令
				authorizeService.saveAuthorize_cancelUser_usercard(usercard);
			}
			//4-修改订户的销户状态
			user.setState("0");
			userDao.update(user);//保存修改
			
	    //迁移业务
		}else if("transferAddress".equals(form.getBusinesstype())){//迁移业务	
			String oldareacode = user.getAreacode();
			//1-修改订户的区域和地址
			user.setAreacode(form.getAreacode());
			user.setAddress(form.getAddress());
			userDao.update(user);//保存修改
			
			//订户改变了区域，需要重新激活
			if(!form.getAreacode().equals(oldareacode)){
				//修改订户机顶盒的区域号
				Userstb userstbForm = new Userstb();
				userstbForm.setUserid(user.getId());
				userstbForm.setAreacode(form.getAreacode());
				userstbDao.updateUserstbAreacode(userstbForm);
				//修改订户智能卡的区域号
				Usercard usercardForm = new Usercard();
				usercardForm.setUserid(user.getId());
				usercardForm.setAreacode(form.getAreacode());
				usercardDao.updateUsercardAreacode(usercardForm);
				//修改订户产品的区域号
				Userproduct userproductForm = new Userproduct();
				userproductForm.setUserid(user.getId());
				userproductForm.setAreacode(form.getAreacode());
				userproductDao.updateUserproductAreacode(userproductForm);
				//迁移激活指令
				authorizeService.saveAuthorize_transferAddress(user);
			}

		//更换机顶盒
		}else if("replaceStb".equals(form.getBusinesstype())){//更换机顶盒
			//获取新更换的机顶盒
			Stb replaceStb = user.getReplacestb();
			//改变新更换机顶盒为使用状态
			replaceStb.setState("1");
			stbDao.updateStbState(replaceStb);
			
			//改变旧机顶盒的回收状态
			Stb oldstb = new Stb();
			oldstb.setStbno(form.getStbno());
			oldstb.setState(form.getStbstate());
			stbDao.updateStbState(oldstb);
			
			//清空机卡配对关系
			Card card =  cardDao.findByStbnoStr(form.getStbno());
			if(card != null){
				card.setStbno(null);
				cardDao.update(card);
			}
			
			//获取需要更换的用户旧机顶盒信息,用来修改保存新的机顶盒
			Userstb userstb =  userstbDao.findByStbnoStr(form.getStbno());
			
			//暂时保存订户旧机顶盒信息
			Userstb olduserstb = new Userstb();
			Tools.setVOToVO(userstb, olduserstb);
			
			if("0".equals(replaceStb.getIncardflag()) || "1".equals(replaceStb.getIncardflag())){//外置卡机顶盒
				//1-更换订户旧机顶盒信息
				userstb.setStbno(replaceStb.getStbno());
				userstb.setIncardflag(replaceStb.getIncardflag());
				userstb.setAddtime(Tools.getCurrentTime());
				userstb.setPrice(replaceStb.getSaleprice());
				userstbDao.update(userstb);
				
				//2-更新机卡绑定关系
				Usercard usercard = usercardDao.findByStbnoStr(form.getStbno());
				//绑定新的机顶盒号
				if(usercard != null){
					usercard.setStbno(replaceStb.getStbno());
					usercardDao.update(usercard);
				}
				
			} else if("2".equals(replaceStb.getIncardflag())){//无卡机顶盒
				//1-更新订户产品授权号
				Userproduct userproductForm = new Userproduct();
				userproductForm.setUserid(userstb.getUserid());
				userproductForm.setTerminalid(userstb.getStbno());
				List<Userproduct> userproductList = userproductDao.findByTerminalid(userproductForm);
				for (Userproduct userproduct : userproductList) {
					userproduct.setTerminalid(replaceStb.getStbno());
					userproduct.setStbno(replaceStb.getStbno());
					userproductDao.update(userproduct);
				}
				
				//2-更换订户旧机顶盒信息
				userstb.setStbno(replaceStb.getStbno());
				userstb.setIncardflag(replaceStb.getIncardflag());
				userstb.setAddtime(Tools.getCurrentTime());
				userstb.setPrice(replaceStb.getSaleprice());
				userstbDao.update(userstb);
				
				//3-判断更换机顶盒主副机状态
				if("0".equals(userstb.getMothercardflag())){//主机更换，还得修改子机关联的主机号
					//找到关联的子机列表
					List<Userstb> subuserstblist = userstbDao.findByMotherstbno(olduserstb.getStbno());
					for (Userstb subuserstb : subuserstblist) {
						//更新子机的主机号
						subuserstb.setMothercardid(userstb.getStbno());
						userstbDao.update(subuserstb);
					}
				}
				
			}
			
			//发送更换机顶盒指令
			authorizeService.saveAuthorize_replaceStb(userstb,olduserstb);
			
			//清理过更换机顶盒的信息
			user.setReplacestb(null);
			
		}else if("replaceCard".equals(form.getBusinesstype())){//更换智能卡
			//获取新更换的智能卡
			Card replaceCard = user.getReplacecard();
			
			//改变新更换智能卡库存状态
			replaceCard.setState("1");
			cardDao.updateCardState(replaceCard);
			//改变新智能卡的配对关系
			replaceCard.setStbno(null);
			cardDao.updateStbno(replaceCard);
			
			
			//改变旧智能卡为回收状态
			Card oldcard = new Card();
			oldcard.setCardid(form.getCardid());
			oldcard.setState(form.getCardstate());
			cardDao.updateCardState(oldcard);
			
			//清空机卡配对关系
			oldcard.setStbno(null);
			cardDao.update(oldcard);
			
			
			//获取需要更换的订户智能卡信息
			Usercard usercard = usercardDao.findByCardidStr(form.getCardid());
			
			//暂时保存订户旧智能卡信息
			Usercard oldusercard = new Usercard();
			Tools.setVOToVO(usercard, oldusercard);
			
			//更新订户产品的智能卡号
			Userproduct userproductForm = new Userproduct();
			userproductForm.setUserid(usercard.getUserid());
			userproductForm.setTerminalid(usercard.getCardid());
			List<Userproduct> userproductList = userproductDao.findByTerminalid(userproductForm);
			for (Userproduct userproduct : userproductList) {
				userproduct.setTerminalid(replaceCard.getCardid());
				userproduct.setCardid(replaceCard.getCardid());
				userproductDao.update(userproduct);
			}
			
			//更换订户智能卡信息
			usercard.setCardid(replaceCard.getCardid());
			usercard.setPrice(replaceCard.getSaleprice());
			usercard.setAddtime(Tools.getCurrentTime());
			usercard.setIncardflag("0");//更换的智能卡都是外置卡
			usercardDao.update(usercard);//更换智能卡信息
			
			//判断更换机顶盒主副机状态
			if("0".equals(usercard.getMothercardflag())){//母机更换，还得修改子机关联的母机号
				//找到关联的子机列表
				List<Usercard> subusercardlist = usercardDao.findByMothercardid(oldusercard.getCardid());
				for (Usercard subusercard : subusercardlist) {
					//更新子机的主机号
					subusercard.setMothercardid(usercard.getCardid());
					usercardDao.update(subusercard);
				}
			}
			
			//发送更换智能卡指令
			authorizeService.saveAuthorize_replaceCard(usercard,oldusercard);
			
			//清理过更换智能卡的信息
			user.setReplacecard(null);
			
		//订户补卡	
		}else if("fillcard".equals(form.getBusinesstype())){//订户补卡
			//获取新的智能卡
			Card replaceCard = user.getReplacecard();
			
			//改变新智能卡库存状态
			replaceCard.setState("1");
			cardDao.updateCardState(replaceCard);
			//改变新智能卡的配对关系
			replaceCard.setStbno(null);
			cardDao.updateStbno(replaceCard);
			
			//改变旧智能卡为回收状态
			Card oldcard = new Card();
			oldcard.setCardid(form.getCardid());
			oldcard.setState(form.getCardstate());
			cardDao.updateCardState(oldcard);
			
			//清空机卡配对关系
			oldcard.setStbno(null);
			cardDao.update(oldcard);
			
			//获取需要更换的订户智能卡信息
			Usercard usercard = usercardDao.findByCardidStr(form.getCardid());
			
			//暂时保存订户旧智能卡信息
			Usercard oldusercard = new Usercard();
			Tools.setVOToVO(usercard, oldusercard);
			
			
			//更新订户产品的智能卡号
			Userproduct userproductForm = new Userproduct();
			userproductForm.setUserid(usercard.getUserid());
			userproductForm.setTerminalid(usercard.getCardid());
			List<Userproduct> userproductList = userproductDao.findByTerminalid(userproductForm);
			for (Userproduct userproduct : userproductList) {
				userproduct.setTerminalid(replaceCard.getCardid());
				userproduct.setCardid(replaceCard.getCardid());
				userproductDao.update(userproduct);
			}
			
			//更换订户智能卡信息
			usercard.setCardid(replaceCard.getCardid());
			usercard.setPrice(replaceCard.getSaleprice());
			usercard.setAddtime(Tools.getCurrentTime());
			usercard.setIncardflag("0");//更换的智能卡都是外置卡
			usercardDao.update(usercard);//更换智能卡信息
			
			//判断更换机顶盒主副机状态
			if("0".equals(usercard.getMothercardflag())){//母机更换，还得修改子机关联的母机号
				//找到关联的子机列表
				List<Usercard> subusercardlist = usercardDao.findByMothercardid(oldusercard.getCardid());
				for (Usercard subusercard : subusercardlist) {
					//更新子机的主机号
					subusercard.setMothercardid(usercard.getCardid());
					usercardDao.update(subusercard);
				}
			}
			
			//发送更换智能卡指令
			authorizeService.saveAuthorize_replaceCard(usercard,oldusercard);
			
			//清理过更换智能卡的信息
			user.setReplacecard(null);
		}
		
		//业务主表
		Userbusiness userbusiness = user.getBuyingbusiness();
		userbusiness.setAddtime(Tools.getCurrentTime());
		userbusiness.setShouldmoney(form.getShouldmoney());
		userbusiness.setPaymoney(form.getPaymoney());
		userbusiness.setPaytype(StringUtils.isEmpty(form.getPaytype())?"0":form.getPaytype());
		userbusiness.setAccountmoney(form.getAccountmoney());
		userbusiness.setRemark(form.getRemark());
		userbusinessDao.save(userbusiness);
		
		//业务明细表
		List<Userbusinessdetail> buyingbusinessdetaillist = user.getBuyingbusinessdetaillist();
		for (Userbusinessdetail userbusinessdetail : buyingbusinessdetaillist) {
			userbusinessdetail.setBusinessid(userbusiness.getId());
			userbusinessdetail.setAddtime(Tools.getCurrentTime());
			userbusinessdetailDao.save(userbusinessdetail);
		}
		
		//账户余额支付
		if("1".equals(form.getPaytype())){
			//BigDecimal的compareTo方法来进行比较。
			//返回的结果是int类型，-1表示小于，0是等于，1是大于。
			if(user.getAccount().compareTo(form.getAccountmoney()) == -1){
				form.setReturninfo(getMessage("page.execution.failure"));
				return "user/feeCount";
			}else{
				//修改订户账户余额
				user.setRechargemoney(new BigDecimal(0).subtract(form.getAccountmoney()));
				userDao.rechargeAccount(user);
				
				//增加订户账户出入账记录
				Useraccountlog useraccountlog = new Useraccountlog();
				useraccountlog.setNetid(user.getNetid());
				useraccountlog.setUserid(user.getId());
				useraccountlog.setType("1");//类型（0-入账；1-出账）
				useraccountlog.setMoney(new BigDecimal(0).subtract(form.getAccountmoney()));
				useraccountlog.setOperatorid(operator.getId());
				useraccountlog.setAddtime(Tools.getCurrentTime());
				useraccountlog.setSource("0");
				useraccountlog.setBusinesstypekey("accountpayment");
				useraccountlogDao.save(useraccountlog);
				
				//账户余额支付，增加一条明细
				Userbusinessdetail businessdetail = new Userbusinessdetail();
				businessdetail.setBusinessid(userbusiness.getId());
				businessdetail.setNetid(user.getNetid());
				businessdetail.setOperatorid(operator.getId());
				businessdetail.setUserid(user.getId());
				businessdetail.setAreacode(user.getAreacode());
				businessdetail.setStoreid(operator.getStoreid());
				businessdetail.setBusinesstypekey("accountpayment");
				businessdetail.setBusinesstypename(getMessage("business.type.accountpayment"));
				
				businessdetail.setContent(getMessage("userbusiness.accountmoney")+":"+new BigDecimal(0).subtract(form.getAccountmoney()).toString());
				
				businessdetail.setPrice(form.getAccountmoney()); 
				businessdetail.setTotalmoney(new BigDecimal(0).subtract(form.getAccountmoney()));//负的金额
				businessdetail.setAddtime(Tools.getCurrentTime());
				businessdetail.setLogout("0");
				businessdetail.setSource("0");
				
				userbusinessdetailDao.save(businessdetail);
			}
		}
		
		//清空业务主记录
		user.setBuyingbusiness(null);
		//清空业务明细
		user.setBuyingbusinessdetaillist(null);
		
		operator.setUser(user);
		getSession().setAttribute("Operator", operator);
		
		form.setUserbusiness(userbusiness);
		
		form.setReturninfo(getMessage("page.execution.success"));
		
		//加载打印模板
		Printtemplate printtemplate = new Printtemplate();
		List<Printtemplate> templateList = printtemplateDao.queryByList(printtemplate);
		Map<String, String> map = new HashMap<String, String>();
		for (Printtemplate template : templateList) {
			map.put(template.getValue(),template.getName());
		}
		form.setTemplateMap(map);
		
		form.setTemplateList(templateList);
		
		
		return "user/feeCount";
	}
	
	/**
	 * 账户余额充值业务
	 */
	@RequestMapping(value="/saveRechargeAccount")
	public String saveRechargeAccount(User form){
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		
		//封装业务主表
		Userbusiness userbusiness = new Userbusiness();
		userbusiness.setNetid(user.getNetid());
		userbusiness.setOperatorid(operator.getId());
		userbusiness.setUserid(user.getId());
		userbusiness.setStoreid(operator.getStoreid());
		userbusiness.setAreacode(user.getAreacode());
		BigDecimal rechargemoney = new BigDecimal(0);
		String rechargemoneyStr = getRequest().getParameter("rechargemoney");
		if(StringUtils.isNotEmpty(rechargemoneyStr)){
			rechargemoney = new BigDecimal(rechargemoneyStr); 
		}
		userbusiness.setTotalmoney(rechargemoney);
		userbusiness.setShouldmoney(rechargemoney);
		userbusiness.setSource("0");
		userbusiness.setLogout("0");
		userbusiness.setPaytype("0");
		userbusiness.setAddtime(Tools.getCurrentTime());
		userbusiness.setShouldmoney(rechargemoney);
		userbusiness.setPaymoney(rechargemoney);
		userbusinessDao.save(userbusiness);
		
		//封装业务明细
		Userbusinessdetail businessdetail = new Userbusinessdetail();
		businessdetail.setBusinessid(userbusiness.getId());
		businessdetail.setNetid(user.getNetid());
		businessdetail.setOperatorid(operator.getId());
		businessdetail.setUserid(user.getId());
		businessdetail.setAreacode(user.getAreacode());
		businessdetail.setStoreid(operator.getStoreid());
		businessdetail.setBusinesstypekey("rechargeaccount");
		Businesstype businesstype  = businesstypeDao.findByTypekeyStr("rechargeaccount");
		if(businesstype == null){
			businesstype = new Businesstype();
		}
		businessdetail.setBusinesstypename(businesstype.getTypename());
		businessdetail.setFeename(businesstype.getFeename());
		businessdetail.setContent("账户充值:"+userbusiness.getTotalmoney());
		businessdetail.setPrice(userbusiness.getTotalmoney()); 
		businessdetail.setTotalmoney(userbusiness.getTotalmoney());
		businessdetail.setAddtime(Tools.getCurrentTime());
		businessdetail.setLogout("0");
		businessdetail.setSource("0");
		userbusinessdetailDao.save(businessdetail);
		
		//修改订户账户余额
		user.setRechargemoney(userbusiness.getTotalmoney());
		userDao.rechargeAccount(user);
		
		//增加订户账户出入账记录
		Useraccountlog useraccountlog = new Useraccountlog();
		useraccountlog.setNetid(user.getNetid());
		useraccountlog.setUserid(user.getId());
		useraccountlog.setType("0");//类型（0-入账；1-出账）
		useraccountlog.setMoney(userbusiness.getTotalmoney());
		useraccountlog.setOperatorid(operator.getId());
		useraccountlog.setAddtime(Tools.getCurrentTime());
		useraccountlog.setSource("0");
		useraccountlog.setBusinesstypekey("rechargeaccount");
		useraccountlogDao.save(useraccountlog);
		
		form.setReturninfo(getMessage("page.execution.success"));
		return businessUnit(form);
	}
	
	/**
	 * 其他费用收费业务
	 */
	@RequestMapping(value="/saveFeecollection")
	public String saveFeecollection(User form){
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		
		//封装业务主表
		Userbusiness userbusiness = new Userbusiness();
		userbusiness.setNetid(user.getNetid());
		userbusiness.setOperatorid(operator.getId());
		userbusiness.setUserid(user.getId());
		userbusiness.setStoreid(operator.getStoreid());
		userbusiness.setAreacode(user.getAreacode());
		BigDecimal price = new BigDecimal(0);
		String priceStr = getRequest().getParameter("price");
		if(StringUtils.isNotEmpty(priceStr)){
			price = new BigDecimal(priceStr); 
		}
		userbusiness.setTotalmoney(price);
		userbusiness.setShouldmoney(price);
		userbusiness.setSource("0");
		userbusiness.setLogout("0");
		userbusiness.setPaytype("0");
		userbusiness.setAddtime(Tools.getCurrentTime());
		userbusiness.setShouldmoney(price);
		userbusiness.setPaymoney(price);
		userbusinessDao.save(userbusiness);
		
		//封装业务明细
		Userbusinessdetail businessdetail = new Userbusinessdetail();
		businessdetail.setBusinessid(userbusiness.getId());
		businessdetail.setNetid(user.getNetid());
		businessdetail.setOperatorid(operator.getId());
		businessdetail.setUserid(user.getId());
		businessdetail.setAreacode(user.getAreacode());
		businessdetail.setStoreid(operator.getStoreid());
		String typekey = getRequest().getParameter("typekey");
		businessdetail.setBusinesstypekey(typekey);
		Businesstype businesstype = businesstypeDao.findByTypekeyStr(typekey);
		businessdetail.setBusinesstypename(businesstype.getTypename());
		
		businessdetail.setContent(businesstype.getTypename()+":"+userbusiness.getTotalmoney());
		businessdetail.setPrice(userbusiness.getTotalmoney()); 
		businessdetail.setTotalmoney(userbusiness.getTotalmoney());
		businessdetail.setAddtime(Tools.getCurrentTime());
		businessdetail.setLogout("0");
		businessdetail.setSource("0");
		userbusinessdetailDao.save(businessdetail);
		
		//发票模板
		Printtemplate printtemplate = new Printtemplate();
		List<Printtemplate> templateList = printtemplateDao.queryByList(printtemplate);
		Map<String, String> map = new HashMap<String, String>();
		for (Printtemplate template : templateList) {
			map.put(template.getValue(),template.getName());
		}
		userbusiness.setTemplateMap(map);
		userbusiness.setTemplateList(templateList);
		
		//将业务主表保存到user中
		form.setBuyingbusiness(userbusiness);
		
		//将业务明细保存到user中
		List<Userbusinessdetail> buyingbusinessdetaillist = new ArrayList<Userbusinessdetail>();
		buyingbusinessdetaillist.add(businessdetail);
		form.setBuyingbusinessdetaillist(buyingbusinessdetaillist);
		
		form.setReturninfo(getMessage("page.execution.success"));
		return businessUnit(form);
	}
	
	/**
	 * 电子钱包充值业务
	 */
	@RequestMapping(value="/saveRechargeWallet")
	public String saveRechargeWallet(User form){
		
		//加锁
		synchronized(lock) {
			Operator operator =  (Operator)getSession().getAttribute("Operator");
			User user = operator.getUser();
			
			//封装业务主表
			Userbusiness userbusiness = new Userbusiness();
			userbusiness.setNetid(user.getNetid());
			userbusiness.setOperatorid(operator.getId());
			userbusiness.setUserid(user.getId());
			userbusiness.setStoreid(operator.getStoreid());
			userbusiness.setAreacode(user.getAreacode());
			BigDecimal rechargemoney = new BigDecimal(0);
			String rechargemoneyStr = getRequest().getParameter("rechargemoney");
			if(StringUtils.isNotEmpty(rechargemoneyStr)){
				rechargemoney = new BigDecimal(rechargemoneyStr); 
			}
			userbusiness.setTotalmoney(rechargemoney);
			userbusiness.setShouldmoney(rechargemoney);
			userbusiness.setSource("0");
			userbusiness.setPaytype("0");
			userbusiness.setLogout("0");
			userbusiness.setAddtime(Tools.getCurrentTime());
			userbusiness.setShouldmoney(rechargemoney);
			userbusiness.setPaymoney(rechargemoney);
			userbusinessDao.save(userbusiness);
			
			//封装业务明细
			Userbusinessdetail businessdetail = new Userbusinessdetail();
			businessdetail.setBusinessid(userbusiness.getId());
			businessdetail.setNetid(user.getNetid());
			businessdetail.setOperatorid(operator.getId());
			businessdetail.setUserid(user.getId());
			businessdetail.setAreacode(user.getAreacode());
			businessdetail.setStoreid(operator.getStoreid());
			businessdetail.setTerminalid(form.getTerminalid());
			businessdetail.setTerminaltype(form.getTerminaltype());
			if("0".equals(form.getTerminaltype())){//无卡机顶盒
				businessdetail.setCardid("");
				businessdetail.setStbno(form.getTerminalid());
			}else if("1".equals(form.getTerminaltype())){//智能卡
				businessdetail.setCardid(form.getTerminalid());
				businessdetail.setStbno("");
			}
			
			businessdetail.setBusinesstypekey("rechargewallet");
			Businesstype businesstype  = businesstypeDao.findByTypekeyStr("rechargewallet");
			if(businesstype == null){
				businesstype = new Businesstype();
			}
			businessdetail.setBusinesstypename(businesstype.getTypename());
			businessdetail.setFeename(businesstype.getFeename());
			businessdetail.setContent(getMessage("userbusiness.walletrecharge.rechargemoney")+":"+userbusiness.getTotalmoney());
			businessdetail.setPrice(userbusiness.getTotalmoney()); 
			businessdetail.setTotalmoney(userbusiness.getTotalmoney());
			businessdetail.setAddtime(Tools.getCurrentTime());
			businessdetail.setLogout("0");
			businessdetail.setSource("0");
			userbusinessdetailDao.save(businessdetail);
			
			//发送电子钱包充值授权   
			if("0".equals(form.getTerminaltype())){//无卡机顶盒
				Userstb userstb = new Userstb();
				userstb.setStbno(form.getTerminalid());
				userstb = userstbDao.findByStbno(userstb);
				userstb.setRechargemoney(getRequest().getParameter("rechargemoney"));
				//保存授权
				authorizeService.saveAuthorize_rechargeWallet_userstb(userstb);
			}else if("1".equals(form.getTerminaltype())){//智能卡
				Usercard usercard = new Usercard();
				usercard.setCardid(form.getTerminalid());
				usercard = usercardDao.findByCardid(usercard);
				usercard.setRechargemoney(getRequest().getParameter("rechargemoney"));
				//保存授权
				authorizeService.saveAuthorize_rechargeWallet_usercard(usercard);
			}
			
			form.setReturninfo(getMessage("page.execution.success"));
			return businessUnit(form);
		}
	}
	
	/**
	 * 充值卡充值业务
	 */
	@RequestMapping(value="/saveRechargeGiftcard")
	public String saveRechargeGiftcard(User form){
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		
		String giftcardno = getRequest().getParameter("giftcardno");
		if(StringUtils.isEmpty(giftcardno)){
			form.setReturninfo(getMessage("giftcard.giftcardno.empty"));
			return businessUnit(form);
		}
		String password = getRequest().getParameter("password");
		if(StringUtils.isEmpty(password)){
			form.setReturninfo(getMessage("giftcard.password.empty"));
			return businessUnit(form);
		}
		
		//验证充值卡号正确性
		Giftcard giftcard = giftcardDao.findByGiftcardnoStr(giftcardno);
		if(giftcard == null){
			form.setReturninfo(getMessage("giftcard.giftcardno.empty"));
			return businessUnit(form);
		}
		
		//加密后的密码
		String passwordEncrypt= AesSecret.aesEncrypt(password, AesSecret.key);
		if(!passwordEncrypt.equals(giftcard.getPassword())){
			form.setReturninfo(getMessage("giftcard.password.incorrect"));
			return businessUnit(form);
		}
		if(!"1".equals(giftcard.getState())){
			form.setReturninfo(getMessage("giftcard.giftcardno.Invalid"));
			return businessUnit(form);
		}
		if(!"0".equals(giftcard.getUsedflag())){
			form.setReturninfo(getMessage("giftcard.giftcardno.used"));
			return businessUnit(form);
		}
		
		//封装业务主表
		Userbusiness userbusiness = new Userbusiness();
		userbusiness.setNetid(user.getNetid());
		userbusiness.setOperatorid(operator.getId());
		userbusiness.setUserid(user.getId());
		userbusiness.setStoreid(operator.getStoreid());
		userbusiness.setAreacode(user.getAreacode());
		BigDecimal rechargemoney = new BigDecimal(0);
		userbusiness.setTotalmoney(rechargemoney);
		userbusiness.setShouldmoney(rechargemoney);
		userbusiness.setSource("0");
		userbusiness.setLogout("0");
		userbusiness.setPaytype("3");//充值卡支付
		userbusiness.setAddtime(Tools.getCurrentTime());
		userbusiness.setShouldmoney(rechargemoney);
		userbusiness.setPaymoney(rechargemoney);
		userbusinessDao.save(userbusiness);
		
		//封装业务明细
		Userbusinessdetail businessdetail = new Userbusinessdetail();
		businessdetail.setBusinessid(userbusiness.getId());
		businessdetail.setNetid(user.getNetid());
		businessdetail.setOperatorid(operator.getId());
		businessdetail.setUserid(user.getId());
		businessdetail.setAreacode(user.getAreacode());
		businessdetail.setStoreid(operator.getStoreid());
		businessdetail.setBusinesstypekey("giftcardrecharge");
		Businesstype businesstype  = businesstypeDao.findByTypekeyStr("giftcardrecharge");
		if(businesstype == null){
			businesstype = new Businesstype();
		}
		businessdetail.setBusinesstypename(businesstype.getTypename());
		businessdetail.setFeename(businesstype.getFeename());
		businessdetail.setContent(getMessage("giftcard.giftcardno")+":"+giftcardno);
		businessdetail.setPrice(userbusiness.getTotalmoney()); 
		businessdetail.setTotalmoney(userbusiness.getTotalmoney());
		businessdetail.setAddtime(Tools.getCurrentTime());
		businessdetail.setLogout("0");
		businessdetail.setSource("0");
		userbusinessdetailDao.save(businessdetail);
		
		//修改订户账户余额
		user.setRechargemoney(giftcard.getAmount());
		userDao.rechargeAccount(user);
		
		//增加订户账户出入账记录
		Useraccountlog useraccountlog = new Useraccountlog();
		useraccountlog.setNetid(user.getNetid());
		useraccountlog.setUserid(user.getId());
		useraccountlog.setType("0");//类型（0-入账；1-出账）
		useraccountlog.setMoney(giftcard.getAmount());
		useraccountlog.setOperatorid(operator.getId());
		useraccountlog.setAddtime(Tools.getCurrentTime());
		useraccountlog.setSource("0");
		useraccountlog.setBusinesstypekey("giftcardrecharge");
		useraccountlogDao.save(useraccountlog);
		
		//修改充值卡为已使用状态
		giftcard.setUsedflag("1");
		giftcardDao.update(giftcard);
		
		form.setReturninfo(getMessage("page.execution.success"));
		return businessUnit(form);
	}
	
	/**
	 * 保存问题投诉
	 */
	@RequestMapping(value="/saveProblemcomplaint")
	public String saveProblemcomplaint(User form){
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		
		//增加问题投诉
		Problemcomplaint problemcomplart = new  Problemcomplaint();
		problemcomplart.setNetid(user.getNetid());
		problemcomplart.setAreacode(user.getAreacode());
		problemcomplart.setUserid(user.getId());
		problemcomplart.setOperatorid(operator.getId());
		problemcomplart.setType(getRequest().getParameter("type"));
		problemcomplart.setProblemtype(getRequest().getParameter("problemtype"));
		problemcomplart.setContent(getRequest().getParameter("content"));
		problemcomplart.setAddtime(Tools.getCurrentTime());
		problemcomplart.setState("0");
		problemcomplart.setResource("0");
		
		problemcomplaintDao.save(problemcomplart);
		//form.setReturninfo(getMessage("page.execution.success"));
		
		form.setReturninfo(getMessage("page.execution.success"));
		return businessUnit(form);
	}
	
	/**
	 * 授权刷新
	 */
	@RequestMapping(value="/saveAuthorizeRefresh")
	public String saveAuthorizeRefresh(User form){
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		
		String[] idArray = getRequest().getParameterValues("ids");
		
		for (String id : idArray) {
			
			Userproduct userproduct  =  userproductDao.findById(Integer.parseInt(id));
			//发送授权
			authorizeService.saveAuthorize_buyProduct(userproduct);
		}
		
		form.setReturninfo(getMessage("page.execution.success"));
		return businessUnit(form);
	}
	
	/**
	 * 退订业务
	 */
	@RequestMapping(value="/unitCancelBusiness")
	public String unitCancelBusiness(Userbusiness form) {
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		
		//临时变量，存放总金额
		BigDecimal totalmoney = new BigDecimal(0);
		//业务主表
		Userbusiness userbusiness = new Userbusiness();
		//业务明细表
		List<Userbusinessdetail> buyingbusinessdetaillist = new ArrayList<Userbusinessdetail>();
		
		//退订产品
		if("cancelproduct".equals(form.getBusinesstype())){//产品退订
			Userbusinessdetail businessdetail = new Userbusinessdetail();
			businessdetail.setNetid(user.getNetid());
			businessdetail.setOperatorid(operator.getId());
			businessdetail.setAreacode(user.getAreacode());
			businessdetail.setUserid(user.getId());
			businessdetail.setStoreid(operator.getStoreid());
			//需要退订的产品
			Userproduct userproduct = userproductDao.findById(form.getUserproductid());
			businessdetail.setTerminalid(userproduct.getTerminalid());
			businessdetail.setTerminaltype(userproduct.getTerminaltype());
			businessdetail.setCardid(userproduct.getCardid());
			businessdetail.setStbno(userproduct.getStbno());
			businessdetail.setProductid(userproduct.getProductid());
			businessdetail.setProductname(userproduct.getProductname());
			businessdetail.setType(userproduct.getType());
			businessdetail.setAddtime(Tools.getCurrentTime());
			businessdetail.setStarttime(userproduct.getStarttime());
			businessdetail.setEndtime(userproduct.getEndtime());
			businessdetail.setBusinesstypekey("cancelproduct");
			//获取业务类型
			Businesstype businesstype = businesstypeDao.findByTypekeyStr("cancelproduct");
			if(businesstype == null){
				businesstype = new Businesstype();
			}
			businessdetail.setBusinesstypename(businesstype.getTypename());
			businessdetail.setFeename(businesstype.getFeename());
			businessdetail.setContent(userproduct.getProductname()+":"+StringUtils.substring(userproduct.getStarttime(), 0,10)+"至"+StringUtils.substring(userproduct.getEndtime(),0,10));
			businessdetail.setPrice(userproduct.getPrice()); 
			businessdetail.setTotalmoney(userproduct.getTotalmoney());
			totalmoney = totalmoney.add(businessdetail.getTotalmoney()==null?new BigDecimal("0"):businessdetail.getTotalmoney()); //累加产品的费用
			businessdetail.setLogout("0");
			businessdetail.setSource("0");
			buyingbusinessdetaillist.add(businessdetail);//封装业务明细
		//设备回收
		} else if("rebackdevice".equals(form.getBusinesstype())){//设备回收
			//机顶盒回收
			if("0".equals(form.getTerminaltype())){//机顶盒回收
				Userbusinessdetail businessdetail = new Userbusinessdetail();
				businessdetail.setNetid(user.getNetid());
				businessdetail.setOperatorid(operator.getId());
				businessdetail.setAreacode(user.getAreacode());
				businessdetail.setUserid(user.getId());
				businessdetail.setStoreid(operator.getStoreid());
				//需要回收的机顶盒
				Userstb userstb = userstbDao.findByStbnoStr(form.getTerminalid());
				businessdetail.setTerminalid(userstb.getStbno());
				businessdetail.setTerminaltype("0");
				businessdetail.setCardid(null);
				businessdetail.setStbno(userstb.getStbno());
				businessdetail.setAddtime(Tools.getCurrentTime());
				businessdetail.setBusinesstypekey("rebackstb");
				//获取业务类型
				Businesstype businesstype = businesstypeDao.findByTypekeyStr("rebackstb");
				if(businesstype == null){
					businesstype = new Businesstype();
				}
				businessdetail.setBusinesstypename(businesstype.getTypename());
				businessdetail.setFeename(businesstype.getFeename());
				businessdetail.setContent(userstb.getStbno());
				businessdetail.setPrice(userstb.getPrice()); 
				businessdetail.setTotalmoney(userstb.getPrice());
				totalmoney = totalmoney.add(businessdetail.getTotalmoney()==null?new BigDecimal("0"):businessdetail.getTotalmoney()); //累加产品的费用
				businessdetail.setLogout("0");
				businessdetail.setSource("0");
				buyingbusinessdetaillist.add(businessdetail);//封装业务明细
			//智能卡回收
			}else if("1".equals(form.getTerminaltype())){//智能卡回收
				Userbusinessdetail businessdetail = new Userbusinessdetail();
				businessdetail.setNetid(user.getNetid());
				businessdetail.setOperatorid(operator.getId());
				businessdetail.setAreacode(user.getAreacode());
				businessdetail.setUserid(user.getId());
				businessdetail.setStoreid(operator.getStoreid());
				//需要回收的智能卡
				Usercard usercard = usercardDao.findByCardidStr(form.getTerminalid());
				businessdetail.setTerminalid(usercard.getCardid());
				businessdetail.setTerminaltype("1");
				businessdetail.setCardid(usercard.getCardid());
				businessdetail.setStbno(null);
				businessdetail.setAddtime(Tools.getCurrentTime());
				businessdetail.setBusinesstypekey("rebackcard");
				//获取业务类型
				Businesstype businesstype = businesstypeDao.findByTypekeyStr("rebackcard");
				if(businesstype == null){
					businesstype = new Businesstype();
				}
				businessdetail.setBusinesstypename(businesstype.getTypename());
				businessdetail.setFeename(businesstype.getFeename());
				businessdetail.setContent(usercard.getCardid());
				businessdetail.setPrice(usercard.getPrice()); 
				businessdetail.setTotalmoney(usercard.getPrice());
				totalmoney = totalmoney.add(businessdetail.getTotalmoney()==null?new BigDecimal("0"):businessdetail.getTotalmoney()); //累加产品的费用
				businessdetail.setLogout("0");
				businessdetail.setSource("0");
				buyingbusinessdetaillist.add(businessdetail);//封装业务明细
			}
			
			
		}
		//封装业务主表
		userbusiness.setNetid(user.getNetid());
		userbusiness.setOperatorid(operator.getId());
		userbusiness.setUserid(user.getId());
		userbusiness.setStoreid(operator.getStoreid());
		userbusiness.setAreacode(user.getAreacode());
		userbusiness.setTotalmoney(totalmoney);
		userbusiness.setShouldmoney(totalmoney);
		userbusiness.setSource("0");
		userbusiness.setLogout("0");
		
		//将业务主表保存到user中
		user.setBuyingbusiness(userbusiness);
		
		//将业务明细保存到user中
		user.setBuyingbusinessdetaillist(buyingbusinessdetaillist);
		
		operator.setUser(user);
		getSession().setAttribute("Operator", operator);
		
		form.setUserbusiness(userbusiness);
		return "user/feeCountCancel";
	}
	
	/**
	 * 保存取消业务信息
	 */
	@RequestMapping(value="/cancelBusiness")
	public String cancelBusiness(Userbusiness form) {
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		
		//销户订户和未审核订户 能办理退订业务
//		if("0".equals(user.getState())||"3".equals(user.getState())){
//			form.setReturninfo(getMessage("page.execution.failure"));
//			return "user/feeCountCancel";
//		}
		
		//产品退订
		if("cancelproduct".equals(form.getBusinesstype())){
			//需要退订的产品
			Userproduct userproduct = userproductDao.findById(form.getUserproductid());
			//删除订户产品
			userproductDao.delete(form.getUserproductid());
			//发送反授权指令(将开始时间修改成结束时间)
			userproduct.setStarttime(userproduct.getEndtime());
			authorizeService.saveAuthorize_buyProduct(userproduct);
		//设备回收
		}else if("rebackdevice".equals(form.getBusinesstype())){
			//机顶盒回收
			if("0".equals(form.getTerminaltype())){//机顶盒回收
				//查询出该机顶盒信息
				Userstb userstb = userstbDao.findByStbnoStr(form.getTerminalid());
				
				//改变机顶盒库存状态
				Stb oldstb = stbDao.findByStbnoStr(form.getTerminalid());
				oldstb.setState(form.getDevicestate());//重置机顶盒状态
				stbDao.updateStbState(oldstb);
				//清空机卡配对关系
				Card card =  cardDao.findByStbnoStr(form.getTerminalid());
				if(card != null){
					card.setStbno(null);
					cardDao.update(card);
				}
				
				//无卡机顶盒（1发送产品反授权，2停机指令,3删除订户机顶盒）
				if("2".equals(userstb.getIncardflag())){//无卡机顶盒
					//删除订户产品
					Userproduct userproductform= new Userproduct();
					userproductform.setTerminalid(form.getTerminalid());
					List<Userproduct> userproductlist = userproductDao.queryByList(userproductform);
					for (Userproduct userproduct : userproductlist) {
						
						//删除未到期的产品
						userproductDao.delete(userproduct.getId());
						
						//1-产品发送反授权
						userproduct.setStarttime(userproduct.getEndtime());//产品反授权的开始时间等于结束时间
						authorizeService.saveAuthorize_buyProduct(userproduct);
					}
					//2-发送机顶盒停机指令授权
					authorizeService.saveAuthorize_stopAndOnStb(userstb,"0");
					
					//3-删除订户的机顶盒信息
					userstbDao.delete(userstb.getId());
					
				//内置卡机顶盒（1；找出内置卡号；2-发送产品反授权，3-智能卡停机指令，4删除订户智能卡，5删除订户机顶盒，6机卡绑定解除）
				}else if("1".equals(userstb.getIncardflag()) || "0".equals(userstb.getIncardflag())){
					//1-删除订户机顶盒
					userstbDao.delete(userstb.getId());
					//2-删除智能卡机卡绑定关系
					Usercard usercard = usercardDao.findByStbnoStr(userstb.getStbno());
					if (usercard != null) {
						//解除绑定关系
						usercard.setStbno("");
						usercardDao.update(usercard);
					}
					//3-发送机卡绑定关系
					authorizeService.saveAuthorize_stbBingCard(userstb);
				}
			//智能卡回收
			}else if("1".equals(form.getTerminaltype())){//智能卡回收
				//查询出该智能卡信息（1发送产品反授权，2停机指令,3删除订户智能卡，4发送机卡绑定授权）
				Usercard usercard = usercardDao.findByCardidStr(form.getTerminalid());
					
				//改变旧智能卡为回收状态
				Card oldcard = cardDao.findByCardidStr(form.getTerminalid());
				oldcard.setState(form.getDevicestate());//设置状态
				oldcard.setStbno(null);//清空配对关系
				cardDao.update(oldcard);
				
				//删除智能卡的所有产品信息
				Userproduct userproductform= new Userproduct();
				userproductform.setUserid(user.getId());
				userproductform.setTerminalid(form.getTerminalid());
				List<Userproduct> userproductlist = userproductDao.queryByList(userproductform);
				for (Userproduct userproduct : userproductlist) {
					
					//删除未到期的产品
					userproductDao.delete(userproduct.getId());
					
					//1-产品发送反授权
					userproduct.setStarttime(userproduct.getEndtime());//产品反授权的开始时间等于结束时间
					authorizeService.saveAuthorize_buyProduct(userproduct);
				}
				//2-发送智能卡停机指令授权
				authorizeService.saveAuthorize_stopAndOnCard(usercard,"0");
				
				//3-删除订户的智能卡信息
				usercardDao.delete(usercard.getId());
				//4-发送机卡绑定授权
				if(StringUtils.isNotEmpty(usercard.getStbno())){
					Userstb userstb = userstbDao.findByStbnoStr(usercard.getStbno());
					authorizeService.saveAuthorize_stbBingCard(userstb);
				}
			    
			}
		}
		
		//业务主表
		Userbusiness userbusiness = user.getBuyingbusiness();
		userbusiness.setAddtime(Tools.getCurrentTime());
		userbusiness.setTotalmoney(new BigDecimal(0).subtract(userbusiness.getTotalmoney()));
		userbusiness.setShouldmoney(new BigDecimal(0).subtract(form.getShouldmoney()));
		if("1".equals(form.getPaytype())){//返回到账户余额
			userbusiness.setAccountmoney(new BigDecimal(0).subtract(form.getShouldmoney()));
			userbusiness.setPaymoney(new BigDecimal(0));
		}else{
			userbusiness.setAccountmoney(new BigDecimal(0));
			userbusiness.setPaymoney(new BigDecimal(0).subtract(form.getShouldmoney()));
		}
		
		userbusiness.setPaytype(StringUtils.isEmpty(form.getPaytype())?"0":form.getPaytype());
		//userbusiness.setAccountmoney(form.getAccountmoney());
		userbusiness.setRemark(form.getRemark());
		userbusinessDao.save(userbusiness);
		
		//业务明细表
		List<Userbusinessdetail> buyingbusinessdetaillist = user.getBuyingbusinessdetaillist();
		for (Userbusinessdetail userbusinessdetail : buyingbusinessdetaillist) {
			userbusinessdetail.setBusinessid(userbusiness.getId());
			userbusinessdetail.setAddtime(Tools.getCurrentTime());
			//由于退订业务可以修改预退金额，故明细需要修改实付金额
			userbusinessdetail.setTotalmoney(new BigDecimal(0).subtract(form.getShouldmoney()));
			userbusinessdetailDao.save(userbusinessdetail);
		}
		
		//退钱到账户余额
		if("1".equals(form.getPaytype())){
			//修改订户账户余额
			user.setRechargemoney(form.getShouldmoney());
			userDao.rechargeAccount(user);
			
			//增加订户账户出入账记录
			Useraccountlog useraccountlog = new Useraccountlog();
			useraccountlog.setNetid(user.getNetid());
			useraccountlog.setUserid(user.getId());
			useraccountlog.setType("0");//类型（0-入账；1-出账）
			useraccountlog.setMoney(form.getShouldmoney());
			useraccountlog.setOperatorid(operator.getId());
			useraccountlog.setAddtime(Tools.getCurrentTime());
			useraccountlog.setSource("0");
			useraccountlog.setBusinesstypekey("accountrefund");
			useraccountlogDao.save(useraccountlog);
			
			//账户余额支付，增加一条明细
			Userbusinessdetail businessdetail = new Userbusinessdetail();
			businessdetail.setBusinessid(userbusiness.getId());
			businessdetail.setNetid(user.getNetid());
			businessdetail.setOperatorid(operator.getId());
			businessdetail.setUserid(user.getId());
			businessdetail.setAreacode(user.getAreacode());
			businessdetail.setStoreid(operator.getStoreid());
			businessdetail.setBusinesstypekey("accountrefund");
			businessdetail.setBusinesstypename(getMessage("business.type.accountrefund"));
			businessdetail.setContent(getMessage("userbusiness.accountrefund.refundmoney")+":"+form.getShouldmoney());
			
			businessdetail.setPrice(form.getShouldmoney()); 
			businessdetail.setTotalmoney(form.getShouldmoney());
			businessdetail.setAddtime(Tools.getCurrentTime());
			businessdetail.setLogout("0");
			businessdetail.setSource("0");
			
			userbusinessdetailDao.save(businessdetail);
		}
		
		//清空业务主记录
		user.setBuyingbusiness(null);
		//清空业务明细
		user.setBuyingbusinessdetaillist(null);
		
		operator.setUser(user);
		getSession().setAttribute("Operator", operator);
		
		form.setUserbusiness(userbusiness);
		
		form.setReturninfo(getMessage("page.execution.success"));
		
		//加载打印模板
		Printtemplate printtemplate = new Printtemplate();
		List<Printtemplate> templateList = printtemplateDao.queryByList(printtemplate);
		Map<String, String> map = new HashMap<String, String>();
		for (Printtemplate template : templateList) {
			map.put(template.getValue(),template.getName());
		}
		form.setTemplateMap(map);
		
		form.setTemplateList(templateList);
		
		return "user/feeCountCancel";
	}
	
	/**
	 *  获取用户信息
	 * @return
	 */
	@RequestMapping(value="/getUserInfo")
	public String getUserInfo(User form) {
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		//封装订户信息
		form.setUser(user);
		return "user/userInfo";
	}
	
	/**
	 *  获取用户机顶盒信息
	 * @return
	 */
	@RequestMapping(value="/getUserstbInfo")
	public String getUserstbInfo(User form) {
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		//封装订户信息
		form.setUser(user);
		return "user/userStbInfo";
	}
	
	/**
	 * 机顶盒购买初始化
	 * @return
	 */
	@RequestMapping(value="/buyStbUint")
	public String buyDeviceUint(User form) {
		User user =  form.getUser();
		//封装订户信息
		//form.setUser(user);
		return "user/buyDevice";
	}
	
	/**
	 *  获取用户正在购买的机顶盒信息
	 * @return
	 */
	@RequestMapping(value="/getBuyingStbInfo")
	public String getBuyingStbInfo(User form) {
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		//封装订户信息
		form.setUser(user);
		List<Userstb> buyinglist = user.getBuyingstblist();
		for (Userstb userstb : buyinglist) {
			System.out.println("stbno="+userstb.getStbno() +"------motherflag="+userstb.getMothercardflag()+"----motherid="+userstb.getMothercardid());
		}
		return "user/buyingStbInfo";
	}
	
	/**
	 * 查询机顶盒信息
	 */
	@RequestMapping(value="/findStbListForDialog")
	public String findStbListForDialog(Stb form) {
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		form.setQuerynetid(String.valueOf(user.getNetid()));//根据用户所在网络
		form.setPager_openset(5);
		form.setPager_count(stbDao.findByCount(form));
		List<Stb> stblist = stbDao.findByList(form);
		for (Stb stb : stblist) {
			stb.setProvider(providerDao.findById(stb.getProviderid()));
			stb.setNetwork(networkDao.findById(stb.getNetid()));
			stb.setServer(serverDao.findById(stb.getServerid()));
			Card card = cardDao.findByStbnoStr(stb.getStbno());
			if(card != null){
				stb.setPaircardid(card.getCardid());
			}
		}
		form.setStblist(stblist);
		return "user/findStbListForDialog";
	}
	
	/**
	 * 增加购买中的机顶盒
	 * @return
	 */
	@ResponseBody //此标志就是返回jesion数据给页面的标志
	@RequestMapping(value="/addBuyingStb")
	public Map<String,Object> addBuyingStb(Stb form) {
		//封装返回给页面的json对象
		HashMap<String,Object> responseJson = new HashMap<String,Object>();
		
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		List<Userstb> buyingstblist = user.getBuyingstblist();
		for (Userstb userstb : buyingstblist) {
			if(userstb.getStbno().equals(form.getStbno())){
				responseJson.put("flag", "0");//选择机顶盒失败
				responseJson.put("error", getMessage("userstb.buyingstbno.existed"));
				return responseJson;
			}
		}
		
		//封装正在购买中的机顶盒
		Stb stb = stbService.findStbInfoByStbnoStr(form.getStbno());
		Server server = serverDao.findById(stb.getServerid());
		Userstb userstb = new Userstb();
		userstb.setNetid(user.getNetid());
		userstb.setAreacode(user.getAreacode());
		userstb.setUserid(user.getId());
		userstb.setServerid(stb.getServerid());
		userstb.setStbno(stb.getStbno());
		userstb.setIncardflag(stb.getIncardflag());
		userstb.setState("1");
		//默认为母机
		userstb.setMothercardflag("0");
		userstb.setPrice(stb.getMainprice());
		userstb.setServername(server.getServername());
		userstb.setVersiontype(server.getVersiontype());
        //设置该机顶盒默认未购买智能卡
		userstb.setBuycardflag("0");
		//设置该机顶盒默认没有配对的智能卡
		userstb.setPaircardflag("0");
		
		//查看该机顶盒是否配对了智能卡
		Card card = cardService.findCardInfoByStbnoStr(form.getStbno());
		if(card != null ){//如果配对了智能卡，封装正在购买卡的LIST
			List<Usercard> buyingcardlist = user.getBuyingcardlist();
			Usercard usercard = new Usercard();
			usercard.setNetid(user.getNetid());
			usercard.setAreacode(user.getAreacode());
			usercard.setUserid(user.getId());
			usercard.setServerid(card.getServerid());
			usercard.setCardid(card.getCardid());
			usercard.setIncardflag(card.getIncardflag());
			usercard.setStbno(card.getStbno());
			usercard.setState("1");
			//默认为母机
			usercard.setMothercardflag("0");
			usercard.setPrice(card.getMainprice());
			buyingcardlist.add(usercard);
			user.setBuyingcardlist(buyingcardlist);
			
			//修改成该机顶盒购买了智能卡
			userstb.setBuycardflag("1");
			userstb.setPaircardflag("1");
		}
		
		//添加到机顶盒购买中列表
		buyingstblist.add(userstb);
		user.setBuyingstblist(buyingstblist);
		
		operator.setUser(user);
		getSession().setAttribute("Operator", operator);
		responseJson.put("flag", "1");//选择机顶盒成功
		
		return responseJson;
	}
	
	/**
	 * 删除购买中的机顶盒
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@ResponseBody //此标志就是返回jesion数据给页面的标志
	@RequestMapping(value="/deleteBuyingStb")
	public Map<String,Object> deleteBuyingStb(Stb form) {
		//封装返回给页面的json对象
		HashMap<String,Object> responseJson = new HashMap<String,Object>();
		
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		List<Userstb> buyingstblist = user.getBuyingstblist();
		Iterator <Userstb> stbIterator = buyingstblist.iterator();  
		while(stbIterator.hasNext()) { 
			Userstb userstb = stbIterator.next();//得到此机顶盒信息
		    if(userstb.getStbno().equals(form.getStbno())) {  
		    	stbIterator.remove();  //删除机顶盒
		    	if("0".equals(userstb.getIncardflag()) || "1".equals(userstb.getIncardflag())){//删除的机顶盒为外置卡或者内置卡，需要删除购买中的卡信息
		    		//封装删除的智能卡号
			    	HashMap<String,String> deleteCardidMp = new HashMap<String,String>();
			    	
		    		String stbno = userstb.getStbno();
		    		//删除正在购买中的智能卡
		    		List<Usercard> buyingcardlist = user.getBuyingcardlist();
		    		Iterator <Usercard> cardIterator = buyingcardlist.iterator();  
		    		while(cardIterator.hasNext()){
		    			Usercard usercard =  cardIterator.next();
		    			if(usercard.getStbno().equals(stbno)){
		    				String cardid = usercard.getCardid();
		    				deleteCardidMp.put(cardid, cardid);
		    				cardIterator.remove();
		    			}
		    		}
		    		user.setBuyingcardlist(buyingcardlist);
		    		
		    		//删除正在购买中的产品
		    		List<Userproduct> buyingproductlist = user.getBuyingproductlist();
		    		Iterator <Userproduct> productIterator = buyingproductlist.iterator();  
		    		while(productIterator.hasNext()){
		    			if(deleteCardidMp.containsKey(productIterator.next().getCardid())){
		    				productIterator.remove();
		    			}
		    		}
		    		user.setBuyingproductlist(buyingproductlist);
		    	} else if("2".equals(userstb.getIncardflag())){//删除的机顶盒为无卡机顶盒，需要删除购买中的产品信息
		    		//无卡机顶盒直接用机顶盒号购买产品
		    		String stbno = userstb.getStbno();
		    		
		    		//删除正在购买中的产品
		    		List<Userproduct> buyingproductlist = user.getBuyingproductlist();
		    		Iterator <Userproduct> productIterator = buyingproductlist.iterator();  
		    		while(productIterator.hasNext()){
		    			if(productIterator.next().getStbno().equals(stbno)){
		    				productIterator.remove();
		    			}
		    		}
		    		user.setBuyingproductlist(buyingproductlist);
		    	}
		    } 
		    
		}
		//封装正在购买中的机顶盒
		user.setBuyingstblist(buyingstblist);
		
		operator.setUser(user);
		getSession().setAttribute("Operator", operator);
		responseJson.put("flag", "1");//选择机顶盒成功
		
		return responseJson;
	}
	
	
	/**
	 *  获取用户正在购买的智能卡信息
	 * @return
	 */
	@RequestMapping(value="/getBuyingCardInfo")
	public String getBuyingCardInfo(User form) {
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		//封装订户信息
		form.setUser(user);
		return "user/buyingCardInfo";
	}
	
	/**
	 * 查询智能卡信息
	 */
	@RequestMapping(value="/findCardListForDialog")
	public String findCardListForDialog(Card form) {
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		
		Stb stb = new Stb();
		stb.setStbno(form.getStbno());
		stb = stbDao.findByStbno(stb);
		if(stb ==null){
			stb = new Stb();
			stb.setVersiontype("gos_pn");
		}
		
		form.setQuerynetid(String.valueOf(user.getNetid()));//根据用户所在网络
		form.setQueryversiontype(stb.getVersiontype());
		form.setPager_openset(5);
		form.setPager_count(cardDao.findByCount(form));
		List<Card> cardlist = cardDao.findByList(form);
		for (Card card : cardlist) {
			card.setProvider(providerDao.findById(card.getProviderid()));
			card.setNetwork(networkDao.findById(card.getNetid()));
			card.setServer(serverDao.findById(card.getServerid()));
		}
		form.setCardlist(cardlist);
		
		return "user/findCardListForDialog";
	}
	
	/**
	 * 增加购买中的智能卡
	 * @return
	 */
	@ResponseBody //此标志就是返回jesion数据给页面的标志
	@RequestMapping(value="/addBuyingCard")
	public Map<String,Object> addBuyingCard(Card form) {
		//封装返回给页面的json对象
		HashMap<String,Object> responseJson = new HashMap<String,Object>();
		
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		List<Usercard> buyingcardlist = user.getBuyingcardlist();
		for (Usercard usercard : buyingcardlist) {
			if(usercard.getCardid().equals(form.getCardid())){
				responseJson.put("flag", "0");//选择智能卡失败
				responseJson.put("error", getMessage("usercard.buyingcardid.existed"));
				return responseJson;
			}
		}
		
		//封装正在购买中的智能卡
		Card card = cardService.findCardInfoByCardidStr(form.getCardid());
//		if(!StringUtils.isEmpty(card.getStbno())){
//			responseJson.put("flag", "0");//选择智能卡失败
//			responseJson.put("error", getMessage("card.stb.paired"));
//			return responseJson;
//		}
		
		Usercard usercard = new Usercard();
		usercard.setNetid(user.getNetid());
		usercard.setAreacode(user.getAreacode());
		usercard.setUserid(user.getId());
		usercard.setServerid(card.getServerid());
		usercard.setCardid(card.getCardid());
		usercard.setIncardflag(card.getIncardflag());
		if(StringUtils.isEmpty(form.getStbno())){
			usercard.setStbno(null);
		}else{
			usercard.setStbno(form.getStbno());
		}
		usercard.setState("1");
		
		//默认为主卡
		usercard.setMothercardflag("0");
		usercard.setPrice(card.getMainprice());
		//系统自动判断子母机
		//系统设置的每台主机允许的副机数量
		String sub_number_allowed = systemparaService.findSystemParaByCodeStr("number_subdevice_allowed_per_main");
		if(StringUtils.isEmpty(sub_number_allowed)){
			sub_number_allowed =  "2";
		}
		
		//查看该订户正在购买的智能卡是否有主机
		List<Usercard> buyingusercardlist = user.getBuyingcardlist();
		if(buyingusercardlist != null && buyingusercardlist.size() > 0){
			for (Usercard buyingusercard : buyingusercardlist) {//封装正在购买的智能卡
				if("0".equals(buyingusercard.getMothercardflag())){//只封装主机
					//临时保存的主机数据库存在的副机数量
					Integer sub_number_now = 0;
					//循环叠加购买中该主机的副机数量
					for (Usercard buyingusercardDmp : user.getBuyingcardlist()) {
						if("1".equals(buyingusercardDmp.getMothercardflag())){//判断是否为子机，如果是子机
							if(buyingusercard.getCardid().equals(buyingusercardDmp.getMothercardid())){//判断该子机的主机号是否等于
								sub_number_now += 1;//每找到一次就循环加1
							}
						}
					}
					//此主设备的子设备数量超出指定的数量
					if(sub_number_now < Integer.parseInt(sub_number_allowed)){
						usercard.setMothercardflag("1");
						usercard.setMothercardid(buyingusercard.getCardid());
						usercard.setPrice(card.getSubprice());
						break;
					}
				}
			}
		}
		
		//此智能卡依然为主机，再次判断已经购买的智能卡是否为主机
		if("0".equals(usercard.getMothercardflag())){
			//查看该订户已经购买智能卡是否有主机
			List<Usercard> buyedusercardlist = user.getUsercardlist();
			if(buyedusercardlist != null && buyedusercardlist.size() > 0){
				for (Usercard buyedusercard :buyedusercardlist) {//封装已经购买的智能卡
					if("0".equals(buyedusercard.getMothercardflag())){//只封装主机
						//临时保存的主机数据库存在的副机数量
						Integer sub_number_now = 0;
						//查询出该母卡已经绑定的子卡信息
						List<Usercard>  subusercardlist = usercardDao.findByMothercardid(buyedusercard.getCardid());
						if(subusercardlist != null && subusercardlist.size() > 0){
							sub_number_now += subusercardlist.size();
						}
						//循环叠加购买中的副机数量
						for (Usercard buyingusercardDmp : user.getBuyingcardlist()) {
							if("1".equals(buyingusercardDmp.getMothercardflag())){//判断是否为子机，如果是子机
								if(buyedusercard.getCardid().equals(buyingusercardDmp.getMothercardid())){//判断该子机的主机号是否等于
									sub_number_now += 1;//每找到一次就循环加1
								}
							}
						}
						
						//此主设备的子设备数量超出指定的数量
						if(sub_number_now < Integer.parseInt(sub_number_allowed)){
							usercard.setMothercardflag("1");
							usercard.setMothercardid(buyedusercard.getCardid());
							usercard.setPrice(card.getSubprice());
							break;
						}
					}
				}
			}
		}
		
		//默认为标清
		usercard.setCardflag("0");
		buyingcardlist.add(usercard);
		user.setBuyingcardlist(buyingcardlist);
		
		//找到购买中的机顶盒，修改机顶盒已经购买智能卡状态
		if(!StringUtils.isEmpty(usercard.getStbno())){//表示配对了机顶盒，修改机顶盒已经购买智能卡状态
			String stbno = usercard.getStbno();
    		
			//1-找出正在购买中的机顶盒
    		List<Userstb> buyingstblist = user.getBuyingstblist();
    		Iterator <Userstb> stbIterator = buyingstblist.iterator();  
    		while(stbIterator.hasNext()){
    			Userstb userstb =  stbIterator.next();
    			//找到配对的机顶盒
    			if(userstb.getStbno().equals(stbno)){
    				//设置该机顶盒已经绑定了智能卡
        			userstb.setBuycardflag("1");
    				//stbIterator.remove();
    			}
    		}
    		user.setBuyingstblist(buyingstblist);
    		
    		//2-找出已经购买的的机顶盒
    		List<Userstb> userstblist = user.getUserstblist();
    		for (Userstb userstb : userstblist) {
    			//找到配对的机顶盒
    			if(userstb.getStbno().equals(stbno)){
    				//设置该机顶盒已经绑定了智能卡
        			userstb.setBuycardflag("1");
    				//stbIterator.remove();
    			}
			}
    		user.setUserstblist(userstblist);
		}
		
		operator.setUser(user);
		getSession().setAttribute("Operator", operator);
		responseJson.put("flag", "1");//选择智能卡成功
		
		return responseJson;
	}
	
	/**
	 * 删除购买中的智能卡
	 * @return
	 */
	@ResponseBody //此标志就是返回jesion数据给页面的标志
	@RequestMapping(value="/deleteBuyingOtherfee")
	public Map<String,Object> deleteBuyingOtherfee(Businesstype form) {
		//封装返回给页面的json对象
		HashMap<String,Object> responseJson = new HashMap<String,Object>();
		
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		List<Businesstype> buyingotherfeelist = user.getBuyingotherfeelist();
		Iterator <Businesstype> otherfeeIterator = buyingotherfeelist.iterator();  
		while(otherfeeIterator.hasNext()) { 
			//得到此智能卡信息
			Businesstype businesstype = otherfeeIterator.next();
			if(businesstype.getTypekey().equals(form.getTypekey())) {  
				//删除此其他收费
				otherfeeIterator.remove();  
		    }  
		}
		//封装正在购买中的智能卡
		user.setBuyingotherfeelist(buyingotherfeelist);
		
		operator.setUser(user);
		getSession().setAttribute("Operator", operator);
		responseJson.put("flag", "1");//选择智能卡成功
		
		return responseJson;
	}
	
	/**
	 * 删除购买中的智能卡
	 * @return
	 */
	@ResponseBody //此标志就是返回jesion数据给页面的标志
	@RequestMapping(value="/deleteBuyingCard")
	public Map<String,Object> deleteBuyingCard(Card form) {
		//封装返回给页面的json对象
		HashMap<String,Object> responseJson = new HashMap<String,Object>();
		
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		List<Usercard> buyingcardlist = user.getBuyingcardlist();
		Iterator <Usercard> cardIterator = buyingcardlist.iterator();  
		while(cardIterator.hasNext()) { 
			//得到此智能卡信息
			Usercard usercard = cardIterator.next();
			if(usercard.getCardid().equals(form.getCardid())) {  
			    
				//删除智能卡
				cardIterator.remove();  
				
				//修改该智能卡配对的机顶盒购买智能卡状态
				if(!StringUtils.isEmpty(usercard.getStbno())){//表示配对了机顶盒，删除机顶盒信息
					String stbno = usercard.getStbno();
		    		//1-找出正在购买中的机顶盒
		    		List<Userstb> buyingstblist = user.getBuyingstblist();
		    		Iterator <Userstb> stbIterator = buyingstblist.iterator();  
		    		while(stbIterator.hasNext()){
		    			Userstb userstb =  stbIterator.next();
		    			//找到配对的机顶盒，且该机顶盒不是库存就配对的，就一起删除掉
		    			if(userstb.getStbno().equals(stbno)){
		    				//判断该机顶盒是否在库存中就配对该智能卡
		    				if("1".equals(userstb.getPaircardflag())){//如果是，就一起删除
		    					stbIterator.remove();
		    				}else{//如果不是，就修改该机顶盒已经还未购买机顶盒标志
		    					userstb.setBuycardflag("0");
		    				}
		    				
		    			}
		    		}
		    		user.setBuyingstblist(buyingstblist);
		    		
		    		//2-找出已经购买的的机顶盒
		    		List<Userstb> userstblist = user.getUserstblist();
		    		for (Userstb userstb : userstblist) {
		    			//找到配对的机顶盒
		    			if(userstb.getStbno().equals(stbno)){
		    				//修改该机顶盒已经还未购买机顶盒标志
		        			userstb.setBuycardflag("0");
		    				//stbIterator.remove();
		    			}
					}
		    		user.setUserstblist(userstblist);
				}
		    	
		    	//删除该智能卡购买中的产品
	    		List<Userproduct> buyingproductlist = user.getBuyingproductlist();
	    		Iterator <Userproduct> productIterator = buyingproductlist.iterator();  
	    		while(productIterator.hasNext()){
	    			if(productIterator.next().getCardid().equals(form.getCardid())){
	    				productIterator.remove();
	    			}
	    		}
	    		//重新封装正在购买中的智能卡
	    		user.setBuyingproductlist(buyingproductlist);
		    }  
		}
		//封装正在购买中的机顶盒
		user.setBuyingcardlist(buyingcardlist);
		
		operator.setUser(user);
		getSession().setAttribute("Operator", operator);
		responseJson.put("flag", "1");//选择智能卡成功
		
		return responseJson;
	}
	
	/**
	 * 增加购买中的其他收费
	 * @return
	 */
	@ResponseBody //此标志就是返回jesion数据给页面的标志
	@RequestMapping(value="/addBuyingOtherfee")
	public Map<String,Object> addBuyingOtherfee(Businesstype form) {
		//封装返回给页面的json对象
		HashMap<String,Object> responseJson = new HashMap<String,Object>();
		
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		
		//判断该操作员是否拥有操作产品收视优惠的权限
		if("产品收视优惠".equals(form.getTypekey())){
			if(!"1".equals(operator.getRemark())){
				responseJson.put("flag", "0");//该操作员没有操作产品收视优惠的权限
				responseJson.put("error", "该操作员没有添加产品收视优惠的权限");//该操作员没有操作产品收视优惠的权限
				return responseJson;
			}
		}
		
		List<Businesstype> buyingotherfeelist = user.getBuyingotherfeelist();
		//临时数组，用来保存购买中的其他费项
		List<String> dmpBuyingtypekeyList = new ArrayList<String>();
		for (Businesstype businesstype : buyingotherfeelist) {
			dmpBuyingtypekeyList.add(businesstype.getTypekey());
		}
		
		if(!dmpBuyingtypekeyList.contains(form.getTypekey())){//业务类型不在购买列表中
			
			Businesstype businesstype = new Businesstype();
			businesstype.setTypekey(form.getTypekey());
			businesstype.setPrice(form.getPrice());
			Businesstype businesstypedep = businesstypeDao.findByTypekeyStr(form.getTypekey());
			businesstype.setTypename(businesstypedep.getTypename());
			businesstype.setFeename(businesstypedep.getFeename());
			businesstype.setRemark(form.getRemark());
			buyingotherfeelist.add(businesstype);
		}
		
		user.setBuyingotherfeelist(buyingotherfeelist);
		
		operator.setUser(user);
		getSession().setAttribute("Operator", operator);
		responseJson.put("flag", "1");//选择智能卡成功
		
		return responseJson;
	}
	
	/**
	 * 获取收取中的其他费用Json
	 */
	@ResponseBody
	@RequestMapping(value = "/getBuyingOtherfeeJson")
	public Map<String, Object> getBuyingOtherfeeJson(Businesstype form) {
		//封装JSon的Map
		Map<String, Object> result = new HashMap<String, Object>(); 
		
		List<HashMap<String, Object>> objectlist = new ArrayList<HashMap<String, Object>>();
		
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		List<Businesstype> buyingotherfeelist = user.getBuyingotherfeelist();
		Integer total = buyingotherfeelist.size();
		
		for (Businesstype businesstype : buyingotherfeelist) {
			HashMap<String, Object> objectMap = new HashMap<String, Object>();
			//产品信息
			objectMap.put("id", businesstype.getTypekey());
			objectMap.put("typekey", businesstype.getTypekey());
			objectMap.put("typename", businesstype.getTypename());
			objectMap.put("price", businesstype.getPrice());
			objectMap.put("feename", businesstype.getFeename());
			objectMap.put("remark", businesstype.getRemark());
			objectlist.add(objectMap);
		}
		
		result.put("total", total);//页面总数
		result.put("rows", objectlist);
		return result;
	}
	
	
	/**
	 *  获取用户正在购买的产品信息
	 * @return
	 */
	@RequestMapping(value="/getBuyingProductInfo")
	public String getBuyingProductInfo(User form) {
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		//封装订户信息
		form.setUser(user);
		return "user/buyingProductInfo";
	}
	
	/**
	 * 查询购买的产品信息
	 */
	@RequestMapping(value="/findProductListForDialog")
	public String findProductListForDialog(Product form) {
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		if("".equals(user.getUserlevelid()) || null == user.getUserlevelid()){
			form.setQuerynetid(String.valueOf(user.getNetid()));
			form.setQuerystate("1");
			form.setPager_openset(5);
			form.setPager_count(productDao.findByCount(form));
			List<Product> productlist = productDao.findByList(form);
			for (Product product : productlist) {
				product.setNetwork(networkDao.findById(product.getNetid()));
			}
			form.setProductlist(productlist);
			return "user/findProductListForDialog";
		}
		Userlevelproduct userlevelproductform = new Userlevelproduct(); 
		userlevelproductform.setUserlevelid(user.getUserlevelid());
		userlevelproductform.setNetid(user.getNetid());
		if(!"".equals(form.getQueryproductid()) && null != form.getQueryproductid()){
			userlevelproductform.setQueryproductid(form.getQueryproductid());
		}
		if(!"".equals(form.getQueryproductname()) && null != form.getQueryproductname()){
			userlevelproductform.setQueryproductname(form.getQueryproductname());
		}
		Integer count = userlevelproductDao.findeffectiveproductCount(userlevelproductform);
		userlevelproductform.setPager_openset(5);
		//userlevelproductform.setPager_count(count);
		form.setPager_openset(5);
		form.setPager_count(count);
		List<Userlevelproduct> userlevleproductlist = userlevelproductDao.queryeffectiveproduct(userlevelproductform);
		List<Product> productlist = new ArrayList<Product>();
		for(Userlevelproduct userlevelproduct : userlevleproductlist){
			Product product = new Product();
			product.setNetwork(networkDao.findById(user.getNetid()));
			product.setProductid(userlevelproduct.getProductid());
			product.setProductname(userlevelproduct.getProductname());
			product.setPricepermonth(userlevelproduct.getPricepermonth());
			product.setPriceperday(userlevelproduct.getPriceperday());
			product.setSubpricepermonth(userlevelproduct.getSubpricepermonth());
			product.setSubpriceperday(userlevelproduct.getSubpriceperday());
			product.setState(userlevelproduct.getState());
			productlist.add(product);
		}
		form.setProductlist(productlist);
		
		return "user/findProductListForDialog";
	}
	
	/**
	 * 增加购买中的产品
	 * @return
	 */
	@ResponseBody //此标志就是返回jesion数据给页面的标志
	@RequestMapping(value="/addBuyingProduct")
	public Map<String,Object> addBuyingProduct(Product form) {
		//封装返回给页面的json对象
		HashMap<String,Object> responseJson = new HashMap<String,Object>();
		
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		
		//封装正在购买中的产品
		form.setNetid(user.getNetid());
		Product product = new Product();
		if("".equals(user.getUserlevelid()) || null == user.getUserlevelid()){
			product = productDao.findByProductid(form);
		}else{
			Userlevelproduct userlevelproductform = new Userlevelproduct();
			userlevelproductform.setQueryproductid(form.getProductid());
			userlevelproductform.setNetid(user.getNetid());
			userlevelproductform.setUserlevelid(user.getUserlevelid());
			Userlevelproduct userlevelproduct = userlevelproductDao.findeffectiveproduct(userlevelproductform);
			product.setProductid(userlevelproduct.getProductid());
			product.setProductname(userlevelproduct.getProductname());
			product.setPricepermonth(userlevelproduct.getPricepermonth());
			product.setPriceperday(userlevelproduct.getPriceperday());
			product.setSubpricepermonth(userlevelproduct.getSubpricepermonth());
			product.setSubpriceperday(userlevelproduct.getSubpriceperday());
		}
		
		String terminalid = "";//终端号
		String termainaltype = "";//终端类型
		Integer serverid = null;//CAS服务器ID
		if(StringUtils.isNotEmpty(form.getCardid())){//智能卡购买
			Card card = cardDao.findByCardidStr(form.getCardid());
			serverid = card.getServerid();
			terminalid = form.getCardid();
			//验证智能卡号是否存在
			String flag = "0";
			List<Usercard> usercardlist = user.getBuyingcardlist();
			for(Usercard usercard : usercardlist){
				if(terminalid.equals(usercard.getCardid()) || terminalid == usercard.getCardid()){
					flag = "1";
					break;
				}
			}
			if(flag == "0"){//不在正在购买的智能卡列表中
				usercardlist = usercardDao.findByUserid(user.getId());
				for(Usercard usercard : usercardlist){
					if(terminalid.equals(usercard.getCardid()) || terminalid == usercard.getCardid()){
						flag = "1";
						break;
					}
				}
			}
			if(flag == "0"){//不存在
				responseJson.put("flag", flag);
				responseJson.put("error", getMessage("购买产品失败"));
				return responseJson;
			}
			
			termainaltype = "1";
		}else if(StringUtils.isNotEmpty(form.getStbno())){//无卡机顶盒购买
			terminalid = form.getStbno();
			termainaltype = "0";
			Stb stb = stbDao.findByStbnoStr(form.getStbno());
			serverid = stb.getServerid();
		}
		
		//判断子母卡购买
		String mothercardflag = form.getMothercardflag();
		String mothercardid = form.getMothercardid();
		
		//判断购买的产品是否在购买列表中
		List<Userproduct> buyingproductlist = user.getBuyingproductlist();
		for (Userproduct userproduct : buyingproductlist) {
			if(terminalid.equals(userproduct.getTerminalid()) && form.getProductid().equals(userproduct.getProductid())){
				responseJson.put("flag", "0");//选择产品失败
				responseJson.put("error", getMessage("userproduct.buyingproductid.existed"));
				return responseJson;
			}
		}
		
		//判断该智能卡机正在购买中的基本包是否包含该授权时间段
		Userproduct userproductForm = new Userproduct();
		userproductForm.setUserid(user.getId());
		//查询数据中每张智能卡购买产品的最大授权时间
		HashMap<String,HashMap<String,String>> allProductLastEndTime  = userproductService.findAllProductLastEndTimeListByUserid(userproductForm);
		//先找出该智能卡购买的所有产品的最大授权时间MAP
		HashMap<String,String> userproductHP = allProductLastEndTime.get(terminalid);
		//购买中的产品不是基本包，得判断该智能卡是否购买了基本包的授权时间,
		if(!"1".equals(form.getProductid())){
			//先得到已经购买列表中该智能卡的购买基本包时间段
			String buyedEndtime_base = "";//基本包的购买结束日期
			for(Userproduct userproduct : buyingproductlist){
				if(terminalid.equals(userproduct.getTerminalid()) && "1".equals(userproduct.getProductid())){
					buyedEndtime_base = userproduct.getEndtime();
				}
			}
			
			//buyedEndtime_base为空，表示在正在购买列表中未找到该基本包的购买记录，此时还得去数据中获取基本包的最大授权时间
			if(StringUtils.isEmpty(buyedEndtime_base)){
				if(userproductHP != null ){//该智能卡已经购买了一些产品，
					//获取该智能卡已经购买的基本包的数据库最大授权时间
					buyedEndtime_base = userproductHP.get("1");
				}
			}
			
			if(StringUtils.isNotEmpty(buyedEndtime_base)){//该智能卡已经购买了该产品，找出该智能卡购买该产品的最大授权时间
				//基本包的最大授权时间与当前付费产品的购买到期时间进行比较
				int flag = Tools.compare_date(buyedEndtime_base,form.getEndtime(),"yyyy-MM-dd");
				if(flag < 0 ){//表示该产品的购买时间超过了基本包的授权时间
					responseJson.put("flag", "0");//选择产品失败
					responseJson.put("error", getMessage("购买时间超过了基本包的授权时间"));
					return responseJson;
				}
			}else{
				responseJson.put("flag", "0");//选择产品失败
				responseJson.put("error", getMessage("该智能卡还未购买基本包"));
				return responseJson;
			}
		}else{//购买的产品为基本包，如果是副机购买，则需要判断该副机的主机购买的基本包授权结束时间
			if("1".equals(mothercardflag)){//购买产品的终端为副机，则需要判断副机是否继承主机授权
				//查询参数设置-子卡是否继承母卡授权
				String systempara = systemparaService.findSystemParaByCodeStr("sub_hold_main_flag");
				if("1".equals(systempara)){//系统设置为1-继承。则子卡不能单独购买产品
					responseJson.put("flag", "0");//选择产品失败
					responseJson.put("error", getMessage("userproduct.subholdmain"));
					return responseJson;
				}else{//系统设置为0-不继承。则判断子卡购买的基本包时间不能超过主机的基本包授权时间
					//得到已经购买列表中该智能卡的主机购买基本包时间段
					String buyedEndtime_base = "";//主机的基本包的购买结束日期
					for(Userproduct userproduct : buyingproductlist){
						if(mothercardid.equals(userproduct.getTerminalid()) && "1".equals(userproduct.getProductid())){
							buyedEndtime_base = userproduct.getEndtime();
						}
					}
					
					//buyedEndtime_base为空，表示在正在购买列表中未找到该基本包的购买记录，此时还得去数据中获取主机基本包的最大授权时间
					if(StringUtils.isEmpty(buyedEndtime_base)){
						//先找出该智能卡购买的所有产品的最大授权时间MAP
						HashMap<String,String> userproductHP_base = allProductLastEndTime.get(mothercardid);
						if(userproductHP_base != null ){//该智能卡已经购买了一些产品，
							//获取该智能卡已经购买的基本包的数据库最大授权时间
							buyedEndtime_base = userproductHP_base.get("1");
						}
					}
					
					if(StringUtils.isNotEmpty(buyedEndtime_base)){//该智能卡的主机已经购买了基本包
						//主机的基本包的最大授权时间与当前付费产品的购买到期时间进行比较
						int flag = Tools.compare_date(buyedEndtime_base,form.getEndtime(),"yyyy-MM-dd");
						if(flag < 0 ){//表示该产品的购买时间超过了基本包的授权时间
							responseJson.put("flag", "0");//选择产品失败
							responseJson.put("error", getMessage("副机购买基本包时间超过了主机基本包的授权时间"));
							return responseJson;
						}
					}else{
						responseJson.put("flag", "0");//选择产品失败
						responseJson.put("error", getMessage("该智能卡还未购买基本包"));
						return responseJson;
					}
				}
			}
		}
		
		Userproduct userproduct = new Userproduct();
		userproduct.setUserid(user.getId());
		userproduct.setNetid(user.getNetid());
		userproduct.setAreacode(user.getAreacode());
		userproduct.setServerid(serverid);
		userproduct.setTerminalid(terminalid);
		userproduct.setTerminaltype(termainaltype);
		userproduct.setCardid(form.getCardid());
		userproduct.setStbno(form.getStbno());
		userproduct.setProductid(product.getProductid());
		userproduct.setProductname(product.getProductname());
		userproduct.setStarttime(form.getStarttime() + " 00:00:00");
		userproduct.setEndtime(form.getEndtime() + " 23:59:59");
		userproduct.setUnit(form.getUnit());
		userproduct.setBuyamount(form.getBuyamount());
		BigDecimal price = new BigDecimal(0);
		if("month".equals(form.getUnit())){//按月购买
			if("1".equals(mothercardflag)){//副机购买
				price = product.getSubpricepermonth();
			}else{//主机购买
				price = product.getPricepermonth();
			}
		}else if("day".equals(form.getUnit())){//按天购买
			if("1".equals(mothercardflag)){//副机购买
				price = product.getSubpriceperday();
			}else{//主机购买
				price = product.getPriceperday();
			}
		}
		userproduct.setPrice(price);
		userproduct.setTotalmoney(price.multiply(new BigDecimal(form.getBuyamount())));
		userproduct.setType("1");
		userproduct.setSource("0");
		userproduct.setState("1");
		userproduct.setRemark(String.valueOf(operator.getId()));
		buyingproductlist.add(userproduct);
		user.setBuyingproductlist(buyingproductlist);
		
		operator.setUser(user);
		getSession().setAttribute("Operator", operator);
		responseJson.put("flag", "1");//选择产品成功
		
		return responseJson;
	}
	
	/**
	 * 删除购买中的产品
	 * @return
	 */
	@ResponseBody //此标志就是返回jesion数据给页面的标志
	@RequestMapping(value="/deleteBuyingProduct")
	public Map<String,Object> deleteBuyingProduct(Userproduct form) {
		//封装返回给页面的json对象
		HashMap<String,Object> responseJson = new HashMap<String,Object>();
		
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		List<Userproduct> buyingproductlist = user.getBuyingproductlist();
		Iterator <Userproduct> productIterator = buyingproductlist.iterator();  
		
		while(productIterator.hasNext()) { 
			 Userproduct userproduct = productIterator.next();
		    if(userproduct.getTerminalid().equals(form.getTerminalid()) && userproduct.getProductid().equals(form.getProductid())) {  
		    	productIterator.remove();  
		    }  
		}
		//封装正在购买中的产品
		user.setBuyingproductlist(buyingproductlist);
		
		operator.setUser(user);
		getSession().setAttribute("Operator", operator);
		responseJson.put("flag", "1");
		
		return responseJson;
	}
	
	/**
	 *  获取用户已经购买的智能卡信息
	 * @return
	 */
	@RequestMapping(value="/getUserCardInfo")
	public String getUserCardInfo(User form) {
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		
		//封装机顶盒信息
		Userstb userstb = new Userstb();
		userstb.setUserid(user.getId());
		List<Userstb> userstblist = userstbDao.queryByList(userstb);
		user.setUserstblist(userstblist);
		
		//封装智能卡信息
		Usercard usercard = new Usercard();
		usercard.setUserid(user.getId());
		List<Usercard> usercardlist = usercardDao.queryByList(usercard);
		user.setUsercardlist(usercardlist);
		//封装订户信息
		form.setUser(user);
		return "user/userCardInfo";
	}
	
	/**
	 * 查询机顶盒信息
	 */
	@RequestMapping(value="/findTransferUserListForDialog")
	public String findTransferUserListForDialog(User form) {
		Operator operator =  (Operator)getSession().getAttribute("Operator");
//		User userform = operator.getUser();
//		form.setQuerynetid(String.valueOf(userform.getNetid()));//根据用户所在网络
//		form.setPager_openset(5);
//		form.setPager_count(userDao.findByCount(form));
//		List<User> userlist = userDao.findByList(form);
//		for (User user : userlist) {
//			Network network = networkDao.findById(user.getNetid()); 
//			user.setNetwork(network);
//			Area area = new Area();
//            area.setNetid(user.getNetid());
//            area.setAreacode(user.getAreacode());
//			user.setArea(areaDao.findByAreacode(area));
//		}
//		
//		form.setUserlist(userlist);
		
		return "user/findTransferUserListForDialog";
	}
    
	/**
	 * 选择过户新用户
	 * @return
	 */
	@ResponseBody //此标志就是返回jesion数据给页面的标志
	@RequestMapping(value="/getTransferUser")
	public Map<String,Object> getTransferUser(User form) {
		//封装返回给页面的json对象
		HashMap<String,Object> responseJson = new HashMap<String,Object>();
		
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		if(user.getId() == form.getId()){
			responseJson.put("flag", "0");//选择过户新订户失败
			responseJson.put("error", getMessage("user.transferuser.oneself"));
			return responseJson;
		}
		
		//封装新过户的订户
		User transferuser = userDao.findById(form.getId());
		if(transferuser == null){
			transferuser = new User();
		}
		
		if(user.getNetid() != transferuser.getNetid()){
			responseJson.put("flag", "0");//选择过户新订户失败
			responseJson.put("error", getMessage("user.transferuser.netid.same"));
			return responseJson;
		}
		
		transferuser.setNetwork(networkDao.findById(transferuser.getNetid()));
		Area area = new Area();
		area.setAreacode(transferuser.getAreacode());
		area.setNetid(transferuser.getNetid());
		transferuser.setArea(areaDao.findByAreacode(area));
		
		user.setTransferuser(transferuser);
		
		operator.setUser(user);
		getSession().setAttribute("Operator", operator);
		responseJson.put("flag", "1");//选择过户新订户成功
		
		return responseJson;
	}
	
	/**
	 *  获取用户已经购买的设备信息
	 * @return
	 */
	@RequestMapping(value="/getReplaceDeviceInfo")
	public String getReplaceDeviceInfo(User form) {
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		//封装订户信息
		form.setUser(user);
		return "user/replaceDeviceInfo";
	}
	
	/**
	 * 查询机顶盒信息
	 */
	@RequestMapping(value="/findReplaceStbListForDialog")
	public String findReplaceStbListForDialog(Stb form) {
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		//旧机顶盒的信息
		Stb oldstb = stbDao.findByStbno(form);
		//新的机顶盒必须要跟机顶盒信息一样的前端服务器SERVER，才能完成替换
		Server server = serverDao.findById(oldstb.getServerid());
		form.setQueryserverid(String.valueOf(oldstb.getServerid()));
		form.setServer(server);
		
		form.setQuerynetid(String.valueOf(user.getNetid()));//根据用户所在网络
		form.setPager_openset(5);
		form.setPager_count(stbDao.findByCount(form));
		List<Stb> stblist = stbDao.findByList(form);
		for (Stb stb : stblist) {
			stb.setProvider(providerDao.findById(stb.getProviderid()));
			stb.setNetwork(networkDao.findById(stb.getNetid()));
			stb.setServer(serverDao.findById(stb.getServerid()));
			Card card = cardDao.findByStbnoStr(stb.getStbno());
			if(card != null){
				stb.setPaircardid(card.getCardid());
			}
		}
		form.setStblist(stblist);
		
		return "user/findReplaceStbListForDialog";
	}
	
	
	/**
	 * 选择新更换的机顶盒
	 * @return
	 */
	@ResponseBody //此标志就是返回jesion数据给页面的标志
	@RequestMapping(value="/getReplaceStb")
	public Map<String,Object> getReplaceStb(Stb form) {
		//封装返回给页面的json对象
		HashMap<String,Object> responseJson = new HashMap<String,Object>();
		
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		Stb replaceStb = stbService.findStbInfoByStbnoStr(form.getReplacestbno());
		//Stb oldStb = stbService.findStbInfoByStbnoStr(form.getStbno());
		//获取旧的订户机顶盒信息
		Userstb oldUserstb = userstbDao.findByStbnoStr(form.getStbno());
		//外置卡机顶盒只能换外置卡机顶盒，内置卡机顶盒只能换内置卡机顶盒
//		if(!oldStb.getIncardflag().equals(replaceStb.getIncardflag())){
//			responseJson.put("flag", "0");//选择更换机顶盒失败
//			responseJson.put("error", getMessage("user.replacestb.type.same"));
//			return responseJson;
//		}
		
//		//判断新更换机顶盒是否有配对关系
//		Card card = cardDao.findByStbnoStr(form.getReplacestbno());
//		if(card != null){
//			responseJson.put("flag", "0");//选择更换机顶盒失败
//			responseJson.put("error", getMessage("card.stb.paired"));
//			return responseJson;
//		}
		
		//封装正在购买中的机顶盒
		replaceStb.setNetwork(networkDao.findById(replaceStb.getNetid()));
		replaceStb.setServer(serverDao.findById(replaceStb.getServerid()));
		replaceStb.setProvider(providerDao.findById(replaceStb.getProviderid()));
		if("1".equals(oldUserstb.getMothercardflag())){//子机
			replaceStb.setSaleprice(replaceStb.getSubprice());
		}else{ //否则都为母机价格
			replaceStb.setSaleprice(replaceStb.getMainprice());
		}
		
		user.setReplacestb(replaceStb);
		
		operator.setUser(user);
		getSession().setAttribute("Operator", operator);
		responseJson.put("flag", "1");//选择过户新订户成功
		
		return responseJson;
	}
	
	/**
	 * 查询智能卡信息
	 */
	@RequestMapping(value="/findReplaceCardListForDialog")
	public String findReplaceCardListForDialog(Card form) {
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		form.setQuerynetid(String.valueOf(user.getNetid()));//根据用户所在网络
		
		//旧机顶盒的信息
		Card oldcard = cardDao.findByCardid(form);
		//新的机顶盒必须要跟机顶盒信息一样的前端服务器SERVER，才能完成替换
		Server server = serverDao.findById(oldcard.getServerid());
		form.setQueryserverid(String.valueOf(oldcard.getServerid()));
		form.setServer(server);
		
		form.setPager_openset(5);
		form.setPager_count(cardDao.findByCount(form));
		List<Card> cardlist = cardDao.findByList(form);
		for (Card card : cardlist) {
			card.setProvider(providerDao.findById(card.getProviderid()));
			card.setNetwork(networkDao.findById(card.getNetid()));
			card.setServer(serverDao.findById(card.getServerid()));
		}
		form.setCardlist(cardlist);
		
		return "user/findReplaceCardListForDialog";
	}
	
	/**
	 * 选择新更换的智能卡
	 * @return
	 */
	@ResponseBody //此标志就是返回jesion数据给页面的标志
	@RequestMapping(value="/getReplaceCard")
	public Map<String,Object> getReplaceCard(Card form) {
		//封装返回给页面的json对象
		HashMap<String,Object> responseJson = new HashMap<String,Object>();
		
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		Card replaceCard = cardService.findCardInfoByCardidStr(form.getReplacecardid());
		//Card oldCard  = cardService.findCardInfoByCardidStr(form.getCardid());
		//获取旧的订户智能卡信息
		Usercard oldUsercard = usercardDao.findByCardidStr(form.getCardid());
		//外置卡只能换外置卡，内置卡只能换内置卡
//		if(!oldStb.getIncardflag().equals(replaceStb.getIncardflag())){
//			responseJson.put("flag", "0");//选择更换机顶盒失败
//			responseJson.put("error", getMessage("user.replacestb.type.same"));
//			return responseJson;
//		}
		
//		//判断新更换机顶盒是否有配对关系
//		if(!StringUtils.isEmpty(replaceCard.getStbno())){
//			responseJson.put("flag", "0");//选择更换机顶盒失败
//			responseJson.put("error", getMessage("card.stb.paired"));
//			return responseJson;
//		}
		
		//封装正在购买中的机顶盒
		replaceCard.setNetwork(networkDao.findById(replaceCard.getNetid()));
		replaceCard.setServer(serverDao.findById(replaceCard.getServerid()));
		replaceCard.setProvider(providerDao.findById(replaceCard.getProviderid()));
		if("1".equals(oldUsercard.getMothercardflag())){//子机
			replaceCard.setSaleprice(replaceCard.getSubprice());
		}else{ //否则都为母机价格
			replaceCard.setSaleprice(replaceCard.getMainprice());
		}
		
		user.setReplacecard(replaceCard);
		
		operator.setUser(user);
		getSession().setAttribute("Operator", operator);
		responseJson.put("flag", "1");//
		
		return responseJson;
	}
	
	/**
	 * 验收设备是否能够回收-如果有副机，请先回收副机
	 * @return
	 */
	@ResponseBody //此标志就是返回jesion数据给页面的标志
	@RequestMapping(value="/validRebakDevice")
	public Map<String,Object> validRebakDevice(Userproduct form) {
		//封装返回给页面的json对象
		HashMap<String,Object> responseJson = new HashMap<String,Object>();
		
		if("0".equals(form.getTerminaltype())){//机顶盒
			
			Userstb usestb = userstbDao.findByStbnoStr(form.getTerminalid());
			if("2".equals(usestb.getIncardflag())){//无卡机顶盒
				List<Userstb> userstblist = userstbDao.findByMotherstbno(form.getTerminalid());
				if(userstblist != null && userstblist.size() > 0){//表示该终端有副机，请先回收副机
					responseJson.put("flag", "0");//回收失败
					responseJson.put("error", getMessage("userbusiness.rebackdevice.sub.exist"));
					return responseJson;
				}
			}else if("0".equals(usestb.getIncardflag()) ||  "1".equals(usestb.getIncardflag())){
				
				String send_stbcardpair_flag = systemparaService.findSystemParaByCodeStr("send_stbcardpair_flag");
				if("1".equals(send_stbcardpair_flag)){//系统需要机卡绑定
					//查询是否有绑定的智能卡
					Usercard usercard = usercardDao.findByStbnoStr(usestb.getStbno());
					if(usercard != null){
						responseJson.put("flag", "0");//回收失败
						responseJson.put("error", getMessage("usercard.userstb.binded"));
						return responseJson;
					}
				}
				
			}
			
			
		}else if("1".equals(form.getTerminaltype())){//智能卡
			
			List<Usercard> usercardlist = usercardDao.findByMothercardid(form.getTerminalid());
			if(usercardlist != null && usercardlist.size() > 0){//表示该终端有副机，请先回收副机
				responseJson.put("flag", "0");//回收失败
				responseJson.put("error", getMessage("userbusiness.rebackdevice.sub.exist"));
				return responseJson;
			}
			
			String send_stbcardpair_flag = systemparaService.findSystemParaByCodeStr("send_stbcardpair_flag");
			if("1".equals(send_stbcardpair_flag)){//系统需要机卡绑定
				//查询是否有绑定的智能卡
				Usercard usercard = usercardDao.findByCardidStr(form.getTerminalid());
				if(!StringUtils.isEmpty(usercard.getStbno())){
					responseJson.put("flag", "0");//回收失败
					responseJson.put("error", getMessage("usercard.userstb.binded"));
					return responseJson;
				}
			}
		}
		
		responseJson.put("flag", "1");//
		return responseJson;
	}
	
	
	/**
	 * 弹出窗口查询用户条件
	 */
	@RequestMapping(value="/queryUserForDialog")
	public String queryUserForDialog(User form) {
		// 构建上级网络Map对象
		List<Network> list = networkDao.queryByList(new Network());
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Network network = (Network) iterator.next();
			map.put(network.getId(), network.getNetname());
		}
		form.setNetworkmap(map);
		
		return "user/queryUserForDialog";
	}
	
	/**
	 * 弹出窗口查询用户信息
	 */
	@RequestMapping(value="/findByListForDialog")
	public String findByListForDialog(User form) {
		form.setPager_openset(10);
		form.setPager_count(userDao.findByCount(form));
		List<User> userlist = userDao.findByList(form);
		for (User user : userlist) {
			user.setNetwork(networkDao.findById(user.getNetid()));
		}
		form.setUserlist(userlist);
		
		// 构建上级网络Map对象
		List<Network> list = networkDao.queryByList(new Network());
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Network network = (Network) iterator.next();
			map.put(network.getId(), network.getNetname());
		}
		form.setNetworkmap(map);
		
		return "user/findUserListForDialog";
	}
	
	/**
	 * 切换订户信息
	 * @return
	 */
	@ResponseBody //此标志就是返回jesion数据给页面的标志
	@RequestMapping(value="/switchUserInfo")
	public Map<String,Object> switchUserInfo(User form) {
		//封装返回给页面的json对象
		HashMap<String,Object> responseJson = new HashMap<String,Object>();
		User user =  userDao.findById(form.getId());
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		if(user != null ){
			operator.setUser(user);
			getSession().setAttribute("Operator", operator);
			responseJson.put("flag", "1");
			//System.out.println("1111111111111111:" + operator.getUser().getUsername());
		}else{
			responseJson.put("flag", "0");
		}
	
		return responseJson;
	}
	
	/**
     * 订户查询-获取网络的下拉列表框值
     */
	@ResponseBody //此标签表示返回的数据不是html标签的页面，而是其他某种格式的数据时（如json、xml等）使用；
	@RequestMapping(value="/initNetworkJson")
    public List<HashMap<String, Object>> initNetworkJson(Network form) {
		//封装返回给页面的json对象
		List<HashMap<String, Object>> networksJSON = new ArrayList<HashMap<String, Object>>();
		
		Operator operator = (Operator)getSession().getAttribute("Operator");
		String operatorlevel = operator.getOperatorlevel();
		if("0".equals(operatorlevel)){//系统级别操作员
			//封装默认的请选择行数据()
			HashMap<String,Object> selectednetworkMap = new HashMap<String, Object>();
			selectednetworkMap.put("id", "");
			selectednetworkMap.put("netid", "");
			selectednetworkMap.put("netname", getMessage("page.select"));
			networksJSON.add(selectednetworkMap);
			
	    	List<Network> networkList = networkDao.queryByList(form);
	 		for (Network network : networkList) {
	 			HashMap<String,Object> networkMap = new HashMap<String, Object>();
	 			networkMap.put("id", network.getId());
	 			networkMap.put("netid", network.getNetid());
	 			networkMap.put("netname", network.getNetname());
	 			networksJSON.add(networkMap);
	        }
		} else {//不是系统级别的操作员，只能查询操作员所在的网络订户
			Network network = networkDao.findById(operator.getNetid());
			if(network == null){
				network = new Network();
			}
			HashMap<String,Object> networkMap = new HashMap<String, Object>();
			networkMap.put("id", network.getId());
 			networkMap.put("netid", network.getNetid());
 			networkMap.put("netname", network.getNetname());
 			networksJSON.add(networkMap);
		}
	    
 		return networksJSON;
	}
	
	/**
     * 订户查询-获取区域的树形下拉列表框值
     */
	@ResponseBody //此标签表示返回的数据不是html标签的页面，而是其他某种格式的数据时（如json、xml等）使用；
	@RequestMapping(value="/getAreaTreeJson")
	public List<Map<String,Object>> getAreaTreeJson(Area form) {  
		Operator operator = (Operator)getSession().getAttribute("Operator");
		String areacode = operator.getAreacode();
		//区域对象
		List<Map<String, Object>> areaTreeJSON = new ArrayList<Map<String, Object>>();
		
		//如果该操作员的区域为空，增加 请选择
		if(StringUtils.isEmpty(areacode)){
			//添加请选择项-默认为空，查询所有
		    HashMap<String,Object> selectMap = new HashMap<String, Object>();
		    selectMap.put("id", "");
		    selectMap.put("text", getMessage("page.select"));
		    areaTreeJSON.add(selectMap);
		    //先判断网络有没有选择
		    if(StringUtils.isNotEmpty(form.getQuerynetid())){
		    	form.setQueryareacode(areacode);
		    	List<Area> areaList = areaDao.queryByList(form);
			    //封装区域属性结构信息
			    for(Area area:areaList){  
			    	if(null == area.getPid()){ 
			    	    HashMap<String,Object> areaMap = new HashMap<String, Object>();
			    	    areaMap.put("id", area.getAreacode());
			    	    areaMap.put("text", area.getAreaname()+"("+area.getAreacode()+")");
			    	    
			    	    List<Map<String, Object>> children = buildNode(area.getId(),areaList);  
			    	    if(null != children && 0 < children.size()){ 
			            	areaMap.put("state", "closed");
			            	areaMap.put("children", children);
			            }  
			            areaTreeJSON.add(areaMap);  
			    	}
			    }  
		    }
		}else{
			 //先判断网络有没有选择
		    if(StringUtils.isNotEmpty(form.getQuerynetid())){
		    	form.setQueryareacode(areacode);
		    	List<Area> areaList = areaDao.queryByList(form);
			    //封装区域属性结构信息
			    for(Area area:areaList){  
			    	if(areacode.equals(area.getAreacode())){//从自己开始封装
			    		 HashMap<String,Object> areaMap = new HashMap<String, Object>();
				    	    areaMap.put("id", area.getAreacode());
				    	    areaMap.put("text", area.getAreaname()+"("+area.getAreacode()+")");
				    	    
				    	    List<Map<String, Object>> children = buildNode(area.getId(),areaList);  
				    	    if(null != children && 0 < children.size()){ 
				            	areaMap.put("state", "closed");
				            	areaMap.put("children", children);
				            }  
				            areaTreeJSON.add(areaMap);  
			    	}
			    }  
		    }
		}
		
	   return areaTreeJSON;
	}  
	
	/** 
	 * 构建区域树型菜单json数据 
	 */  
	public List<Map<String, Object>> buildNode(int pid,List<Area> areaList){  
	    List<Map<String, Object>> areaTreeJSON = new ArrayList<Map<String, Object>>();
	    for(Area area:areaList){  
	    	 HashMap<String,Object> areaMap = new HashMap<String, Object>();
	        if(null != area.getPid() && area.getPid().equals(pid)){  
	        	areaMap.put("id", area.getAreacode());
	    	    areaMap.put("text", area.getAreaname()+"("+area.getAreacode()+")");
	    	    List<Map<String, Object>> children = buildNode(area.getId(),areaList);  
	            if(null != children && 0 < children.size()){ 
	            	areaMap.put("state", "closed");
	            	areaMap.put("children", children);
	            }  
	            areaTreeJSON.add(areaMap);   
	        }  
	    }  
	    return areaTreeJSON;  
	} 
	
	@ResponseBody
	@RequestMapping(value = "/getUsercardJsonForResetbinding")
	public List<HashMap<String, Object>> getUsercardJsonForResetbinding(String stbno) {
		Operator operator = (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		List<Usercard> cardList = user.getUsercardlist();
		//封装返回给页面的json对象
		List<HashMap<String, Object>> usercardsJSON = new ArrayList<HashMap<String, Object>>();
		
		//封装默认的请选择行数据()
		HashMap<String,Object> selectednetworkMap = new HashMap<String, Object>();
		selectednetworkMap.put("id", "");
		selectednetworkMap.put("netname", getMessage("page.select"));
		usercardsJSON.add(selectednetworkMap);
		
		if(StringUtils.isNotEmpty(stbno)){
			for (Usercard usercard : cardList) {
				if (StringUtils.isEmpty(usercard.getStbno())) {//还没机卡绑定的智能卡
					HashMap<String, Object> usercardMap = new HashMap<String, Object>();
				    usercardMap.put("id", usercard.getCardid());
				    usercardMap.put("name", usercard.getCardid());
				    usercardsJSON.add(usercardMap);
				}
			}
		}
		return usercardsJSON;
	}
	
	/**
	 * 重置机卡绑定
	 */
	@RequestMapping(value="/saveResetBinding")
	public String saveResetBinding(User form){
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		
		String stbno = getRequest().getParameter("stbno");
		String cardid = getRequest().getParameter("cardid");
		
		Userstb userstb = userstbDao.findByStbnoStr(stbno);
		//1-判断该机顶盒的机卡绑定关系、
		Usercard usercardpair = usercardDao.findByStbnoStr(userstb.getStbno());
		if (usercardpair !=null) {
			form.setReturninfo(getMessage("usercard.userstb.binded"));
			return businessUnit(form);
		}
		
		Usercard usercard = usercardDao.findByCardidStr(cardid);
		usercard.setStbno(userstb.getStbno());
		usercardDao.updateStbno(usercard);
		
		//3-发送机卡绑定关系
		authorizeService.saveAuthorize_stbBingCard(userstb);
		
		form.setReturninfo(getMessage("page.execution.success"));
		return businessUnit(form);
	}
	
	/**
	 * 查询机顶盒绑定的智能卡号
	 */
	@ResponseBody
	@RequestMapping(value = "/getBindedUsercardJsonForRemovebinding")
	public List<HashMap<String, Object>> getBindedUsercardJsonForRemovebinding(String stbno) {
		Operator operator = (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		List<Usercard> cardList = user.getUsercardlist();
		//封装返回给页面的json对象
		List<HashMap<String, Object>> usercardsJSON = new ArrayList<HashMap<String, Object>>();
		
		if(StringUtils.isNotEmpty(stbno)){
			for (Usercard usercard : cardList) {
				if (stbno.equals(usercard.getStbno())) {//智能卡与机顶盒已经绑定
					HashMap<String, Object> usercardMap = new HashMap<String, Object>();
				    usercardMap.put("id", usercard.getCardid());
				    usercardMap.put("name", usercard.getCardid());
				    usercardsJSON.add(usercardMap);
				}
			}
		}
		return usercardsJSON;
	}
	
	
	/**
	 * 解除机卡绑定关系
	 */
	@RequestMapping(value="/saveRemoveBinding")
	public String saveRemoveBinding(User form){
		Operator operator =  (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		
		String stbno = getRequest().getParameter("stbno");
		
		Userstb userstb = userstbDao.findByStbnoStr(stbno);
		//1-清除该机顶盒的机卡绑定关系
		Usercard usercardpair = usercardDao.findByStbnoStr(userstb.getStbno());
		if (usercardpair !=null) {
			//解除绑定的机顶盒
			usercardpair.setStbno(null);
			usercardDao.updateStbno(usercardpair);
		}
		
		//2-发送机卡解绑关系
		authorizeService.saveAuthorize_stbBingCard(userstb);
		
		form.setReturninfo(getMessage("page.execution.success"));
		return businessUnit(form);
	}
	
	//------------------------------------------------------------------------------
	//取系统参数设置的授权到期日期，如果为空，默认为2050-12-01
	public String getExpiredTime(){
			Systempara para = new Systempara();
			para.setCode(Constant.AUTH_EXPIRED_TIME);
			para = systemparaDao.findByCode(para);
			if(para != null && !"".equals(para.getValue())){
				return para.getValue();
			}else{
				return "2050-12-01";
			}
		}
	
	//取系统参数值
	public  String getSystemParaByCode(String code){
		Systempara para = new Systempara();
		para.setCode(code);
		para = systemparaDao.findByCode(para);
		if(para != null && !"".equals(para.getValue())){
			return para.getValue();
		}else{
			return "";
		}
	}
	
	/**
     * 获取用户级别的下拉列表框值JSON
     */
	@ResponseBody //此标签表示返回的数据不是html标签的页面，而是其他某种格式的数据时（如json、xml等）使用；
	@RequestMapping(value="/initUserlevelJson")
    public List<HashMap<String, Object>> initUserlevelJson(Userlevel form) {
		//封装返回给页面的json对象
		List<HashMap<String, Object>> objectJSON = new ArrayList<HashMap<String, Object>>();
		
		//封装默认的请选择行数据()
		HashMap<String,Object> selectedoperatorMap = new HashMap<String, Object>();
		selectedoperatorMap.put("id", "");
		selectedoperatorMap.put("name", getMessage("page.select"));
		objectJSON.add(selectedoperatorMap);
	    
    	List<Userlevel> userlevels = userlevelDao.queryByList(form);
 		for (Userlevel userlevel : userlevels) {
 			HashMap<String,Object> objectMap = new HashMap<String, Object>();
 			objectMap.put("id", userlevel.getId());
 			objectMap.put("name", userlevel.getLevelname());
 			objectJSON.add(objectMap);
        }
		
 		return objectJSON;
	}
	
	/**
     * 修改订户智能卡标识
     */
	@RequestMapping(value="/updatesigncard")
    public String updatesigncard(User form) {
		//封装返回给页面的json对象
		form.setBusinesstype("businessInfo");
		//operator.setUser(user);
		//getSession().setAttribute("Operator", operator);
		Usercard usercard = usercardDao.findByCardidStr(getRequest().getParameter("cardid"));
		usercard.setCardflag(getRequest().getParameter("cardflag"));
		usercardDao.update(usercard);
		//user.setReturninfo(getMessage("page.execution.success"));
 		return businessUnit(form);
    }
	
}
	
