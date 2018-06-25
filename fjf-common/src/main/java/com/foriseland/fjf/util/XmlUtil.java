package com.foriseland.fjf.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.MapperWrapper;

public class XmlUtil {

	@SuppressWarnings("unchecked")
	public static <IN> IN in(String input, Class<IN> clazz) {
		
		XStream stream = new XStream(){
			
			protected MapperWrapper wrapMapper(MapperWrapper next) {
		        return new MapperWrapper(next) {
		            @Override
		            @SuppressWarnings("rawtypes")
		            public boolean shouldSerializeMember(Class definedIn, String fieldName) {
		            	
		            	if(definedIn == Object.class) {
		            		return false;
		            	}
		               
		               return super.shouldSerializeMember(definedIn, fieldName);
		            }
		        };
		    }
		};
		stream.alias("xml", clazz);
		stream.autodetectAnnotations(true);
		return (IN) stream.fromXML(input);
	}

	public static <OUT> String out(OUT out) {
		XStream stream = new XStream();
		stream.alias("xml", out.getClass());
		stream.autodetectAnnotations(true);
		stream.setMode(XStream.NO_REFERENCES);
		return stream.toXML(out);
	}
}
