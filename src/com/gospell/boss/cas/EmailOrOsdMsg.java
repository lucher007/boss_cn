package com.gospell.boss.cas;

import java.io.Serializable;

import com.gospell.boss.po.BaseField;

@SuppressWarnings("serial")
public class EmailOrOsdMsg extends BaseField implements Serializable {

	private String Show_Time_Len; // OSD显示时间长度
	private String Show_Times;    // OSD显示次数
	private String Priority;      // OSD优先级
	private String Expired_Time;  // 过期时间
	private String Write_Paper_Time; // 写信时间
	private String Email_Title_Name; // Email标题的名称
	private String MsgData; // OSD 和EMAIL内容

	public String getShow_Time_Len() {
		return Show_Time_Len;
	}

	public void setShow_Time_Len(String show_Time_Len) {
		Show_Time_Len = show_Time_Len;
	}

	public String getShow_Times() {
		return Show_Times;
	}

	public void setShow_Times(String show_Times) {
		Show_Times = show_Times;
	}

	public String getPriority() {
		return Priority;
	}

	public void setPriority(String priority) {
		Priority = priority;
	}

	public String getExpired_Time() {
		return Expired_Time;
	}

	public void setExpired_Time(String expired_Time) {
		Expired_Time = expired_Time;
	}

	public String getMsgData() {
		return MsgData;
	}

	public void setMsgData(String msgData) {
		MsgData = msgData;
	}

	public String getWrite_Paper_Time() {
		return Write_Paper_Time;
	}

	public void setWrite_Paper_Time(String write_Paper_Time) {
		Write_Paper_Time = write_Paper_Time;
	}

	public String getEmail_Title_Name() {
		return Email_Title_Name;
	}

	public void setEmail_Title_Name(String email_Title_Name) {
		Email_Title_Name = email_Title_Name;
	}

}
