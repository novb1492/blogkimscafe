package com.example.blogkimscafe.service;






import java.util.HashMap;
import org.springframework.stereotype.Service;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

@Service
public class coolSmsService {
    
 
    private final String apikey="NCSFT0AZ2O3FHMAX";
    private final String APISecret="AHZNZ3IIMGSYIXFLR7HQDBYA5KPFSFCS";


    public void sendMessege() {
       
        String api_key = apikey;
        String api_secret = APISecret;
        Message coolsms = new Message(api_key, api_secret);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", "01091443409");
        params.put("from", "01091443409");
        params.put("type", "SMS");
        params.put("text", "문자 내용");
  
        try {
            coolsms.send(params);
        } catch (CoolsmsException e) {
            e.printStackTrace();
        }
       
    }
}
