package com.foriseland.fjf.sequence;

import com.foriseland.fjf.datasource.DataSource;
import com.foriseland.fjf.exception.BaseExcepton;
import com.foriseland.fjf.sequence.handle.SequenceUtil;

public class TestSequence {
	
	public static void main(String[] args) throws BaseExcepton {
		SequenceUtil util = new SequenceUtil();
		for (int i = 0; i < 20; i++) {
			long nextId = util.sequenceNextVal(DataSource.class);
			System.out.println(nextId);
		}
	}

}
