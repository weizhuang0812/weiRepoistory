package com.alibaba.dubbo.rpc.filter;

import java.util.List;

import com.alibaba.dubbo.rpc.RpcException;

public class IpWhiteList {
	
	public IpWhiteList(List<String> ips) {
		if(null == ips) {
			throw new RpcException("IpWhiteList is null,please check IpWhiteList ips.");
		}
		this.allowedIps = ips;
	}

	private List<String> allowedIps;

	
	public boolean contains(String ip) {
		if(allowedIps.contains(ip)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	
	public List<String> getAllowedIps() {
		return allowedIps;
	}

	public void setAllowedIps(List<String> allowedIps) {
		this.allowedIps = allowedIps;
	}
}