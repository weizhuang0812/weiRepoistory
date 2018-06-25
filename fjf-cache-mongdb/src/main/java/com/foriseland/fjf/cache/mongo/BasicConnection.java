/*
 * Copyright 2017 .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.foriseland.fjf.cache.mongo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

/**
 * Basic implement of BuguConnection, create by BuguFramework.
 * 
 * @author Frank Wen(xbwen@hotmail.com)
 */
class BasicConnection implements BuguConnection {
    
    private final static org.apache.log4j.Logger logger = LogManager.getLogger(BasicConnection.class.getName());
    
    private String host;
    private int port = 27017;
    private List<ServerAddress> serverList;
    private List<MongoCredential> credentialList;
    private MongoClientOptions options;
    private String database;
    private String username;
    private String password;
    private MongoClient mongoClient;
    private DB db;
    
    @Override
    public void connect(String host, int port, String database){
        this.host = host;
        this.port = port;
        this.database = database;
        try {
            doConnect();
        } catch (UnknownHostException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
    
    @Override
    public void connect(String host, int port, String database, String username, String password){
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        try {
            doConnect();
        } catch (UnknownHostException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
    
    @Override
    public void connect(List<ServerAddress> serverList, List<MongoCredential> credentialList, String database){
        this.serverList = serverList;
        this.credentialList = credentialList;
        this.database = database;
        try {
            doConnect();
        } catch (UnknownHostException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
    
    @Override
    public void connect(){
        try {
            doConnect();
        } catch (UnknownHostException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
    
    private void doConnect() throws UnknownHostException{
        if(host != null && serverList != null){
            logger.error("Error when connect to database server! You should set database host or server list, but not both!");
            return;
        }
        if(username != null && password != null && database != null){
            credentialList = new ArrayList<MongoCredential>();
            MongoCredential cred = MongoCredential.createCredential(username, database, password.toCharArray());
            credentialList.add(cred);
        }
        if(host != null){
            ServerAddress sa = new ServerAddress(host, port);
            if(credentialList != null){
                if(options != null){
                    mongoClient = new MongoClient(sa, credentialList, options);
                }else{
                    mongoClient = new MongoClient(sa, credentialList);
                }
            }else{
                if(options != null){
                    mongoClient = new MongoClient(sa, options);
                }else{
                    mongoClient = new MongoClient(sa);
                }
            }
        }
        else if(serverList != null){
            if(credentialList != null){
                if(options != null){
                    mongoClient = new MongoClient(serverList, credentialList, options);
                }else{
                    mongoClient = new MongoClient(serverList, credentialList);
                }
            }else{
                if(options != null){
                    mongoClient = new MongoClient(serverList, options);
                }else{
                    mongoClient = new MongoClient(serverList);
                }
            }
        }
        //get the database
        db = mongoClient.getDB(database);
    }
    
    @Override
    public void close(){
        if(mongoClient != null){
            mongoClient.close();
            mongoClient = null;
        }
    }
    
    @Override
    public BuguConnection setHost(String host){
        this.host = host;
        return this;
    }
    
    @Override
    public BuguConnection setPort(int port){
        this.port = port;
        return this;
    }
    
    @Override
    public BuguConnection setDatabase(String database){
        this.database = database;
        return this;
    }
    
    @Override
    public BuguConnection setUsername(String username){
        this.username = username;
        return this;
    }
    
    @Override
    public BuguConnection setPassword(String password){
        this.password = password;
        return this;
    }

    @Override
    public BuguConnection setOptions(MongoClientOptions options) {
        this.options = options;
        return this;
    }

    @Override
    public BuguConnection setServerList(List<ServerAddress> serverList) {
        this.serverList = serverList;
        return this;
    }

    @Override
    public BuguConnection setCredentialList(List<MongoCredential> credentialList) {
        this.credentialList = credentialList;
        return this;
    }

    @Override
    public DB getDB() {
        return db;
    }

    @Override
    public MongoClient getMongoClient() {
        return mongoClient;
    }
    
}
