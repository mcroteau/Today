package today.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:application.properties")
public class UiService {

    @Value("${business.name}")
    String businessName;

    @Value("${business.email}")
    String businessEmail;

    public String getBusinessName(){
        return this.businessName;
    }

    public String getBusinessEmail(){
        return this.businessEmail;
    }
}
