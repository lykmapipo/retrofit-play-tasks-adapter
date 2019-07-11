package com.github.lykmapipo.retrofit.adapter;

import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.annotation.Nullable;

import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A {@linkplain CallAdapter.Factory call adapter} which creates {@linkplain Task}.
 * <p>
 * Adding this class to {@link Retrofit} allows you to return {@link Task} from
 * service methods.
 * <pre><code>
 * interface MyService {
 *   &#64;GET("user/me")
 *   Task&lt;User&gt; getUser()
 * }
 * </code></pre>
 * There are two configurations supported for the {@code Task} type parameter:
 * <ul>
 * <li>Direct body (e.g., {@code Task<User>}) returns the deserialized body for 2XX
 * responses, sets {@link retrofit2.HttpException HttpException} errors for non-2XX responses, and
 * sets {@link IOException} for network errors.</li>
 * <li>Response wrapped body (e.g., {@code Task<Response<User>>}) returns a
 * {@link Response} object for all HTTP responses and sets {@link IOException} for network
 * errors</li>
 * </ul>
 */
public final class TaskCallAdapterFactory extends CallAdapter.Factory {
    private TaskCallAdapterFactory() {
    }

    public static TaskCallAdapterFactory create() {
        return new TaskCallAdapterFactory();
    }

    @Override
    @Nullable
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(returnType) != Task.class) {
            return null;
        }
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalStateException(
                    "Task return type must be parameterized as Task<Foo> or Task<? extends Foo>");
        }
        Type innerType = getParameterUpperBound(0, (ParameterizedType) returnType);

        if (getRawType(innerType) != Response.class) {
            // Generic type is not Response<T>. Use it for body-only adapter.
            return new BodyCallAdapter<>(innerType);
        }

        if (!(innerType instanceof ParameterizedType)) {
            throw new IllegalStateException(
                    "Response must be parameterized as Response<Foo> or Response<? extends Foo>");
        }

        Type responseType = getParameterUpperBound(0, (ParameterizedType) innerType);
        return new ResponseCallAdapter<>(responseType);
    }
}
