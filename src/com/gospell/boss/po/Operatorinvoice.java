package com.gospell.boss.po;

import java.io.Serializable;
import java.util.List;
/**
 * 用户实体类
 */
public class Operatorinvoice extends BaseField implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;                //数据库ID
	private Integer operatorid;        //操作员编号
	private String startinvoicecode;   //开始发票号
	private String endinvoicecode;     //结束发票号
	private String remark;             //备注
	
	/******************数据库辅助字段*************************/
	private Operatorinvoice operatorinvoice;
	private List<Operatorinvoice> operatorinvoicelist;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOperatorid() {
		return operatorid;
	}
	public void setOperatorid(Integer operatorid) {
		this.operatorid = operatorid;
	}
	public String getStartinvoicecode() {
		return startinvoicecode;
	}
	public void setStartinvoicecode(String startinvoicecode) {
		this.startinvoicecode = startinvoicecode;
	}
	public String getEndinvoicecode() {
		return endinvoicecode;
	}
	public void setEndinvoicecode(String endinvoicecode) {
		this.endinvoicecode = endinvoicecode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Operatorinvoice getOperatorinvoice() {
		return operatorinvoice;
	}
	public void setOperatorinvoice(Operatorinvoice operatorinvoice) {
		this.operatorinvoice = operatorinvoice;
	}
	public List<Operatorinvoice> getOperatorinvoicelist() {
		return operatorinvoicelist;
	}
	public void setOperatorinvoicelist(List<Operatorinvoice> operatorinvoicelist) {
		this.operatorinvoicelist = operatorinvoicelist;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
