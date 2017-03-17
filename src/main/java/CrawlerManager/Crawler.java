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
    private Set<String> allProcessedUrl;
    private List<Page> result;
    Predicate<URL> urlPredicate;

    public Crawler(URL url) throws MalformedURLException {
        this.seedUrl = url;
        this.pageRequestQueue = new PageRequestQueue();
        this.pageRequestQueue.add(seedUrl);
        this.urlCrawlRule = new UrlCrawlRule(this.seedUrl.toString());

        this.allProcessedUrl = new HashSet<>();
        this.result = new ArrayList<>();
        setUrlPredicate();
    }

    private void setUrlPredicate() {
        List<Predicate<URL>> urlFilters = new ArrayList<>();
        urlFilters.add(alreadyProccesedUrl());
        urlFilters.add(pageRequestQueue.isAlreadyInQueue());
        urlFilters.add(sameDomainUrl());
        urlFilters.add(robotFileExclusion());
        this.urlPredicate = urlFilters.stream().reduce(Predicate::and).orElse(x->true);
    }

    public void startCrawling(){
        while(!pageRequestQueue.isEmpty()){
            URL urlToBeProcessed = pageRequestQueue.remove();
            allProcessedUrl.add(urlToBeProcessed.toString());

            Optional<Page> processedPage = PageExtractor.execute(urlToBeProcessed);
            if(!processedPage.isPresent())
                continue;

            result.add(processedPage.get());

            Set<URL> nextUrlsToProcess = processedPage.get().getChildren()
                    .stream()
                    .filter(urlPredicate)
                    .collect(Collectors.toSet());
            pageRequestQueue.addAll(nextUrlsToProcess);

            System.out.println("done");
        }
    }

    private Predicate<URL> alreadyProccesedUrl(){
        return url-> !allProcessedUrl.contains(url.toString());
    }

    private Predicate<URL> sameDomainUrl(){
        return url -> url.toString().indexOf(seedUrl.toString())==0;
    }

    private Predicate<URL> robotFileExclusion(){
        return url -> urlCrawlRule.isAllowed(url.toString());
    }
}
