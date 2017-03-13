package CrawlerManager;

import crawlerDownloder.Page;
import crawlerDownloder.PageExtractor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by bajaj on 12/03/17.
 */
public class Start {

    public static void main(String[] args) throws MalformedURLException {
        URL seedUrl = new URL(args[0]);

        Set<String> urlInProcessingQueue = new HashSet<>();

        Queue<URL> pageRequestQueue = new ArrayDeque<>();
        pageRequestQueue.add(seedUrl);
        urlInProcessingQueue.add(seedUrl.toString());

        Set<String> allProcessedUrl = new HashSet<>();

        List<Page> result = new ArrayList<>();



        while(!pageRequestQueue.isEmpty()){
            URL urlToBeProcessed = pageRequestQueue.remove();
            allProcessedUrl.add(urlToBeProcessed.toString());
            urlInProcessingQueue.remove(urlToBeProcessed.toString());

            Optional<Page> processedPage = PageExtractor.execute(urlToBeProcessed);
            if(!processedPage.isPresent())
                continue;

            result.add(processedPage.get());

            Set<URL> nextUrlsToProcess = processedPage.get().getChildren()
                    .stream()
                    .filter(x->!allProcessedUrl.contains(x.toString()))
                    .filter(x->!urlInProcessingQueue.contains(x.toString()))
                    .filter(x->x.toString().indexOf(seedUrl.toString())==0)
                    .collect(Collectors.toSet());

            pageRequestQueue.addAll(nextUrlsToProcess);
            nextUrlsToProcess.forEach(x->urlInProcessingQueue.add(x.toString()));

            System.out.println("done");
        }

        System.out.println("done all");

    }

}
