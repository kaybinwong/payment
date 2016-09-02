package com.kayb.support.wxpay.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * @author @kaybinwong
 * @since 2016/8/25
 */
public class BooleanDeserializer extends JsonDeserializer<Boolean> {

    @Override
    public Boolean deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String r = jp.getValueAsString();
        return "Y".equals(r);
    }
}
