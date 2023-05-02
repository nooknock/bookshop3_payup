package com.bookshop01.order.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookshop01.order.service.ApiService01;

@RestController
public class naverController {
	
	@Autowired
	private ApiService01 apiService01;
	
	@RequestMapping("/test/naverOrder.do")
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
		String payType=dataMap.get("payType");
		
		String timestamp="20230501112700";
		String apiCertKey="ac805b30517f4fd08e3e80490e559f8e";
		
		String signature=apiService01.encrypt(merchantId+"|"+orderNumber+"|"+amount+"|"+apiCertKey+"|"+timestamp);
		
		String returnUrl="https://api.testpayup.co.kr/ep/api/naver/"+merchantId+"/order"; //리턴이 아니라 그냥 url
		
		map.put("merchantId", merchantId);
		map.put("orderNumber", orderNumber);
		
		map.put("amount", amount); //테스트용으로 100원 고정
		map.put("returnUrl", "naver"); //모바일에서 쓰는 데이터라 PC는 상관없다
		map.put("itemName", itemName);
		map.put("userName", userName); //테스트용이름
		map.put("timestamp", timestamp);
		map.put("signature", signature);
		map.put("userAgent", userAgent);
		if(payType.equals("네이버페이(카드)")) {
			map.put("payType", "CARD");
		}else {
			map.put("payType", "POINT");
		}
		
		System.out.println("네이버데이터map : "+map.toString());
		
		resultMap=apiService01.restApi(map, returnUrl);
		
		System.out.println("네이버페이 나가는 데이터 = "+resultMap.toString());
		//나가는 데이터 = {good_mny=100, site_cd=T0000, Ret_URL=asdasfasd, affiliaterCode=0005, buyr_name=테스트이름, ordr_idxx=20230502144516NV0769, good_name=마인크래프트 무작정 따라하기, naverpay_point_direct=N, responseCode=0000, responseMsg=성공}
		
		return resultMap;
	}
}
