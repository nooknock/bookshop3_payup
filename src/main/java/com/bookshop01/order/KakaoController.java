package com.bookshop01.order;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookshop01.order.service.ApiService01;

@RestController
public class KakaoController {
	
	@Autowired
	private ApiService01 apiService01;
	
	@RequestMapping("/test/kakaoOrder.do")
	public Map<String,Object> kakaoOrder(@RequestParam Map<String,String> dataMap) throws Exception{
		
		System.out.println("들어오는 데이터 = "+dataMap.toString());
		
		Map<String,String> map=new HashMap<String,String>();
		Map<String,Object> resultMap=new HashMap<String, Object>();
		
		//결제 연동 하기
		String merchantId="himedia";
		String orderNumber="Test_choi1234";
		String amount="100";
		String itemName=(String)dataMap.get("itemName");
		String userName="테스트이름";
		String userAgent="WP";
		
		String timestamp="20230501112700";
		String apiCertKey="ac805b30517f4fd08e3e80490e559f8e";
		
		String signature=apiService01.encrypt(merchantId+"|"+orderNumber+"|"+amount+"|"+apiCertKey+"|"+timestamp);
		
		String returnUrl="https://api.testpayup.co.kr/ep/api/kakao/"+merchantId+"/order";
		
		map.put("merchantId", merchantId);
		map.put("orderNumber", orderNumber);
		
		map.put("amount", amount); //테스트용으로 100원 고정
		map.put("returnUrl", "asdasfasd"); //모바일에서 쓰는 데이터라 PC는 상관없다
		map.put("itemName", itemName);
		map.put("userName", userName); //테스트용이름
		map.put("timestamp", timestamp);
		map.put("signature", signature);
		map.put("userAgent", userAgent);
		
		resultMap=apiService01.restApi(map, returnUrl);
		
		System.out.println("나가는 데이터 = "+resultMap.toString());
		
		return resultMap;
	}
}
