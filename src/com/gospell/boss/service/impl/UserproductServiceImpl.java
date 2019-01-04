package com.gospell.boss.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gospell.boss.common.Tools;
import com.gospell.boss.dao.IProviderDao;
import com.gospell.boss.dao.ICardDao;
import com.gospell.boss.dao.IUserproductDao;
import com.gospell.boss.po.Provider;
import com.gospell.boss.po.Card;
import com.gospell.boss.po.Usercard;
import com.gospell.boss.po.Userproduct;
import com.gospell.boss.service.ICardService;
import com.gospell.boss.service.IUserproductService;

/**
 * @Title UserproductServiceImpl.java
 * @version 1.0 高斯贝尔高安Ca实现类
 */
@Service("userproductService")
public class UserproductServiceImpl implements IUserproductService {
	@Autowired
	private IUserproductDao userproductDao;
	
	
	/**
	 * 查询订户下每张智能卡所购买产品的最大授权结束时间
	 * 
	 * @param Userproduct
	 * @return
	 */
	public HashMap<String,HashMap<String,String>> findAllProductLastEndTimeListByUserid(Userproduct form) {
		//获取订户信息
		HashMap<String,HashMap<String,String>> objectHp= new HashMap<String,HashMap<String,String>>();
		List<Userproduct> userproductlist = userproductDao.findProductLastEndTimeListByUserid(form);
		
		if(userproductlist != null){
			for (Userproduct userproduct : userproductlist) {
				if(objectHp.containsKey(userproduct.getCardid())){
					HashMap<String,String> productHp = objectHp.get(userproduct.getCardid());
					productHp.put(userproduct.getProductid(), userproduct.getEndtime());
					objectHp.put(userproduct.getCardid(), productHp);
				}else{
					HashMap<String,String> productHp = new HashMap<String,String>();
					productHp.put(userproduct.getProductid(), userproduct.getEndtime());
					objectHp.put(userproduct.getCardid(), productHp);
				}
			}
		}
		return objectHp;
	}
	
	/**
	 * 封装每张智能卡所购买产品的最大授权结束时间
	 * 
	 * @param Userproduct
	 * @return
	 */
	public HashMap<String,HashMap<String,String>> findAllProductLastEndTimeListByUserid(List<Userproduct> userproductlist) {
		//获取订户信息
		HashMap<String,HashMap<String,String>> objectHp= new HashMap<String,HashMap<String,String>>();
		if(userproductlist != null){
			for (Userproduct userproduct : userproductlist) {
				if(objectHp.containsKey(userproduct.getCardid())){
					HashMap<String,String> productHp = objectHp.get(userproduct.getCardid());
					productHp.put(userproduct.getProductid(), userproduct.getEndtime());
					objectHp.put(userproduct.getCardid(), productHp);
				}else{
					HashMap<String,String> productHp = new HashMap<String,String>();
					productHp.put(userproduct.getProductid(), userproduct.getEndtime());
					objectHp.put(userproduct.getCardid(), productHp);
				}
			}
		}
		return objectHp;
	}
	
	
	/**
	 * 查询订户的已授权卡和未授权卡
	 * 
	 * @param usercardlist 订户所有的智能卡
	 * @param maxEndtiemProductMap 订户每张智能卡购买的产品的最大授权结束时间Hashmap
	 * @param key 0-未授权；1-已授权 
	 * @return
	 */
	public HashMap<String,List<Usercard>> findAuthorizecardFromAllcard(List<Usercard> usercardlist,HashMap<String,HashMap<String,String>> maxEndtiemProductMap) {
		
		HashMap<String,List<Usercard>> objectMap = new HashMap<String,List<Usercard>>();
		
		//未授权list
		List<Usercard> usercardlist_0 =  new ArrayList<Usercard>();
		//已授权list
		List<Usercard> usercardlist_1 =  new ArrayList<Usercard>();
		if(usercardlist != null){
			for (Usercard usercard : usercardlist) {
				if(maxEndtiemProductMap != null){
					HashMap<String,String> endtimeHp = maxEndtiemProductMap.get(usercard.getCardid());
					if(endtimeHp != null){
						//获取基本包的到期时期
						String endtime = endtimeHp.get("1");
						if(Tools.compare_date(endtime,Tools.getCurrentTimeByFormat("yyyy-MM-dd"),"yyyy-MM-dd") < 0){
							usercardlist_0.add(usercard);
						}else{
							usercardlist_1.add(usercard);
						}
					}else{
						usercardlist_0.add(usercard);
					}
				}
			}
		}
		
		objectMap.put("0", usercardlist_0);
		objectMap.put("1", usercardlist_1);
		
		return objectMap;
		
	}
	
	/**
	 * 查询该智能卡下该产品的最大授权结束日期
	 * 
	 * @param maxEndtiemProductMap 订户每张智能卡购买的产品的最大授权结束时间Hashmap
	 * @param cardid 
	 * @param productid 
	 * @return endtime-"yyyy-MM-dd"
	 */
	public String findProductLastEndTimeByCardidAndProductid(HashMap<String,HashMap<String,String>> maxEndtiemProductMap,String cardid,String productid) {
		String endtime = "";
		if(maxEndtiemProductMap != null){
			HashMap<String,String> endtimeHp = maxEndtiemProductMap.get(cardid);
			if(endtimeHp != null){
				//获取基本包的到期时期
				endtime = endtimeHp.get("productid");
			}
		}
		return endtime;
	}
	
}
