package CrawlerManager;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import crawlerDownloder.Page;
import crawlerDownloder.PageExtractor;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.URL;
import java.util.Collections;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;


/**
 * Created by bajaj on 19/03/17.
 */
public class CrawlerTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(18089);

    @Mock
    UrlCrawlRule urlCrawlRule;

    @Mock
    SiteMapProcessor siteMapProcessor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testBasicFunctionalityOfCrawler() throws Exception {
        stubFor(get(urlEqualTo("/")).willReturn(
                aResponse().withStatus(200).withHeader("Content-Type", "text/html").
                        withBodyFile("crawlPageLanding.html")));

        stubFor(get(urlEqualTo("/blogs/")).willReturn(
                aResponse().withStatus(200).withHeader("Content-Type", "text/html").
                        withBodyFile("crawlPageBlog.html")));

        stubFor(get(urlEqualTo("/stories/")).willReturn(
                aResponse().withStatus(200).withHeader("Content-Type", "text/html").
                        withBodyFile("crawlPageStories.html")));

        when(urlCrawlRule.isAllowed(anyString())).thenReturn(true);
        when(siteMapProcessor.getSiteMapUrls(any())).thenReturn(Collections.emptyList());

        PageExtractor pageExtractor = new PageExtractor();

        String url = "http://localhost:18089/";

        Crawler crawler = new Crawler(new URL(url), siteMapProcessor, urlCrawlRule, pageExtractor);

        List<Page> pageList = crawler.startCrawling(100);

        assertEquals("Number of pages scrolled should be 3", 3, pageList.size());

        assertEquals("First page scrolled contents", 4, pageList.get(0).getAssets().size());

        Page blogPage = null;
        Page storiesPage = null;
        for(Page page: pageList) {
            if(page.getUrl().equals(new URL("http://localhost:18089/blogs/")))
                blogPage = page;
            if(page.getUrl().equals(new URL("http://localhost:18089/stories/")))
                storiesPage = page;
        }

        assertEquals("Blog page scrolled contents", 6, blogPage.getAssets().size());
        assertEquals("Stories page scrolled contents", 8, storiesPage.getAssets().size());
    }

}