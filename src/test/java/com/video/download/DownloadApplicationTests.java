package com.video.download;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.video.download.common.CommonUtils;
import com.video.download.common.encrypt.Encryption;
import com.video.download.dao.IAccountService;
import com.video.download.job.CrawlerJob;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.RequestEntity;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class DownloadApplicationTests {

    @Autowired
    private IAccountService accountService;

    @Autowired
    private CrawlerJob crawlerJob;

    @Test
    void contextLoads() {
        accountService.getAllRobotAccount();
    }


    @Test
    public void testPostUrl(){
        crawlerJob.fetchPathJob();;
    }

    public static void main(String[] args) throws IOException {

        Map params = new HashMap();

        params.put("timestamp",1582347687);
        params.put("data","0EB44C7F3C20A63808D6C2237D6F1A5FC64F2074D08F8339CC26C74A171D29D99EDCE379AB7F94CF50ECF95CAD938DCD65E881DF2C0B7F71FE15185F7CA1016E4FD500A14C90C4D14967BD45E95F505688583D130E1712DEF5BB750DFE479F602F78E02A0C37A12A9D874C7478006D5867B9905ADCF46DA3974232DE8A107A11FB70307C3AE60BAA10E49B812452D61F843A095ECDBC7B0977251D853A70D5DEB6547C9800222ABE215541CFE32CF7A452C2BB444FFE97D62D4E8071");
        params.put("sign","8bfb341cb4a4e4bb80603513b88b5943");

        parserBodyMap(params);
        String str = "timestamp=1582347687&sign=8bfb341cb4a4e4bb80603513b88b5943&data=0EB44C7F3C20A63808D6C2237D6F1A5FC64F2074D08F8339CC26C74A171D29D99EDCE379AB7F94CF50ECF95CAD938DCD65E881DF2C0B7F71FE15185F7CA1016E4FD500A14C90C4D14967BD45E95F505688583D130E1712DEF5BB750DFE479F602F78E02A0C37A12A9D874C7478006D5867B9905ADCF46DA3974232DE8A107A11FB70307C3AE60BAA10E49B812452D61F843A095ECDBC7B0977251D853A70D5DEB6547C9800222ABE215541CFE32CF7A452C2BB444FFE97D62D4E8071";

        System.out.println("Result:" + sendPost("http://interface.my91apimy.com:8080/api.php",parserBodyMap(params)));
    }

    private static String parserBodyMap(Map map){
        if(map.isEmpty()){
            return "";
        }

        StringBuilder body = new StringBuilder();
        int i = 0;

        for (Object key : map.keySet()) {
            if(i != 0){
                body.append("&");
            }
            body.append(key.toString())
                    .append("=")
                    .append(map.get(key));
            i++;
        }
        System.out.println(body.toString());
        return body.toString();
    }

    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }


}
