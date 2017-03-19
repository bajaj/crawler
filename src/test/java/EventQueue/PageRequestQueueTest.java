package EventQueue;

import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertEquals;


/**
 * Created by bajaj on 20/03/17.
 */
public class PageRequestQueueTest {

    @Test
    public void testIsEmpty() throws Exception {
        PageRequestQueue pageRequestQueue = new PageRequestQueue();
        assertEquals("queue should be empty initially", true, pageRequestQueue.isEmpty());

        pageRequestQueue.add(new URL("https://gocardless.com/"));
        assertEquals("queue should not be empty after adding", false, pageRequestQueue.isEmpty());

        pageRequestQueue.remove();
        assertEquals("queue should be empty after remove", true, pageRequestQueue.isEmpty());
    }

    @Test
    public void testRemove() throws Exception {
        PageRequestQueue pageRequestQueue = new PageRequestQueue();
        URL url = new URL("https://gocardless.com/");
        pageRequestQueue.add(url);
        assertEquals("Returned object from remove call", url, pageRequestQueue.remove());
    }

    @Test
    public void testAddAll() throws Exception {
        PageRequestQueue pageRequestQueue = new PageRequestQueue();
        URL url1 = new URL("https://gocardless.com/");
        pageRequestQueue.add(url1);

        URL url2 = new URL("https://gocardless.com/support/");
        pageRequestQueue.add(url2);

        assertEquals("Returned object from 1st remove call", url1, pageRequestQueue.remove());
        assertEquals("Returned object from 2nd remove call", url2, pageRequestQueue.remove());
    }

    @Test
    public void testAdd() throws Exception {
        PageRequestQueue pageRequestQueue = new PageRequestQueue();
        URL url1 = new URL("https://gocardless.com/");
        pageRequestQueue.add(url1);
        assertEquals("Returned object from 1st remove call", url1, pageRequestQueue.remove());
    }

}