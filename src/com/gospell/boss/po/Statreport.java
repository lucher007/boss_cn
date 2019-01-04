package com.gospell.boss.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.gospell.boss.common.Tools;

/**
 * 用户实体类
 */
public class Statreport extends BaseField implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String operatortype;  //操作员类型
	private String exporttype;    //导出excel类型
	
	//所有报表查询条件
	private String netid;        //网络ID
	private String storeid;      //区域ID
	private String operatorid;    //营业员ID
	private String querydatetype; //统计时间类型
	private String queryyear;      //按年
	private String querymonth;     //按月
	private String queryday;       //按天
	private String querydate;      //查询时间
	private String businesstype;   //业务类型
	private String[] businesstypes;  //多选业务类型
	private String querystate;  
	private int userlevel;
	private String queryusertype;

	private String querystarttime;
	private String queryendtime;
	private String querycardflag;
	private String querymothercardflag;
	private String source;   //办理方式（0-营业厅；1-移动终端）
	private String printdate; //打印时间
	
	private Map<String, String> excelMap;
	
	//订户收费记录查询条件
	private Integer queryuserid;
	private String queryusercode;
	private String queryusername;
	private String querynetid;
	
	private String queryareacode;
	private String queryareacodevalid;
	
	//订户终端状态查询条件
	private String terminalstate;
	private String queryversiontype;
	
	//营业员报表
	private String businesstypekey;  
	private Integer count;
	private Integer usercount;
	private BigDecimal totalprice;  //某一类型的总金额
	private Integer addcount;       //合计数量
	private BigDecimal addprice;    //合计总金
	
	//报表详细
	private Integer userid;          //
	private String usercode;        //
	private String username;        // 
	private String address;         //地址
	private String telephone;          //移动电话
	private String documentno;      //证件号码
	private String cardid;          //
	private BigDecimal price;       //单价
	private BigDecimal totalmoney;  //总费用
	private String addtime;         //
	private String operatorname;    //
	private String areacode;        //
	private String content;
	private String businesstypename; //
	private List<Statreport> statreportList;
	private String mothercardflag;
	
	//订户终端状态
	private String stbno;
	private String state; //机顶盒状态
	
	//订户产品到期统计
	private String terminalid;
	private String terminaltype;
	private String productid;
	private String productname;
	private String starttime;
	private String endtime;
	
	//订户产品收视统计
	private String queryproductid;
	private String queryproductname;
	
	//发票打印报表
	private String invoicecode;
	
	public String getBusinesstypekey() {
		return businesstypekey;
	}
	public void setBusinesstypekey(String businesstypekey) {
		this.businesstypekey = businesstypekey;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public BigDecimal getTotalprice() {
		return totalprice;
	}
	public void setTotalprice(BigDecimal totalprice) {
		this.totalprice = totalprice;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getNetid() {
		return netid;
	}
	public void setNetid(String netid) {
		this.netid = netid;
	}
	public String getStoreid() {
		return storeid;
	}
	public void setStoreid(String storeid) {
		this.storeid = storeid;
	}
	public String getOperatorid() {
		return operatorid;
	}
	public void setOperatorid(String operatorid) {
		this.operatorid = operatorid;
	}
	public String getQuerydatetype() {
		return querydatetype;
	}
	public void setQuerydatetype(String querydatetype) {
		this.querydatetype = querydatetype;
	}
	public String getQueryyear() {
		return queryyear;
	}
	public void setQueryyear(String queryyear) {
		this.queryyear = queryyear;
	}
	public String getQuerymonth() {
		return querymonth;
	}
	public void setQuerymonth(String querymonth) {
		this.querymonth = querymonth;
	}
	public String getQueryday() {
		return queryday;
	}
	public void setQueryday(String queryday) {
		this.queryday = queryday;
	}
	public String getQuerydate() {
		return querydate;
	}
	public void setQuerydate(String querydate) {
		this.querydate = querydate;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCardid() {
		return cardid;
	}
	public void setCardid(String cardid) {
		this.cardid = cardid;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getOperatorname() {
		return operatorname;
	}
	public void setOperatorname(String operatorname) {
		this.operatorname = operatorname;
	}
	public String getAreacode() {
		return areacode;
	}
	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}
	public String getBusinesstype() {
		return businesstype;
	}
	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}
	public String getQuerystarttime() {
		return querystarttime;
	}
	public void setQuerystarttime(String querystarttime) {
		this.querystarttime = querystarttime;
	}
	public String getQueryendtime() {
		return queryendtime;
	}
	public void setQueryendtime(String queryendtime) {
		this.queryendtime = queryendtime;
	}
	public Integer getAddcount() {
		return addcount;
	}
	public void setAddcount(Integer addcount) {
		this.addcount = addcount;
	}
	public BigDecimal getAddprice() {
		return addprice;
	}
	public void setAddprice(BigDecimal addprice) {
		this.addprice = addprice;
	}
	public Map<String, String> getExcelMap() {
		return excelMap;
	}
	public void setExcelMap(Map<String, String> excelMap) {
		this.excelMap = excelMap;
	}
	public List<Statreport> getStatreportList() {
		return statreportList;
	}
	public void setStatreportList(List<Statreport> statreportList) {
		this.statreportList = statreportList;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getOperatortype() {
		return operatortype;
	}
	public void setOperatortype(String operatortype) {
		this.operatortype = operatortype;
	}
	public String[] getBusinesstypes() {
		return businesstypes;
	}
	public void setBusinesstypes(String[] businesstypes) {
		this.businesstypes = businesstypes;
	}
	public String getUsercode() {
		return StringUtils.leftPad(String.valueOf(userid), 10, "0");
	}
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	public String getBusinesstypename() {
		return businesstypename;
	}
	public void setBusinesstypename(String businesstypename) {
		this.businesstypename = businesstypename;
	}
	public String getQueryusercode() {
		return queryusercode;
	}
	public void setQueryusercode(String queryusercode) {
		this.queryusercode = queryusercode;
	}
	public String getQueryusername() {
		return queryusername;
	}
	public void setQueryusername(String queryusername) {
		this.queryusername = queryusername;
	}
	public String getQuerynetid() {
		return querynetid;
	}
	public void setQuerynetid(String querynetid) {
		this.querynetid = querynetid;
	}
	public String getQueryareacode() {
		return queryareacode;
	}
	public void setQueryareacode(String queryareacode) {
		this.queryareacode = queryareacode;
	}
	public String getTerminalstate() {
		return terminalstate;
	}
	public void setTerminalstate(String terminalstate) {
		this.terminalstate = terminalstate;
	}
	public String getStbno() {
		return stbno;
	}
	public void setStbno(String stbno) {
		this.stbno = stbno;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getQuerystate() {
		return querystate;
	}
	public void setQuerystate(String querystate) {
		this.querystate = querystate;
	}
	public String getTerminalid() {
		return terminalid;
	}
	public void setTerminalid(String terminalid) {
		this.terminalid = terminalid;
	}
	public String getTerminaltype() {
		return terminaltype;
	}
	public void setTerminaltype(String terminaltype) {
		this.terminaltype = terminaltype;
	}
	public String getProductid() {
		return productid;
	}
	public void setProductid(String productid) {
		this.productid = productid;
	}
	public String getProductname() {
		return productname;
	}
	public void setProductname(String productname) {
		this.productname = productname;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getQueryproductid() {
		return queryproductid;
	}
	public void setQueryproductid(String queryproductid) {
		this.queryproductid = queryproductid;
	}
	public BigDecimal getTotalmoney() {
		return totalmoney;
	}
	public void setTotalmoney(BigDecimal totalmoney) {
		this.totalmoney = totalmoney;
	}
	public String getQueryareacodevalid() {
		return Tools.getAreacodeValid(queryareacode);
	}
	public void setQueryareacodevalid(String queryareacodevalid) {
		this.queryareacodevalid = queryareacodevalid;
	}
	public String getQueryversiontype() {
		return queryversiontype;
	}
	public void setQueryversiontype(String queryversiontype) {
		this.queryversiontype = queryversiontype;
	}
	public Integer getQueryuserid() {
		return queryuserid;
	}
	public void setQueryuserid(Integer queryuserid) {
		this.queryuserid = queryuserid;
	}
	public String getQueryproductname() {
		return queryproductname;
	}
	public void setQueryproductname(String queryprodectname) {
		this.queryproductname = queryprodectname;
	}
	public String getInvoicecode() {
		return invoicecode;
	}
	public void setInvoicecode(String invoicecode) {
		this.invoicecode = invoicecode;
	}
	public String getExporttype() {
		return exporttype;
	}
	public void setExporttype(String exporttype) {
		this.exporttype = exporttype;
	}
	public int getUserlevel() {
		return userlevel;
	}
	public void setUserlevel(int userlevel) {
		this.userlevel = userlevel;
	}
	public String getQueryusertype() {
		return queryusertype;
	}
	public void setQueryusertype(String queryusertype) {
		this.queryusertype = queryusertype;
	}
	public String getQuerycardflag() {
		return querycardflag;
	}
	public void setQuerycardflag(String querycardflag) {
		this.querycardflag = querycardflag;
	}
	public String getQuerymothercardflag() {
		return querymothercardflag;
	}
	public void setQuerymothercardflag(String querymothercardflag) {
		this.querymothercardflag = querymothercardflag;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDocumentno() {
		return documentno;
	}
	public void setDocumentno(String documentno) {
		this.documentno = documentno;
	}
	public String getPrintdate() {
		return printdate;
	}
	public void setPrintdate(String printdate) {
		this.printdate = printdate;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public Integer getUsercount() {
		return usercount;
	}
	public void setUsercount(Integer usercount) {
		this.usercount = usercount;
	}
	public String getMothercardflag() {
		return mothercardflag;
	}
	public void setMothercardflag(String mothercardflag) {
		this.mothercardflag = mothercardflag;
	}
	

}
