package com.gospell.boss.dao;

import java.util.List;
import com.gospell.boss.po.Operatorinvoice;

/**
 * 数据层接口
 */
public interface IOperatorinvoiceDao {
	
	/**
	 * 操作员发票号添加
	 * 
	 * @param ScaleConf
	 * @return
	 */
	public Integer save(Operatorinvoice form);
	
	/**
	 * 操作员发票号更新
	 * 
	 * @param Operatorinvoice
	 * @return
	 */
	public Integer update(Operatorinvoice form);
	
	/**
	 * 操作员发票号删除
	 * 
	 * @param id
	 * @return
	 */
	public Integer delete(Integer id);
	
	/**
	 * 操作员发票号分页查询
	 * 
	 * @param Operatorinvoice
	 * @return
	 */
	public List<Operatorinvoice> findByList(Operatorinvoice form);
	
	/**
	 * 操作员发票号全部查询
	 * 
	 * @param Operatorinvoice
	 * @return
	 */
	public List<Operatorinvoice> queryByList(Operatorinvoice form);
	
	/**
	 * 操作员发票号分页总数
	 * 
	 * @param Operatorinvoice
	 * @return
	 */
	public Integer findByCount(Operatorinvoice form);
	
	/**
	 * 操作员发票号查询根据ID
	 * 
	 * @param Operatorinvoice
	 * @return
	 */
	public Operatorinvoice findById(Integer id);
	
	/**
	 * 操作员发票号查询根据操作员ID
	 * 
	 * @param Operatorinvoice
	 * @return
	 */
	public Operatorinvoice findByOperatorid(Integer operatorid);
	
	/**
	 * 操作员发票号查询根据OperatorID
	 * 
	 * @param Operatorinvoice
	 * @return
	 */
	public Operatorinvoice findByOperatorId(Integer id);
	
	
}
