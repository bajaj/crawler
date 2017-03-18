package CrawlerManager;

import EventQueue.PageRequestQueue;
import crawlerDownloder.Page;
import crawlerDownloder.PageExtractor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by bajaj on 18/03/17.
 */
public class Crawler {
    private URL seedUrl ;
    private PageRequestQueue pageRequestQueue;
    private UrlCrawlRule urlCrawlRule;
    private Set<URL> allProcessedUrl;
    Predicate<URL> urlPredicate;

    public Crawler(URL url) throws MalformedURLException {
        seedUrl = url;

        pageRequestQueue = new PageRequestQueue();
        pageRequestQueue.add(seedUrl);
        pageRequestQueue.addAll(SiteMapProcessor.getSiteMapUrls(seedUrl));

        urlCrawlRule = new UrlCrawlRule(seedUrl.toString());

        allProcessedUrl = new HashSet<>();
        setUrlPredicate();
    }

    public List<Page> startCrawling(long maxSecondsToCrawl){
        List<Page> pageList = new ArrayList<>();

        long startedTimeMillis = System.currentTimeMillis();

        while(!pageRequestQueue.isEmpty()){
            URL urlToBeProcessed = pageRequestQueue.remove();
            allProcessedUrl.add(urlToBeProcessed);
9
            Optional<Page> processedPage = PageExtractor.execute(urlToBeProcessed);
            if(!processedPage.isPresent())
                continue;

            pageList.add(processedPage.get());

            Set<URL> nextUrlsToProcess = processedPage.get().getChildren()
                    .stream()
                    .filter(urlPredicate)
                    .collect(Collectors.toSet());
            pageRequestQueue.addAll(nextUrlsToProcess);

            if(((System.currentTimeMillis() - startedTimeMillis)/1000l) > maxSecondsToCrawl) {
                System.out.println("Done crawling for the mentioned time");
                break;
            }
        }

        return pageList;
    }

    private void setUrlPredicate() {
        List<Predicate<URL>> urlFilters = new ArrayList<>();
        urlFilters.add(alreadyProcessedUrl());
        urlFilters.add(sameDomainUrl());
        urlFilters.add(robotFileExclusion());
        this.urlPredicate = urlFilters.stream().reduce(Predicate::and).orElse(x->true);
    }

    /**
     * Predicate for not crawling already crawled url
     * @return
     */
    private Predicate<URL> alreadyProcessedUrl(){
        return url-> !allProcessedUrl.contains(url);
    }

    /**
     * Predicate for crawling only seed domain url
     * @return
     */
    private Predicate<URL> sameDomainUrl(){
        return url -> url.getHost().equals(seedUrl.getHost());
    }

    /**
     * Not crawling url which are excluded by robot.txt file
     * @return
     */
    private Predicate<URL> robotFileExclusion(){
        return url -> urlCrawlRule.isAllowed(url.toString());
    }
}
