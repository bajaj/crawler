package EventQueue;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import crawlerDownloder.PageExtractor;

import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by bajaj on 12/03/17.
 */
public class PageRequestQueue {

    private static Integer MIN_THREAD = 1;
    private static Integer MAX_THREAD = 1;
    private static Long KEEP_ALIVE_TIME = 0L;

    private EventBus eventBus;

    private PageExtractor pageExtractor;

    public PageRequestQueue(PageExtractor pageExtractor){
        this.pageExtractor = pageExtractor;
    }

    public void publish(URL url){
        if(eventBus == null)
            initiateEventBus();
        eventBus.post(url);
    }

    private void initiateEventBus(){
        synchronized (this) {
            if(eventBus == null) {
                ThreadPoolExecutor executor = new ThreadPoolExecutor(MIN_THREAD, MAX_THREAD,
                        KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>());
                eventBus = new AsyncEventBus(executor);
                eventBus.register(pageExtractor);
            }
        }
    }





}
