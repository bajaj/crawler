package CrawlerManager;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by bajaj on 12/03/17.
 */
public class Start {

    public static void main(String[] args) throws MalformedURLException {
        URL seedUrl = new URL(args[0]);
        Crawler crawler = new Crawler(seedUrl);
        crawler.startCrawling();
    }

}
