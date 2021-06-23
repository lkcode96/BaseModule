package com.lk.baselibrary.utils.convert;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author: Administrator
 * @date: 2021/6/21
 */
public class FloatNullAdapter extends TypeAdapter<Float> {
    @Override
    public Float read(JsonReader reader) throws IOException {
        // TODO Auto-generated method stub
        if (reader.peek() == JsonToken.STRING) {
            reader.skipValue(); //跳过当前
            return 0f;
        }
        BigDecimal bigDecimal = new BigDecimal(reader.nextString());
        return bigDecimal.floatValue();
    }

    @Override
    public void write(JsonWriter writer, Float value) throws IOException {
        // TODO Auto-generated method stub
        writer.value(value);
    }
}
