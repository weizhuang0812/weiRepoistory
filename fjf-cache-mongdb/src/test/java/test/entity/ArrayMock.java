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
package test.entity;

import com.bugull.mongo.annotations.Entity;
import com.foriseland.fjf.cache.mongo.SimpleEntity;

/**
 *
 * @author Frank Wen(xbwen@hotmail.com)
 */
@Entity
public class ArrayMock extends SimpleEntity {
    
    private byte[] binary;
    
    private int[] one;
    
    private double[][] two;

    private String[][][] three;

    public byte[] getBinary() {
        return binary;
    }

    public void setBinary(byte[] binary) {
        this.binary = binary;
    }

    public int[] getOne() {
        return one;
    }

    public void setOne(int[] one) {
        this.one = one;
    }

    public double[][] getTwo() {
        return two;
    }

    public void setTwo(double[][] two) {
        this.two = two;
    }

    public String[][][] getThree() {
        return three;
    }

    public void setThree(String[][][] three) {
        this.three = three;
    }
    
}
