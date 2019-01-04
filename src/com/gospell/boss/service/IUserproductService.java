package com.gospell.boss.service;

import java.util.HashMap;
import java.util.List;

import com.gospell.boss.po.Usercard;
import com.gospell.boss.po.Userproduct;

/**
 * CA业务层接口
 */
public interface IUserproductService {
	
	/**
	 * 查询订户下每张智能卡所购买产品的最大授权结束时间
	 * 
	 * @param Userproduct
	 * @return
	 */
	public HashMap<String,HashMap<String,String>> findAllProductLastEndTimeListByUserid(Userproduct form);
	
	/**
	 * 封装每张智能卡所购买产品的最大授权结束时间
	 * 
	 * @param Userproduct
	 * @return
	 */
	public HashMap<String,HashMap<String,String>> findAllProductLastEndTimeListByUserid(List<Userproduct> userproductlist);
	
	/**
	 * 查询订户的已授权卡和未授权卡
	 * 
	 * @param usercardlist 订户所有的智能卡
	 * @param maxEndtiemProductMap 订户每张智能卡购买的产品的最大授权结束时间Hashmap
	 * @param Key 0-未授权；1-已授权 
	 * @return
	 */
	public HashMap<String,List<Usercard>> findAuthorizecardFromAllcard(List<Usercard> usercardlist,HashMap<String,HashMap<String,String>> maxEndtiemProductMap);
	
	/**
	 * 查询该智能卡下该产品的最大授权结束日期
	 * 
	 * @param maxEndtiemProductMap 订户每张智能卡购买的产品的最大授权结束时间Hashmap
	 * @param cardid 
	 * @param productid 
	 * @return endtime-"yyyy-MM-dd"
	 */
	public String findProductLastEndTimeByCardidAndProductid(HashMap<String,HashMap<String,String>> maxEndtiemProductMap,String cardid,String productid);
}
