package ConnectServe;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 赖志林 on 2018/3/7.
 */

public class ConnectData {
    private URL url;
    private HttpURLConnection httpURLConnection;


    public ConnectData(final String urlStr , final Handler mHandler){
        new Thread() {
            @Override
            public void run() {
                try {
                    url = new URL(urlStr);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(2000);//设置连接超时时间，单位ms
                    httpURLConnection.setReadTimeout(2000);//设置读取超时时间，单位ms
                    //连接服务器
                    httpURLConnection.connect();
                    if(HttpURLConnection.HTTP_OK==httpURLConnection.getResponseCode()){
                        //得到httpURLConnection的输入流，这里面包含服务器返回来的java对象
                        OutputStream outStrm = httpURLConnection.getOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(outStrm);
                        //oos.write();
                    }else{
                        Log.e("error","错误");
                    }
                } catch (Exception exception) {
                    Log.e("w", exception+"w");
                }
            }
        } .start();
    }
}
