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

package com.bugull.mongo.lucene.utils;

import com.bugull.mongo.lucene.backend.ValueComparer;
import com.foriseland.fjf.cache.mongo.BuguEntity;
import com.bugull.mongo.cache.FieldsCache;
import com.bugull.mongo.lucene.annotations.Compare;
import com.bugull.mongo.lucene.annotations.IndexFilter;
import java.lang.reflect.Field;

/**
 *
 * @author Frank Wen(xbwen@hotmail.com)
 */
public final class IndexFilterChecker {
    
    public static boolean needIndex(BuguEntity obj){
        Class<?> clazz = obj.getClass();
        Field[] fields = FieldsCache.getInstance().get(clazz);
        for(Field f : fields){
            IndexFilter filter = f.getAnnotation(IndexFilter.class);
            if(filter != null){
                Compare compare = filter.compare();
                String value = filter.value();
                ValueComparer comparer = new ValueComparer(obj);
                if(! comparer.isFit(f, compare, value)){
                    return false;
                }
            }
        }
        return true;
    }
    
}
