package org.sitenv.xdrmessagesender.services.enums;

import org.springframework.core.convert.converter.Converter;

public class XdrMessageTypeEnumConverter implements Converter<String, XdrMessageType> {

    public XdrMessageType convert(String source) {
        if(source != null){
            try {
                return XdrMessageType.valueOf(source.toUpperCase());
            } catch(Exception e) {
                return XdrMessageType.MINIMAL;
            }
        }
       return XdrMessageType.MINIMAL;
    }
}
