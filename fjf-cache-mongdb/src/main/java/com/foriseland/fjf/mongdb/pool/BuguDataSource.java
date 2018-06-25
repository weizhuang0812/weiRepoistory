package com.foriseland.fjf.mongdb.pool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.foriseland.fjf.cache.mongo.BuguConnection;
import com.foriseland.fjf.cache.mongo.BuguFramework;
import com.foriseland.fjf.mongdb.exception.MongdbException;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;


public class BuguDataSource {

	private String clusterNodes; // db connection
	private String databaseName; // 数据库
	private String username;
	private String password;
	
	public void init() throws MongdbException {
		if(null == clusterNodes) {
			throw new MongdbException("clusterNodes is null");
		}
		
		if(null == databaseName) {
			throw new MongdbException("databaseName is null");
		}
		Set<HostAndPort> hostAndPortSet = this.bindHostAndPort();
		if(null == hostAndPortSet) {
			throw new MongdbException("clusterNodes is Exception");
		}
		List<ServerAddress> serverList = new ArrayList<ServerAddress>(hostAndPortSet.size());
		for(HostAndPort item : hostAndPortSet) {
			serverList.add(new ServerAddress(item.getHost(),item.getPort()));
		}
		BuguConnection conn = BuguFramework.getInstance().createConnection();
		conn.setServerList(serverList).setCredentialList(credentialList()).setDatabase(databaseName).connect();
		//BuguConnection conn = BuguFramework.getInstance().createConnection();
		//conn.connect("192.168.2.1", 27017, "fl_media");
	}
	
	private List<MongoCredential> credentialList(){
		List<MongoCredential> credentialList = null;
		if(StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
			credentialList = new ArrayList<MongoCredential>();
			MongoCredential credentialA = MongoCredential.createCredential(username, databaseName, password.toCharArray());
			credentialList.add(credentialA);
		}
		return credentialList;
	}
	
	private Set<HostAndPort> bindHostAndPort(){
		String[]arrays = clusterNodes.split(",");
		if(null == arrays){
			return null;
		}
		Set<HostAndPort> hostAndPortSet = new HashSet<HostAndPort>();
		for(String item : arrays){
			String[]subArray = item.split(":");
			if(null == subArray){
				continue;
			}
			if(subArray.length<1){
				continue;
			}
			HostAndPort bean = new HostAndPort(subArray[0],Integer.parseInt(subArray[1]));
			hostAndPortSet.add(bean);
		}
		return hostAndPortSet;
	}
	
	
	public void destroy() {
		BuguFramework.getInstance().destroy();
	}
	
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public String getClusterNodes() {
		return clusterNodes;
	}
	public void setClusterNodes(String clusterNodes) {
		this.clusterNodes = clusterNodes;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}