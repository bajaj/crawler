package CrawlerManager;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;


/**
 * Created by bajaj on 19/03/17.
 */
public class SiteMapProcessorTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(18089);

    @Before
    public void init(){
        stubFor(get(urlEqualTo("/sitemap.xml")).willReturn(
                aResponse().withStatus(200).withHeader("Content-Type", "application/xml").
                        withBodyFile("testSiteMap.xml")));
    }

    @Test
    public void testSiteMapUrl() throws Exception {
        String url = "http://localhost:18089/";
        SiteMapProcessor siteMapProcessor = new SiteMapProcessor();
        Collection<URL> urlCollection =
                siteMapProcessor.getSiteMapUrls(new URL(url));

        assertEquals("Size of sitemap url", 2, urlCollection.size());
        assertEquals("first sitemap url match", "http://localhost:18089/en-ie/", ((ArrayList)urlCollection).get(0).toString());
        assertEquals("second sitemap url match", "http://localhost:18089/stories/", ((ArrayList)urlCollection).get(1).toString());
    }
}