package com.example.blogkimscafe.service;



import com.example.blogkimscafe.model.reservation.BuyerInforDto;
import com.example.blogkimscafe.model.reservation.IamprotDto;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@Service
public class iamportservice {
    
    private final String imp_key="7336595505277037";
    private final String imp_secret="19412b4bca453060662162083d1ccc8ee7c53bd98a2f33faedd7ebc3e6ad4c359c36f899ebd6ddec";
    private RestTemplate restTemplate=new RestTemplate();
    private HttpHeaders headers=new HttpHeaders();
    JSONObject body=new JSONObject();

    private IamprotDto getToken() {

        headers.setContentType(MediaType.APPLICATION_JSON);
        
        JSONObject body=new JSONObject();
        body.put("imp_key", imp_key);
        body.put("imp_secret", imp_secret);
        try {  
            HttpEntity<JSONObject>entity=new HttpEntity<>(body,headers);
            IamprotDto token=restTemplate.postForObject("https://api.iamport.kr/users/getToken",entity,IamprotDto.class);
            
            System.out.println(token+" FULLtoken");
            System.out.println(token.getResponse().get("access_token")+" token");
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("gettoken에서 오류가 발생");
        }
        return null;
    }
    public boolean confrimBuyerInfor(String imp_uid) {
        IamprotDto iamprotDto=getToken();
        try {
            if(iamprotDto==null){
                throw new Exception();
            }
            headers.clear();
            headers.add("Authorization",(String) iamprotDto.getResponse().get("access_token"));
            HttpEntity<JSONObject>entity=new HttpEntity<JSONObject>(headers);

            BuyerInforDto buyerInfor =restTemplate.postForObject("https://api.iamport.kr/payments/"+imp_uid+"",entity,BuyerInforDto.class);
            System.out.println(buyerInfor+" fullinfor");
            System.out.println(buyerInfor.getResponse().get("amount")+" priceinfor");
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getBuyerInfor 검증 실패"); 
        }
        return false;
    }
    public void cancleBuy(String imp_uid) {
        try {
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("cancleBuy가 실패 했습니다 직접 환불 바랍니다");
            throw new RuntimeException("환불에 실패 했습니다 다시시도 바랍니다");
        }
    }
}