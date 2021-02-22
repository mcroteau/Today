package sequence.model.markup;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name="urlset")
public class UrlSet {

    List<Url> url;

    public List<Url> getUrl() {
        return url;
    }

    @XmlElement
    public void setUrl(List<Url> url) {
        this.url = url;
    }
}
