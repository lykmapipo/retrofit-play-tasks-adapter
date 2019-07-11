package com.github.lykmapipo.retrofit.adapter;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

final class BodyCallAdapter<T> implements CallAdapter<T, Task<T>> {
    private final Type responseType;

    BodyCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public Task<T> adapt(Call<T> call) {
        final TaskCompletionSource<T> source = new TaskCompletionSource<T>();

        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    source.setResult(response.body());
                } else {
                    source.setException(new HttpException(response));
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                source.setException(new Exception(t));
            }
        });

        return source.getTask();
    }
}

