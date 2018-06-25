package com.foriseland.fjf.sequence.domain;

import java.io.Serializable;

import com.foriseland.fjf.sequence.util.MathUtils;
import com.foriseland.fjf.sequence.util.SnowflakeIdWorker;

/**
 * @Description: 对应sequence表  
 * @author wangHaiyang  
 * @date 2017-12-27
 * @version v_1.0     
*/
public class SequenceModel implements Serializable {
	private static final long serialVersionUID = 6600315135422457571L;

	private String sequenceName;
    
    private long seqCurrentVal;
    
    private SnowflakeIdWorker idWorker; // SnowflakeIdWorker
    
    private final long sequenceMask = -1L ^ (-1L << 12L);
    
    public long getNextValue(){
    	return idWorker.nextId();
    }

	public SnowflakeIdWorker getIdWorker() {
		return idWorker;
	}
	
	public void createIdWorker(){
		idWorker = new SnowflakeIdWorker(this.workId(),this.datacenterId());
	}

	public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public long getSeqCurrentVal() {
        return seqCurrentVal;
    }

    public void setSeqCurrentVal(long seqCurrentVal) {
        this.seqCurrentVal = seqCurrentVal;
    }
    
    /*
	 * 工作ID (0~31)
	 * 将sequenceName中的hashCode方法就是根据一定的规则将与对象相关的信息（比如对象的存储地址，对象的字段等）映射成一个数值
	 * -2147483648 到 2147483647
	 * 映射为为散列值
	 */
	private int workId(){
		int hash = sequenceName.hashCode();
		int mathValue = MathUtils.getValue(31);
		Long longValue = (hash * mathValue * sequenceMask);
		longValue = (longValue.longValue() %31);
		return MathUtils.positive(longValue.intValue());
	}
	/*
	 * 数据中心ID(0~31)
	 */
	private int datacenterId(){
		return 31;
	}
}