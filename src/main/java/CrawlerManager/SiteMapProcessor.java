package CrawlerManager;

import crawlercommons.sitemaps.SiteMap;
import crawlercommons.sitemaps.SiteMapParser;
import crawlercommons.sitemaps.SiteMapURL;
import crawlercommons.sitemaps.UnknownFormatException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Created by bajaj on 19/03/17.
 */
public class SiteMapProcessor {

    /**
     * Returns all the urls present in the sitemap.xml file
     * @param url
     * @return
     */
    public Collection<URL> getSiteMapUrls(URL url){
        SiteMapParser siteMapParser= new SiteMapParser();
        SiteMap siteMap = null;
        try {
            siteMap = (SiteMap) siteMapParser.parseSiteMap(getSiteMapXmlUrl(url));
        } catch (UnknownFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(null != siteMap){
            return siteMap.getSiteMapUrls().stream().map(SiteMapURL::getUrl).collect(Collectors.toList());
        }
        else
            return Collections.emptyList();
    }
    
     private URL getSiteMapXmlUrl(URL url) throws MalformedURLException {
        if(url.toString().endsWith("/"))
            return new URL(url.toString() + "sitemap.xml");
        else
            return new URL(url.toString() + "/sitemap.xml");
    }

}
