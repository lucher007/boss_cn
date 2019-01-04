package com.gospell.boss.common;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

/**
 * 多语言国际化
 */
public class MyAcceptHeaderLocaleResolver extends AcceptHeaderLocaleResolver {
	
	public Locale resolveLocale(HttpServletRequest request) {
		HttpSession session=request.getSession();
        //多语言国际化配置
//		Locale locale=(Locale)session.getAttribute("locale");
//        if (locale==null){
//            session.setAttribute("locale",request.getLocale());
//            return request.getLocale();
//        }else{
//            return locale;
//        }
		
		//只支持中文
		Locale locale = new Locale("zh", "CN");
		session.setAttribute("locale",request.getLocale());
		return locale;
        
    }

    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        request.getSession().setAttribute("locale",locale);
    }
}
