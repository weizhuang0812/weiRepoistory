package com.foriseland.fjf.mq.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * InnoDB free: 67584 kB
 * 
 **/
@SuppressWarnings("serial")
public class FLKafkaProducer implements Serializable {

	/** 主键 **/
	private String kafkaProducerId;

	/** 分区 **/
	private Integer kafkaPartition;

	/****/
	private String kafkaTopic;

	/****/
	private Long kafkaOffset;

	/****/
	private String kafkaKey;

	/****/
	private String kafkaValue;

	/****/
	private String kafkaChecksum;

	/****/
	private String exceptionMessage;

	/****/
	private FLOpertionType opertionType;

	/****/
	private java.util.Date updateTime;

	/****/
	private java.util.Date resolveTime;

	public String getKafkaProducerId() {
		return kafkaProducerId;
	}

	public void setKafkaProducerId(String kafkaProducerId) {
		this.kafkaProducerId = kafkaProducerId;
	}

	public Integer getKafkaPartition() {
		return kafkaPartition;
	}

	public void setKafkaPartition(Integer kafkaPartition) {
		this.kafkaPartition = kafkaPartition;
	}

	public String getKafkaTopic() {
		return kafkaTopic;
	}

	public void setKafkaTopic(String kafkaTopic) {
		this.kafkaTopic = kafkaTopic;
	}

	public Long getKafkaOffset() {
		return kafkaOffset;
	}

	public void setKafkaOffset(Long kafkaOffset) {
		this.kafkaOffset = kafkaOffset;
	}

	public String getKafkaKey() {
		return kafkaKey;
	}

	public void setKafkaKey(String kafkaKey) {
		this.kafkaKey = kafkaKey;
	}

	public String getKafkaValue() {
		return kafkaValue;
	}

	public void setKafkaValue(String kafkaValue) {
		this.kafkaValue = kafkaValue;
	}

	public String getKafkaChecksum() {
		return kafkaChecksum;
	}

	public void setKafkaChecksum(String kafkaChecksum) {
		this.kafkaChecksum = kafkaChecksum;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public FLOpertionType getOpertionType() {
		return opertionType;
	}

	public void setOpertionType(FLOpertionType opertionType) {
		this.opertionType = opertionType;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getResolveTime() {
		return resolveTime;
	}

	public void setResolveTime(Date resolveTime) {
		this.resolveTime = resolveTime;
	}
}
