/*
 * Copyright (c) www.bugull.com
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

package com.bugull.mongo.cache;

import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.LogManager;

/**
 * Cache(Map) holds entity classes' constructor, for performance purporse.
 * 
 * @author Frank Wen(xbwen@hotmail.com)
 */
@SuppressWarnings("unchecked")
public class ConstructorCache {
    
    private final static org.apache.log4j.Logger logger = LogManager.getLogger(ConstructorCache.class.getName());
    
    private final ConcurrentMap<String, SoftReference<Constructor<?>>> cache = new ConcurrentHashMap<String, SoftReference<Constructor<?>>>();
    
    private ConstructorCache(){
        
    }
    
    private static class Holder {
        final static ConstructorCache instance = new ConstructorCache();
    } 
    
    public static ConstructorCache getInstance(){
        return Holder.instance;
    }
    
    private <T> Constructor<T> get(Class<T> clazz){
        String name = clazz.getName();
        Constructor<T> cons = null;
        boolean recycled = false;
        SoftReference<Constructor<?>> sr = cache.get(name);
        if(sr != null){
            cons = (Constructor<T>)sr.get();
            if(cons == null){
                recycled = true;
            }else{
                return cons;
            }
        }
        //if not exists
        Class<T>[] types = null;
        try {
            cons = clazz.getConstructor(types);
        } catch (Exception ex) {
            logger.error("Something is wrong when get constructor", ex);
        }
        sr = new SoftReference<Constructor<?>>(cons);
        if(recycled){
            cache.put(name, sr);
            return cons;
        }else{
            SoftReference<Constructor<?>> temp = cache.putIfAbsent(name, sr);
            if(temp != null){
                return (Constructor<T>)temp.get();
            }else{
                return (Constructor<T>)sr.get();
            }
        }
    }
    
    public <T> T create(Class<T> clazz){
        T obj = null;
        Constructor<T> cons = get(clazz);
        Object[] args = null;
        try {
            obj = cons.newInstance(args);
        } catch (Exception ex) {
            logger.error("Something is wrong when create new instance", ex);
        } 
        return obj;
    }
    
}
