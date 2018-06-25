package com.foriseland.fjf.mq.filter;

import org.apache.rocketmq.common.filter.FilterContext;
import org.apache.rocketmq.common.filter.MessageFilter;
import org.apache.rocketmq.common.message.MessageExt;

public class MessageFilterImpl implements MessageFilter {

	// private Logger logger = Logger.getLogger(MessageFilterImpl.class);
	
    @Override
    public boolean match(MessageExt msg, FilterContext context) {
        String aValue = msg.getUserProperty("a");
        if (aValue != null) {
            int a = Integer.parseInt(aValue);
            if (a >7) {
                return true;
            }
        }

        return false;
    }
}
