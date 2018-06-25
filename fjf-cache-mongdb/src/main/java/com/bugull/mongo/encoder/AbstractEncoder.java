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

package com.bugull.mongo.encoder;

import com.bugull.mongo.utils.FieldUtil;
import java.lang.reflect.Field;

/**
 *
 * @author Frank Wen(xbwen@hotmail.com)
 */
public abstract class AbstractEncoder implements Encoder{
    
    protected Field field;
    protected Object value;
    protected Class<?> clazz;
    
    protected boolean withoutCascade;
    
    protected AbstractEncoder(Object obj, Field field){
        this.field = field;
        value = FieldUtil.get(obj, field);
        clazz = obj.getClass();
    }
    
    @Override
    public boolean isNullField(){
        return value == null;
    }

    @Override
    public void setWithoutCascade(boolean withoutCascade) {
        this.withoutCascade = withoutCascade;
    }
    
}
