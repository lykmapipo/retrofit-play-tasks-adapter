package com.github.lykmapipo.retrofit.adapter;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

@RunWith(RobolectricTestRunner.class)
public class TaskCallAdapterFactoryTest {
    private MockWebServer mockWebServer;
    private String baseUrl;

    @Before
    public void setup() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @Before
    public void before() throws Exception {
        baseUrl = mockWebServer.url("/v1/").toString();
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
        baseUrl = null;
        mockWebServer = null;
    }

    public interface Api {
        @GET("users")
        @Headers({
                "Accept: application/json",
        })
        Call<List<User>> list();
    }
}