package com.github.lykmapipo.retrofit.adapter;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;

import static okhttp3.mockwebserver.SocketPolicy.DISCONNECT_AFTER_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, shadows = {ShadowPreconditions.class})
public final class TaskTest {
    @Rule
    public final MockWebServer server = new MockWebServer();

    private Service service;

    @Before
    public void setUp() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(new StringConverterFactory())
                .addCallAdapterFactory(TaskCallAdapterFactory.create())
                .build();
        service = retrofit.create(Service.class);
    }

    @Test
    public void bodySuccess200() throws Exception {
        server.enqueue(new MockResponse().setBody("Hi"));

        Task<String> task = service.body();
        String result = Tasks.await(task);
        assertThat(result).isEqualTo("Hi");
    }

    @Test
    public void bodySuccess404() {
        server.enqueue(new MockResponse().setResponseCode(404));

        Task<String> task = service.body();
        try {
            Tasks.await(task);
            fail();
        } catch (Exception e) {
            Exception exception = task.getException();
            assertThat(exception)
                    .isInstanceOf(HttpException.class) // Required for backwards compatibility.
                    .isInstanceOf(retrofit2.HttpException.class)
                    .hasMessage("HTTP 404 Client Error");
        }
    }

    @Test
    public void bodyFailure() {
        server.enqueue(new MockResponse().setSocketPolicy(DISCONNECT_AFTER_REQUEST));

        Task<String> task = service.body();
        try {
            Tasks.await(task);
            fail();
        } catch (Exception e) {
            Exception exception = task.getException();
            assertThat(exception).isInstanceOf(IOException.class);
        }
    }

    @Test
    public void responseSuccess200() throws Exception {
        server.enqueue(new MockResponse().setBody("Hi"));

        Task<Response<String>> task = service.response();
        Response<String> response = Tasks.await(task);
        assertThat(response.isSuccessful()).isTrue();
        assertThat(response.body()).isEqualTo("Hi");
    }

    @Test
    public void responseSuccess404() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(404).setBody("Hi"));

        Task<Response<String>> task = service.response();
        Response<String> response = Tasks.await(task);
        assertThat(response.isSuccessful()).isFalse();
        assertThat(response.errorBody().string()).isEqualTo("Hi");
    }

    @Test
    public void responseFailure() {
        server.enqueue(new MockResponse().setSocketPolicy(DISCONNECT_AFTER_REQUEST));

        Task<Response<String>> task = service.response();
        try {
            Tasks.await(task);
            fail();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IOException.class);
        }
    }

    interface Service {
        @GET("/")
        Task<String> body();

        @GET("/")
        Task<Response<String>> response();
    }
}
