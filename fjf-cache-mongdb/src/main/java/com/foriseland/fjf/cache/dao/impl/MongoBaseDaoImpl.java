package com.foriseland.fjf.cache.dao.impl;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.foriseland.fjf.cache.dao.IMongoBaseDao;
import com.foriseland.fjf.cache.util.EmptyUtils;
import com.foriseland.fjf.cache.util.PageModel;

/**
 * 基本操作接口MongoDB数据库实现类
 * <p>
 * ClassName: BaseDaoImpl
 * </p>
 * <p>
 * Description:本实现类适用于MongoDB数据库，以下代码仅供参考，本人水平有限，可能会存在些许问题（如有更好方案可告知我，一定虚心学习），
 * 再次提醒，仅供参考！！
 * </p>
 * 
 * 
 * @author liyiguang
 * @date 2018年2月28日
 */
public abstract class MongoBaseDaoImpl<T> implements IMongoBaseDao<T> {

	protected abstract Class<T> getEntityClass();

	@Autowired
	protected MongoTemplate mgt;

	@Override
	public void save(T entity) {
		mgt.save(entity);
	}
	
	@Override
	public void insertAll(Collection<? extends Object> objectsToSave) {
		mgt.insertAll(objectsToSave);
	}
	
	
	@Override
	public void updateByAnySelective(Map<String,Object> mp,T entity) {
		// 反向解析对象
		Map<String, Object> map = null;
		try {
			map = parseEntity(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 生成参数
		Update update = new Update();
		if (EmptyUtils.isNotEmpty(map)) {
			for (String key : map.keySet()) {
				if(null!=map.get(key)) {
					update.set(key, map.get(key));
				}
			}
		}
		//创建query
		Query query=createQuery(mp,null);
		mgt.updateFirst(query, update, getEntityClass());
	}
	
	@Override
	public void updateByAny(Map<String,Object> mp,T entity) {
		// 反向解析对象
		Map<String, Object> map = null;
		try {
			map = parseEntity(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 生成参数
		Update update = new Update();
		if (EmptyUtils.isNotEmpty(map)) {
			for (String key : map.keySet()) {
				update.set(key, map.get(key));
			}
		}
		//创建query
		Query query=createQuery(mp,null);
		mgt.updateFirst(query, update, getEntityClass());
	}
	
	
	@Override
	public void updateByAnySelective(String[] params, Object[] values,T entity) {
		// 反向解析对象
		Map<String, Object> map = null;
		try {
			map = parseEntity(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 生成参数
		Update update = new Update();
		if (EmptyUtils.isNotEmpty(map)) {
			for (String key : map.keySet()) {
				if(null!=map.get(key)) {
					update.set(key, map.get(key));
				}
			}
		}
		//创建query
		Query query=createQuery(params,values,null);
		mgt.updateFirst(query, update, getEntityClass());
	}

	@Override
	public void updateByAny(String[] params, Object[] values,T entity) {
		// 反向解析对象
		Map<String, Object> map = null;
		try {
			map = parseEntity(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 生成参数
		Update update = new Update();
		if (EmptyUtils.isNotEmpty(map)) {
			for (String key : map.keySet()) {
				update.set(key, map.get(key));
			}
		}
		//创建query
		Query query=createQuery(params,values,null);
		mgt.updateFirst(query, update, getEntityClass());
	}
	
	@Override
	public void update(T entity) {
		// 反向解析对象
		Map<String, Object> map = null;
		try {
			map = parseEntity(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ID字段
		String idName = null;
		Object idValue = null;

		// 生成参数
		Update update = new Update();
		if (EmptyUtils.isNotEmpty(map)) {
			for (String key : map.keySet()) {
				if (key.indexOf("{") != -1) {
					// 设置ID
					idName = key.substring(key.indexOf("{") + 1, key.indexOf("}"));
					idValue = map.get(key);
				} else {
					update.set(key, map.get(key));
				}
			}
		}
		mgt.updateFirst(new Query().addCriteria(where(idName).is(idValue)), update, getEntityClass());
	}
	
	@Override
	public void delete(Serializable... ids) {
		if (EmptyUtils.isNotEmpty(ids)) {
			for (Serializable id : ids) {
				mgt.remove(mgt.findById(id, getEntityClass()));
			}
		}

	}
	
	@Override
	public void deleteByAny(String propName, Object propValue) {
		List<T> findByProp = findByProp(propName,propValue);
		if(EmptyUtils.isNotEmpty(findByProp)) {
			for (T t : findByProp) {
				mgt.remove(t);
			}
		}
	}

	@Override
	public T find(Serializable id) {
		return mgt.findById(id, getEntityClass());
	}

	@Override
	public List<T> findAll() {
		return mgt.findAll(getEntityClass());
	}

	@Override
	public List<T> findAll(String order) {
		List<Order> orderList = parseOrder(order);
		if (EmptyUtils.isEmpty(orderList)) {
			return findAll();
		}
		return mgt.find(new Query().with(new Sort(orderList)), getEntityClass());
	}

	@Override
	public List<T> findByProp(String propName, Object propValue) {
		return findByProp(propName, propValue, null);
	}

	@Override
	public List<T> findByProp(String propName, Object propValue, String order) {
		Query query = new Query();
		// 参数
		query.addCriteria(where(propName).is(propValue));
		// 排序
		List<Order> orderList = parseOrder(order);
		if (EmptyUtils.isNotEmpty(orderList)) {
			query.with(new Sort(orderList));
		}
		return mgt.find(query, getEntityClass());
	}

	@Override
	public List<T> findByProps(String[] propName, Object[] propValue) {
		return findByProps(propName, propValue, null);
	}
	
	@Override
	public List<T> findByProps(Map<String,Object> map) {
		return findByProps(map, null);
	}

	@Override
	public List<T> findByProps(String[] propName, Object[] propValue, String order) {
		Query query = createQuery(propName, propValue, order);
		return mgt.find(query, getEntityClass());
	}
	
	@Override
	public List<T> findByProps(Map<String,Object> map, String order) {
		Query query = createQuery(map, order);
		return mgt.find(query, getEntityClass());
	}

	@Override
	public T uniqueByProp(String propName, Object propValue) {
		return mgt.findOne(new Query(where(propName).is(propValue)), getEntityClass());
	}

	@Override
	public T uniqueByProps(String[] propName, Object[] propValue) {
		Query query = createQuery(propName, propValue,null);
		return mgt.findOne(query, getEntityClass());
	}
	
	@Override
	public T uniqueByProps(Map<String,Object> map) {
		Query query = createQuery(map, null);
		return mgt.findOne(query, getEntityClass());
	}

	@Override
	public PageModel<T> pageAll(int pageNo, int pageSize) {
		return pageAll(pageNo, pageSize, null);
	}

	@Override
	public PageModel<T> pageAll(int pageNo, int pageSize, String order) {
		return pageByProp(pageNo, pageSize, null, null, order);
	}

	@Override
	public PageModel<T> pageByProp(int pageNo, int pageSize, String param, Object value) {
		return pageByProp(pageNo, pageSize, param, value, null);
	}

	@Override
	public PageModel<T> pageByProp(int pageNo, int pageSize, String param, Object value, String order) {
		String[] params = null;
		Object[] values = null;
		if (EmptyUtils.isNotEmpty(param)) {
			params = new String[] { param };
			values = new Object[] { value };
		}
		return pageByProps(pageNo, pageSize, params, values, order);
	}

	@Override
	public PageModel<T> pageByProps(int pageNo, int pageSize, String[] params, Object[] values) {
		return pageByProps(pageNo, pageSize, params, values, null);
	}
	
	@Override
	public PageModel<T> pageByProps(int pageNo, int pageSize, Map<String,Object> map) {
		return pageByProps(pageNo, pageSize, map, null);
	}

	@Override
	public PageModel<T> pageByProps(int pageNo, int pageSize, String[] params, Object[] values, String order) {
		// 创建分页模型对象
		PageModel<T> page = new PageModel<>(pageNo, pageSize);

		// 查询总记录数
		int count = countByCondition(params, values);
		page.setTotalCount(count);

		// 查询数据列表
		Query query = createQuery(params, values, order);

		// 设置分页信息
		query.skip(page.getFirstResult());
		query.limit(page.getPageSize());
		// 封装结果数据
		page.setList(mgt.find(query, getEntityClass()));

		return page;
	}
	
	@Override
	public PageModel<T> pageByProps(int pageNo, int pageSize,Map<String,Object> map, String order) {
		// 创建分页模型对象
		PageModel<T> page = new PageModel<>(pageNo, pageSize);
		
		// 查询总记录数
		int count = countByCondition(map);
		page.setTotalCount(count);
		
		// 查询数据列表
		Query query = createQuery(map, order);
		
		// 设置分页信息
		query.skip(page.getFirstResult());
		query.limit(page.getPageSize());
		// 封装结果数据
		page.setList(mgt.find(query, getEntityClass()));
		
		return page;
	}

	@Override
	public int countByCondition(String[] params, Object[] values) {
		Query query = createQuery(params, values, null);
		Long count = mgt.count(query, getEntityClass());
		return count.intValue();
	}
	
	@Override
	public int countByCondition(Map<String,Object> map) {
		Query query = createQuery(map, null);
		Long count = mgt.count(query, getEntityClass());
		return count.intValue();
	}

	/**
	 * 创建带有where条件（只支持等值）和排序的Query对象
	 * 
	 * @param params
	 *            参数数组
	 * @param values
	 *            参数值数组
	 * @param order
	 *            排序
	 * @return Query对象
	 */
	protected Query createQuery(String[] params, Object[] values, String order) {
		Query query = new Query();

		// where 条件
		if (EmptyUtils.isNotEmpty(params) && EmptyUtils.isNotEmpty(values)) {
			for (int i = 0; i < params.length; i++) {
				query.addCriteria(where(params[i]).is(values[i]));
			}
		}

		// 排序
		List<Order> orderList = parseOrder(order);
		if (EmptyUtils.isNotEmpty(orderList)) {
			query.with(new Sort(orderList));
		}

		return query;
	}
	/**
	 * 创建带有where条件（只支持等值）和排序的Query对象
	 * 
	 *  @param map
	 *           key:参数名
	 *           value:参数值
	 * @param order
	 *            排序
	 * @return Query对象
	 */
	protected Query createQuery(Map<String,Object> map, String order) {
		Query query = new Query();
		
		// where 条件
		if(EmptyUtils.isNotEmpty(map)) {
			for (String key:map.keySet()) {
				query.addCriteria(where(key).is(map.get(key)));
			}
		}
		
		// 排序
		List<Order> orderList = parseOrder(order);
		if (EmptyUtils.isNotEmpty(orderList)) {
			query.with(new Sort(orderList));
		}
		
		return query;
	}

	/**
	 * 解析Order字符串为所需参数
	 * 
	 * @param order
	 *            排序参数，如[id]、[id asc]、[id asc,name desc]
	 * @return Order对象集合
	 */
	protected List<Order> parseOrder(String order) {
		List<Order> list = null;
		if (EmptyUtils.isNotEmpty(order)) {
			list = new ArrayList<Order>();
			// 共有几组排序字段
			String[] fields = order.split(",");
			Order o = null;
			String[] item = null;
			for (int i = 0; i < fields.length; i++) {
				if (EmptyUtils.isEmpty(fields[i])) {
					continue;
				}
				item = fields[i].split(" ");
				if (item.length == 1) {
					o = new Order(Direction.ASC, item[0]);
				} else if (item.length == 2) {
					o = new Order("desc".equalsIgnoreCase(item[1]) ? Direction.DESC : Direction.ASC, item[0]);
				} else {
					throw new RuntimeException("排序字段参数解析出错");
				}
				list.add(o);
			}
		}
		return list;
	}

	/**
	 * 将对象的字段及值反射解析为Map对象<br>
	 * 这里使用Java反射机制手动解析，并且可以识别注解为主键的字段，以达到根据id进行更新实体的目的<br>
	 * key：字段名称，value：字段对应的值
	 * 
	 * @param t
	 *            要修改的对象
	 * @return Map对象，注意：id字段的key封装为“{id字段名称}”，以供后续识别
	 * @throws Exception
	 */
	protected Map<String, Object> parseEntity(T t) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		/*
		 * 解析ID
		 */
		String idName = "";
		Field[] declaredFields = getEntityClass().getDeclaredFields();
		for (Field field : declaredFields) {
			if (field.isAnnotationPresent(Id.class)) {
				field.setAccessible(true);
				map.put("{" + field.getName() + "}", field.get(t));
				idName = field.getName();
				break;
			}
		}
		/*
		 * 解析其他属性
		 */
		Method[] methods = getEntityClass().getDeclaredMethods();
		if (EmptyUtils.isNotEmpty(methods)) {
			for (Method method : methods) {
				if (method.getName().startsWith("get") && method.getModifiers() == Modifier.PUBLIC) {
					String fieldName = parse2FieldName(method.getName());
					if (!fieldName.equals(idName)) {
						map.put(fieldName, method.invoke(t));
					}
				}
			}
		}

		return map;
	}

	/**
	 * 将get方法名转换为对应的字段名称
	 * 
	 * @param methodName
	 *            如：getName
	 * @return 如：name
	 */
	private String parse2FieldName(String methodName) {
		String name = methodName.replace("get", "");
		name = name.substring(0, 1).toLowerCase() + name.substring(1);
		return name;
	}

}