package com.foriseland.fjf.util;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;

/**
 * 分页数据处理
 */
public class PageBean<T> implements Serializable {
	private static final long serialVersionUID = 3945092807822911567L;

	private int pageNum;
	private int pageSize;
	private int pages;
	private int size;
	private long total;
	private List<T> result;

	public PageBean() {
		super();
	}
	/**
	 * 设置分页初始数据。
	 * @param pageNum {@link Integer}：当前页；
	 * @param pageSize {@link Integer}：页条数；
	 */
	public PageBean(int pageNum, int pageSize) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}
	/**
	 * 包装Page对象，因为直接返回Page对象，在JSON处理以及其他情况下会被当成List来处理， 而出现一些问题。
	 * @param result {@link List}<{@link T}>：返回结果集；
	 */
	public PageBean(List<T> result) {
		this.set(result);
	}

	/**
	 * 当前页
	 */
	public int getPageNum() {
		return pageNum;
	}
	/**
	 * 当前页
	 */
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	/**
	 * 每页记录数
	 */
	public int getPageSize() {
		return pageSize;
	}
	/**
	 * 每页记录数
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * 总页数
	 */
	public int getPages() {
		return pages;
	}
	/**
	 * 总页数
	 */
	public void setPages(int pages) {
		this.pages = pages;
	}
	/**
	 * 当前页的数量 <= pageSize，该属性来自ArrayList的size属性
	 */
	public int getSize() {
		return size;
	}
	/**
	 * 当前页的数量 <= pageSize，该属性来自ArrayList的size属性
	 */
	public void setSize(int size) {
		this.size = size;
	}
	/**
	 * 总记录数
	 */
	public long getTotal() {
		return total;
	}
	/**
	 * 总记录数
	 */
	public void setTotal(long total) {
		this.total = total;
	}
	/**
	 * 结果集
	 */
	public List<T> getResult() {
		return result;
	}
	/**
	 * 结果集
	 */
	public void setResult(List<T> result) {
		this.result = result;
	}

	/**
	 * 设置分页结果实体data
	 * @param result {@link List}<{@link T}>：返回结果集；
	 * @return {@link PageBean}：包含各分页数据信息。
	 */
	public PageBean<T> set(List<T> result) {
		if (result instanceof Page) {
			Page<T> page = (Page<T>) result;
			this.pageNum = page.getPageNum();
			this.pageSize = page.getPageSize();
			this.total = page.getTotal();
			this.pages = page.getPages();
			this.size = page.size();
			this.result = page;
		}
		return this;
	}

	@Override
	public String toString() {
		String js = JSON.toJSONString(this);
		return js;
	}
}
