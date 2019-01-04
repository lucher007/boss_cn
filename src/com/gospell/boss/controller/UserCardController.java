package com.gospell.boss.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gospell.boss.common.ExportExcel;
import com.gospell.boss.common.Tools;
import com.gospell.boss.dao.IUsercardDao;
import com.gospell.boss.dao.IUserproductDao;
import com.gospell.boss.dao.IUserstbDao;
import com.gospell.boss.po.BusinessReport;
import com.gospell.boss.po.Card;
import com.gospell.boss.po.Network;
import com.gospell.boss.po.Operator;
import com.gospell.boss.po.Product;
import com.gospell.boss.po.Server;
import com.gospell.boss.po.Stb;
import com.gospell.boss.po.User;
import com.gospell.boss.po.Usercard;
import com.gospell.boss.po.Userproduct;
import com.gospell.boss.po.Userstb;
import com.gospell.boss.service.ICardService;
import com.gospell.boss.service.IStbService;
import com.gospell.boss.service.ISystemparaService;

/**
 * 用户控制层
 */
@Controller
@Scope("prototype")
@RequestMapping("/usercard")
@Transactional
public class UserCardController extends BaseController{
	
	@Autowired 
	private ServletContext servletContext;
	@Autowired
	private IUsercardDao usercardDao; 
	
	@ResponseBody
	@RequestMapping(value = "/usercardJson")
	public Map<String, Object> usercardJson(Usercard form,int rows,int page) {
		form.setPager_offset(rows*(page-1));
		form.setPager_openset(rows);
		Integer total = usercardDao.findByCount(form);
		List<Usercard> usercardlist = usercardDao.findByList(form);
		Map<String, Object> json = new HashMap<String, Object>(); 
		List<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
		for (Usercard usercard : usercardlist) {
			HashMap<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("id", usercard.getId());
			dataMap.put("cardid", usercard.getCardid());
			dataMap.put("addtime", usercard.getAddtime());
			dataMap.put("state", usercard.getState());  
			dataMap.put("mothercardflag", usercard.getMothercardflag());
			dataMap.put("mothercardid", usercard.getMothercardid());
			dataMap.put("stbno", usercard.getStbno());
			dataMap.put("price", usercard.getPrice());
			dataMap.put("cardflag", usercard.getCardflag());
			dataList.add(dataMap);
		}
		json.put("total", total);
		json.put("rows", dataList);
		return json;
	}
	
	/**
	 * 集团用户导出智能卡
	 * @param form
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/exportUsercaradidExcel")
	public void exportUsercaradidExcel(Usercard form , HttpServletResponse response) throws Exception {
		Operator operator = (Operator)getSession().getAttribute("Operator");
		User user = operator.getUser();
		form.setUserid(user.getId());
		List<Usercard> usercardlist = usercardDao.queryByList(form);
		
		String reportName = "Usergroupcard_Operatorid" + operator.getOperatorname();
		//表头
		String[] columntitle = {"智能卡号"};
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		String[] type = reportName.split("_");
		XSSFSheet sheet = workbook.createSheet(type[1]);
		// EXCEL 主SHEET 名称
		XSSFRow row = sheet.createRow((short) 0);
		XSSFCell cell = null;
		int nColumn = columntitle.length;
		// 填写表头
		cell = row.createCell((0));
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("订户编号");
		cell = row.createCell((1));
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(user.getId());
		row = sheet.createRow((short) 1);
		cell = row.createCell((0));
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("订户名称");
		cell = row.createCell((1));
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(user.getUsername());
		row = sheet.createRow((short) 2);
		cell = row.createCell((0));
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(columntitle[0]);
		int rowIndex = 3;
		// 向EXCEL中逐行填表
		for (Usercard usercard : usercardlist) {
			row = sheet.createRow((short) rowIndex);
			for (int j = 0; j < nColumn; j++) {
				cell = row.createCell(j);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(usercard.getCardid());
			}
			rowIndex++;
		}
		
		String filename = reportName + ".xlsx";
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment; filename=" + filename);
		OutputStream ouputStream = response.getOutputStream();
		workbook.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}
	
	
}
