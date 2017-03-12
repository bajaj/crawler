package crawlerDownloder;

import java.net.URL;

/**
 * Created by bajaj on 12/03/17.
 */
public class Page {
    private URL url;
    private String[] assets;
    private URL[] children;


    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String[] getAssets() {
        return assets;
    }

    public void setAssets(String[] assets) {
        this.assets = assets;
    }

    public URL[] getChildren() {
        return children;
    }

    public void setChildren(URL[] children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Page page = (Page) o;

        return url.equals(page.url);

    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }
}
