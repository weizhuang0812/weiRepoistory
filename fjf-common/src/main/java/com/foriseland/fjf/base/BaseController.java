package com.foriseland.fjf.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;

import com.foriseland.fjf.lang.DateEditor;
/**
 * @copyright 复华创新平台
 * @description: 基础Controller提供一些基础公共的属性方法
 * @author wangHaiyang
 * @modified wangHaiyang
 * @date 2017-12-01 上午10:49:25
 * @version V1.0
 */
public class BaseController {

	private static final Map<String, String> GOBLE_CONFIG = new HashMap<String, String>();

	/**
	 * 获得Spring ModelAndView 视图
	 * 
	 * @return
	 */
	public ModelAndView modelAndView() {
		ModelAndView mav = new ModelAndView();
		mav.getModel().put("gobleConfig", getGobleConfig());
		return mav;
	}
	
	
	public Integer getPageNum(Integer pageNum){
		if(pageNum == null){
			return 1;
		}
		return pageNum;
	}
	
	public Integer getPageSize(Integer pageSize){
		if(pageSize == null){
			return 10;
		}
		return pageSize;
	}

	/**
	 * 向页面响应提示信息
	 * 
	 * @param param1
	 * @param param2
	 * @return
	 */
	private Map<String, String> getGobleConfig() {
		if (GOBLE_CONFIG.size() == 0) {
			InputStream in = BaseController.class.getResourceAsStream("/goble-config.properties");
			try {
				if(in == null){
					return null;
				}
				Properties prop = new Properties();
				prop.load(in);
				Set<Entry<Object, Object>> propSet = prop.entrySet();
				Iterator<Entry<Object, Object>> iterator = propSet.iterator();
				while (iterator.hasNext()) {
					Entry<Object, Object> en = iterator.next();
					GOBLE_CONFIG.put(String.valueOf(en.getKey()), String.valueOf(en.getValue()));
				}
			} catch (IOException e) {
				return GOBLE_CONFIG;
			}
		}
		return GOBLE_CONFIG;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new DateEditor());
	}
}
