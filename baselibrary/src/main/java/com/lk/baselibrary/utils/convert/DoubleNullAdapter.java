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
public class DoubleNullAdapter extends TypeAdapter<Double> {
    @Override
    public Double read(JsonReader reader) throws IOException {
        // TODO Auto-generated method stub
        if (reader.peek() == JsonToken.STRING) {
            reader.skipValue(); //跳过当前
            return 0.0;
        }
        BigDecimal bigDecimal = new BigDecimal(reader.nextString());
        return bigDecimal.doubleValue();
    }

    @Override
    public void write(JsonWriter writer, Double value) throws IOException {
        // TODO Auto-generated method stub
        writer.value(value);
    }
}
