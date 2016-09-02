package com.kayb.support.wxpay.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.kayb.support.wxpay.model.enums.FeeType;

import java.io.IOException;

/**
 * @author @kaybinwong
 * @since 2016/8/25
 */
public class FeeTypeDeserializer extends JsonDeserializer<FeeType> {

    @Override
    public FeeType deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return FeeType.from(jp.getValueAsString());
    }
}
