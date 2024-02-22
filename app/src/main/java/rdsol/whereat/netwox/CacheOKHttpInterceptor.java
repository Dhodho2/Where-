package rdsol.whereat.netwox;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Response;

public class CacheOKHttpInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept ( @NotNull Chain chain ) throws IOException {
        Response response = chain.proceed( chain.request() );

        CacheControl builder  = new CacheControl.Builder().maxAge( 111, TimeUnit.DAYS ).maxStale( 365,TimeUnit.DAYS ).onlyIfCached().build();


        return response.newBuilder().removeHeader( "Pragma" ).removeHeader( "Cache-Control" ).header( "Cache-Control",builder.toString() ).build() ;
    }
}
