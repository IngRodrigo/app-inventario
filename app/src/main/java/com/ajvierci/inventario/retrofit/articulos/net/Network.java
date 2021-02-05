package com.ajvierci.inventario.retrofit.articulos.net;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class Network {

    private static Network instance;
    private Retrofit retrofit;
    private OkHttpClient client;

    private final static String BASE_URL = "http://ajvapp-desa.ajvierci.com.py:8088/";
    private static Strategy strategy = new AnnotationStrategy();
    private static Serializer serializer = new Persister(strategy);


    static {
        instance = new Network();
    }

    private Network() {
    }

    private static OkHttpClient getClient() {
        if (instance.client != null)
            return instance.client;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        instance.client = new OkHttpClient.Builder()

                .addInterceptor(interceptor)
                .build();
        return instance.client;
    }

    // 构建一个Retrofit
    private static Retrofit getRetrofit() {
        if (instance.retrofit != null)
            return instance.retrofit;

        // OK Client
        OkHttpClient client = getClient();

        // Retrofit
        Retrofit.Builder builder = new Retrofit.Builder();


        instance.retrofit = builder.baseUrl(BASE_URL)
                // 设置client
                .client(client)
                // 设置Json解析器
                .addConverterFactory(SimpleXmlConverterFactory.create(serializer))
                .build();

        return instance.retrofit;
    }


    public static RemoteService remote() {
        return Network.getRetrofit().create(RemoteService.class);
    }

}