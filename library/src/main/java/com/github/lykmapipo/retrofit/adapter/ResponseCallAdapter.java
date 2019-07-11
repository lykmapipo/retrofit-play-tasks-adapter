package com.github.lykmapipo.retrofit.adapter;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

final class ResponseCallAdapter<T> implements CallAdapter<T, Task<Response<T>>> {
    private final Type responseType;

    ResponseCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public Task<Response<T>> adapt(Call<T> call) {
        final TaskCompletionSource<Response<T>> source = new TaskCompletionSource<Response<T>>();

        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                source.setResult(response);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                try {
                    source.setException((Exception) t);
                } catch (Exception e) {
                    source.setException(new Exception(t));
                }
            }
        });

        return source.getTask();
    }
}
