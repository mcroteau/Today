package sequence.service;

import com.plivo.api.Plivo;
import com.plivo.api.models.message.MessageCreateResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@PropertySource("classpath:application.properties")
public class PhoneService {

    @Value("${plivo.api.key}")
    private String apiKey;

    @Value("${plivo.secret.key}")
    private String secretKey;

    private static final String PLIVO_PHONE = "+18302026537";
    private static final String NOTIFY_PHONE = "+19076879557";

    public boolean validate(String phone){
        try{

            Plivo.init(apiKey, secretKey);
            MessageCreateResponse message = com.plivo.api.models.message.Message.creator(
                    PLIVO_PHONE, Collections.singletonList("+1" + phone), "Okay! Setup complete!")
                    .create();

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean send(String phones, String notification){
        try{

            Plivo.init(apiKey, secretKey);
            MessageCreateResponse message = com.plivo.api.models.message.Message.creator(
                    PLIVO_PHONE, Collections.singletonList(phones), notification)
                    .create();

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean support(String notification){
        try{

            Plivo.init(apiKey, secretKey);
            MessageCreateResponse message = com.plivo.api.models.message.Message.creator(
                    PLIVO_PHONE, Collections.singletonList(NOTIFY_PHONE), notification)
                    .create();

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

}