package sequence.model.markup;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlType(propOrder = { "loc", "lastmod", "priority" })
public class Url {

    String loc;
    String lastmod;
    String priority;

    public String getLoc() {
        return loc;
    }

    @XmlElement
    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getLastmod() {
        return lastmod;
    }

    @XmlElement
    public void setLastmod(String lastmod) {
        this.lastmod = lastmod;
    }

    public String getPriority() {
        return priority;
    }

    @XmlElement
    public void setPriority(String priority) {
        this.priority = priority;
    }
}
