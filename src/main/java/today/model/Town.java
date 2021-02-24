package sequence.model;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Town {

    long id;
    String name;
    String townUri;
    int count;

    List<Prospect> prospects;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTownUri() {
        return townUri;
    }

    public void setTownUri(String townUri) {
        this.townUri = townUri;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCountZero(){
        return NumberFormat.getInstance(Locale.US).format(count);
    }

    public List<Prospect> getLocations() {
        return prospects;
    }

    public void setLocations(List<Prospect> prospects) {
        this.prospects = prospects;
    }
}
