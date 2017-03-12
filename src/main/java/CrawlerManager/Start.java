package CrawlerManager;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by bajaj on 12/03/17.
 */
public class Start {

    public static void main(String[] args) throws MalformedURLException {

        //String seedUrl  = args[0];

        URL url = new URL("https://gocardless.com/");

        URL url2 = new URL("https://gocardless.com/");

        System.out.println(url.equals(url2));


    }
}
