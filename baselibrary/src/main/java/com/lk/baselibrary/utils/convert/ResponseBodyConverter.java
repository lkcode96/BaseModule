/*
 * Copyright (C) 2015 Square, Inc.
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
package com.lk.baselibrary.utils.convert;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.TypeAdapter;
import com.lk.baselibrary.utils.ResponseThrowable;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class ResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final TypeAdapter<T> adapter;

    ResponseBodyConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String json = value.string();
        Log.e("mTag", json);
        try {
            JSONObject object = JSONObject.parseObject(json);
            int code = object.getInteger("code");
            if (code != 200) {
                throw new ResponseThrowable(code, object.getString("message"));
            }
            return adapter.fromJson(json);
        } catch (ResponseThrowable responseThrowable) {
            responseThrowable.printStackTrace();
            throw new ResponseThrowable(responseThrowable.getCode(), responseThrowable.getMsg());
        } catch (Exception e) {
            throw new ResponseThrowable(-1, Objects.requireNonNull(e.getMessage()));
        } finally {
            value.close();
        }
    }

}
