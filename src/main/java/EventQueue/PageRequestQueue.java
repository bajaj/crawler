package EventQueue;

import java.net.URL;
import java.util.*;
import java.util.function.Predicate;

/**
 * Created by bajaj on 12/03/17.
 */
public class PageRequestQueue {

    private Set<String> urlInProcessingQueue;
    private Queue<URL> pageRequestQueue;

    public PageRequestQueue(){
        urlInProcessingQueue = new HashSet<>();
        pageRequestQueue = new ArrayDeque<>();
    }

    public boolean isEmpty(){
        return pageRequestQueue.isEmpty();
    }

    public URL remove(){
        URL url = pageRequestQueue.remove();
        urlInProcessingQueue.remove(url.toString());
        return url;
    }

    public boolean addAll(Collection<URL> urlList){
        urlList.forEach(url -> urlInProcessingQueue.add(url.toString()));
        return pageRequestQueue.addAll(urlList);
    }

    public boolean add(URL url){
        return addAll(Collections.singletonList(url));
    }

    public Predicate<URL> isAlreadyInQueue(){
        return url -> !urlInProcessingQueue.contains(url.toString());
    }
}
