package CrawlerManager;

import crawlercommons.robots.BaseRobotRules;
import crawlercommons.robots.SimpleRobotRules;
import crawlercommons.robots.SimpleRobotRulesParser;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Represents rules for crawling a url
 * Created by bajaj on 13/03/17.
 */
public class UrlCrawlRule {
    private String url;
    private BaseRobotRules rules;

    public UrlCrawlRule(String url){
        this.url = url;
        try {
            this.rules = getRobotRuleForUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
            rules = new SimpleRobotRules(SimpleRobotRules.RobotRulesMode.ALLOW_ALL);
        }
    }

    /**
     * Check whether the url is allowed and the path is not mentioned in the robots.txt
     * @param url
     * @return
     */
    public boolean isAllowed(String url){
        return rules.isAllowed(url);
    }

    private BaseRobotRules getRobotRuleForUrl(String  url) throws IOException {
        String USERAGENT = "test";
        URL urlObj = new URL(url);
        String hostId = urlObj.getProtocol() + "://" + urlObj.getHost()
                + (urlObj.getPort() > -1 ? ":" + urlObj.getPort() : "");

        HttpGet httpget = new HttpGet(hostId + "/robots.txt");
        HttpContext context = new BasicHttpContext();
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(httpget, context);
        if (response.getStatusLine() != null && response.getStatusLine().getStatusCode() == 404) {
            // consume entity to deallocate connection
            EntityUtils.consumeQuietly(response.getEntity());
            return new SimpleRobotRules(SimpleRobotRules.RobotRulesMode.ALLOW_ALL);
        } else {
            BufferedHttpEntity entity = new BufferedHttpEntity(response.getEntity());
            SimpleRobotRulesParser robotParser = new SimpleRobotRulesParser();
           return robotParser.parseContent(hostId, IOUtils.toByteArray(entity.getContent()),
                    "text/plain", USERAGENT);
        }
    }
}
