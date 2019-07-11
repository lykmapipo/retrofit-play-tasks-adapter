package com.github.lykmapipo.retrofit.adapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

final class StringConverterFactory extends Converter.Factory {
    @Override
    public Converter<ResponseBody, String> responseBodyConverter(
            Type type, Annotation[] annotations,
            Retrofit retrofit
    ) {
        return ResponseBody::string;
    }

    @Override
    public Converter<String, RequestBody> requestBodyConverter(
            Type type, Annotation[] parameterAnnotations,
            Annotation[] methodAnnotations, Retrofit retrofit
    ) {
        return value -> RequestBody.create(value, MediaType.get("text/plain"));
    }
}
