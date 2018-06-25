package com.foriseland.fjf.sequence.handle;

import com.foriseland.fjf.sequence.domain.SequenceModel;

/**
 * @description: 序列对象
 * @author wangHaiyang
 * @date 2017-12-27
 * @version v_1.0
 */
public class SequenceHandle {

	private String sequenceName; // 序列名称

	private SequenceModel sequenceModel; // Sequence存储数据对象

	/**
	 * 根据序列名称 创建一个新的实例 SequenceObject.
	 * @param sequenceName  序列名称
	 * @param SequenceModel 存储数据对象
	 */
	public SequenceHandle(String sequenceName, SequenceModel sequenceModel) {
		this.sequenceName = sequenceName;
		this.sequenceModel = sequenceModel;
	}

	public void updateModel() {
		if (sequenceModel == null) {
			sequenceModel = new SequenceModel();
			sequenceModel.setSequenceName(sequenceName);
			sequenceModel.createIdWorker();
		}
	}

	public SequenceModel getSequenceModel() {
		if (sequenceModel == null) {
			this.updateModel();
		}
		return sequenceModel;
	}

}
