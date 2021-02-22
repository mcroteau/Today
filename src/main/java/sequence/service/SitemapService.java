package sequence.service;

import sequence.common.Sequence;
import sequence.model.Prospect;
import sequence.model.Town;
import sequence.model.markup.Url;
import sequence.model.markup.UrlSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class SitemapService {

    @Autowired
    private ApplicationContext appCntx;

    public static final String BASE = "https://www.dynamicsgain.org/z/";
    public static final String TOWNS_SITEMAP = "sitemaps/towns.xml";
    public static final String LOCATIONS_SITEMAP = "sitemaps/locations.xml";

    public boolean writeLocations(List<Prospect> prospects) throws Exception {
        UrlSet urlSet = new UrlSet();
        List<Url> urls = new ArrayList<>();
        for(Prospect prospect : prospects){
            String loc = BASE + "prospects/" + prospect.getLocationUri();
            Url url = new Url();
            url.setLoc(loc);
            url.setLastmod(Sequence.getBing());

            url.setPriority("1.0");
            urls.add(url);
        }

        urlSet.setUrl(urls);

        JAXBContext cntx = JAXBContext.newInstance(UrlSet.class);
        Marshaller marshaller = cntx.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        marshaller.marshal(urlSet, new File(getPath(LOCATIONS_SITEMAP)));

        return true;
    }

    public boolean writeTowns(List<Town> towns) throws Exception {
        UrlSet urlSet = new UrlSet();
        List<Url> urls = new ArrayList<>();
        for(Town town: towns){
            String loc = BASE + "towns/" + town.getTownUri();
            Url url = new Url();
            url.setLoc(loc);
            url.setLastmod(Sequence.getBing());

            url.setPriority("1.0");
            urls.add(url);
        }

        urlSet.setUrl(urls);

        JAXBContext cntx = JAXBContext.newInstance(UrlSet.class);
        Marshaller marshaller = cntx.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        marshaller.marshal(urlSet, new File(getPath(TOWNS_SITEMAP)));

        return true;
    }

    private String getPath(String sitemap){
        try {
            Resource propResource = appCntx.getResource(".");
            String appPath = propResource.getURI().getPath();
            return appPath + sitemap;
        }catch(Exception ex){ex.printStackTrace();}
        return "";
    }

}
