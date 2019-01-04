package com.gospell.boss.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gospell.boss.common.AesSecret;
import com.gospell.boss.common.ConfigUtil;
import com.gospell.boss.common.Tools;
import com.gospell.boss.dao.IDispatchDao;
import com.gospell.boss.dao.IOperatorDao;

import com.gospell.boss.dao.IAreaDao;
import com.gospell.boss.dao.IBusinesstypeDao;
import com.gospell.boss.dao.ICardDao;
import com.gospell.boss.dao.IGiftcardDao;
import com.gospell.boss.dao.IHelpinfoDao;
import com.gospell.boss.dao.IHttprequestlogDao;
import com.gospell.boss.dao.IMessageDao;
import com.gospell.boss.dao.INetworkDao;
import com.gospell.boss.dao.IProblemcomplaintDao;
import com.gospell.boss.dao.IProductDao;
import com.gospell.boss.dao.IProductservicerefDao;
import com.gospell.boss.dao.IPromotioninfoDao;
import com.gospell.boss.dao.IProviderDao;
import com.gospell.boss.dao.IServiceDao;
import com.gospell.boss.dao.IStbDao;
import com.gospell.boss.dao.ISystemparaDao;
import com.gospell.boss.dao.IUserDao;
import com.gospell.boss.dao.IUseraccountlogDao;
import com.gospell.boss.dao.IUserbusinessDao;
import com.gospell.boss.dao.IUserbusinessdetailDao;
import com.gospell.boss.dao.IUsercardDao;
import com.gospell.boss.dao.IUserfeedbackDao;
import com.gospell.boss.dao.IUserlevelDao;
import com.gospell.boss.dao.IUserlevelproductDao;
import com.gospell.boss.dao.IUserproductDao;
import com.gospell.boss.dao.IUserstbDao;
import com.gospell.boss.po.Area;
import com.gospell.boss.po.Businesstype;
import com.gospell.boss.po.Card;
import com.gospell.boss.po.Dispatch;
import com.gospell.boss.po.Giftcard;
import com.gospell.boss.po.Helpinfo;
import com.gospell.boss.po.Httprequestlog;
import com.gospell.boss.po.Message;
import com.gospell.boss.po.Network;
import com.gospell.boss.po.Operator;
import com.gospell.boss.po.Problemcomplaint;
import com.gospell.boss.po.Problemcomplaintdetail;
import com.gospell.boss.po.Product;
import com.gospell.boss.po.Productextend;
import com.gospell.boss.po.Productserviceref;
import com.gospell.boss.po.Provider;
import com.gospell.boss.po.Service;
import com.gospell.boss.po.Serviceextend;
import com.gospell.boss.po.Stb;
import com.gospell.boss.po.User;
import com.gospell.boss.po.Useraccountlog;
import com.gospell.boss.po.Userbusiness;
import com.gospell.boss.po.Userbusinessdetail;
import com.gospell.boss.po.Usercard;
import com.gospell.boss.po.Userfeedback;
import com.gospell.boss.po.Userlevel;
import com.gospell.boss.po.Userlevelproduct;
import com.gospell.boss.po.Userproduct;
import com.gospell.boss.po.Userstb;
import com.gospell.boss.service.IAuthorizeService;
import com.gospell.boss.service.IStbService;
import com.gospell.boss.service.ISystemparaService;
import com.gospell.boss.service.IUserproductService;

/**
 * 用户控制层
 */
@Controller
@Scope("prototype")
@RequestMapping("/httpForMps")
@Transactional
public class HttpForMpsController extends BaseController {

	@Autowired
	private IUserDao userDao;
	@Autowired
	private IUsercardDao usercardDao;
	@Autowired
	private IUserproductDao userproductDao;
	@Autowired
	private IUserbusinessDao userbusinessDao;
	@Autowired
	private IUserbusinessdetailDao userbusinessdetailDao;
	@Autowired
	private IUserstbDao userstbDao;
	@Autowired
	private IDispatchDao dispatchDao;
	@Autowired
	private IOperatorDao operatorDao;
	@Autowired
	private IProblemcomplaintDao problemcomplaintDao;
	@Autowired
	private IProductDao productDao;
	@Autowired
	private IServiceDao serviceDao;
	@Autowired
	private INetworkDao networkDao;
	@Autowired
	private IAreaDao areaDao;
	@Autowired
	private ISystemparaDao systemparaDao;
	@Autowired
	private IAuthorizeService authorizeService;
	@Autowired
	private IStbDao stbDao;
	@Autowired
	private IStbService stbserver;
	@Autowired
	private ICardDao cardDao;
	@Autowired
	private IGiftcardDao giftcardDao;
	@Autowired
	private IHttprequestlogDao httprequestlogDao;
	@Autowired
	private IUseraccountlogDao useraccountlogDao;
	@Autowired
	private IMessageDao messageDao;
	@Autowired
	private IUserfeedbackDao userfeedbackDao;
	@Autowired
	private IProviderDao providerDao;
	@Autowired
	private IPromotioninfoDao promotioninfoDao;
	@Autowired
	private IProductservicerefDao productservicerefDao;
	@Autowired
	private IHelpinfoDao helpinfoDao; 
	@Autowired
	private ISystemparaService systemparaService;
	@Autowired
	private IUserlevelproductDao userlevelproductDao;
	@Autowired
	private IUserproductService serproductService;
	@Autowired
	private IUserlevelDao userlevelDao;
	@Autowired
	private IBusinesstypeDao businesstypeDao; 
	
	@ResponseBody
	@RequestMapping(value = "/userLogin")
	// 用户登录
	// http://localhost:8080/boss/httpForMps/userLogin?usercode=1290000450&password=123456
	public Map<String, Object> saveUserLogin(User form, HttpServletRequest request) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String stbno = Tools.getStr(request.getParameter("stbno"));
		String cardid = Tools.getStr(request.getParameter("cardid"));
		String password = Tools.getStr(request.getParameter("password"));
		responseMap.put("status", "0");
		User user = null;
		if (stbno != "" && stbno != null) { // 扫码登录
			Stb stb_finder = new Stb();
			stb_finder.setStbno(stbno);
			Stb stb = stbDao.findByStbno(stb_finder);
			if (stb != null) {
				Userstb userstb = new Userstb();
				userstb.setStbno(stbno);
				userstb = userstbDao.findByStbno(userstb);
				if (userstb == null || userstb.getUserid() == null) {
					responseMap.put("status", "8");//此设备号没有绑定订户
				} else {
					user = userDao.findById(userstb.getUserid());
					if (user != null) {
						if(password.equals(user.getPassword())){
							String userstate = user.getState();
							if ("0".equals(userstate)) {
								responseMap.put("status", "3");//该订户已被注销
							} else if ("1".equals(userstate)) {
								responseMap = this.userInfoPackage(user, responseMap);
							} else if ("2".equals(userstate)) {
								responseMap.put("status", "4");//该订户已报停
							} else if ("3".equals(userstate)) {
								responseMap.put("status", "5");//该订户待审核
							}
						}else{
							responseMap.put("status", "6");//登录密码错误
						}
					} else {
						responseMap.put("status", "9");//此设备号绑定订户资料错误
					}
				}
			} else {
				responseMap.put("status", "7");//此设备号没有入库记录
			}
		} else if (cardid != "" && cardid != null) { //智能卡登录
			//外置卡查询
			Card card_finder = new Card();
			card_finder.setCardid(cardid);
			Card card = cardDao.findByCardid(card_finder);
			//内置卡查询
			//Stb oldStb = stbDao.findByCardidStr(cardid);
			if (card != null) {
				Usercard usercard = new Usercard();
				usercard.setCardid(cardid);
				usercard = usercardDao.findByCardid(usercard);
				if (usercard == null || usercard.getUserid() == null) {
					responseMap.put("status", "8");//此设备号没有绑定订户
				} else {
					user = userDao.findById(usercard.getUserid());
					if (user != null) {
						if(password.equals(user.getPassword())){
							String userstate = user.getState();
							if ("0".equals(userstate)) {
								responseMap.put("status", "3");//该订户已被注销
							} else if ("1".equals(userstate)) {
								responseMap = this.userInfoPackage(user, responseMap);
							} else if ("2".equals(userstate)) {
								responseMap.put("status", "4");//该订户已报停
							} else if ("3".equals(userstate)) {
								responseMap.put("status", "5");//该订户待审核
							}
						}else{
							responseMap.put("status", "6");//登录密码错误
						}
					} else {
						responseMap.put("status", "9");//此设备号绑定订户资料错误
					}
				}
			} else {
				responseMap.put("status", "7");//此设备号没有入库记录
			}
		} else { // usercode+password登录
			Integer userid = Integer.valueOf(request.getParameter("usercode"));
			user = userDao.findById(userid);
			if (user != null) {
				if(password.equals(user.getPassword())){
					String userstate = user.getState();
					if ("0".equals(userstate)) {
						responseMap.put("status", "3");//该订户已被注销
					} else if ("1".equals(userstate)) {
						responseMap = this.userInfoPackage(user, responseMap);
					} else if ("2".equals(userstate)) {
						responseMap.put("status", "4");//该订户已报停
					} else if ("3".equals(userstate)) {
						responseMap.put("status", "5");//该订户待审核
					}
				}else{
					responseMap.put("status", "6");//登录密码错误
				}
			} else {
				responseMap.put("status", "2");//订户不存在
			}
		}
		this.saveRequestRecord(request, responseMap.get("status").toString());
		return responseMap;
	}
    
	// 代买订户登录(跟订户接口一样处理，就是不需要登录密码)
	// test url:
	// http://localhost:8080/boss/httpForMps/userLogin?usercode=0190000001&password=123456
	// http://localhost:8080/boss/httpForMps/userLogin?stbno=ff201713
	// http://localhost:8080/boss/httpForMps/userLogin?cardid=00000001
	// http://localhost:8080/boss/httpForMps/saveRegisterUserByQrcode?stbno=8006542305&areacode=10001&username=1233332&mobile=123123&telephone=1233321&documenttype=%E8%BA%AB%E4%BB%BD%E8%AF%81&documentno=12333321&email=@qq.com&address=12333%E5%9C%B0%E6%96%B9&password=abcd
	@ResponseBody
	@RequestMapping(value = "/userLoginByReplace")
	public Map<String, Object> saveUserLoginByReplace(User form, HttpServletRequest request) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String stbno = Tools.getStr(request.getParameter("stbno"));
		String cardid = Tools.getStr(request.getParameter("cardid"));
		//String password = Tools.getStr(request.getParameter("password"));
		responseMap.put("status", "0");
		User user = null;
		if (stbno != "" && stbno != null) { // 扫码登录
			Stb stb_finder = new Stb();
			stb_finder.setStbno(stbno);
			Stb stb = stbDao.findByStbno(stb_finder);
			if (stb != null) {
				Userstb userstb = new Userstb();
				userstb.setStbno(stbno);
				userstb = userstbDao.findByStbno(userstb);
				if (userstb == null || userstb.getUserid() == null) {
					responseMap.put("status", "8");//此设备号没有绑定订户
				} else {
					user = userDao.findById(userstb.getUserid());
					if (user != null) {
						//if(password.equals(user.getPassword())){
							String userstate = user.getState();
							if ("0".equals(userstate)) {
								responseMap.put("status", "3");//该订户已被注销
							} else if ("1".equals(userstate)) {
								responseMap = this.userInfoPackage(user, responseMap);
							} else if ("2".equals(userstate)) {
								responseMap.put("status", "4");//该订户已报停
							} else if ("3".equals(userstate)) {
								responseMap.put("status", "5");//该订户待审核
							}
						//}else{
						//	responseMap.put("status", "6");//登录密码错误
						//}
					} else {
						responseMap.put("status", "9");//此设备号绑定订户资料错误
					}
				}
			} else {
				responseMap.put("status", "7");//此设备号没有入库记录
			}
		} else if (cardid != "" && cardid != null) { //智能卡登录
			Card card_finder = new Card();
			card_finder.setCardid(cardid);
			Card card = cardDao.findByCardid(card_finder);
			if (card != null) {
				Usercard usercard = new Usercard();
				usercard.setCardid(cardid);
				usercard = usercardDao.findByCardid(usercard);
				if (usercard == null || usercard.getUserid() == null) {
					responseMap.put("status", "8");//此设备号没有绑定订户
				} else {
					user = userDao.findById(usercard.getUserid());
					if (user != null) {
						//if(password.equals(user.getPassword())){
							String userstate = user.getState();
							if ("0".equals(userstate)) {
								responseMap.put("status", "3");//该订户已被注销
							} else if ("1".equals(userstate)) {
								responseMap = this.userInfoPackage(user, responseMap);
							} else if ("2".equals(userstate)) {
								responseMap.put("status", "4");//该订户已报停
							} else if ("3".equals(userstate)) {
								responseMap.put("status", "5");//该订户待审核
							}
						//}else{
						//	responseMap.put("status", "6");//登录密码错误
						//}
					} else {
						responseMap.put("status", "9");//此设备号绑定订户资料错误
					}
				}
			} else {
				responseMap.put("status", "7");//此设备号没有入库记录
			}
		} else { // usercode+password登录
			Integer userid = Integer.valueOf(request.getParameter("usercode"));
			user = userDao.findById(userid);
			if (user != null) {
				//if(password.equals(user.getPassword())){
					String userstate = user.getState();
					if ("0".equals(userstate)) {
						responseMap.put("status", "3");//该订户已被注销
					} else if ("1".equals(userstate)) {
						responseMap = this.userInfoPackage(user, responseMap);
					} else if ("2".equals(userstate)) {
						responseMap.put("status", "4");//该订户已报停
					} else if ("3".equals(userstate)) {
						responseMap.put("status", "5");//该订户待审核
					}
				//}else{
				//	responseMap.put("status", "6");//登录密码错误
				//}
			} else {
				responseMap.put("status", "2");//订户不存在
			}
		}
		this.saveRequestRecord(request, responseMap.get("status").toString());
		return responseMap;
	}
	
	
	
	
	// 扫机顶盒二维码，绑定订户
	// test url:
	// http://localhost:8080/boss/httpForMps/saveRegisterUserByQrcode?stbno=8006542305&areacode=10001&username=1233332&mobile=123123&telephone=1233321&documenttype=%E8%BA%AB%E4%BB%BD%E8%AF%81&documentno=12333321&email=@qq.com&address=12333%E5%9C%B0%E6%96%B9&password=abcd
	@ResponseBody
	@RequestMapping(value = "/saveRegisterUserByQrcode")
	public Map<String, Object> saveRegisterUserByQrcode(User form, HttpServletRequest request) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String stbno = request.getParameter("stbno");
		String usercode = request.getParameter("usercode");
		Stb register_stb = stbserver.findStbInfoByStbnoStr(stbno);
		if (register_stb != null) {
			Userstb userstb = new Userstb();
			userstb.setStbno(register_stb.getStbno());
			if (userstbDao.findByStbno(userstb) != null) {
				responseMap.put("status", "0-此stb号已经被绑定");
			} else {
				if (usercode != null && usercode != "") { // 有usercode直接绑定
					userstb.setNetid(register_stb.getNetid());
					userstb.setUserid(Integer.valueOf(usercode));
					userstb.setServerid(register_stb.getServerid());
					userstb.setIncardflag(register_stb.getIncardflag());
					userstb.setState("1");
					userstb.setAddtime(Tools.getCurrentTime());
					// 默认为母机
					userstb.setMothercardflag("0");
					userstb.setPrice(register_stb.getMainprice());
					userstbDao.save(userstb);
					responseMap.put("status", "1-绑定机顶盒成功");
				} else { // 没有usercode 注册帐号
					form.setNetid(register_stb.getNetid());
					form.setState("3");
					form.setScore(new BigDecimal(0));
					form.setAccount(new BigDecimal(0));
					if (form.getPassword() == null || "".equals(form.getPassword())) {
						form.setPassword(ConfigUtil.getConfigFilePath("app_password", "system.properties"));
					}
					if (form.getPaypassword() == null || "".equals(form.getPaypassword())) {
						form.setPaypassword(ConfigUtil.getConfigFilePath("app_paypassword", "system.properties"));
					}
					userDao.save(form);
					responseMap.put("status", "1-注册的usercode为:" + form.getUsercode() + "，请耐心等待审核");
					userstb.setNetid(register_stb.getNetid());
					userstb.setUserid(form.getId());
					userstb.setServerid(register_stb.getServerid());
					userstb.setIncardflag(register_stb.getIncardflag());
					userstb.setState("1");
					userstb.setAddtime(Tools.getCurrentTime());
					// 默认为母机
					userstb.setMothercardflag("0");
					userstb.setPrice(register_stb.getMainprice());
					userstbDao.save(userstb);
				}
			}
		} else {
			responseMap.put("status", "0-此机顶盒没有入库记录");
		}

		this.saveRequestRecord(request, responseMap.get("status").toString());
		return responseMap;
	}

	@ResponseBody
	@RequestMapping(value = "/queryArea")
	// 区域查询
	// test url:
	// http://localhost:8080/boss/httpForMps/queryArea
	public Map<String, Object> saveQueryArea(HttpServletRequest request) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<HashMap<String, Object>> arealist = new ArrayList<HashMap<String, Object>>();
		List<Area> areas = areaDao.queryByList(new Area());
		for (Area area : areas) {
			HashMap<String, Object> areaMap = new HashMap<String, Object>();
			areaMap.put("id", area.getId());
			areaMap.put("netid", area.getNetid());
			areaMap.put("pid", area.getPid());
			areaMap.put("areacode", area.getAreacode());
			areaMap.put("areaname", area.getAreaname());
			arealist.add(areaMap);
		}
		responseMap.put("arealist", arealist);
		responseMap.put("status", "1");
		this.saveRequestRecord(request, responseMap.get("status").toString());

		return responseMap;
	}

	@ResponseBody
	@RequestMapping(value = "/queryProblemAndDispatch")
	// 用户登录
	// test url:
	// http://localhost:8080/boss/httpForMps/queryProblemAndDispatch?usercode=00000027
	public Map<String, Object> saveQueryProblemAndDispatch(User form, HttpServletRequest request) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String usercode = request.getParameter("usercode");
		User user = userDao.findById(Integer.valueOf(usercode));
		responseMap.put("status", "0");
		responseMap = (user != null) ? this.problemAndDispatchPackage(user, responseMap) : responseMap;
		this.saveRequestRecord(request, responseMap.get("status").toString());
		return responseMap;
	}

	@ResponseBody
	@RequestMapping(value = "/operatorLogin")
	// test url:
	// http://localhost:8080/boss/httpForMps/operatorLogin?id=1&password=0
	public Map<String, Object> saveOperatorLogin(Operator form, HttpServletRequest request) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("status", "0");
		Operator operator = null;
		operator = operatorDao.findByAnyInfo(form);
		responseMap = operator != null && operator.getPassword().equals(form.getPassword()) ? this.operatorInfoPackege(operator, responseMap) : responseMap;
		this.saveRequestRecord(request, responseMap.get("status").toString());
		return responseMap;
	}

	@ResponseBody
	@RequestMapping(value = "/findDispatchByUserid")
	// test url:
	// http://localhost:8080/boss/httpForMps/findDispatchByUserid?userid=29
	public Map<String, Object> saveFindDispatchByUserid(Dispatch form, HttpServletRequest request) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Integer userid = form.getUserid();
		List<Dispatch> dispatchs = dispatchDao.findByUserid(userid);
		responseMap.put("status", "0");
		responseMap = dispatchs.isEmpty() ? responseMap : this.dispatchInfoPackage(responseMap, dispatchs);
		this.saveRequestRecord(request, responseMap.get("status").toString());
		return responseMap;
	}

	@ResponseBody
	@RequestMapping(value = "/updateAndFindDispatchByDispatcherid")
	// test url:
	// http://localhost:8080/boss/httpForMps/updateAndFindDispatchByDispatcherid?dispatcherid=2
	public Map<String, Object> updateAndFindDispatchByDispatcherid(Dispatch form, HttpServletRequest request) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("status", "0");
		Integer dispatcherid = form.getDispatcherid();
		List<Dispatch> dispatchs = dispatchDao.findByDispatherid(dispatcherid);
		if (!dispatchs.isEmpty()) {
			return this.dispatchInfoPackage(responseMap, dispatchs);
		} else {
			responseMap.put("status", "1-没有新的工单");
		}
		this.saveRequestRecord(request, responseMap.get("status").toString());
		return responseMap;
	}

	@ResponseBody
	@RequestMapping(value = "/updateReturnInfo")
	// ID(dispatchid必须存在) 2<=state<=4 dealresult dealdate
	// dispatcherid(对应的OPERATORID中OPERATOR TYPE必须为维修员)
	// test url:
	// http://localhost:8080/boss/httpForMps/updateReturnInfo?id=75&state=4&dealresult=testmpsapi&dispatcherid=2
	public Map<String, Object> updateReturnInfo(Dispatch form, HttpServletRequest request) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("status", "0");
		Dispatch dispatch = dispatchDao.findById(form.getId());
		if (dispatch != null) {
			// 存在该工单,接下来验证发起请求的人员是否有资格
			Operator operator = operatorDao.findById(form.getDispatcherid());
			if (operator != null) {
				Integer state = Integer.valueOf(form.getState());
				if (operator.getOperatortype().equals("2") && state <= 4 && state >= 2) {
					// 验证成功，则更新该工单
					form.setDealdate(Tools.getCurrentTime());
					dispatchDao.updateReturnInfo(form);
					responseMap.put("status", "1");
				} else {
					responseMap.put("status", "0");
				}
			} else {
				// 不存在OPERATOR
				responseMap.put("status", "0");
			}
		} else {
			// 不存在工单
			responseMap.put("status", "0");
		}
		this.saveRequestRecord(request, responseMap.get("status").toString());
		return responseMap;
	}

	@ResponseBody
	@RequestMapping(value = "/updateOperatorPassword")
	// test url:
	// http://localhost:8080/boss/httpForMps/updateOperatorPassword?operatorcode=00000002&password=1&oldpwd=1
	public Map<String, Object> updateOperatorPassword(Operator form, HttpServletRequest request) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("status", "0");
		String oldpwd = request.getParameter("oldpwd");
		String operatorcode = request.getParameter("operatorcode");
		if (operatorcode != null && operatorcode != "") {
			Integer id = Integer.valueOf(operatorcode);
			form.setId(id);
			if (oldpwd != null && oldpwd != "" && oldpwd.equals(operatorDao.findById(id).getPassword())) {
				operatorDao.updatePassword(form);
				responseMap.put("status", "1");
			}
		}
		this.saveRequestRecord(request, responseMap.get("status").toString());
		return responseMap;
	}

	@ResponseBody
	@RequestMapping(value = "/updateUserPassword")
	// test url:
	// http://localhost:8080/boss/httpForMps/updateUserPassword?usercode=00000001&password=1&oldpwd=13579
	public Map<String, Object> updateUserPassword(User form, HttpServletRequest request) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("status", "0");
		String oldpwd = request.getParameter("oldpwd");
		String usercode = request.getParameter("usercode");
		if (usercode != null && usercode != "") {
			Integer id = Integer.valueOf(usercode);
			form.setId(id);
			if (oldpwd != null && oldpwd != "" && oldpwd.equals(userDao.findById(id).getPassword())) {
				userDao.updatePassword(form);
				responseMap.put("status", "1");
			}
		}
		this.saveRequestRecord(request, responseMap.get("status").toString());
		return responseMap;
	}

	@ResponseBody
	@RequestMapping(value = "/updateUserInfo")
	// http://localhost:8080/boss/httpForMps/updateUserInfo?usercode=00000001&mobile=1899999999&address=朝阳区一段56号
	public Map<String, Object> updateUserInfo(User form, HttpServletRequest request) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("status", "0");
		String usercode = request.getParameter("usercode");
		String mobile = request.getParameter("mobile");
		String address = request.getParameter("address");
		
		if (usercode != null && usercode != "") {
			Integer id = Integer.valueOf(usercode);
			User user = userDao.findById(id);
			if(user != null && "1".equals(user.getState())){//订户存在且正常
				if(StringUtils.isNotEmpty(mobile)){
					user.setMobile(mobile);
				}
				if(StringUtils.isNotEmpty(address)){
					user.setAddress(address);
				}
				
				userDao.update(user);
				responseMap.put("status", "1");
			}
		}
		this.saveRequestRecord(request, responseMap.get("status").toString());
		return responseMap;
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateUserPayPassword")
	// http://localhost:8080/boss/httpForMps/updateUserPayPassword?usercode=00000001&newpaypwd=1&oldpaypwd=13579
	public Map<String, Object> updateUserPayPassword(User form, HttpServletRequest request) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("status", "0");
		String oldpaypwd = Tools.getStr(request.getParameter("oldpaypwd"));
		String paypassword = Tools.getStr(request.getParameter("newpaypwd"));
		String usercode = request.getParameter("usercode");
		if (usercode != null && usercode != "") {
			Integer id = Integer.valueOf(usercode);
			form.setId(id);
			form.setPaypassword(paypassword);
			if (oldpaypwd.equals(userDao.findById(id).getPaypassword())) {
				userDao.updatePaypassword(form);
				responseMap.put("status", "1");
			}
		}
		this.saveRequestRecord(request, responseMap.get("status").toString());
		return responseMap;
	}
   
	
	/**
	 * 产品购买
	 * usercode-余额增加订户编码；buyerid-代办人ID
	 * source-方式(0-营业厅；1-MPS；2-代办）
	 *  {"status":"0"}为支付金额不正确 
	 *  {"status":"1"}为成功 
	 *  {"status":"2"}用户编码不存在                                                             
	 *  {"status":"3"}产品不存在或者无效                                                      
	 *  {"status":"4"}子机继承了主机,不能购买
	 *  {"status":"5"}子机购买的产品授权不能超出主机授权的范围
	 *  {"status":"6"}请选择用智能卡购买                                                       
	 *  {"status":"7"}该机顶盒不存在或者无效                                             
	 *  {"status":"8"}该智能卡不存在或者无效											
	 *  {"status":"9"}子卡继承了母卡，不能购买				
	 *  {"status":"10"}子卡购买的产品授权不能超出母卡授权的范围     
	 *  {"status":"11"}购买的产品授权时间出现重复（已经购买）     
	 *	{"status":"12"}账户余额不足
     *  {"status":"13"}支付密码错误
     *  {"status":"14"}付费包的授权结束时间不合法，不能购买
     *  {"status":"15"}没有购买基本包，不能购买
     *  {"status":"16"}母卡没有购买基本包，不能购买
     *  {"status":"17"}子卡购买基本包的授权时间超过母卡购买基本包的授权时间，不能购买
	 */
	@ResponseBody
	@RequestMapping(value = "/updateUserAccount")
	public Map<String, Object> updateUserAccount(User form, HttpServletRequest request) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String usercode = request.getParameter("usercode");
		String paytype = request.getParameter("paytype"); // 支付类型（0-现金支付；1-余额支付；2-积分支付；3-其他支付）
		String jsonStr = request.getParameter("billInfo");
		JSONObject jsonObj = new JSONObject(jsonStr);
		
		//验证产品是否购买资格权限（数据是否正确）
		Map<String, Object> returnHashmap = buyProductValidation(request);
		if(!"1".equals(returnHashmap.get("status"))){
			return returnHashmap;
		}
		
		
		User user = userDao.findById(Integer.valueOf(usercode));
		if (user != null) {
			if (!paytype.equals("1")) {
					/***************** 封装userbusiness主表 ******************/
					Userbusiness userbusiness = this.packUserbusiness(user, jsonObj);
					userbusiness.setPaytype(paytype);
					userbusinessDao.save(userbusiness);
					/***************** 封装userbusinessdetail附表 *****************/
					JSONArray details = jsonObj.getJSONArray("billDetail");
					for (int i = 0; i < details.length(); i++) {
						
						JSONObject detail = details.getJSONObject(i);
						Userbusinessdetail userbusinessdetail = this.packUserbusinessdetail(user, jsonObj, detail);
						
						if ("buyproduct".equals(detail.getString("buytype"))) {
							/***************** 封装userproduct表 *****************/
							Userproduct userproduct = this.packUserproduct(user, jsonObj, detail);
							userproductDao.save(userproduct);
							userproductDao.deleteInvalidProduct(userproduct);
							// //发送授权指令
							authorizeService.saveAuthorize_buyProduct(userproduct);
							
							//设置产品的操作业务信息
							userbusinessdetail.setStarttime(userproduct.getStarttime());
							userbusinessdetail.setEndtime(userproduct.getEndtime());
							userbusinessdetail.setContent(userproduct.getProductname()+":"+StringUtils.substring(userproduct.getStarttime(), 0,10)+"至"+StringUtils.substring(userproduct.getEndtime(),0,10));
							userbusinessdetail.setRemark(userproduct.getProductname()+":"+StringUtils.substring(userproduct.getStarttime(), 0,10)+"至"+StringUtils.substring(userproduct.getEndtime(),0,10));
						}
						
						userbusinessdetail.setBusinessid(userbusiness.getId());
						userbusinessdetailDao.save(userbusinessdetail);
						
					}
					responseMap.put("status", "1");
			} else { // 1-余额支付；
				String paypassword = request.getParameter("paypassword");
				User buyerUser = new User();//付款人
				String source = jsonObj.getString("source");
				if("2".equals(source)){//代办
					Integer buyerid = jsonObj.getInt("buyerid");
					buyerUser  = userDao.findById(buyerid);
				}else{
					buyerUser = user;
				}
				if (paypassword.equals(buyerUser.getPaypassword())) {
					BigDecimal shouldmoney = new BigDecimal(jsonObj.getString("totalMoney"));
					if (buyerUser.getAccount().compareTo(shouldmoney) > -1) { // 返回的结果是int类型，-1表示小于，0是等于，1是大于。
						// 这里直接取值封装然后插入数据库(userbusiness,userbusinessdetail)
						buyerUser.setAccount(buyerUser.getAccount().subtract(shouldmoney));
						userDao.update(buyerUser);
						//增加订户账户出入账记录
						Useraccountlog useraccountlog = new Useraccountlog();
						useraccountlog.setNetid(buyerUser.getNetid());
						useraccountlog.setUserid(buyerUser.getId());
						useraccountlog.setType("1");//类型（0-入账；1-出账）
						useraccountlog.setMoney(new BigDecimal(0).subtract(shouldmoney));
						useraccountlog.setOperatorid(null);
						useraccountlog.setAddtime(Tools.getCurrentTime());
						useraccountlog.setSource(source);
						useraccountlog.setBusinesstypekey("accountpayment");
						useraccountlogDao.save(useraccountlog);
						
						/***************** 封装userbusiness主表 ******************/
						Userbusiness userbusiness = this.packUserbusiness(user, jsonObj);
						userbusiness.setPaytype(paytype);
						userbusinessDao.save(userbusiness);
						/***************** 封装userbusinessdetail附表 *****************/
						JSONArray details = jsonObj.getJSONArray("billDetail");
						for (int i = 0; i < details.length(); i++) {
							JSONObject detail = details.getJSONObject(i);
							Userbusinessdetail userbusinessdetail = this.packUserbusinessdetail(user, jsonObj, detail);
							
							if ("buyproduct".equals(detail.getString("buytype"))) {
								/***************** 封装userproduct表 *****************/
								Userproduct userproduct = this.packUserproduct(user, jsonObj, detail);
								userproductDao.save(userproduct);
								userproductDao.deleteInvalidProduct(userproduct);
								// //发送授权指令
								authorizeService.saveAuthorize_buyProduct(userproduct);
								
								//设置产品的操作业务信息
								userbusinessdetail.setStarttime(userproduct.getStarttime());
								userbusinessdetail.setEndtime(userproduct.getEndtime());
								userbusinessdetail.setContent(userproduct.getProductname()+":"+StringUtils.substring(userproduct.getStarttime(), 0,10)+"至"+StringUtils.substring(userproduct.getEndtime(),0,10));
								userbusinessdetail.setRemark(userproduct.getProductname()+":"+StringUtils.substring(userproduct.getStarttime(), 0,10)+"至"+StringUtils.substring(userproduct.getEndtime(),0,10));
								
							}
							
							userbusinessdetail.setBusinessid(userbusiness.getId());
							userbusinessdetailDao.save(userbusinessdetail);
						}
						responseMap.put("status", "1");
					} else {
						responseMap.put("status", "12");
						responseMap.put("errmsg", "余额不足");
					}
				} else {
					responseMap.put("status", "13");
					responseMap.put("errmsg", "支付密码错误");
				}
			}
		} else {
			responseMap.put("status", "2");
			responseMap.put("errmsg", "用户编码不存在");
		}
		this.saveRequestRecord(request, responseMap.get("status").toString());
		return responseMap;
	}

	@ResponseBody
	@RequestMapping(value = "/saveProblemComplaint")
	// http://localhost:8080/boss/httpForMps/saveProblemComplaint?netid=135&userid=27&type=0&problemtype=2&content=testcontent
	public Map<String, Object> saveProblemComplaint(@RequestParam(value = "file", required = false) MultipartFile[] files, HttpServletRequest request) throws IOException {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Problemcomplaint form = new Problemcomplaint();
		form.setNetid(Integer.valueOf(request.getParameter("netid")));
		form.setUserid(Integer.valueOf(request.getParameter("userid")));
		form.setTelephone(request.getParameter("telephone"));
		User user = userDao.findById(form.getUserid());
		form.setAddress(user.getAddress());
		form.setType(request.getParameter("type"));
		form.setProblemtype(request.getParameter("problemtype"));
		form.setContent(request.getParameter("content"));
		form.setAddtime(Tools.getCurrentTime());
		form.setState("0");
		form.setResource("1");
		problemcomplaintDao.save(form);
		if (files != null) {
			for (MultipartFile file : files) {
				try {
					if (!file.isEmpty()) {
						String filename = file.getOriginalFilename();
						String[] strArray = filename.split("[.]");
						String filetype = strArray[strArray.length - 1];
						String upload_extend_path = systemparaDao.findByCodeStr("upload_file_path").getValue();
						String folderpath = upload_extend_path + File.separatorChar + "problem" + File.separatorChar + "problemid_" + form.getId();
						File folder = new File(folderpath);
						if (!folder.exists()) {
							folder.mkdirs();
						}
						String preservefilename = Tools.getNowRandom() + "_" + filename;
						String preservepath = folderpath + File.separatorChar + preservefilename;
						File savefile = new File(preservepath);
						file.transferTo(savefile);
						Problemcomplaintdetail problemdetail = new Problemcomplaintdetail();
						problemdetail.setComplaintid(form.getId());
						problemdetail.setNetid(form.getNetid());
						problemdetail.setType(filetype);
						problemdetail.setFilename(filename);
						problemdetail.setPreservefilename(preservefilename);
						problemdetail.setPreserveurl(preservepath);
						problemcomplaintDao.saveDetail(problemdetail);
					}
				} catch (Exception e) {
					responseMap.put("status", "0");
					this.saveRequestRecord(request, responseMap.get("status").toString());
					return responseMap;
				}
			}
		}
		responseMap.put("status", "1");
		this.saveRequestRecord(request, responseMap.get("status").toString());
		return responseMap;
	}

	/**
	 * 充值卡充值
	 * giftcardno-充值卡号；password-充值密码
	 * usercode-余额增加订户编码；buyercode-代办人编号
	 * source-方式(0-营业厅；1-MPS；2-代办）
	 *  {"status":"1"}为成功 
		{"status":"2"}充值卡不存在
        {"status":"3"}充值卡无效
        {"status":"4"}充值卡已使用
        {"status":"5"}充值密码不正确
		{"status":"6"余额增加订户编码错误
        {"status":"7"}代办人编码错误
	 */
	@ResponseBody
	@RequestMapping(value = "/saveRechargeByGiftcard")
	// http://localhost:8080/boss/httpForMps/saveRechargeByGiftcard?giftcardno=20170516A000017&password=095268&usercode=00000030&source=1
	public Map<String, Object> saveRechargeByGiftcard(HttpServletRequest request) {
		//回复对象
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String giftcardno = request.getParameter("giftcardno");
		String password = request.getParameter("password");
		String usercode = request.getParameter("usercode");
		String source = request.getParameter("source");
		String buyercode = request.getParameter("buyercode");
		
		//充值卡不能为空
		if(StringUtils.isEmpty(giftcardno)){
			responseMap.put("status", "2");
			this.saveRequestRecord(request, responseMap.get("status").toString());
			return responseMap;
		}
		//查询充值卡信息
		Giftcard giftcard = giftcardDao.findByGiftcardnoStr(giftcardno);
		//充值卡号不存在
		if(giftcard == null){
			responseMap.put("status", "2");
			this.saveRequestRecord(request, responseMap.get("status").toString());
			return responseMap;
		}
		
		//充值密码不能为空
		if(StringUtils.isEmpty(password)){
			responseMap.put("status", "5");
			this.saveRequestRecord(request, responseMap.get("status").toString());
			return responseMap;
		}
		//加密后的密码
		String passwordEncrypt= AesSecret.aesEncrypt(password, AesSecret.key);
		if(!passwordEncrypt.equals(giftcard.getPassword())){
			responseMap.put("status", "5");
			this.saveRequestRecord(request, responseMap.get("status").toString());
			return responseMap;
		}
		//充值卡无效
		if(!"1".equals(giftcard.getState())){
			responseMap.put("status", "3");
			this.saveRequestRecord(request, responseMap.get("status").toString());
			return responseMap;
		}
		if(!"0".equals(giftcard.getUsedflag())){
			responseMap.put("status", "4");
			this.saveRequestRecord(request, responseMap.get("status").toString());
			return responseMap;
		}
		User user = userDao.findById(Integer.valueOf(usercode));
		if(user == null){
			responseMap.put("status", "6");
			this.saveRequestRecord(request, responseMap.get("status").toString());
			return responseMap;
		}
		//封装业务主表
		Userbusiness userbusiness = new Userbusiness();
		userbusiness.setNetid(user.getNetid());
		userbusiness.setOperatorid(null);
		userbusiness.setUserid(user.getId());
		userbusiness.setStoreid(null);
		userbusiness.setAreacode(user.getAreacode());
		BigDecimal rechargemoney = new BigDecimal(0);
		userbusiness.setTotalmoney(rechargemoney);
		userbusiness.setShouldmoney(rechargemoney);
		userbusiness.setSource(source);
		userbusiness.setLogout("0");
		userbusiness.setPaytype("0");
		userbusiness.setAddtime(Tools.getCurrentTime());
		userbusiness.setShouldmoney(rechargemoney);
		userbusiness.setPaymoney(rechargemoney);
		if("2".equals(source)){//代买人
			userbusiness.setBuyerid(Integer.valueOf(buyercode));//封装代买人信息
		}
		userbusinessDao.save(userbusiness);
		
		//封装业务明细
		Userbusinessdetail businessdetail = new Userbusinessdetail();
		businessdetail.setBusinessid(userbusiness.getId());
		businessdetail.setNetid(user.getNetid());
		businessdetail.setOperatorid(null);
		businessdetail.setUserid(user.getId());
		businessdetail.setAreacode(user.getAreacode());
		businessdetail.setStoreid(null);
		businessdetail.setBusinesstypekey("giftcardrecharge");
		businessdetail.setBusinesstypename(getMessage("business.type.giftcardrecharge"));
		
		businessdetail.setContent(getMessage("giftcard.giftcardno")+":"+giftcardno);
		businessdetail.setPrice(userbusiness.getTotalmoney()); 
		businessdetail.setTotalmoney(userbusiness.getTotalmoney());
		businessdetail.setAddtime(Tools.getCurrentTime());
		businessdetail.setLogout("0");
		businessdetail.setSource(source);
		if("2".equals(source)){//代买人
			businessdetail.setBuyerid(Integer.valueOf(buyercode));;//封装代买人信息
		}
		
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
		useraccountlog.setOperatorid(null);
		useraccountlog.setAddtime(Tools.getCurrentTime());
		useraccountlog.setSource(source);
		useraccountlog.setBusinesstypekey("giftcardrecharge");
		useraccountlogDao.save(useraccountlog);
		
		//修改充值卡为已使用状态
		giftcard.setUsedflag("1");
		giftcardDao.update(giftcard);
		
		responseMap.put("status", "1");
		responseMap.put("amount", giftcard.getAmount());//卡面额
		responseMap.put("chargetime", Tools.getCurrentTime());//充值时间
		this.saveRequestRecord(request, responseMap.get("status").toString());
		return responseMap;
	}
	
	/**
	 * 账户充值
	 * usercode-订户编号；paytype-付费类型（0-现金支付；1-余额支付；2-积分支付；3-其他支付）
	 * chargemoney-充值金额；buyercode-代办人编号
	 * source-方式(0-营业厅；1-MPS；2-代办）
	 *  {"status":"0"}为失败 
		{"status":"1"}为成功 .
		{"status":"6"余额增加订户编码错误
        {"status":"7"}代办人编码错误
	 */
	@ResponseBody
	@RequestMapping(value = "/saveRecharge")
	// http://localhost:8080/boss/httpForMps/saveRecharge?usercode=00000030&paytype=0&chargemoney=30.00&&source=1&buyercode=0000030
	public Map<String, Object> saveRecharge(HttpServletRequest request) {
		//回复对象
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String usercode = request.getParameter("usercode");
		String paytype = request.getParameter("paytype");
		String chargemoney = request.getParameter("chargemoney");
		String source = request.getParameter("source");
		String buyercode = request.getParameter("buyercode");
		
		
		User user = userDao.findById(Integer.valueOf(usercode));
		if(user == null){
			responseMap.put("status", "6");
			this.saveRequestRecord(request, responseMap.get("status").toString());
			return responseMap;
		}
		//封装业务主表
		Userbusiness userbusiness = new Userbusiness();
		userbusiness.setNetid(user.getNetid());
		userbusiness.setOperatorid(null);
		userbusiness.setUserid(user.getId());
		userbusiness.setStoreid(null);
		userbusiness.setAreacode(user.getAreacode());
		BigDecimal rechargemoney = new BigDecimal(chargemoney);
		userbusiness.setTotalmoney(rechargemoney);
		userbusiness.setShouldmoney(rechargemoney);
		userbusiness.setSource(source);
		userbusiness.setLogout("0");
		userbusiness.setPaytype("0");
		userbusiness.setPaytype(paytype);
		userbusiness.setAddtime(Tools.getCurrentTime());
		userbusiness.setShouldmoney(rechargemoney);
		userbusiness.setPaymoney(rechargemoney);
		if("2".equals(source)){//代买人
			userbusiness.setBuyerid(Integer.valueOf(buyercode));//封装代买人信息
		}
		userbusinessDao.save(userbusiness);
		
		//封装业务明细
		Userbusinessdetail businessdetail = new Userbusinessdetail();
		businessdetail.setBusinessid(userbusiness.getId());
		businessdetail.setNetid(user.getNetid());
		businessdetail.setOperatorid(null);
		businessdetail.setUserid(user.getId());
		businessdetail.setAreacode(user.getAreacode());
		businessdetail.setStoreid(null);
		businessdetail.setBusinesstypekey("rechargeaccount");
		businessdetail.setBusinesstypename(getMessage("business.type.rechargeaccount"));
		businessdetail.setContent(getMessage("userbusiness.accountrecharge.rechargemoney")+":"+userbusiness.getTotalmoney());
		businessdetail.setPrice(userbusiness.getTotalmoney()); 
		businessdetail.setTotalmoney(userbusiness.getTotalmoney());
		businessdetail.setAddtime(Tools.getCurrentTime());
		businessdetail.setLogout("0");
		businessdetail.setSource(source);
		if("2".equals(source)){//代买人
			businessdetail.setBuyerid(Integer.valueOf(buyercode));;//封装代买人信息
		}
		
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
		useraccountlog.setOperatorid(null);
		useraccountlog.setAddtime(Tools.getCurrentTime());
		useraccountlog.setSource(source);
		useraccountlog.setBusinesstypekey("rechargeaccount");
		useraccountlogDao.save(useraccountlog);
		
		responseMap.put("status", "1");
		this.saveRequestRecord(request, responseMap.get("status").toString());
		return responseMap;
	}
	
	
	/**
	 * 保存留言
	 * messagertype-留言人类型 1-订户 2-客服
	 * messagerid-留言人ID；
	 * dispatchid-工单ID
	 * content-留言内容
	 * addtime-留言时间（yyyy-MM-dd HH:mm:ss）
	 *  {"status":"0"}为保存失败 
		{"status":"1"}为保存成功
	 */
	@ResponseBody
	@RequestMapping(value = "/saveMessage")
	// http://localhost:8080/boss/httpForMps/saveMessage?messageInfo={'messagertype':'1','messagerid':'00000030',
    //'dispatchid':'2','content':'家庭住址在哪儿呢','addtime':'2017-05-18 14:38:20'}
	public Map<String, Object> saveMessage(HttpServletRequest request) {
		//回复对象
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("status", "0");
		
		//String jsonStr = request.getParameter("messageInfo");
		
		//System.out.println("messageInfo="+jsonStr);
		
		// String pwd = request.getParameter("paypassword");
		//JSONObject jsonObj = new JSONObject(jsonStr);
		
		String messagertype = Tools.getStr(request.getParameter("messagertype"));
		String messagerid = Tools.getStr(request.getParameter("messagerid"));
		String dispatchid = Tools.getStr(request.getParameter("dispatchid"));
		String content = Tools.getStr(request.getParameter("content"));
		String addtime = Tools.getStr(request.getParameter("addtime"));
		
		//留言人ID不能为空
		if(StringUtils.isEmpty(messagerid)){
			responseMap.put("status", "3");
			this.saveRequestRecord(request, responseMap.get("status").toString());
			return responseMap;
		}
		//工单ID不存在
		if(StringUtils.isEmpty(dispatchid)){
			responseMap.put("status", "2");
			this.saveRequestRecord(request, responseMap.get("status").toString());
			return responseMap;
		}
		Dispatch dispatch = dispatchDao.findById(Integer.parseInt(dispatchid));
		if(dispatch == null){
			responseMap.put("status", "2");
			this.saveRequestRecord(request, responseMap.get("status").toString());
			return responseMap;
		}
		
		
		
		//封装留言对象
		Message message = new Message();
		message.setNetid(dispatch.getNetid());
		message.setMessagertype(messagertype);
		message.setMessagerid(messagerid);
		message.setDispatchid(dispatchid);
		message.setContent(content);
		message.setAddtime(addtime);
		
		//保存留言
		messageDao.save(message);
		
		responseMap.put("status", "1");
		this.saveRequestRecord(request, responseMap.get("status").toString());
		return responseMap;
	}
	
	/**
	 * 保存订户反馈
	 * userid-订户ID
	 * type-反馈类型（1-意见反馈 ； 2-产品使用反馈）
	 * producttype-产品类型（1-产品；2-业务；3-事件）
	 * productid-产品ID
	 * productname-产品名称
	 * content-反馈内容
	 * mobile-联系电话(type为1时有)
	 *  {"status":"0"}为保存失败 
		{"status":"1"}为保存成功
		{"status":"6"}为订户不存在
	 */
	@ResponseBody
	@RequestMapping(value = "/saveFeedback")
	// http://localhost:8080/boss/httpForMps/saveFeedback?usercode=00000030&type=1&content=sss&mobile=sss
	// http://localhost:8080/boss/httpForMps/saveFeedback?usercode=00000030&type=2&content=sss&producttype=sss&programName&productid=00012
	public Map<String, Object> saveFeedback(HttpServletRequest request) {
		//回复对象
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("status", "0");
		
		String usercode = request.getParameter("usercode");
		String type = request.getParameter("type");
		String producttype = request.getParameter("producttype");
		String productid = request.getParameter("productid");
		String productname = request.getParameter("productname");
		String content = request.getParameter("content");
		String mobile = request.getParameter("mobile");
		
		//反馈人ID不能为空
		if(StringUtils.isEmpty(usercode)){
			responseMap.put("status", "6");
			this.saveRequestRecord(request, responseMap.get("status").toString());
			return responseMap;
		}
		
		User user = userDao.findById(Integer.parseInt(usercode));
		if(user == null){
			responseMap.put("status", "6");
			this.saveRequestRecord(request, responseMap.get("status").toString());
			return responseMap;
		}
		
		//封装留言对象
		Userfeedback userfeedback = new Userfeedback();
		userfeedback.setNetid(user.getNetid());
		userfeedback.setUserid(user.getId());
		userfeedback.setType(type);
		userfeedback.setProducttype(producttype);
		userfeedback.setProductid(productid);
		userfeedback.setProductname(productname);
		userfeedback.setContent(content);
		userfeedback.setMobile(mobile);	
		userfeedback.setAddtime(Tools.getCurrentTime());
		
		//保存订户反馈
		userfeedbackDao.save(userfeedback);
		
		responseMap.put("status", "1");
		this.saveRequestRecord(request, responseMap.get("status").toString());
		return responseMap;
	}
	
	
	private Map<String, Object> userInfoPackage(User user, Map<String, Object> responseMap) {
		responseMap.put("status", "1");
		// 封装用户信息
		HashMap<String, Object> userMap = new HashMap<String, Object>();
		Integer userid = user.getId();
		userMap.put("userid", user.getId());
		userMap.put("netid", user.getNetid());
		userMap.put("areacode", user.getAreacode());
		String netname = networkDao.findById(user.getNetid()) == null ? "" : networkDao.findById(user.getNetid()).getNetname();
		userMap.put("netname", netname);
		Area find_area = new Area();
		find_area.setNetid(user.getNetid());
		find_area.setAreacode(user.getAreacode());
		String areaname = areaDao.findByAreacode(find_area) == null ? "" : areaDao.findByAreacode(find_area).getAreaname();
		userMap.put("areaname", areaname);
		userMap.put("telephone", user.getTelephone());
		userMap.put("usercode", user.getUsercode());
		userMap.put("username", user.getUsername());
		userMap.put("usertype", user.getUsertype());
		userMap.put("mobile", user.getMobile());
		userMap.put("documenttype", user.getDocumenttype());
		userMap.put("documentno", user.getDocumentno());
		userMap.put("email", user.getEmail());
		userMap.put("address", user.getAddress());
		userMap.put("password", user.getPassword());
		userMap.put("score", user.getScore());
		userMap.put("account", user.getAccount());
		userMap.put("state", user.getState());
		userMap.put("userlevelid", user.getUserlevelid());
		Userlevel userlevel = userlevelDao.findById(user.getUserlevelid());
		if(userlevel!=null){
			userMap.put("userlevelname", userlevel.getLevelname());
		}
		//用户智能卡信息
		List<HashMap<String, Object>> cardlist = new ArrayList<HashMap<String, Object>>();
		List<Usercard> usercards = usercardDao.findByUserid(userid);
		for (Usercard usercard : usercards) {
			HashMap<String, Object> usercardMap = new HashMap<String, Object>();
			usercardMap.put("cardid", usercard.getCardid());
			usercardMap.put("type", usercard.getIncardflag());
			usercardMap.put("buytime", usercard.getAddtime());
			usercardMap.put("state", usercard.getState());
			usercardMap.put("relatedstbno", usercard.getStbno());
			//增加子母卡信息2017-09-14
			usercardMap.put("mothercardflag", usercard.getMothercardflag());
			usercardMap.put("mothercardid", usercard.getMothercardid());
			usercardMap.put("cardflag", usercard.getCardflag());
			cardlist.add(usercardMap);
		}
		userMap.put("usercardinfo", cardlist);
		
		//未授权，已授权用户信息
		Userproduct userproductform = new Userproduct();
		userproductform.setUserid(userid);
		HashMap<String, HashMap<String, String>> maxEndtiemProductMap = serproductService.findAllProductLastEndTimeListByUserid(userproductform);
		HashMap<String, List<Usercard>> isaccredituserMap = serproductService.findAuthorizecardFromAllcard(usercards, maxEndtiemProductMap);
		List<String> notaccreditUserCard = new ArrayList<String>();
		List<Usercard> notaccreditUser = isaccredituserMap.get("0");
		for (Usercard usercard : notaccreditUser) {
			notaccreditUserCard.add(usercard.getCardid());
		}
		List<String> accreditUserCard = new ArrayList<String>();
		List<Usercard> accreditUser = isaccredituserMap.get("1");
		for (Usercard usercard : accreditUser) {
			accreditUserCard.add(usercard.getCardid());
		}
		userMap.put("notaccreditUserinfo", notaccreditUserCard);//未授权用户智能卡
		userMap.put("accreditUserinfo",accreditUserCard);//已授权用户智能卡
		
		//用户终端购买产品授权结束时间信息
		userMap.put("maxEndtiemProductinfo",maxEndtiemProductMap);

		
		// 用户机顶盒信息
		List<HashMap<String, Object>> stblist = new ArrayList<HashMap<String, Object>>();
		List<Userstb> userstbs = userstbDao.queryByUserid(userid);
		for (Userstb userstb : userstbs) {
			HashMap<String, Object> userstbMap = new HashMap<String, Object>();
			userstbMap.put("stbno", userstb.getStbno());
			userstbMap.put("type", userstb.getIncardflag());
			userstbMap.put("buytime", userstb.getAddtime());
			userstbMap.put("state", userstb.getState());
			//增加子母卡信息2017-09-14
			userstbMap.put("mothercardflag", userstb.getMothercardflag());
			userstbMap.put("mothercardid", userstb.getMothercardid());
			
			stblist.add(userstbMap);
		}
		userMap.put("userstbinfo", stblist);

		// 订购产品信息
		List<HashMap<String, Object>> userproductlist = new ArrayList<HashMap<String, Object>>();
		List<Userproduct> userproducts = userproductDao.findByUserid(userid);
		for (Userproduct userproduct : userproducts) {
			HashMap<String, Object> userproductMap = new HashMap<String, Object>();

			// userproductMap.put("cardid", userproduct.getCardid());
			String terminaltype = userproduct.getTerminaltype();
			String terminalid = "0".equals(terminaltype) ? userproduct.getStbno() : userproduct.getCardid();
			userproductMap.put("terminaltype", terminaltype);
			userproductMap.put("terminalid", terminalid);
			userproductMap.put("productname", userproduct.getProductname());
			userproductMap.put("addtime", userproduct.getAddtime());
			userproductMap.put("startetime", userproduct.getStarttime());
			userproductMap.put("endtime", userproduct.getEndtime());
			userproductMap.put("productid", userproduct.getProductid());
			userproductMap.put("type", userproduct.getType());

			userproductlist.add(userproductMap);
		}
		userMap.put("userproductinfo", userproductlist);
		// 业务记录
		List<HashMap<String, Object>> userbusinesslist = new ArrayList<HashMap<String, Object>>();
		List<Userbusiness> userbusinesses = userbusinessDao.findByUserid(userid);
		for (Userbusiness userbusiness : userbusinesses) {
			HashMap<String, Object> userbusinessMap = new HashMap<String, Object>();
			userbusinessMap.put("businessid", userbusiness.getId());
			userbusinessMap.put("addtime", userbusiness.getAddtime());
			userbusinessMap.put("totalmoney", userbusiness.getTotalmoney());
			userbusinessMap.put("score", userbusiness.getScore());
			userbusinessMap.put("scoremoney", userbusiness.getScoremoney());
			userbusinessMap.put("accountmoney", userbusiness.getAccountmoney());
			userbusinessMap.put("paymoney", userbusiness.getPaymoney());
			userbusinessMap.put("paytype", userbusiness.getPaytype());
			userbusinesslist.add(userbusinessMap);
		}
		userMap.put("userbusinessinfo", userbusinesslist);
		// 业务明细记录
		List<HashMap<String, Object>> userbusinessdetaillist = new ArrayList<HashMap<String, Object>>();
		List<Userbusinessdetail> userbusinessdetails = userbusinessdetailDao.findByUserid(userid);
		for (Userbusinessdetail userbusinessdetail : userbusinessdetails) {
			String type = userbusinessdetail.getBusinesstypekey();
			HashMap<String, Object> businessdetailMap = new HashMap<String, Object>();
			businessdetailMap.put("businessid", userbusinessdetail.getBusinessid()); // 主表ID
			businessdetailMap.put("producttype", type); // 产品类型（1-产品；2-业务；3-事件）
			businessdetailMap.put("productid", userbusinessdetail.getProductid());
			businessdetailMap.put("productname", userbusinessdetail.getProductname());
			businessdetailMap.put("buytime", userbusinessdetail.getAddtime());
			businessdetailMap.put("content", userbusinessdetail.getContent());
			businessdetailMap.put("source", userbusinessdetail.getSource()); // 业务来源（0-BOSS；1-MPS）
			businessdetailMap.put("businesstypekey", userbusinessdetail.getBusinesstypekey()); //业务类型
			businessdetailMap.put("totalmoney", userbusinessdetail.getTotalmoney()); // 总价
			userbusinessdetaillist.add(businessdetailMap);
			userMap.put("businessdetailinfo", userbusinessdetaillist);
		}
		
		// 账户出入账记录
		List<HashMap<String, Object>> accountloglist = new ArrayList<HashMap<String, Object>>();
		List<Useraccountlog> Useraccountlogs = useraccountlogDao.findByUserid(userid);
		for (Useraccountlog useraccountlog : Useraccountlogs) {
			HashMap<String, Object> useraccountlogMap = new HashMap<String, Object>();
			useraccountlogMap.put("type", useraccountlog.getType());
			useraccountlogMap.put("money", useraccountlog.getMoney());
			useraccountlogMap.put("addtime", useraccountlog.getAddtime());
			useraccountlogMap.put("source", useraccountlog.getSource());
			useraccountlogMap.put("businesstypekey", useraccountlog.getBusinesstypekey());
			accountloglist.add(useraccountlogMap);
		}
		userMap.put("accountlog", accountloglist);
		
		responseMap.put("user", userMap);
		return responseMap;
	}

	private Map<String, Object> dispatchInfoPackage(Map<String, Object> responseMap, List<Dispatch> dispatchs) {
		responseMap.put("status", "1");
		List<HashMap<String, Object>> dispatchlist = new ArrayList<HashMap<String, Object>>();
		for (Dispatch dispatch : dispatchs) {
			HashMap<String, Object> dispatchinfoMap = new HashMap<String, Object>();
			// "photoUrls" : "string,照片URL",
			// "photoFlag" : "int,照片标识",
			// "resource" : "string,工单来源",
			// "content" : "string,处理描述",
			dispatchinfoMap.put("userId", dispatch.getUserid());
			String username = userDao.findById(dispatch.getUserid()).getUsername();
			dispatchinfoMap.put("username", username);
			dispatchinfoMap.put("telephone", dispatch.getTelephone());
			dispatchinfoMap.put("address", dispatch.getAddress());
			dispatchinfoMap.put("operatorId", dispatch.getOperatorid());
			String operatorname = operatorDao.findById(dispatch.getOperatorid()).getOperatorname();
			dispatchinfoMap.put("operatorname", operatorname);

			dispatchinfoMap.put("state", dispatch.getState());
			dispatchinfoMap.put("dealDate", dispatch.getDealdate());
			dispatchinfoMap.put("dispatchId", dispatch.getId());
			dispatchinfoMap.put("dispatcherId", dispatch.getDispatcherid());
			dispatchinfoMap.put("addDate", dispatch.getAdddate());
			dispatchinfoMap.put("dealResult", dispatch.getDealresult());
			dispatchinfoMap.put("problemType", dispatch.getProblemtype());
			dispatchinfoMap.put("content", dispatch.getContent());
			/* 封装工单对应的问题投诉单内容 */
			/*HashMap<String, Object> complaintContent = new HashMap<String, Object>();
			List<Problemcomplaintdetail> detailList = problemcomplaintDao.findDetailByComplaintid(dispatch.getComplaintid());
			List<HashMap<String, Object>> complaintfiles = new ArrayList<HashMap<String, Object>>();
			for (Problemcomplaintdetail detail : detailList) {
				HashMap<String, Object> detailinfo = new HashMap<String, Object>();
				detailinfo.put("filepath", detail.getPreserveurl());
				complaintfiles.add(detailinfo);
			}
			Problemcomplaint complaint = problemcomplaintDao.findById(dispatch.getComplaintid());
			if (complaint != null) {
				complaintContent.put("complaintfiles", complaintfiles);
				complaintContent.put("complaintId", dispatch.getComplaintid());
				complaintContent.put("type", complaint.getType());
				complaintContent.put("problemtype", complaint.getProblemtype());
				complaintContent.put("content", complaint.getContent());
				complaintContent.put("addtime", complaint.getAddtime());
				complaintContent.put("state", complaint.getState());
				complaintContent.put("result", complaint.getResult());
				complaintContent.put("resource", complaint.getResource());
			} 
			dispatchinfoMap.put("complaintinfo", complaintContent);*/
			dispatchlist.add(dispatchinfoMap);
			Dispatch dispatchUpdate = new Dispatch();
			dispatchUpdate.setId(dispatch.getId());
			dispatchUpdate.setState("2");
			dispatchDao.updateState(dispatchUpdate);
			/* 封装工单对应的问题投诉单内容 */
			HashMap<String, Object> complaintfilename = new HashMap<String, Object>();

		}
		responseMap.put("dispatchlist", dispatchlist);
		return responseMap;
	}

	private Map<String, Object> operatorInfoPackege(Operator operator, Map<String, Object> responseMap) {
		responseMap.put("status", "1");
		// 封装用户信息
		HashMap<String, Object> operatorMap = new HashMap<String, Object>();
		operatorMap.put("id", operator.getId());
		operatorMap.put("loginname", operator.getLoginname());
		operatorMap.put("password", operator.getPassword());
		operatorMap.put("netid", operator.getNetid());
		Network network = networkDao.findById(operator.getNetid());
		if(network == null){
			network = new Network();
		}
		String netname = network.getNetname();
		operatorMap.put("netname", netname);

		operatorMap.put("storeid", operator.getStoreid());
		operatorMap.put("operatorcode", operator.getOperatorcode());
		operatorMap.put("operatorname", operator.getOperatorname());
		operatorMap.put("operatortype", operator.getOperatortype());
		operatorMap.put("documenttype", operator.getDocumenttype());
		operatorMap.put("documentno", operator.getDocumentno());
		operatorMap.put("email", operator.getEmail());
		operatorMap.put("address", operator.getAddress());
		operatorMap.put("telephone", operator.getTelephone());
		operatorMap.put("mobile", operator.getMobile());
		operatorMap.put("addtime", operator.getAddtime());
		operatorMap.put("state", operator.getState());

		responseMap.put("content", operatorMap);
		return responseMap;
	}

	private Userbusiness packUserbusiness(User user, JSONObject jsonObj) {
		Userbusiness userbusiness = new Userbusiness();
		userbusiness.setNetid(user.getNetid());
		userbusiness.setUserid(user.getId());
		userbusiness.setAreacode(user.getAreacode());
		userbusiness.setTotalmoney(new BigDecimal(jsonObj.getString("totalMoney")));
		userbusiness.setShouldmoney(new BigDecimal(jsonObj.getString("totalMoney")));
		userbusiness.setPaymoney(new BigDecimal(jsonObj.getString("payMoney")));
		userbusiness.setAddtime(Tools.getCurrentTime());
		userbusiness.setLogout("0");
		String source = jsonObj.getString("source");  
		userbusiness.setSource(source); // 0营业厅 1MPS
		if("2".equals(source)){//代办
			userbusiness.setBuyerid(jsonObj.getInt("buyerid"));
		}
		return userbusiness;
	}

	private Userproduct packUserproduct(User user, JSONObject jsonObj, JSONObject detail) {
		Userproduct userproduct = new Userproduct();
		userproduct.setNetid(user.getNetid());
		userproduct.setUserid(user.getId());
		userproduct.setAreacode(user.getAreacode());
		userproduct.setProductid(detail.getString("id"));
		userproduct.setType(detail.getString("type"));
		String source = jsonObj.getString("source");  
		userproduct.setSource(source); // 0营业厅 1MPS
		if("2".equals(source)){//代办
			userproduct.setBuyerid(jsonObj.getInt("buyerid"));
		}
		userproduct.setAddtime(Tools.getCurrentTime());
		userproduct.setState("1");
		userproduct.setTotalmoney(new BigDecimal(detail.getString("totalmoney")));
		String unit = detail.getString("unit");//购买方式
		String buyamount = detail.getString("buyamount");//购买数量
		if(StringUtils.isEmpty(unit)){
			unit = "month";//如果unit为空，默认按月购买
		}
		userproduct.setUnit(unit);
		userproduct.setBuyamount(Integer.valueOf(detail.getString("buyamount")));
		userproduct.setTerminalid(jsonObj.getString("terminalid"));
		userproduct.setTerminaltype(jsonObj.getString("terminaltype"));
		if (userproduct.getTerminaltype().equals("0")) { // stbno
			userproduct.setStbno(userproduct.getTerminalid());
			Userstb stbno = new Userstb();
			stbno.setStbno(userproduct.getStbno());
			userproduct.setServerid(userstbDao.findByStbno(stbno).getServerid());
		} else {
			userproduct.setCardid(userproduct.getTerminalid());
			Usercard cardid = new Usercard();
			cardid.setCardid(userproduct.getCardid());
			userproduct.setServerid(usercardDao.findByCardid(cardid).getServerid());
		}
		
		//设置授权开始和结束时间
		String timedate = userproductDao.findLastEndTimeByProductid(userproduct);//该产品的最大授权结束时间
		if("month".equals(unit)){
			if(timedate != null){
				//数据库最大的授权时间与当前时间进行比较
				int flag = Tools.compare_date(timedate,Tools.getCurrentTimeByFormat("yyyy-MM-dd"),"yyyy-MM-dd");
				if(flag < 0 ){//表示数据库购买时间过期，即该产品的最大的授权开始时间应该为当前时间
					timedate = Tools.getCurrentTimeByFormat("yyyy-MM-dd");
				}else{//表示数据库购买时间未过期，即该产品购买的开始时间应该为最大授权时间+1天
					timedate = Tools.getSpecifiedDayAfter(timedate,"yyyy-MM-dd",1);
				}
				userproduct.setStarttime(timedate + " 00:00:00");
				String date = Tools.getSpecifiedMonthAfter(timedate, "yyyy-MM-dd", Integer.parseInt(buyamount));
				String endtime = Tools.getSpecifiedDayAfter(date, "yyyy-MM-dd", -1);//减一天作为结束时间
				userproduct.setEndtime(endtime + " 23:59:59");
			}else{
				timedate = Tools.getCurrentTimeByFormat("yyyy-MM-dd");
				userproduct.setStarttime(timedate + " 00:00:00");
				String date = Tools.getSpecifiedMonthAfter(timedate, "yyyy-MM-dd", Integer.parseInt(buyamount));
				String endtime = Tools.getSpecifiedDayAfter(date, "yyyy-MM-dd", -1);//减一天作为结束时间
				userproduct.setEndtime(endtime + " 23:59:59");
			}
		}else{//按天
			if(timedate != null){
				//数据库最大的授权时间与当前时间进行比较
				int flag = Tools.compare_date(timedate,Tools.getCurrentTimeByFormat("yyyy-MM-dd"),"yyyy-MM-dd");
				if(flag < 0 ){//表示数据库购买时间过期，即该产品的最大的授权开始时间应该为当前时间
					timedate = Tools.getCurrentTimeByFormat("yyyy-MM-dd");
				}else{//表示数据库购买时间未过期，即该产品购买的开始时间应该为最大授权时间+1天
					timedate = Tools.getSpecifiedDayAfter(timedate,"yyyy-MM-dd",1);
				}
				userproduct.setStarttime(timedate + " 00:00:00");
				String date = Tools.getSpecifiedDayAfter(timedate, "yyyy-MM-dd", Integer.parseInt(buyamount));
				String endtime = Tools.getSpecifiedDayAfter(date, "yyyy-MM-dd", -1);//减一天作为结束时间
				userproduct.setEndtime(endtime + " 23:59:59");
			}else{
				timedate = Tools.getCurrentTimeByFormat("yyyy-MM-dd");
				userproduct.setStarttime(timedate + " 00:00:00");
				String date = Tools.getSpecifiedDayAfter(timedate, "yyyy-MM-dd", Integer.parseInt(buyamount));
				String endtime = Tools.getSpecifiedDayAfter(date, "yyyy-MM-dd", -1);//减一天作为结束时间
				userproduct.setEndtime(endtime + " 23:59:59");
			}
		}
		if ("1".equals(userproduct.getType())) { // product
			Product find_product = new Product();
			find_product.setProductid(userproduct.getProductid());
			find_product.setNetid(userproduct.getNetid());
			Product product = productDao.findByProductid(find_product);
			userproduct.setProductname(product.getProductname());
			if("month".equals(unit)){//按月购买
				//userproduct.setPrice(product.getPricepermonth());
			}else if("day".equals(unit)){
				//userproduct.setPrice(product.getPriceperday());
			}
		} else if ("2".equals(userproduct.getType())) { // service
			Service find_service = new Service();
			find_service.setServiceid(userproduct.getProductid());
			find_service.setNetid(userproduct.getNetid());
			Service service = serviceDao.findByServiceid(find_service);
			userproduct.setProductname(service.getServicename());
			if("month".equals(unit)){//按月购买
				//userproduct.setPrice(service.getPricepermonth());
			}else if("day".equals(unit)){
				//userproduct.setPrice(service.getPriceperday());
			}
		} else {
			// event
		}
		
		userproduct.setPrice(new BigDecimal(detail.getString("totalmoney")).divide(new BigDecimal(detail.getString("buyamount"))));
		return userproduct;
	}

	private Userbusinessdetail packUserbusinessdetail(User user, JSONObject jsonObj, JSONObject detail) {
		Userbusinessdetail userbusinessdetail = new Userbusinessdetail();
		userbusinessdetail.setNetid(user.getNetid());
		userbusinessdetail.setUserid(user.getId());
		userbusinessdetail.setAreacode(user.getAreacode());
		userbusinessdetail.setLogout("0");
		String source = jsonObj.getString("source");  
		userbusinessdetail.setSource(source); // 0营业厅 1MPS
		if("2".equals(source)){//代办
			userbusinessdetail.setBuyerid(jsonObj.getInt("buyerid"));
		}
		userbusinessdetail.setAddtime(Tools.getCurrentTime());
		
		
		userbusinessdetail.setBusinesstypekey(detail.getString("buytype"));
		Businesstype businesstype  = businesstypeDao.findByTypekeyStr(userbusinessdetail.getBusinesstypekey());
		if(businesstype == null){
			businesstype = new Businesstype();
		}
		userbusinessdetail.setBusinesstypename(businesstype.getTypename());
		userbusinessdetail.setFeename(businesstype.getFeename());
		
		if ("buyproduct".equals(userbusinessdetail.getBusinesstypekey())) {
			userbusinessdetail.setTerminaltype(jsonObj.getString("terminaltype"));
			userbusinessdetail.setTerminalid(jsonObj.getString("terminalid"));
			if (userbusinessdetail.getTerminaltype().equals("0")) { // stbno
				userbusinessdetail.setStbno(userbusinessdetail.getTerminalid());
			} else {
				userbusinessdetail.setCardid(userbusinessdetail.getTerminalid());
			}
			userbusinessdetail.setProductid(detail.getString("id"));
			userbusinessdetail.setType(detail.getString("type"));
			if ("1".equals(userbusinessdetail.getType())) {
				Product find_product = new Product();
				find_product.setProductid(userbusinessdetail.getProductid());
				find_product.setNetid(user.getNetid());
				Product product = productDao.findByProductid(find_product);
				userbusinessdetail.setProductname(product.getProductname());
				userbusinessdetail.setContent(product.getProductname());
				//userbusinessdetail.setPrice(product.getPricepermonth());
			} else if ("2".equals(userbusinessdetail.getType())) {
				Service find_service = new Service();
				find_service.setServiceid(userbusinessdetail.getProductid());
				find_service.setNetid(user.getNetid());
				Service service = serviceDao.findByServiceid(find_service);

				userbusinessdetail.setProductname(service.getServicename());
				userbusinessdetail.setContent(service.getServicename());
				//userbusinessdetail.setPrice(service.getPricepermonth());
			} else {
				// ///event
				
			}
//			userbusinessdetail.setStarttime("");
//			userbusinessdetail.setEndtime("");
			userbusinessdetail.setPrice(new BigDecimal(detail.getString("totalmoney")).divide(new BigDecimal(detail.getString("buyamount"))));
			userbusinessdetail.setTotalmoney(new BigDecimal(detail.getString("totalmoney")));
		} else if ("rechargeaccount".equals(userbusinessdetail.getBusinesstypekey())) {
			userbusinessdetail.setContent("充值金额:" + detail.getString("totalmoney"));
			userbusinessdetail.setPrice(new BigDecimal(detail.getString("totalmoney")));
			userbusinessdetail.setTotalmoney(new BigDecimal(detail.getString("totalmoney")));
		}
		return userbusinessdetail;
	}

	private Map<String, Object> problemAndDispatchPackage(User user, Map<String, Object> responseMap) {
		responseMap.put("status", "1");

		// 用户问题信息
		List<HashMap<String, Object>> problemList = new ArrayList<HashMap<String, Object>>();
		List<Problemcomplaint> problems = problemcomplaintDao.findByUserid(user.getId());
		for (Problemcomplaint problem : problems) {
			HashMap<String, Object> problemInfo = new HashMap<String, Object>();
			problemInfo.put("complaintid", problem.getId());
			problemInfo.put("netid", problem.getNetid());
			problemInfo.put("userid", problem.getUserid());
			problemInfo.put("operatorid", problem.getOperatorid());
			problemInfo.put("type", problem.getType()); // 类型(0-故障；1-投诉)
			problemInfo.put("problemtype", problem.getProblemtype());// 问题类型(1-硬件问题；2-软件问题；0-其他问题)
			problemInfo.put("content", problem.getContent());
			problemInfo.put("addtime", problem.getAddtime());
			problemInfo.put("state", problem.getState()); // 状态（0-未受理；1-受理中；3-已处理；4-处理失败）(没有２为了与工单DISPATCH中的STATE同步)
			problemInfo.put("result", problem.getResult());
			problemInfo.put("resource", problem.getResource()); // 问题投诉来源（0-BOSS；1-MPS）
			problemList.add(problemInfo);
		}
		responseMap.put("userProblemList", problemList);

		// 用户工单信息
		List<HashMap<String, Object>> dispatchList = new ArrayList<HashMap<String, Object>>();
		List<Dispatch> dispatchs = dispatchDao.findByUserid(user.getId());
		for (Dispatch dispatch : dispatchs) {
			HashMap<String, Object> dispatchInfo = new HashMap<String, Object>();
			dispatchInfo.put("dispatchid", dispatch.getId());
			dispatchInfo.put("netid", dispatch.getNetid());
			dispatchInfo.put("areacode", dispatch.getArea());
			dispatchInfo.put("complaintid", dispatch.getComplaintid());
			dispatchInfo.put("problemtype", dispatch.getProblemtype()); // 问题类型(1-硬件问题；2-软件问题；0-其他问题)
			dispatchInfo.put("userid", dispatch.getUserid());
			dispatchInfo.put("adddate", dispatch.getAdddate());
			dispatchInfo.put("operatorid", dispatch.getOperatorid());
			dispatchInfo.put("dispatcherid", dispatch.getDispatcherid());
			dispatchInfo.put("content", dispatch.getContent());
			dispatchInfo.put("dealdate", dispatch.getDealdate());
			dispatchInfo.put("dealresult", dispatch.getDealresult());
			dispatchInfo.put("state", dispatch.getState()); // 状态（0-未派单；1-已派单；2-处理中；3-已处理；4-处理失败；
															// 5-结单）
			dispatchList.add(dispatchInfo);
		}
		responseMap.put("userDispatchList", dispatchList);
		return responseMap;
	}
	@ResponseBody
	@RequestMapping(value = "/sendAllProduct")
	public Map<String, Object> sendAllProduct(Product form,HttpServletRequest request) {
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap = this.productInfoPackage(dataMap);
		
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("status", "1");
		this.saveRequestRecord(request, responseMap.get("status").toString());
		return dataMap;
	}
	/**
	 * 推送帮助信息给MPS
	 */
	@ResponseBody
	@RequestMapping(value = "/sendAllHelpinfo")
	public Map<String, Object> sendAllHelpinfo(Helpinfo form,HttpServletRequest request) {
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		List<HashMap<String, Object>> helpinfomaplist = new ArrayList<HashMap<String, Object>>();
		List<Helpinfo> helpinfolist = helpinfoDao.queryByList(form);
		for (Helpinfo helpinfo : helpinfolist) {
			HashMap<String, Object> helpinfoMap = new HashMap<String, Object>();
			helpinfoMap.put("type", helpinfo.getType());
			helpinfoMap.put("question", helpinfo.getQuestion());
			helpinfoMap.put("answer", helpinfo.getAnswer());
			helpinfomaplist.add(helpinfoMap);
		}
		
		dataMap.put("helpinfoList", helpinfomaplist);
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("status", "1");
		this.saveRequestRecord(request, responseMap.get("status").toString());
		return dataMap;
	}
	
	private Map<String, Object> productInfoPackage(Map<String, Object> responseMap) {
		// 产品信息
		List<HashMap<String, Object>> productlist = new ArrayList<HashMap<String, Object>>();
		Product findProduct = new Product();
		findProduct.setQuerystate("1");//只推送激活的产品
		List<Product> products = productDao.findByList(findProduct);
		for (Product product : products) {
			HashMap<String, Object> productMap = new HashMap<String, Object>();
			productMap.put("netid", product.getNetid());
			String netname = "";
			if(networkDao.findById(product.getNetid()) !=null){
			netname = 	networkDao.findById(product.getNetid()).getNetname();
			}
			productMap.put("netname", netname);
			productMap.put("productid", product.getProductid());
			productMap.put("productname", product.getProductname());
			productMap.put("producttype", product.getProducttype());
			productMap.put("pricepermonth", product.getPricepermonth());
			productMap.put("priceperday", product.getPriceperday());
			productMap.put("subpricepermonth", product.getSubpricepermonth());
			productMap.put("subpriceperday", product.getSubpriceperday());
			productMap.put("extendflag", product.getExtendflag());
			productMap.put("state", product.getState());
			// extend info
			Productextend findProductExtend = new Productextend();
			findProductExtend.setNetid(product.getNetid());
			findProductExtend.setProductid(product.getProductid());
			List<Productextend> productextends = promotioninfoDao.findByProduct(findProductExtend);
			List<HashMap<String, Object>> productextendsList = new ArrayList<HashMap<String, Object>>();
			for(Productextend productextend : productextends){
				HashMap<String, Object> productextendMap = new HashMap<String, Object>();
				productextendMap.put("webflag", productextend.getWebflag());
				productextendMap.put("rank", productextend.getRank());
				productextendMap.put("description", productextend.getDescription());
				productextendMap.put("type", productextend.getType());
				productextendMap.put("filename", productextend.getFilename());
				productextendMap.put("preservefilename", productextend.getPreservefilename());
				productextendMap.put("fullpath", productextend.getPreserveurl());
				productextendsList.add(productextendMap);
			}
			productMap.put("productextend",productextendsList);
			// product包含的servicelist
			Productserviceref findProductserviceref = new Productserviceref();
			findProductserviceref.setNetid(product.getNetid());
			findProductserviceref.setProductid(product.getProductid());
			List<Productserviceref> productservicerefs = productservicerefDao.findByProductid(findProductserviceref);
			List<HashMap<String, Object>> serviceIncludedlist = new ArrayList<HashMap<String, Object>>();
			for (Productserviceref productserviceref : productservicerefs) {
				HashMap<String, Object> serviceIncluded = new HashMap<String, Object>();
				//Service service  = serviceDao.findById(productserviceref.getServiceid());
				//serviceIncluded.put("serviceid", service != null ? service.getServiceid() : "");
				serviceIncluded.put("serviceid", productserviceref.getServiceid());
				serviceIncludedlist.add(serviceIncluded);
			}
			productMap.put("serviceIncluded", serviceIncludedlist);
			productlist.add(productMap);
		}
		responseMap.put("productForMPS", productlist);
		
		// service信息
		List<HashMap<String, Object>> serviceList = new ArrayList<HashMap<String, Object>>();
		Service findService = new Service();
		findService.setQuerystate("1");//只推送激活的业务
		List<Service> services = serviceDao.findByList(findService);
		for (Service service : services) {
			HashMap<String, Object> serviceMap = new HashMap<String, Object>();
			serviceMap.put("netid", service.getNetid());
			String netname = "";
			if(networkDao.findById(service.getNetid()) !=null){
			netname = 	networkDao.findById(service.getNetid()).getNetname();
			}
			serviceMap.put("netname", netname);
			serviceMap.put("serviceid", service.getServiceid());
			serviceMap.put("servicename", service.getServicename());
			serviceMap.put("servicetype", service.getServicetype());
			serviceMap.put("chargetype", service.getChargetype());
			serviceMap.put("pricepermonth", service.getPricepermonth());
			serviceMap.put("priceperday", service.getPriceperday());
			serviceMap.put("unit", service.getUnit());
			serviceMap.put("extendflag", service.getExtendflag());
			serviceMap.put("state", service.getState());
			// extend info
			Serviceextend findServiceextend = new Serviceextend();
			findServiceextend.setServiceid(service.getServiceid());
			findServiceextend.setNetid(service.getNetid());
			List<Serviceextend> serviceextends = promotioninfoDao.findByService(findServiceextend);
			List<HashMap<String, Object>> serviceextendList = new ArrayList<HashMap<String, Object>>();
			for(Serviceextend serviceextend : serviceextends){
				HashMap<String, Object> serviceextendMap = new HashMap<String, Object>();
				serviceextendMap.put("webflag", serviceextend.getWebflag());
				serviceextendMap.put("rank", serviceextend.getRank());
				serviceextendMap.put("description", serviceextend.getDescription() );
				serviceextendMap.put("type", serviceextend.getType());
				serviceextendMap.put("filename", serviceextend.getFilename());
				serviceextendMap.put("preservefilename", serviceextend.getPreservefilename());
				serviceextendMap.put("fullpath", serviceextend.getPreserveurl());
				serviceextendList.add(serviceextendMap);
			}
			serviceMap.put("serviceextend", serviceextendList);
			serviceList.add(serviceMap);
		}
		responseMap.put("serviceForMPS", serviceList);
		//用户级别
		List<HashMap<String, Object>> userLevelList = new ArrayList<HashMap<String, Object>>();
		List<Userlevelproduct> userlevelproduct = userlevelproductDao.queryByList(new Userlevelproduct());
		for (Userlevelproduct levelProduct : userlevelproduct) {
			HashMap<String, Object> userLevelMap = new HashMap<String, Object>();
			userLevelMap.put("userlevelid",levelProduct.getUserlevelid());
			userLevelMap.put("productid", levelProduct.getProductid());
			userLevelMap.put("pricepermonth",levelProduct.getPricepermonth() );
			userLevelMap.put("priceperday",levelProduct.getPriceperday() );
			userLevelList.add(userLevelMap);
		}
		responseMap.put("userLevelForMPS", userLevelList);
		return responseMap;
	}
	
	private void saveRequestRecord(HttpServletRequest request, String result) {
		Httprequestlog log = new Httprequestlog();
		log.setRequesturl(request.getRequestURL().toString());
		log.setRequestparam(request.getQueryString());
		log.setResult(result);
		log.setAddtime(Tools.getCurrentTime());
		httprequestlogDao.save(log);
	}
	
	
	/**
	 * 个人用户购买产品验证
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/buyProductValidation")
	public Map<String, Object> buyProductValidation(HttpServletRequest request) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String usercode = request.getParameter("usercode");
		String jsonStr = request.getParameter("billInfo");
		JSONObject jsonObj = new JSONObject(jsonStr);
		String payMoney = jsonObj.getString("payMoney");
		BigDecimal payMoneyValidation = new BigDecimal(payMoney);
		User user = userDao.findById(Integer.valueOf(usercode));
		JSONArray billDetails = jsonObj.getJSONArray("billDetail");
		JSONArray details = jsonObj.getJSONArray("billDetail");//所有产品
		if (user != null) {
			//验证产品是否有效
			for (int i = 0; i < billDetails.length(); i++) {
				JSONObject detail = billDetails.getJSONObject(i);
				Product productform = new Product();
				productform.setNetid(user.getNetid());
				productform.setProductid(detail.getString("id"));
				productform = productDao.findByProductid(productform);
				//产品不存在或者无效
				if(productform == null || "0".equals(productform.getState())){
					responseMap.put("status", "3");//产品不存在或者无效
					return responseMap;
				}
			}
			//查询参数设置-子机（子卡）是否继承母机（母卡）授权
			String systempara = systemparaService.findSystemParaByCodeStr("sub_hold_main_flag");
			String terminaltype = jsonObj.getString("terminaltype");//终端类型  0:机顶盒；1：智能卡；
			String terminalid = jsonObj.getString("terminalid");//终端号  机顶盒：机顶盒号；智能卡：智能卡号
			
			BigDecimal validationMoney = new BigDecimal("0");
			Integer userlevelid = user.getUserlevelid();//订户级别
			Userlevelproduct userlevelproduct = new Userlevelproduct();
			Product product = new Product();
			
			
			if(terminaltype.equals("1")){//智能卡号
				Usercard usercard = usercardDao.findByCardidStr(terminalid);
				if(usercard==null){
					responseMap.put("status", "8");//该智能卡不存在或者无效
					return responseMap;
				}
				if("0".equals(usercard.getMothercardflag())){//主卡
					for (int j = 0; j < details.length(); j++) {
						JSONObject detail = details.getJSONObject(j);
						String productid = detail.getString("id");//产品id
						userlevelproduct.setProductid(productid);
						userlevelproduct.setUserlevelid(userlevelid);
						String unit = detail.getString("unit");
						BigDecimal buyamount = new BigDecimal(detail.getString("buyamount"));//月数或者天数
						//验证购买月数必须大于0
						if(buyamount.compareTo(new BigDecimal(0)) < 1){
							responseMap.put("status", "0");//每个产品的支付金额不正确
							return responseMap;
						}
						Userlevelproduct levelproduct = userlevelproductDao.findByProductidAndUserlevelid(userlevelproduct);
						
						//从后台计算这条产品的的购买金额
						BigDecimal multiply = new BigDecimal("0");
						if(levelproduct != null){
							if("month".equals(unit)){
								multiply = levelproduct.getPricepermonth().multiply(buyamount);
								validationMoney = multiply.add(validationMoney);
							}else if("day".equals(unit)){
								multiply = levelproduct.getPriceperday().multiply(buyamount);
								validationMoney = validationMoney.add(multiply);
							}
						}else{
							Integer netid = user.getNetid();//订户所属网络
							product.setNetid(netid);
							product.setProductid(productid);
							Product oldProduct = productDao.findByProductid(product);
							if("month".equals(unit)){
								multiply = oldProduct.getPricepermonth().multiply(buyamount);
								validationMoney = validationMoney.add(multiply);
							}else if("day".equals(unit)){
								multiply = oldProduct.getPriceperday().multiply(buyamount);
								validationMoney = validationMoney.add(multiply);
							}
						}
						
						//验证这个产品的金额是否计算正确
						//产品金额 
						BigDecimal totalmoney = new BigDecimal(detail.getString("totalmoney"));
						if(multiply.compareTo(totalmoney) != 0){
							responseMap.put("status", "0");//每个产品的支付金额不正确
							return responseMap;
						}
					}
				}else{//副卡
					for (int j = 0; j < details.length(); j++) {
						JSONObject detail = details.getJSONObject(j);
						String productid = detail.getString("id");//产品id
						Integer netid = user.getNetid();//订户所属网络
						product.setNetid(netid);
						product.setProductid(productid);
						Product oldProduct = productDao.findByProductid(product);
						String unit = detail.getString("unit");
						BigDecimal buyamount = new BigDecimal(detail.getString("buyamount"));//月数或者天数
						//验证购买月数必须大于0
						if(buyamount.compareTo(new BigDecimal(0)) < 1){
							responseMap.put("status", "0");//每个产品的支付金额不正确
							return responseMap;
						}
						
						BigDecimal multiply = null;
						if("month".equals(unit)){
							multiply = oldProduct.getSubpricepermonth().multiply(buyamount);
							validationMoney = validationMoney.add(multiply);
						}else if("day".equals(unit)){
							multiply = oldProduct.getSubpriceperday().multiply(buyamount);
							validationMoney = validationMoney.add(multiply);
						}
						//验证这个产品的金额是否计算正确
						//产品金额 
						BigDecimal totalmoney = new BigDecimal(detail.getString("totalmoney"));
						if(multiply.compareTo(totalmoney) != 0){
							responseMap.put("status", "0");//每个产品的支付金额不正确
							return responseMap;
						}
					}
				}
			}
			//验证支付总金额
			if(payMoneyValidation.compareTo(validationMoney) != 0){
				responseMap.put("status", "0");//支付金额不正确
				return responseMap;
			}
			
			if(terminaltype.equals("1")){//智能卡号
				
				Usercard usercard = usercardDao.findByCardidStr(terminalid);
				if(usercard != null){
					//判断子母卡
					if("0".equals(usercard.getMothercardflag())){//母卡
						Userproduct userproductForm = new Userproduct();
						userproductForm.setUserid(user.getId());
						userproductForm.setTerminalids(terminalid);
						//查询数据中该张智能卡购买产品的最大授权时间
						HashMap<String,HashMap<String,String>> allProductLastEndTime  = serproductService.findAllProductLastEndTimeListByUserid(userproductForm);
						if(allProductLastEndTime != null && allProductLastEndTime.size() != 0){//该智能卡已经购买了一些产品，有基本包
							//该卡的基本包在数据库最大的授权结束时间
							String lastEndTime_base = "";
							//该卡的付费包在数据库最大的授权结束时间
							String lastEndTime_fee = "";
							//先找出该智能卡购买的所有产品的最大授权时间MAP
							HashMap<String,String> userproductHP = allProductLastEndTime.get(terminalid);
							lastEndTime_base = userproductHP.get("1");//数据库中该基本包的最大授权时间
							//数据库最大的授权时间与当前时间进行比较
							int flag = Tools.compare_date(lastEndTime_base,Tools.getCurrentTimeByFormat("yyyy-MM-dd"),"yyyy-MM-dd");
							if(flag < 0 ){//表示数据库购买时间过期，判断购买列表中是否购买了基本包；
								boolean buying_Base = false;
								for (int i = 0; i < billDetails.length(); i++) {//基本包
									JSONObject detail = billDetails.getJSONObject(i);
									String productId = detail.getString("id");
									if("1".equals(productId)){//基本包
										buying_Base = true;
										lastEndTime_base = Tools.getCurrentTimeByFormat("yyyy-MM-dd");//该基本包的最大的授权时间应该为当前时间
										String buyamount = detail.getString("buyamount");
										lastEndTime_base = Tools.getSpecifiedMonthAfter(lastEndTime_base,"yyyy-MM-dd",Integer.parseInt(buyamount));
										//顺延后基本包的最大授权时间减一天
										lastEndTime_base = Tools.getSpecifiedDayAfter(lastEndTime_base,"yyyy-MM-dd",-1);
									}
								}
								if(!buying_Base){
									responseMap.put("status", "15");//没有购买基本包，不能购买
									return responseMap;
								}
							}else{//表示数据库购买时间未过期
								//找到购买列表中购买的基本包，获取购买月数，顺延基本包的最大授权时间
								for (int j = 0; j < details.length(); j++) {
									JSONObject detail = details.getJSONObject(j);
									String productid = detail.getString("id");//产品id
									if("1".equals(productid)){
										String buyamount = detail.getString("buyamount");
										lastEndTime_base = Tools.getSpecifiedMonthAfter(lastEndTime_base,"yyyy-MM-dd",Integer.parseInt(buyamount));
									}
								}
							}
							//找到购买列表中购买的付费包，获取购买月数，顺延付费包的最大授权时间
							for (int j = 0; j < details.length(); j++) {
								JSONObject detail = details.getJSONObject(j);
								String productid = detail.getString("id");//产品id
								if(!"1".equals(productid)){
									lastEndTime_fee = userproductHP.get(productid);
									if(StringUtils.isNotEmpty(lastEndTime_fee)){//该产品购买过
										//数据库最大的授权时间与当前时间进行比较
										int feeflag = Tools.compare_date(lastEndTime_fee,Tools.getCurrentTimeByFormat("yyyy-MM-dd"),"yyyy-MM-dd");
										if(feeflag < 0 ){//表示数据库购买时间过期，即该付费包的最大的授权时间应该为当前时间
											lastEndTime_fee = Tools.getCurrentTimeByFormat("yyyy-MM-dd");
										}else{//表示数据库购买时间未过期，即该付费包购买的开始时间应该为最大授权时间+1天
											lastEndTime_fee = Tools.getSpecifiedDayAfter(lastEndTime_fee,"yyyy-MM-dd",1);
										}
									}else{//该产品未购买过，即该付费包的最大的授权时间应该为当前时间
										lastEndTime_fee = Tools.getCurrentTimeByFormat("yyyy-MM-dd");
									}
									String buyamount = detail.getString("buyamount");
									lastEndTime_fee = Tools.getSpecifiedMonthAfter(lastEndTime_fee,"yyyy-MM-dd",Integer.parseInt(buyamount));
									//顺延后基本包的最大授权时间减一天
									lastEndTime_fee = Tools.getSpecifiedDayAfter(lastEndTime_fee,"yyyy-MM-dd",-1);
									//判断该付费产品是否合法
									int isflag = Tools.compare_date(lastEndTime_base,lastEndTime_fee,"yyyy-MM-dd");
									if(isflag < 0 ){
										responseMap.put("status", "14");//付费包的授权结束时间不合法，不能购买
										return responseMap;
									}
								}
							}
						}else{//这张卡未授权
							boolean buying_Base = false;//默认没有购买基本包
							String buyamount_Base = "0";//购买基本包的月数
							String buyamount_Fee = "0";//购买付费包的月数
							for (int i = 0; i < billDetails.length(); i++) {//基本包
								JSONObject detail = billDetails.getJSONObject(i);
								String productId = detail.getString("id");
								if("1".equals(productId)){//基本包
									buying_Base = true;
									buyamount_Base = detail.getString("buyamount");
								}
							}
							if(!buying_Base){
								responseMap.put("status", "15");//没有购买基本包，不能购买
								return responseMap;
							}
							for (int i = 0; i < billDetails.length(); i++) {
								JSONObject detail = billDetails.getJSONObject(i);
								String productId = detail.getString("id");
								if(!"1".equals(productId)){//付费包
									buyamount_Fee = detail.getString("buyamount");
								}
								if(Integer.parseInt(buyamount_Base) < Integer.parseInt(buyamount_Fee)){
									responseMap.put("status", "14");//付费包的授权结束时间不合法，不能购买
									return responseMap;
								}
							}
						}
						//可以购买
						responseMap.put("status", "1");
					}else{//子卡
						//判断是否继承
						if("1".equals(systempara)){//有继承，不能购买
							responseMap.put("status", "9");//子卡继承了母卡，不能购买
							return responseMap;
						}else{//没有继承，判断购买权限
							Userproduct form = new Userproduct();
							form.setUserid(user.getId());
							form.setCardid(usercard.getMothercardid());
							form.setProductid("1");
							//母卡基本包的最大授权时间
							String motherLastTime = userproductDao.findLastEndTimeByProductid(form);
							if(motherLastTime != null){
								int mflag = Tools.compare_date(motherLastTime,Tools.getCurrentTimeByFormat("yyyy-MM-dd"),"yyyy-MM-dd");
								if(mflag > 0){
									//查询数据库中该张智能卡购买产品的最大授权时间
									Userproduct userproductForm = new Userproduct();
									userproductForm.setUserid(user.getId());
									userproductForm.setTerminalids(terminalid);
									HashMap<String,HashMap<String,String>> allProductLastEndTime  = serproductService.findAllProductLastEndTimeListByUserid(userproductForm);
									if(allProductLastEndTime != null && allProductLastEndTime.size() != 0){//该智能卡已经购买了一些产品，有基本包
										//该卡的基本包在数据库最大的授权结束时间
										String lastEndTime_base = "";
										//该卡的付费包在数据库最大的授权结束时间
										String lastEndTime_fee = "";
										//先找出该智能卡购买的所有产品的最大授权时间MAP
										HashMap<String,String> userproductHP = allProductLastEndTime.get(terminalid);
										lastEndTime_base = userproductHP.get("1");//数据库中该基本包的最大授权时间
										//数据库最大的授权时间与当前时间进行比较
										int flag = Tools.compare_date(lastEndTime_base,Tools.getCurrentTimeByFormat("yyyy-MM-dd"),"yyyy-MM-dd");
										if(flag < 0 ){//表示数据库购买时间过期，判断购买列表中是否购买了基本包；
											boolean buying_Base = false;
											for (int i = 0; i < billDetails.length(); i++) {//基本包
												JSONObject detail = billDetails.getJSONObject(i);
												String productId = detail.getString("id");
												if("1".equals(productId)){//基本包
													buying_Base = true;
													lastEndTime_base = Tools.getCurrentTimeByFormat("yyyy-MM-dd");//该基本包的最大的授权时间应该为当前时间
													String buyamount = detail.getString("buyamount");
													lastEndTime_base = Tools.getSpecifiedMonthAfter(lastEndTime_base,"yyyy-MM-dd",Integer.parseInt(buyamount));
													//顺延后基本包的最大授权时间减一天
													lastEndTime_base = Tools.getSpecifiedDayAfter(lastEndTime_base,"yyyy-MM-dd",-1);
												}
											}
											if(!buying_Base){
												responseMap.put("status", "15");//没有购买基本包，不能购买
												return responseMap;
											}
										}else{//表示数据库购买时间未过期
											//找到购买列表中购买的基本包，获取购买月数，顺延基本包的最大授权时间
											for (int j = 0; j < details.length(); j++) {
												JSONObject detail = details.getJSONObject(j);
												String productid = detail.getString("id");//产品id
												if("1".equals(productid)){
													String buyamount = detail.getString("buyamount");
													lastEndTime_base = Tools.getSpecifiedMonthAfter(lastEndTime_base,"yyyy-MM-dd",Integer.parseInt(buyamount));
												}
											}
										}
										
										if(Tools.compare_date(motherLastTime,lastEndTime_base,"yyyy-MM-dd") < 0){
											responseMap.put("status", "17");//子卡购买基本包的授权时间超过母卡购买基本包的授权时间，不能购买
											return responseMap;
										}
										//找到购买列表中购买的付费包，获取购买月数，顺延付费包的最大授权时间
										for (int j = 0; j < details.length(); j++) {
											JSONObject detail = details.getJSONObject(j);
											String productid = detail.getString("id");//产品id
											if(!"1".equals(productid)){
												lastEndTime_fee = userproductHP.get(productid);
												if(StringUtils.isNotEmpty(lastEndTime_fee)){//该产品购买过
													//数据库最大的授权时间与当前时间进行比较
													int feeflag = Tools.compare_date(lastEndTime_fee,Tools.getCurrentTimeByFormat("yyyy-MM-dd"),"yyyy-MM-dd");
													if(feeflag < 0 ){//表示数据库购买时间过期，即该付费包的最大的授权时间应该为当前时间
														lastEndTime_fee = Tools.getCurrentTimeByFormat("yyyy-MM-dd");
													}else{//表示数据库购买时间未过期，即该付费包购买的开始时间应该为最大授权时间+1天
														lastEndTime_fee = Tools.getSpecifiedDayAfter(lastEndTime_fee,"yyyy-MM-dd",1);
													}
												}else{//该产品未购买过，即该付费包的最大的授权时间应该为当前时间
													lastEndTime_fee = Tools.getCurrentTimeByFormat("yyyy-MM-dd");
												}
												String buyamount = detail.getString("buyamount");
												lastEndTime_fee = Tools.getSpecifiedMonthAfter(lastEndTime_fee,"yyyy-MM-dd",Integer.parseInt(buyamount));
												//顺延后基本包的最大授权时间减一天
												lastEndTime_fee = Tools.getSpecifiedDayAfter(lastEndTime_fee,"yyyy-MM-dd",-1);
												//判断该付费产品是否合法
												int isflag = Tools.compare_date(lastEndTime_base,lastEndTime_fee,"yyyy-MM-dd");
												if(isflag < 0 ){
													responseMap.put("status", "14");//付费包的授权结束时间不合法，不能购买
													return responseMap;
												}
											}
										}
									}else{//这张卡未授权
										boolean buying_Base = false;//默认没有购买基本包
										String buyamount_Base = "0";//购买基本包的月数
										String buyamount_Fee = "0";//购买付费包的月数
										String lastEndTimebase = "";//基本包授权时间
										for (int i = 0; i < billDetails.length(); i++) {//基本包
											JSONObject detail = billDetails.getJSONObject(i);
											String productId = detail.getString("id");
											if("1".equals(productId)){//基本包
												buying_Base = true;
												buyamount_Base = detail.getString("buyamount");
												lastEndTimebase = Tools.getCurrentTimeByFormat("yyyy-MM-dd");//该基本包的最大的授权时间应该为当前时间
												lastEndTimebase = Tools.getSpecifiedMonthAfter(lastEndTimebase,"yyyy-MM-dd",Integer.parseInt(buyamount_Base));
												//顺延后基本包的最大授权时间减一天
												lastEndTimebase = Tools.getSpecifiedDayAfter(lastEndTimebase,"yyyy-MM-dd",-1);
											}
										}
										if(!buying_Base){
											responseMap.put("status", "15");//没有购买基本包，不能购买
											return responseMap;
										}
										if(Tools.compare_date(motherLastTime,lastEndTimebase,"yyyy-MM-dd") < 0){
											responseMap.put("status", "17");//子卡购买基本包的授权时间超过母卡购买基本包的授权时间，不能购买
											return responseMap;
										}
										for (int i = 0; i < billDetails.length(); i++) {
											JSONObject detail = billDetails.getJSONObject(i);
											String productId = detail.getString("id");
											if(!"1".equals(productId)){//付费包
												buyamount_Fee = detail.getString("buyamount");
											}
											if(Integer.parseInt(buyamount_Base) < Integer.parseInt(buyamount_Fee)){
												responseMap.put("status", "14");//付费包的授权结束时间不合法，不能购买
												return responseMap;
											}
										}
									}
									//可以购买
									responseMap.put("status", "1");
								}else{//母卡基本包的最大授权时间过期
									responseMap.put("status", "16");//母卡没有购买基本包，不能购买
									return responseMap;
								}
							}else{//母卡基本包的最大授权时间为空，即母卡没有购买基本包，子卡也不能购买
								responseMap.put("status", "16");//母卡没有购买基本包，不能购买
								return responseMap;
							}
						}
					}
				}
			}
		}else{
			responseMap.put("status", "2");//用户编码不存在
		}
		return responseMap;
	}
	
	/**
	 * 集团用户支付
	 * @param form
	 * @param request
	 * @return
	 *  {"status":"0"}为支付金额不正确 
	 *  {"status":"1"}为成功 
	 *  {"status":"2"}用户编码不存在                                                             
	 *  {"status":"3"}产品不存在或者无效                                                      
	 *  {"status":"4"}子机继承了主机,不能购买
	 *  {"status":"5"}子机购买的产品授权不能超出主机授权的范围
	 *  {"status":"6"}请选择用智能卡购买                                                       
	 *  {"status":"7"}该机顶盒不存在或者无效                                             
	 *  {"status":"8"}该智能卡不存在或者无效											
	 *  {"status":"9"}子卡继承了母卡，不能购买				
	 *  {"status":"10"}子卡购买的产品授权不能超出母卡授权的范围     
	 *  {"status":"11"}购买的产品授权时间出现重复（已经购买）     
	 *	{"status":"12"}账户余额不足
     *  {"status":"13"}支付密码错误
     *  {"status":"14"}付费包的授权结束时间不合法，不能购买
     *  {"status":"15"}没有购买基本包，不能购买
     *  {"status":"16"}母卡没有购买基本包，不能购买
     *  {"status":"17"}子卡购买基本包的授权时间超过母卡购买基本包的授权时间，不能购买
	 */
	@ResponseBody
	@RequestMapping(value = "/updateUserGroupAccount")
	public Map<String, Object> updateUserGroupAccount(User form, HttpServletRequest request){
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String usercode = request.getParameter("usercode");
		String paytype = request.getParameter("paytype"); // 支付类型（0-现金支付；1-余额支付；2-积分支付；3-其他支付）
		String jsonStr = request.getParameter("billInfo");
		JSONObject jsonObj = new JSONObject(jsonStr);
		//验证产品是否购买资格权限（数据是否正确）
		Map<String, Object> returnHashmap = buyGroupProductValidation(request);
		if(!"1".equals(returnHashmap.get("status"))){
			return returnHashmap;
		}
		User user = userDao.findById(Integer.valueOf(usercode));
		if (user != null) {
			if (!paytype.equals("1")) {
					/***************** 封装userbusiness主表 ******************/
					Userbusiness userbusiness = this.packUserbusiness(user, jsonObj);
					userbusiness.setPaytype(paytype);
					userbusinessDao.save(userbusiness);
					/***************** 封装userproduct *****************/
					JSONArray details = jsonObj.getJSONArray("billDetail");
					JSONArray terminalids = jsonObj.getJSONArray("terminalidlist");//所有终端
					List<Userproduct> userproductList = new ArrayList<Userproduct>();//批量封装Userproduct
					for (int j = 0; j < terminalids.length(); j++) {//循环终端
						String terminalid = (String)terminalids.get(j);
						for (int i = 0; i < details.length(); i++) {//循环产品
							JSONObject detail = details.getJSONObject(i);
							if ("buyproduct".equals(detail.getString("buytype"))) {
								Userproduct userproduct = this.packGroupUserproduct(user, jsonObj, detail,terminalid);
								userproductList.add(userproduct);
							}
						}
					}
					//批量保存Userproduct
					userproductDao.saveBatch(userproductList);
					//批量发送授权指令
					authorizeService.saveAuthorize_buyProduct_batch(userproductList);
					
					//批量删除每个产品的过期授权记录
					for (Userproduct userproduct : userproductList) {
						userproductDao.deleteInvalidProduct(userproduct);
					}
					
					//产品费用,每一种产品封装一条记录明细
					saveProductBusinessdetail(user,userproductList,userbusiness.getId());
					
					responseMap.put("status", "1");
			} else { // 1-余额支付；
				String paypassword = request.getParameter("paypassword");
				User buyerUser = new User();//付款人
				String source = jsonObj.getString("source");
				if("2".equals(source)){//代办
					Integer buyerid = jsonObj.getInt("buyerid");
					buyerUser  = userDao.findById(buyerid);
				}else{
					buyerUser = user;
				}
				if (paypassword.equals(buyerUser.getPaypassword())) {
					BigDecimal shouldmoney = new BigDecimal(jsonObj.getString("totalMoney"));
					if (buyerUser.getAccount().compareTo(shouldmoney) > -1) { // 返回的结果是int类型，-1表示小于，0是等于，1是大于。
						// 这里直接取值封装然后插入数据库(userbusiness,userbusinessdetail)
						buyerUser.setAccount(buyerUser.getAccount().subtract(shouldmoney));
						userDao.update(buyerUser);
						//增加订户账户出入账记录
						Useraccountlog useraccountlog = new Useraccountlog();
						useraccountlog.setNetid(buyerUser.getNetid());
						useraccountlog.setUserid(buyerUser.getId());
						useraccountlog.setType("1");//类型（0-入账；1-出账）
						useraccountlog.setMoney(new BigDecimal(0).subtract(shouldmoney));
						useraccountlog.setOperatorid(null);
						useraccountlog.setAddtime(Tools.getCurrentTime());
						useraccountlog.setSource(source);
						useraccountlog.setBusinesstypekey("accountpayment");
						useraccountlogDao.save(useraccountlog);
						
						/***************** 封装userbusiness主表 ******************/
						Userbusiness userbusiness = this.packUserbusiness(user, jsonObj);
						userbusiness.setPaytype(paytype);
						userbusinessDao.save(userbusiness);
						/***************** 封装userproduct*****************/
						JSONArray details = jsonObj.getJSONArray("billDetail");
						JSONArray terminalids = jsonObj.getJSONArray("terminalidlist");//所有终端
						List<Userproduct> userproductList = new ArrayList<Userproduct>();//批量封装Userproduct
						for (int j = 0; j < terminalids.length(); j++) {
							String terminalid = (String)terminalids.get(j);
							for (int i = 0; i < details.length(); i++) {
								JSONObject detail = details.getJSONObject(i);
								if ("buyproduct".equals(detail.getString("buytype"))) {
									Userproduct userproduct = this.packGroupUserproduct(user, jsonObj, detail,terminalid);
									userproductList.add(userproduct);
								}
							}
						}
						//批量保存Userproduct
						userproductDao.saveBatch(userproductList);
						
						//批量删除每个产品的过期授权记录
						for (Userproduct userproduct : userproductList) {
							userproductDao.deleteInvalidProduct(userproduct);
						}
						
						//批量发送授权指令
						authorizeService.saveAuthorize_buyProduct_batch(userproductList);
						//产品费用,每一种产品封装一条记录明细
						saveProductBusinessdetail(user,userproductList,userbusiness.getId());
						
						responseMap.put("status", "1");
					} else {
						responseMap.put("status", "12");
						responseMap.put("errmsg", "余额不足");
					}
				} else {
					responseMap.put("status", "13");
					responseMap.put("errmsg", "支付密码错误");
				}
			}
		} 
		this.saveRequestRecord(request, responseMap.get("status").toString());
		return responseMap;
	}
	
	/**
	 * 集团用户购买验证
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/buyGroupProductValidation")
	public Map<String, Object> buyGroupProductValidation(HttpServletRequest request) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String usercode = request.getParameter("usercode");//用户编码
		String jsonStr = request.getParameter("billInfo");
		JSONObject jsonObj = new JSONObject(jsonStr);//一次购买信息
		String payMoney = jsonObj.getString("payMoney");//支付金额
		BigDecimal payMoneyValidation = new BigDecimal(payMoney);
		User user = userDao.findById(Integer.valueOf(usercode));
		if (user != null) {
			//验证产品是否有效
			JSONArray billDetails = jsonObj.getJSONArray("billDetail");
			for (int i = 0; i < billDetails.length(); i++) {
				JSONObject detail = billDetails.getJSONObject(i);
				Product productform = new Product();
				productform.setNetid(user.getNetid());
				productform.setProductid(detail.getString("id"));
				productform = productDao.findByProductid(productform);
				//产品不存在或者无效
				if(productform == null || "0".equals(productform.getState())){
					responseMap.put("status", "3");//产品不存在或者无效
					return responseMap;
				}
			}
			//验证总金额
			BigDecimal validationMoney = new BigDecimal("0");
			Userlevelproduct userlevelproduct = new Userlevelproduct();
			Product product = new Product();
			Integer userlevelid = user.getUserlevelid();//订户级别
			JSONArray details = jsonObj.getJSONArray("billDetail");//所有产品
			JSONArray terminalids = jsonObj.getJSONArray("terminalidlist");//所有终端
//			String terminaltype = jsonObj.getString("terminaltype");//终端类型  0:机顶盒；1：智能卡；
			String terminaltype = "1";
			for (int i = 0; i < terminalids.length(); i++) {
				String terminalNum = (String)terminalids.get(i);
				if(terminaltype.equals("0")){//机顶盒号
					Userstb userstb = userstbDao.findByStbnoStr(terminalNum);
					if(userstb==null){
						responseMap.put("status", "7");//该机顶盒不存在或者无效
						return responseMap;
					}
					if("0".equals(userstb.getMothercardflag())){//主机
						for (int j = 0; j < details.length(); j++) {
							JSONObject detail = details.getJSONObject(j);
							String productid = detail.getString("id");//产品id
							userlevelproduct.setProductid(productid);
							userlevelproduct.setUserlevelid(userlevelid);
							Userlevelproduct levelproduct = userlevelproductDao.findByProductidAndUserlevelid(userlevelproduct);
							String unit = detail.getString("unit");
							BigDecimal buyamount = new BigDecimal(detail.getString("buyamount"));//月数或者天数
							//验证购买月数必须大于0
							if(buyamount.compareTo(new BigDecimal(0)) < 1){
								responseMap.put("status", "0");//每个产品的支付金额不正确
								return responseMap;
							}
							BigDecimal multiply = null;
							if("month".equals(unit)){
								if(levelproduct!=null){
									multiply = levelproduct.getPricepermonth().multiply(buyamount);
									validationMoney = validationMoney.add(multiply);
								}else{
									validationMoney = new BigDecimal("0");
								}
							}else if("day".equals(unit)){
								if(levelproduct!=null){
									multiply = levelproduct.getPriceperday().multiply(buyamount);
									validationMoney = validationMoney.add(multiply);
								}else{
									validationMoney = new BigDecimal("0");
								}
							}
							//验证这个产品的金额是否计算正确
							//产品金额 
							BigDecimal totalmoney = new BigDecimal(detail.getString("totalmoney"));
							if(multiply.compareTo(totalmoney) != 0){
								responseMap.put("status", "0");//每个产品的支付金额不正确
								return responseMap;
							}
							
						}
					}else{//副机
						for (int j = 0; j < details.length(); j++) {
							JSONObject detail = details.getJSONObject(j);
							String productid = detail.getString("id");//产品id
							Integer netid = user.getNetid();//订户所属网络
							product.setNetid(netid);
							product.setProductid(productid);
							Product oldProduct = productDao.findByProductid(product);
							String unit = detail.getString("unit");
							BigDecimal buyamount = new BigDecimal(detail.getString("buyamount"));//月数或者天数
							//验证购买月数必须大于0
							if(buyamount.compareTo(new BigDecimal(0)) < 1){
								responseMap.put("status", "0");//每个产品的支付金额不正确
								return responseMap;
							}
							BigDecimal multiply = null;
							if("month".equals(unit)){
								multiply = oldProduct.getSubpricepermonth().multiply(buyamount);
								validationMoney = validationMoney.add(multiply);
							}else if("day".equals(unit)){
								multiply = oldProduct.getSubpriceperday().multiply(buyamount);
								validationMoney = validationMoney.add(multiply);
							}
							//验证这个产品的金额是否计算正确
							//产品金额 
							BigDecimal totalmoney = new BigDecimal(detail.getString("totalmoney"));
							if(multiply.compareTo(totalmoney) != 0){
								responseMap.put("status", "0");//每个产品的支付金额不正确
								return responseMap;
							}
						}
					}
				}else if(terminaltype.equals("1")){//智能卡号
					Usercard usercard = usercardDao.findByCardidStr(terminalNum);
					if(usercard==null){
						responseMap.put("status", "8");//该智能卡不存在或者无效
						return responseMap;
					}
					if("0".equals(usercard.getMothercardflag())){//主卡
						for (int j = 0; j < details.length(); j++) {
							JSONObject detail = details.getJSONObject(j);
							String productid = detail.getString("id");//产品id
							userlevelproduct.setProductid(productid);
							userlevelproduct.setUserlevelid(userlevelid);
							Userlevelproduct levelproduct = userlevelproductDao.findByProductidAndUserlevelid(userlevelproduct);
							String unit = detail.getString("unit");
							BigDecimal buyamount = new BigDecimal(detail.getString("buyamount"));//月数或者天数
							//验证购买月数必须大于0
							if(buyamount.compareTo(new BigDecimal(0)) < 1){
								responseMap.put("status", "0");//每个产品的支付金额不正确
								return responseMap;
							}
							BigDecimal multiply = new BigDecimal("0");
							if("month".equals(unit)){
								if(levelproduct!=null){
									multiply = levelproduct.getPricepermonth().multiply(buyamount);
									validationMoney = multiply.add(validationMoney);
								}else{
									Integer netid = user.getNetid();//订户所属网络
									product.setNetid(netid);
									product.setProductid(productid);
									Product oldProduct = productDao.findByProductid(product);
									multiply = oldProduct.getPricepermonth().multiply(buyamount);
									validationMoney = validationMoney.add(multiply);
								}
							}else if("day".equals(unit)){
								if(levelproduct!=null){
									multiply = levelproduct.getPriceperday().multiply(buyamount);
									validationMoney = validationMoney.add(multiply);
								}else{
									Integer netid = user.getNetid();//订户所属网络
									product.setNetid(netid);
									product.setProductid(productid);
									Product oldProduct = productDao.findByProductid(product);
									multiply = oldProduct.getPriceperday().multiply(buyamount);
									validationMoney = validationMoney.add(multiply);
								}
							}
							
							//验证这个产品的金额是否计算正确
							//产品金额 
							BigDecimal totalmoney = new BigDecimal(detail.getString("totalmoney"));
							if(multiply.compareTo(totalmoney) != 0){
								responseMap.put("status", "0");//每个产品的支付金额不正确
								return responseMap;
							}
						}
					}else{//副卡
						for (int j = 0; j < details.length(); j++) {
							JSONObject detail = details.getJSONObject(j);
							String productid = detail.getString("id");//产品id
							Integer netid = user.getNetid();//订户所属网络
							product.setNetid(netid);
							product.setProductid(productid);
							Product oldProduct = productDao.findByProductid(product);
							String unit = detail.getString("unit");
							BigDecimal buyamount = new BigDecimal(detail.getString("buyamount"));//月数或者天数
							//验证购买月数必须大于0
							if(buyamount.compareTo(new BigDecimal(0)) < 1){
								responseMap.put("status", "0");//每个产品的支付金额不正确
								return responseMap;
							}
							BigDecimal multiply = null;
							if("month".equals(unit)){
								multiply = oldProduct.getSubpricepermonth().multiply(buyamount);
								validationMoney = validationMoney.add(multiply);
							}else if("day".equals(unit)){
								multiply = oldProduct.getSubpriceperday().multiply(buyamount);
								validationMoney = validationMoney.add(multiply);
							}
							//验证这个产品的金额是否计算正确
							//产品金额 
							BigDecimal totalmoney = new BigDecimal(detail.getString("totalmoney"));
							if(multiply.compareTo(totalmoney) != 0){
								responseMap.put("status", "0");//每个产品的支付金额不正确
								return responseMap;
							}
						}
					}
				}
			}
			//验证支付金额
			if(payMoneyValidation.compareTo(validationMoney) != 0){
				responseMap.put("status", "0");//支付金额不正确
				return responseMap;
			}
			
			//验证终端产品是否可以购买
			List<String> cardids = new ArrayList<String>();
			for (int i = 0; i < terminalids.length(); i++) {
				cardids.add((String)terminalids.get(i));
			}
			//拼装terminalids
			StringBuffer cardidsStrBuffer = new StringBuffer("");
			for(int i = 0;i<cardids.size();i++){
				if(i == cardids.size()-1){
					cardidsStrBuffer.append("'").append(cardids.get(i)).append("'") ;
			    }else{
			    	cardidsStrBuffer.append("'").append(cardids.get(i)).append("',") ;
			    }
			}
			String allterminalid = cardidsStrBuffer.toString();
			//判断该智能卡机正在购买中的基本包是否包含该授权时间段
			Userproduct userproductForm = new Userproduct();
			userproductForm.setUserid(user.getId());
			userproductForm.setTerminalids(allterminalid);
			//查询数据中每张智能卡购买产品的最大授权时间
			HashMap<String,HashMap<String,String>> allProductLastEndTime  = serproductService.findAllProductLastEndTimeListByUserid(userproductForm);
			if(allProductLastEndTime != null && allProductLastEndTime.size() != 0){
				//购买中的产品不是基本包，得判断该智能卡是否购买了基本包的授权时间
				for(String cardid : cardids){
					//该卡的基本包在数据库最大的授权结束时间
					String lastEndTime_base = "";
					//该卡的付费包在数据库最大的授权结束时间
					String lastEndTime_fee = "";
					//先找出该智能卡购买的所有产品的最大授权时间MAP
					HashMap<String,String> userproductHP = allProductLastEndTime.get(cardid);
					if(userproductHP != null && userproductHP.size() != 0){//该智能卡已经购买了一些产品，有基本包
						lastEndTime_base = userproductHP.get("1");//数据库中该基本包的最大授权时间
						//数据库最大的授权时间与当前时间进行比较
						int flag = Tools.compare_date(lastEndTime_base,Tools.getCurrentTimeByFormat("yyyy-MM-dd"),"yyyy-MM-dd");
						if(flag < 0 ){//表示数据库购买时间过期，判断购买列表中是否购买了基本包；
							boolean buying_Base = false;
							for (int i = 0; i < billDetails.length(); i++) {//基本包
								JSONObject detail = billDetails.getJSONObject(i);
								String productId = detail.getString("id");
								if("1".equals(productId)){//基本包
									buying_Base = true;
									lastEndTime_base = Tools.getCurrentTimeByFormat("yyyy-MM-dd");//该基本包的最大的授权时间应该为当前时间
									String buyamount = detail.getString("buyamount");
									lastEndTime_base = Tools.getSpecifiedMonthAfter(lastEndTime_base,"yyyy-MM-dd",Integer.parseInt(buyamount));
									//顺延后基本包的最大授权时间减一天
									lastEndTime_base = Tools.getSpecifiedDayAfter(lastEndTime_base,"yyyy-MM-dd",-1);
								}
							}
							if(!buying_Base){
								responseMap.put("status", "15");//没有购买基本包，不能购买
								return responseMap;
							}
						}else{
							//找到购买列表中购买的基本包，获取购买月数，顺延基本包的最大授权时间
							for (int j = 0; j < details.length(); j++) {
								JSONObject detail = details.getJSONObject(j);
								String productid = detail.getString("id");//产品id
								if("1".equals(productid)){
									String buyamount = detail.getString("buyamount");
									lastEndTime_base = Tools.getSpecifiedMonthAfter(lastEndTime_base,"yyyy-MM-dd",Integer.parseInt(buyamount));
								}
							}
						}
						//找到购买列表中购买的付费包，获取购买月数，顺延付费包的最大授权时间
						for (int j = 0; j < details.length(); j++) {
							JSONObject detail = details.getJSONObject(j);
							String productid = detail.getString("id");//产品id
							if(!"1".equals(productid)){
								lastEndTime_fee = userproductHP.get(productid);
								if(StringUtils.isNotEmpty(lastEndTime_fee)){//该产品购买过
									//数据库最大的授权时间与当前时间进行比较
									int feeflag = Tools.compare_date(lastEndTime_fee,Tools.getCurrentTimeByFormat("yyyy-MM-dd"),"yyyy-MM-dd");
									if(feeflag < 0 ){//表示数据库购买时间过期，即该付费包的最大的授权时间应该为当前时间
										lastEndTime_fee = Tools.getCurrentTimeByFormat("yyyy-MM-dd");
									}else{//表示数据库购买时间未过期，即该付费包购买的开始时间应该为最大授权时间+1天
										lastEndTime_fee = Tools.getSpecifiedDayAfter(lastEndTime_fee,"yyyy-MM-dd",1);
									}
								}else{//该产品未购买过，即该付费包的最大的授权时间应该为当前时间
									lastEndTime_fee = Tools.getCurrentTimeByFormat("yyyy-MM-dd");
								}
								String buyamount = detail.getString("buyamount");
								lastEndTime_fee = Tools.getSpecifiedMonthAfter(lastEndTime_fee,"yyyy-MM-dd",Integer.parseInt(buyamount));
								//顺延后基本包的最大授权时间减一天
								lastEndTime_fee = Tools.getSpecifiedDayAfter(lastEndTime_fee,"yyyy-MM-dd",-1);
								//判断该付费产品是否合法
								int isflag = Tools.compare_date(lastEndTime_base,lastEndTime_fee,"yyyy-MM-dd");
								if(isflag < 0 ){
									responseMap.put("status", "14");//付费包的授权结束时间不合法，不能购买
									return responseMap;
								}
							}
						}
					}else{ //该卡还未授权,先验证是否够买了基本包
						boolean buying_Base = false;//默认没有购买基本包
						String buyamount_Base = "";//购买基本包的月数
						String buyamount_Fee = "";//购买付费包的月数
						for (int i = 0; i < billDetails.length(); i++) {//基本包
							JSONObject detail = billDetails.getJSONObject(i);
							String productId = detail.getString("id");
							if("1".equals(productId)){//基本包
								buying_Base = true;
								buyamount_Base = detail.getString("buyamount");
							}
						}
						if(!buying_Base){
							responseMap.put("status", "15");//没有购买基本包，不能购买
							return responseMap;
						}
						for (int i = 0; i < billDetails.length(); i++) {
							JSONObject detail = billDetails.getJSONObject(i);
							String productId = detail.getString("id");
							if(!"1".equals(productId)){//付费包
								buyamount_Fee = detail.getString("buyamount");
							}
							if(Integer.parseInt(buyamount_Base) < Integer.parseInt(buyamount_Fee)){
								responseMap.put("status", "14");//付费包的授权结束时间不合法，不能购买
								return responseMap;
							}
						}
					}
				}
			}else{//这一批卡都未授权
				boolean buying_Base = false;//默认没有购买基本包
				String buyamount_Base = "0";//购买基本包的月数
				String buyamount_Fee = "0";//购买付费包的月数
				for (int i = 0; i < billDetails.length(); i++) {//基本包
					JSONObject detail = billDetails.getJSONObject(i);
					String productId = detail.getString("id");
					if("1".equals(productId)){//基本包
						buying_Base = true;
						buyamount_Base = detail.getString("buyamount");
					}
				}
				if(!buying_Base){
					responseMap.put("status", "15");//没有购买基本包，不能购买
					return responseMap;
				}
				for (int i = 0; i < billDetails.length(); i++) {
					JSONObject detail = billDetails.getJSONObject(i);
					String productId = detail.getString("id");
					if(!"1".equals(productId)){//付费包
						buyamount_Fee = detail.getString("buyamount");
					}
					if(Integer.parseInt(buyamount_Base) < Integer.parseInt(buyamount_Fee)){
						responseMap.put("status", "14");//付费包的授权结束时间不合法，不能购买
						return responseMap;
					}
				}
			}
			responseMap.put("status", "1");//可以购买
		}else{
			responseMap.put("status", "2");//用户编码不存在
		}
		return responseMap;
	}
	
	
	
	private Userproduct packGroupUserproduct(User user, JSONObject jsonObj, JSONObject detail,String terminalid) {
		Userproduct userproduct = new Userproduct();
		userproduct.setNetid(user.getNetid());
		userproduct.setAreacode(user.getAreacode());
		userproduct.setTerminalid(terminalid);
		userproduct.setUserid(user.getId());
		userproduct.setProductid(detail.getString("id"));
		userproduct.setType(detail.getString("type"));
		userproduct.setTerminaltype("1");
		String source = jsonObj.getString("source");  
		userproduct.setSource(source); // 0营业厅 1MPS
		if("2".equals(source)){// 代办  
			userproduct.setBuyerid(jsonObj.getInt("buyerid"));
		}
		userproduct.setAddtime(Tools.getCurrentTime());
		if (userproduct.getTerminaltype().equals("0")) { // stbno
			userproduct.setStbno(userproduct.getTerminalid());
			Userstb stbno = new Userstb();
			stbno.setStbno(userproduct.getStbno());
			userproduct.setServerid(userstbDao.findByStbno(stbno).getServerid());
		} else {
			userproduct.setCardid(userproduct.getTerminalid());
			Usercard cardid = new Usercard();
			cardid.setCardid(userproduct.getCardid());
			userproduct.setServerid(usercardDao.findByCardid(cardid).getServerid());
		}
		userproduct.setState("1");
		userproduct.setTotalmoney(new BigDecimal(detail.getString("totalmoney")));
		String unit = detail.getString("unit");//购买方式
		String buyamount = detail.getString("buyamount");//购买数量
		userproduct.setUnit(unit);
		userproduct.setBuyamount(Integer.valueOf(buyamount));
		userproduct.setTerminaltype(jsonObj.getString("terminaltype"));
		if(StringUtils.isEmpty(unit)){
			unit = "month";//如果unit为空，默认按月购买
		}
		//设置授权开始和结束时间
		if("month".equals(unit)){
			String timedate = userproductDao.findLastEndTimeByProductid(userproduct);//该产品的最大授权结束时间
			if(timedate != null){
				//数据库最大的授权时间与当前时间进行比较
				int flag = Tools.compare_date(timedate,Tools.getCurrentTimeByFormat("yyyy-MM-dd"),"yyyy-MM-dd");
				if(flag < 0 ){//表示数据库购买时间过期，即该产品的最大的授权时间应该为当前时间
					timedate = Tools.getCurrentTimeByFormat("yyyy-MM-dd");
				}else{//表示数据库购买时间未过期，即该产品购买的开始时间应该为最大授权时间+1天
					timedate = Tools.getSpecifiedDayAfter(timedate,"yyyy-MM-dd",1);
				}
				userproduct.setStarttime(timedate + " 00:00:00");
				String date = Tools.getSpecifiedMonthAfter(timedate, "yyyy-MM-dd", Integer.parseInt(buyamount));
				String endtime = Tools.getSpecifiedDayAfter(date, "yyyy-MM-dd", -1);//减一天作为结束时间
				userproduct.setEndtime(endtime + " 23:59:59");
			}else{
				timedate = Tools.getCurrentTimeByFormat("yyyy-MM-dd");
				userproduct.setStarttime(timedate + " 00:00:00");
				String date = Tools.getSpecifiedMonthAfter(timedate, "yyyy-MM-dd", Integer.parseInt(buyamount));
				String endtime = Tools.getSpecifiedDayAfter(date, "yyyy-MM-dd", -1);//减一天作为结束时间
				userproduct.setEndtime(endtime + " 23:59:59");
			}
		}
		if ("1".equals(userproduct.getType())) { // product
			Product find_product = new Product();
			find_product.setProductid(userproduct.getProductid());
			find_product.setNetid(userproduct.getNetid());
			Product product = productDao.findByProductid(find_product);
			userproduct.setProductname(product.getProductname());
			if("month".equals(unit)){//按月购买
				userproduct.setPrice(product.getPricepermonth());
			}else if("day".equals(unit)){
				userproduct.setPrice(product.getPriceperday());
			}
		} else if ("2".equals(userproduct.getType())) { // service
			Service find_service = new Service();
			find_service.setServiceid(userproduct.getProductid());
			find_service.setNetid(userproduct.getNetid());
			Service service = serviceDao.findByServiceid(find_service);
			userproduct.setProductname(service.getServicename());
			if("month".equals(unit)){//按月购买
				userproduct.setPrice(service.getPricepermonth());
			}else if("day".equals(unit)){
				userproduct.setPrice(service.getPriceperday());
			}
		} 
		return userproduct;
	}
	/**
	 * 保存每一种产品的明细
	 */
	public void saveProductBusinessdetail(User user,List<Userproduct> userproductList,Integer businessid){
		if(userproductList != null && userproductList.size() > 0){
			HashMap<String,Userproduct>  userproductMap = new HashMap<String,Userproduct>();
			for (Userproduct userproduct : userproductList) {
				if(userproductMap.containsKey(userproduct.getProductid())){
					Userproduct userproductDMP = userproductMap.get(userproduct.getProductid());
					BigDecimal totalmoneydmp = userproductDMP.getTotalmoney().add(userproduct.getTotalmoney());
					userproductDMP.setTotalmoney(totalmoneydmp);
					userproductMap.put(userproductDMP.getProductid(), userproductDMP);
				}else{
					Userproduct userproductDMP = new Userproduct();
					Tools.setVOToVO(userproduct, userproductDMP);
					userproductMap.put(userproductDMP.getProductid(), userproductDMP);
				}
			}
			//循环MAP，针对每一个产品，保留一个记录
			Iterator userproductIter = userproductMap.entrySet().iterator();
			while (userproductIter.hasNext()) {
				Map.Entry userproductEntry = (Map.Entry) userproductIter.next();
				Userproduct userproduct = (Userproduct)userproductEntry.getValue();
				Userbusinessdetail businessdetail = new Userbusinessdetail();
				businessdetail.setNetid(user.getNetid());
				businessdetail.setAreacode(user.getAreacode());
				businessdetail.setUserid(user.getId());
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
				businessdetail.setLogout("0");
				businessdetail.setSource("1");
				
				businessdetail.setBusinessid(businessid);
				
				userbusinessdetailDao.save(businessdetail);
			}
		}
	}
	
}
