package dhu.cst.zjm.encryptmvp.injector.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import dhu.cst.zjm.encryptmvp.MyApplication;
import dhu.cst.zjm.encryptmvp.api.BaseUrl;
import dhu.cst.zjm.encryptmvp.api.interceptor.CacheInterceptor;
import dhu.cst.zjm.encryptmvp.api.repository.BaseRepository;
import dhu.cst.zjm.encryptmvp.api.repository.Repository;
import dhu.cst.zjm.encryptmvp.injector.scope.PerApplication;
import dhu.cst.zjm.encryptmvp.api.interceptor.LoadCookiesInterceptor;
import dhu.cst.zjm.encryptmvp.api.interceptor.SaveCookiesInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zjm on 2017/2/23.
 */
@Module
public class NetworkModule {
    private final MyApplication myApplication;

    public NetworkModule(MyApplication myApplication) {
        this.myApplication = myApplication;
    }


    @Provides
    @PerApplication
    public OkHttpClient provideOkHttpClient() {
        //用于记录应用中的网络请求的信息的拦截器
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                .writeTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                .addInterceptor(new CacheInterceptor())
                .addInterceptor(new LoadCookiesInterceptor(myApplication))
                .addInterceptor(new SaveCookiesInterceptor(myApplication))
                .build();

        OkHttpClient newOkHttpClient = okHttpClient
                .newBuilder()
                .addInterceptor(httpLoggingInterceptor)
                .build();
        return newOkHttpClient;
    }

    @Provides
    @PerApplication
    public Retrofit provideRetrofit(OkHttpClient client) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.BASEHTTP + BaseUrl.BASEIP + BaseUrl.BASEPORT)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }

    @Provides
    @PerApplication
    public BaseRepository provideBaseRepository(Retrofit retrofit) {
        return new Repository(retrofit);
    }

}
