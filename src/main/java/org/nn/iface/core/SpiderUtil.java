package org.nn.iface.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.buf.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.nn.iface.dao.StarDao;
import org.nn.iface.domain.Star;
import org.nn.iface.dto.StarDto;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpiderUtil {

    public static List<Star> getWebPage() {
        List<Star> stars = new ArrayList<>();
        for(int k = 170; k < 180; k++) {
            System.out.println("=======>" + k);
            String url = "https://baike.baidu.com/api/starflower/starflowerstarlist?weekType=thisWeek&rankType=all&page=" + k;
            Document html = null;
            try {
                html = Jsoup.connect(url).ignoreContentType(true).userAgent("Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.15)").timeout(5000).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String msg = html.childNode(0).childNode(1).childNode(0).toString();
            String str[] = msg.split("\"list\":");
            String str2[] = str[1].split(",\"count\"");
            String res = str2[0];
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            try {
                List<StarDto> list = mapper.readValue(res, new TypeReference<List<StarDto>>() {});
                for (int i = 0; i < list.size(); i++) {
                    Star star = new Star();
                    String starPage = SpiderUtil.getByUrl("https://baike.baidu.com" + list.get(i).getUrl(), "utf-8");
                    Document doc = Jsoup.parse(starPage);
                    String intro = doc.childNode(2).childNode(0).childNode(7).attributes().get("content");
                    String name = list.get(i).getName();
                    String pic = "";
                    if(doc.toString().split("background-image: url\\(\'").length >= 2) {
                        pic = doc.toString().split("background-image: url\\(\'")[1].split("'\\)")[0];
                    } else if(doc.toString().split("<div class=\"summary-pic\">").length >= 2){
                        pic = doc.toString().split("<div class=\"summary-pic\">")[1].split("<img src=\"")[1].split("\"")[0];
                    }
                    String[] works = {};
                    if(doc.toString().split("<div class=\"viewport\" id=\"slider_works\">").length >= 2) {
                        works = doc.toString().split("<div class=\"viewport\" id=\"slider_works\">")[1].split("<ul class=\"slider maqueeCanvas\">")[1].split("</ul>")[0].split("title=\"");
                    }
                    List<String> workList = new ArrayList<>();
                    for(int j = 1; j < works.length; j++) {
                        workList.add(works[j].split("\"")[0]);
                    }
                    String resWorks;
                    resWorks = StringUtils.join(workList, ',');
                    star.setIntroduce(intro);
                    star.setName(name);
                    star.setWorks(resWorks);
                    star.setPic(pic);

                    stars.add(star);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stars;
    }

    public static void main(String[] args) {
        SpiderUtil.getWebPage();
    }

    public static String getByUrl(final String url,final String charset){
    	/*RequestConfig defaultRequestConfig = RequestConfig.custom()
				  .setConnectTimeout(5000)
				  .setConnectionRequestTimeout(5000)
				  .build();*/

        //CloseableHttpClient httpclient = HttpClients.custom().setMaxConnTotal(800).setMaxConnPerRoute(800).setDefaultRequestConfig(defaultRequestConfig).build();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(url);
            //System.out.println("executing request " + httpget.getURI());
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    //System.out.println("========responseStatusCode:"+status + "  "+url);
                    if (status == 200) {
                        HttpEntity entity = response.getEntity();
                        if(entity == null){
                            return null;
                        }else{
                            String content = EntityUtils.toString(entity);
                            if(charset != null){
                                content = new String(content.getBytes("ISO-8859-1"),charset);
                            }
                            return content;
                        }
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };
            String responseBody = httpclient.execute(httpget, responseHandler);

            return responseBody;

        } catch (ClientProtocolException e) {
            return getByUrl(url,charset);
        } catch (IOException e) {
            return getByUrl(url,charset);
        } finally {
        }
    }
}
