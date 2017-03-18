package EventQueue;

import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by bajaj on 12/03/17.
 */
public class PageRequestQueue {

    private Set<URL> urlInProcessingQueue;
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
        urlInProcessingQueue.remove(url);
        return url;
    }

    public boolean addAll(Collection<URL> urlList){
        Collection<URL> urlNotInQueue = urlList.stream().filter(isAlreadyInQueue()).collect(Collectors.toSet());
        urlNotInQueue.forEach(url -> urlInProcessingQueue.add(url));
        return pageRequestQueue.addAll(urlNotInQueue);
    }

    public boolean add(URL url){
        return addAll(Collections.singletonList(url));
    }

     private Predicate<URL> isAlreadyInQueue(){
        return url -> !urlInProcessingQueue.contains(url);
    }
}
