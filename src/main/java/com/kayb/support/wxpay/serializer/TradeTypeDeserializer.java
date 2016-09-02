package com.kayb.support.wxpay.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.kayb.support.wxpay.model.enums.TradeType;

import java.io.IOException;

/**
 * @author @kaybinwong
 * @since 2016/8/25
 */
public class TradeTypeDeserializer extends JsonDeserializer<TradeType> {

    @Override
    public TradeType deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return TradeType.from(jp.getValueAsString());
    }
}
