package com.kayb.support.wxpay.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.kayb.util.StringUtil;
import com.kayb.support.wxpay.model.enums.RefundChannel;

import java.io.IOException;

/**
 * @author @kaybinwong
 * @since 2016/8/25
 */
public class RefundChannelDeserializer extends JsonDeserializer<RefundChannel> {

    @Override
    public RefundChannel deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String val = jp.getValueAsString();
        if (StringUtil.isEmpty(val)){
            return null;
        }
        return RefundChannel.from(val);
    }
}
