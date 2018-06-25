package com.foriseland.fjf.sequence.handle;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.foriseland.fjf.exception.BaseExcepton;
import com.foriseland.fjf.sequence.domain.SequenceModel;

/**
 * @description: 得到序列 的工具类 
 * 1.缓存序列当前值，当超过阀值时，更新数据库中序列的值，并返回数据库中最新值
 * 2.先更新，再查询是原子化操作，集群环境中需要分布式锁实现。（暂时未加）
 * 3.调用方式 注入sequenceUtil 然后调用sequenceUtil.sequenceNextVal(序列名称)
 * @author 王海洋
 * @date 2017-12-27 上午11:59:28
 * @version v_1.0
 */
public class SequenceUtil{
	
	private static SequenceUtil instance = null;
	
	public static SequenceUtil create(){
		if(instance == null){
			synchronized (SequenceUtil.class) {
				if(instance == null){
					instance = new SequenceUtil();
				}
			}
		}
		return instance;
	}
	
	// 缓存各个序列
	private static ConcurrentHashMap<String, SequenceModel> sequenceMap = new ConcurrentHashMap<String, SequenceModel>(500);
	
	
	public long sequenceNextVal(Class<?> sequenceModel) throws BaseExcepton {
		if(sequenceModel == null){
			throw  new BaseExcepton("sequenceClass is null"); 
		}
		String className = sequenceModel.getSimpleName();
		if(StringUtils.isBlank(className)){
			throw  new BaseExcepton("sequenceName is null"); 
		}
		long nextId = sequenceNextVal(className);
		return nextId;
	}
	
	/**
	 * getSeqNextVal(根据序列名字得到序列的下一个值) (这里描述这个方法适用条件 – 可选)
	 * @param sequenceName 序列名称
	 * @return 返回该序列的下一个值 long
	 * @throws BaseExcepton 
	 * @exception @since 1.0
	 */
	public long sequenceNextVal(String sequenceName){
		// 当该序列不存在时，创建该序列
		SequenceModel seqObject = sequenceMap.get(sequenceName);
		if (seqObject == null) {
			seqObject = new SequenceHandle(sequenceName, seqObject).getSequenceModel();
			sequenceMap.putIfAbsent(sequenceName, seqObject);
		}
		return seqObject.getNextValue();
	}
}
