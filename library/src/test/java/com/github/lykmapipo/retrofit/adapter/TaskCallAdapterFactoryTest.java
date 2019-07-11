package com.github.lykmapipo.retrofit.adapter;

import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.mockwebserver.MockWebServer;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, shadows = {ShadowPreconditions.class})
public final class TaskCallAdapterFactoryTest {
    private static final Annotation[] NO_ANNOTATIONS = new Annotation[0];

    @Rule
    public final MockWebServer server = new MockWebServer();

    private final CallAdapter.Factory factory = TaskCallAdapterFactory.create();
    private Retrofit retrofit;

    @Before
    public void setUp() {
        retrofit = new Retrofit.Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(new StringConverterFactory())
                .addCallAdapterFactory(factory)
                .build();
    }

    @Test
    public void responseType() {
        Type bodyClass = new TypeToken<Task<String>>() {
        }.getType();
        assertThat(factory.get(bodyClass, NO_ANNOTATIONS, retrofit).responseType())
                .isEqualTo(String.class);

        Type bodyWildcard = new TypeToken<Task<? extends String>>() {
        }.getType();
        assertThat(factory.get(bodyWildcard, NO_ANNOTATIONS, retrofit).responseType())
                .isEqualTo(String.class);

        Type bodyGeneric = new TypeToken<Task<List<String>>>() {
        }.getType();
        assertThat(factory.get(bodyGeneric, NO_ANNOTATIONS, retrofit).responseType())
                .isEqualTo(new TypeToken<List<String>>() {
                }.getType());

        Type responseClass = new TypeToken<Task<Response<String>>>() {
        }.getType();
        assertThat(factory.get(responseClass, NO_ANNOTATIONS, retrofit).responseType())
                .isEqualTo(String.class);

        Type responseWildcard = new TypeToken<Task<Response<? extends String>>>() {
        }.getType();
        assertThat(factory.get(responseWildcard, NO_ANNOTATIONS, retrofit).responseType())
                .isEqualTo(String.class);

        Type resultClass = new TypeToken<Task<Response<String>>>() {
        }.getType();
        assertThat(factory.get(resultClass, NO_ANNOTATIONS, retrofit).responseType())
                .isEqualTo(String.class);

        Type resultWildcard = new TypeToken<Task<Response<? extends String>>>() {
        }.getType();
        assertThat(factory.get(resultWildcard, NO_ANNOTATIONS, retrofit).responseType())
                .isEqualTo(String.class);
    }

    @Test
    public void nonListenableTaskReturnsNull() {
        CallAdapter<?, ?> adapter = factory.get(String.class, NO_ANNOTATIONS, retrofit);
        assertThat(adapter).isNull();
    }

    @Test
    public void rawTypeThrows() {
        Type observableType = new TypeToken<Task>() {
        }.getType();
        try {
            factory.get(observableType, NO_ANNOTATIONS, retrofit);
            fail();
        } catch (IllegalStateException e) {
            assertThat(e).hasMessage(
                    "Task return type must be parameterized as Task<Foo> or Task<? extends Foo>");
        }
    }

    @Test
    public void rawResponseTypeThrows() {
        Type observableType = new TypeToken<Task<Response>>() {
        }.getType();
        try {
            factory.get(observableType, NO_ANNOTATIONS, retrofit);
            fail();
        } catch (IllegalStateException e) {
            assertThat(e).hasMessage(
                    "Response must be parameterized as Response<Foo> or Response<? extends Foo>");
        }
    }
}

