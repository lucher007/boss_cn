package com.gospell.boss.dao;

import java.math.BigDecimal;
import java.util.List;
import com.gospell.boss.po.Statreport;
import com.gospell.boss.po.Userstb;

/**
 * 数据层接口
 */
public interface IStatreportDao {
	
	/**
	 * 操作员业务统计报表
	 * 
	 * @param Statreport
	 * @return
	 */
	public List<Statreport> operatorBusinessStat(Statreport form);
	
	/**
	 * 操作员业务统计报表明细
	 * 
	 * @param Statreport
	 * @return
	 */
	public List<Statreport> operatorBusinessStatdetail(Statreport form);
	
	/**
	 * 查询操作员姓名
	 * 
	 * @param Statreport
	 * @return
	 */
	public String findOperatorname(Statreport form);
	
	/**
	 * 查询订户姓名
	 * 
	 * @param Statreport
	 * @return
	 */
	public String findUsername(Statreport form);
	
	/**
	 * 明细计数
	 * 
	 * @param Statreport
	 * @return
	 */
	public Integer operatorBusinessStatCount(Statreport form);
	
	/**
	 * excel导出报表明细查询
	 * 
	 * @param Statreport
	 * @return
	 */
	public List<Statreport> exportBusinessdetail(Statreport form);
	
	/**
	 * 查询操作员网络
	 * 
	 * @param Statreport
	 * @return
	 */
	public String findOperatorNetid(Statreport form);
	
	/**
	 * 订户收费记录查询
	 * 
	 * @param Statreport
	 * @return
	 */
	public List<Statreport> userChargeRecord(Statreport form);
	
	/**
	 * 订户收费记录数目
	 * 
	 * @param Statreport
	 * @return
	 */
	public Integer userChargeRecordCount(Statreport form);
	
	/**
	 * 订户终端状态查询数目
	 * 
	 * @param Statreport
	 * @return
	 */
	public Integer userterminalstatestatCount(Statreport form);
	
	/**
	 * 订户终端状态查询
	 * 
	 * @param Statreport
	 * @return
	 */
	public List<Statreport> userterminalstatestat(Statreport form);
	
	/**
	 * 订户终端状态查询导出EXCEL
	 * 
	 * @param Statreport
	 * @return
	 */
	public List<Statreport> exportUserTerminalstateStat(Statreport form);
	
	/**
	 * 订户产品到期统计数目
	 * 
	 * @param Statreport
	 * @return
	 */
	public Integer userproductExpiredStatCount(Statreport form);
	
	/**
	 * 订户产品到期统计
	 * 
	 * @param Statreport
	 * @return
	 */
	public List<Statreport> userproductExpiredStat(Statreport form);
	
	/**
	 * 订户产品到期统计导出EXCEL
	 * 
	 * @param Statreport
	 * @return
	 */
	public List<Statreport> exportUserProductExpiredStat(Statreport form);
	
	/**
	 * 订户产品收视统计
	 * 
	 * @param Statreport
	 * @return
	 */
	public List<Statreport> userProductAudienceStat(Statreport form);
	
	/**
	 * 订户产品收视明细统计
	 * 
	 * @param Statreport
	 * @return
	 */
	public List<Statreport> userProductAudienceDetailStat(Statreport form);
	
	/**
	 * 订户产品收视明细统计数目
	 * 
	 * @param Statreport
	 * @return
	 */
	public Integer userProductAudienceDetailStatCount(Statreport form);
	
	/**
	 * 订户产品收视明细统计导出EXCEL
	 * 
	 * @param Statreport
	 * @return
	 */
	public List<Statreport> exportUserProductAudienceDetailStat(Statreport form);
	
	/**
	 * 明细计数
	 * 
	 * @param Statreport
	 * @return
	 */
	public BigDecimal operatorBusinessPayment(Statreport form);
	
	/**
	 * 订户终端状态查询_普安
	 * 
	 * @param Statreport
	 * @return
	 */
	public List<Statreport> userterminalstatestat_pn(Statreport form);
	
	/**
	 * 订户终端状态查询数目_普安
	 * 
	 * @param Statreport
	 * @return
	 */
	public Integer userterminalstatestatCount_pn(Statreport form);
	
	/**
	 * 订户终端状态查询导出EXCEL
	 * 
	 * @param Statreport
	 * @return
	 */
	public List<Statreport> exportUserTerminalstateStat_pn(Statreport form);
	
	/**
	 * 订户产品欠费统计
	 * 
	 * @param Statreport
	 * @return
	 */
	public List<Statreport> userProductArrearsStat(Statreport form);
	
	/**
	 * 订户产品欠费统计数目
	 * 
	 * @param Statreport
	 * @return
	 */
	public Integer userProductArrearsStatCount(Statreport form);
	
	/**
	 * 订户产品欠费统计导出EXCEL
	 * 
	 * @param Statreport
	 * @return
	 */
	public List<Statreport> exportUserProductArrearsStat(Statreport form);
	
	/**
	 * 订户收费记录导出EXCEL
	 * 
	 * @param Statreport
	 * @return
	 */
	public List<Statreport> exportUserChargeRecord(Statreport form);
	
	/**
	 * 产品购买收费统计
	 * 
	 * @param Statreport
	 * @return
	 */
	public List<Statreport> userProductChargeStat(Statreport form);
	
	/**
	 * 发票打印报表
	 * 
	 * @param Statreport
	 * @return
	 */
	public List<Statreport> invoiceprintStat(Statreport form);
	
	/**
	 * 发票打印报表数目
	 * 
	 * @param Statreport
	 * @return
	 */
	public Integer invoiceprintStatCount(Statreport form);
	
	/**
	 * MPS发票打印报表
	 * 
	 * @param Statreport
	 * @return
	 */
	public List<Statreport> invoiceprintStat_MPS(Statreport form);
	
	/**
	 * MPS发票打印报表数目
	 * 
	 * @param Statreport
	 * @return
	 */
	public Integer invoiceprintStatCount_MPS(Statreport form);
	
	/**
	 * 发票打印报表导出EXCEL
	 * 
	 * @param Statreport
	 * @return
	 */
	public List<Statreport> exportinvoiceprintStat(Statreport form);
	
	/**
	 * MPS发票打印报表导出EXCEL
	 * 
	 * @param Statreport
	 * @return
	 */
	public List<Statreport> exportinvoiceprintStat_MPS(Statreport form);
	
	/**
	 * 前端用户明细_智能卡统计
	 * 
	 * @param Statreport
	 * @return
	 */
	public Integer userdetailStat_card(Statreport form);
	
	/**
	 * 前端用户明细_用户统计
	 * 
	 * @param Statreport
	 * @return
	 */
	public Integer userdetailStat_user(Statreport form);
	
	/**
	 * 前端用户明细_副机统计
	 * 
	 * @param Statreport
	 * @return
	 */
	public Integer userdetailStat_userstb(Statreport form);
	
	/**
	 * 前端用户明细_欠费用户统计
	 * 
	 * @param Statreport
	 * @return
	 */
	public Integer userdetailStat_userpurchased(Statreport form);
	
	/**
	 * 前端用户明细_用户级别统计
	 * 
	 * @param Statreport
	 * @return
	 */
	public Integer userdetailStat_userlevel(Statreport form);
	
	/**
	 * 前端用户明细_高标清卡统计
	 * 
	 * @param Statreport
	 * @return
	 */
	public Integer userdetailStat_usercard(Statreport form);
	
	
}
