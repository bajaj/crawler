package CrawlerManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import crawlerDownloder.Page;
import crawlerDownloder.PageExtractor;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by bajaj on 12/03/17.
 */
public class Start {

    public static void main(String[] args) throws MalformedURLException, JsonProcessingException {
        URL seedUrl = new URL(args[0]);

        Crawler crawler = new Crawler(seedUrl,
                new SiteMapProcessor(),
                new UrlCrawlRule(seedUrl.toString(),
                        HttpClientBuilder.create().build()),
                new PageExtractor());

        List<Page> pageList = crawler.startCrawling(Long.valueOf(args[1]));

        writeToFile(pageList);
        System.out.println("No of pages scrolled " + pageList.size());
    }

    private static void writeToFile(List<Page> pageList){
        PrintWriter writer = null;
        try{
            final ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            writer = new PrintWriter("result.txt", "UTF-8");
            writer.println(mapper.writeValueAsString(pageList));
            System.out.println(mapper.writeValueAsString(pageList));
        } catch (IOException e) {
            System.out.println("Not able to write result to file");
        }
        finally {
            if(null !=writer){
                writer.close();
            }
        }
    }

}
