package com.foriseland.fjf.base;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: IBaseMapper 
 * @Description: DAO基础服务工具类
 * @author: wangHaiyang  
 * @date 2017年12月01日 下午3:09:10 
 * @param <T>
 */
public interface IBaseMapper<T> {
	
	/**
	 * 
	 * 查询（根据主键ID查询）
	 * 
	 **/
	public T getById (Long id ); // @Param("id") 
	
	/**
	 * 传实体多条件查询
	 * @param t
	 * @return
	 */
	public List<T> selectBySelective(T pojo);

	/**
	 * 
	 * 删除（根据主键ID删除）
	 * 
	 **/
	public int deleteById (@Param("id") Long id );

	/**
	 * 
	 * 添加
	 * 
	 **/
	public int save(T record );

	/**
	 * 
	 * 添加 （匹配有值的字段）
	 * 
	 **/
	public int saveSelective(T record );

	/**
	 * 
	 * 修改 （匹配有值的字段）
	 * 
	 **/
	public int updateByIdSelective(T record );

	/**
	 * 
	 * 修改（根据主键ID修改）
	 * 
	 **/
	public int updateById (T record );
}
