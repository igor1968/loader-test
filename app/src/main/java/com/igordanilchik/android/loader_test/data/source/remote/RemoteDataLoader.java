package com.igordanilchik.android.loader_test.data.source.remote;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.igordanilchik.android.loader_test.BuildConfig;
import com.igordanilchik.android.loader_test.data.Catalogue;
import com.igordanilchik.android.loader_test.data.Shop;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class RemoteDataLoader extends AsyncTaskLoader<Shop> {

    private static final String LOG_TAG = RemoteDataLoader.class.getSimpleName();

    private static final String API_BASE_URL = "http://ufa.farfor.ru";
    private static final String API_KEY = "ukAXxeJYZN";

    @Nullable
    private Shop data = null;

    public RemoteDataLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (data != null) {
            deliverResult(data);
        } else {
            forceLoad();
        }
    }

    @Override
    public Shop loadInBackground() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            httpClient.addInterceptor(logging);
        }

        httpClient.addNetworkInterceptor(chain -> {
            okhttp3.Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(httpClient.build())
                .build();

        ClientApi client = retrofit.create(ClientApi.class);
        Call<Catalogue> catalogue = client.loadCatalogue(API_KEY);
        Response<Catalogue> response = null;
        try {
            response = catalogue.execute();
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Network error: ", e);
        }

        Shop result = null;
        if (response != null && response.isSuccessful()) {
            result = response.body().getShop();
        }
        return result;
    }

    @Override
    public void deliverResult(Shop data) {
        this.data = data;
        super.deliverResult(data);
    }

    private static class ProgressResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        private final ProgressListener progressListener;
        private BufferedSource bufferedSource;

        ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override public long contentLength() {
            return responseBody.contentLength();
        }

        @Override public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    if (responseBody.contentLength() > 0) {
                        progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    } else {
                        progressListener.update(totalBytesRead, bytesRead == -1);
                    }
                    return bytesRead;
                }
            };
        }
    }

    interface ProgressListener {
        //Known content length
        void update(long bytesRead, long contentLength, boolean done);
        //Unknown content length
        void update(long bytesRead, boolean done);
    }

    private final ProgressListener progressListener = new ProgressListener() {
        @Override
        public void update(long bytesRead, long contentLength, boolean done) {
            update(bytesRead, done);
            Log.d(LOG_TAG, "Content length: " + contentLength);
            Log.d(LOG_TAG, String.format("%d%% done\n", (100 * bytesRead) / contentLength));
        }

        @Override
        public void update(long bytesRead, boolean done) {
            Log.d(LOG_TAG, "Bytes read: " + bytesRead);
            Log.d(LOG_TAG, "Done: " + done);
        }
    };
}
