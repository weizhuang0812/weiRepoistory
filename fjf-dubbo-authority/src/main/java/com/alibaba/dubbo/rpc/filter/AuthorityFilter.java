package com.alibaba.dubbo.rpc.filter;
import java.util.List;
import java.util.Map;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcResult;
import com.foriseland.fjf.log.LogMgr;
import com.google.common.collect.Lists;

public class AuthorityFilter implements Filter {

	private IpWhiteList ipWhiteList; // ip 白名单验证
	
    public void setIpWhiteList(IpWhiteList ipWhiteList) {   //dubbo通过setter方式自动注入  
        this.ipWhiteList = ipWhiteList;  
    }  
  

    /**
     * 1.防止消费者绕过 注册中心访问提供者 
	   2.在注册中心控制权限，以决定要不要下发令牌给消费者。 
       3.注册中心可灵活改变授权方式，而不需要修改或升级提供者。 
     */
	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		String clientIp = RpcContext.getContext().getRemoteHost();
		LogMgr.sysInfo("dubbo request client ip:", clientIp);
		List<String> ips = Lists.newArrayList();
		ips.add("192.168.0.214");
		ips.add("192.168.0.166");
		IpWhiteList list = new IpWhiteList(ips);
		
		
		boolean isWhileIp = list.contains(clientIp);
		if(isWhileIp == Boolean.FALSE) { // 该请求不是白名单IP
			Throwable exception = new Throwable("Rpc Exception -> Your request [IP] is not on the IpWhiteList,please check the IpWhiteList");
			 return new RpcResult(exception);  
		}
		
		
		String clientToken = invoker.getUrl().getParameter(Constants.TOKEN_KEY); // 获得用户请求token,进行验证
		System.out.println("##############clientToken:"+clientToken);
		Class<?> serviceType = invoker.getInterface();
		Map<String, String> attachments = invocation.getAttachments();
		String remoteToken = attachments == null ? null : attachments.get(Constants.TOKEN_KEY); // 服务端 remote 请求token
		String remoteToken2 = RpcContext.getContext().getAttachment(Constants.TOKEN_KEY); 
		LogMgr.sysInfo("请求进入AuthorityFilter...  -- ", remoteToken+"  remoteToken2:"+remoteToken2);
		System.out.println("##############remoteToken:"+remoteToken);
		if (null == clientToken) {
			throw new RpcException("Invalid token is [*Empty] to AuthorityFilter! Forbid invoke remote service "
					+ serviceType + " method " + invocation.getMethodName() + "() from consumer "
					+ RpcContext.getContext().getRemoteHost() + " to provider "
					+ RpcContext.getContext().getLocalHost());
		}

		// 此处通过秘钥进行判断，两端是否一样

		if (!clientToken.equals(remoteToken)) {
			System.out.println("#############token#:"+clientToken+"  ||| remoteToken:"+remoteToken);
			throw new RpcException("Invalid token is [#error] to AuthorityFilter! Forbid invoke remote service "
					+ serviceType + " method " + invocation.getMethodName() + "() from consumer "
					+ RpcContext.getContext().getRemoteHost() + " to provider "
					+ RpcContext.getContext().getLocalHost());
		}
		
		LogMgr.sysInfo("Token valide success ...");
		return invoker.invoke(invocation);  
	}
	
	/**
	 * 请求Token 验证
	 * @param invoker
	 * @param invocation
	 * @return
	 */
	public Invocation tokenFilter(Invoker<?> invoker,Invocation invocation) {
		
		
		 return invocation;
	}

}
