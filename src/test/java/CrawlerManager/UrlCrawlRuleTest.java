package CrawlerManager;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by bajaj on 19/03/17.
 */
public class UrlCrawlRuleTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(18089);

    @Before
    public void init(){
        stubFor(get(urlEqualTo("/robots.txt")).willReturn(
                aResponse().withStatus(200).withHeader("Content-Type", "text/plain").withBody("# robotstxt.org/\n" +
                        "\n" +
                        "User-agent: *\n" +
                        "Disallow: /connect/\n" +
                        "Disallow: /pay/\n" +
                        "Disallow: /merchants/\n" +
                        "Disallow: /users/\n" +
                        "Disallow: /oauth/\n" +
                        "Disallow: /health_check/\n" +
                        "Disallow: /api/\n")));
    }

    @Test
    public void testAllowedUrls() throws Exception {
        String url = "http://localhost:18089/";
        UrlCrawlRule urlCrawlRule = new UrlCrawlRule(url, HttpClientBuilder.create().build());

        assertEquals("Url path allowed", urlCrawlRule.isAllowed(url+"/stories"), true);
        assertEquals("Url path allowed", urlCrawlRule.isAllowed(url+"/en-es"), true);
        assertEquals("Url path allowed", urlCrawlRule.isAllowed(url+"/blogs"), true);
    }

    @Test
    public void testNotAllowedUrls() throws Exception {
        String url = "http://localhost:18089/";
        UrlCrawlRule urlCrawlRule = new UrlCrawlRule(url, HttpClientBuilder.create().build());

        assertEquals("Url path not allowed", urlCrawlRule.isAllowed(url+"/pay/"), false);
        assertEquals("Url path not allowed", urlCrawlRule.isAllowed(url+"/merchants/"), false);
        assertEquals("Url path not allowed", urlCrawlRule.isAllowed(url+"/users/"), false);
    }

}