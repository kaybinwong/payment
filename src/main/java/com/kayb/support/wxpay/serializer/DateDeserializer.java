package com.kayb.support.wxpay.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.kayb.util.DateUtil;

import java.io.IOException;
import java.util.Date;

/**
 * @author @kaybinwong
 * @since 2016/8/25
 */
public class DateDeserializer extends JsonDeserializer<Date> {
 
    @Override
    public Date deserialize(JsonParser parser, DeserializationContext context)
      throws IOException {
        return DateUtil.dateFromStringByFormat(parser.getValueAsString(), "yyyyMMddHHmmss");
    }
}