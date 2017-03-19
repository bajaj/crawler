package crawlerDownloder;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;

import java.net.URL;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;


/**
 * Created by bajaj on 19/03/17.
 */
public class PageExtractorTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(18089);

    @Test
    public void testPageAssetsAndLinkCount() throws Exception {
        stubFor(get(urlEqualTo("/samplePage.html")).willReturn(
                aResponse().withStatus(200).withHeader("Content-Type", "text/html").
                        withBodyFile("samplePage.html")));

        String url = "http://localhost:18089/samplePage.html";
        PageExtractor pageExtractor = new PageExtractor();
        Optional<Page> page = pageExtractor.execute(new URL(url));

        assertEquals("Page should present", true, page.isPresent() ? true : false);
        assertEquals("Number of assets", 4, page.get().getAssets().size());
        assertEquals("Number of links", 3, page.get().getChildren().size());
    }

    @Test
    public void testPageTimeoutOfTenSeconds() throws Exception {
        stubFor(get(urlEqualTo("/samplePage.html")).willReturn(
                aResponse().withStatus(200).withHeader("Content-Type", "text/html").
                        withBodyFile("samplePage.html").withFixedDelay(20000)));

        String url = "http://localhost:18089/samplePage.html";
        PageExtractor pageExtractor = new PageExtractor();
        Optional<Page> page = pageExtractor.execute(new URL(url));

        assertEquals("Page should be absent", false, page.isPresent() ? true : false);
    }

}