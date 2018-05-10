package ConnectServe;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 赖志林 on 2018/2/28.
 */

public class ConnectServe {
    private  HttpURLConnection httpURLConnection;
    private  URL url = null;
    private  String data ="";

    final  private  List<WorkInfo> workInfos = new ArrayList<WorkInfo>();
    public  ConnectServe(final String urlStr , final Handler mHandler){
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
                        InputStream in=httpURLConnection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String line;
                        while (null != (line = reader.readLine()))
                        {
                            data += line;
                        }
                        Message message = new Message();
                        message = mHandler.obtainMessage();
                        message.obj = data;
                        mHandler .sendMessage(message);
                        //关闭创建的流
                        in.close();
                    }else{
                    }
                } catch (Exception exception) {
                    Log.e("w", exception+"w");
                }
            }
        } .start();
    }
}
