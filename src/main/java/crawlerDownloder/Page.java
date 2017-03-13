package crawlerDownloder;

import java.net.URL;
import java.util.List;
import java.util.Set;

/**
 * Created by bajaj on 12/03/17.
 */
public class Page {
    private URL url;
    private List<String> assets;
    private Set<URL> children;

    public Page(URL url, List<String> assets, Set<URL> children) {
        this.url = url;
        this.assets = assets;
        this.children = children;
    }


    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
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


    public List<String> getAssets() {
        return assets;
    }

    public void setAssets(List<String> assets) {
        this.assets = assets;
    }

    public Set<URL> getChildren() {
        return children;
    }

    public void setChildren(Set<URL> children) {
        this.children = children;
    }
}
