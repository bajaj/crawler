package crawlerDownloder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by bajaj on 12/03/17.
 */

public class PageExtractor {

    private static final int pageTimeOut = 10000;

    /**
     * Gets the html from url and then returns the page object
     * @param url
     * @return
     */
    public Optional<Page> execute(URL url) {
        try {
            Document document = Jsoup.connect(url.toString()).timeout(pageTimeOut).get();
            List<String> assests = getAssets(document);
            Set<URL> links = getLinks(document);
            return Optional.of(new Page(url, assests, links));
        } catch (IOException e) {
            System.out.println("Not able to get contents of " + url.toString());
            return Optional.empty();
        }
    }

    /**
     * Returns all the assets links as string present in the document
     * @param document
     * @return
     */
    private static List<String> getAssets(Document document){
        List<String> result = new ArrayList<>();
        Elements media = document.select("[src]");
        Elements imports = document.select("link[href]");
        result.addAll(media.stream().map(x->x.attr("abs:src")).collect(Collectors.toList()));
        result.addAll(imports.stream().filter(x->x.attr("rel").equals("stylesheet")).map(x->x.attr("abs:href")).collect(Collectors.toList()));
        return result;
    }

    /**
     * Returns all the link present in the document
     * @param document
     * @return
     */
    private static Set<URL> getLinks(Document document){
        Elements links = document.select("a[href]");
        return links.stream().map(x -> {
            try {
                return new URL(x.attr("abs:href"));
            } catch (MalformedURLException e) {
                return null;
            }
        }).filter(x -> null != x)
        .collect(Collectors.toSet());
    }
}



