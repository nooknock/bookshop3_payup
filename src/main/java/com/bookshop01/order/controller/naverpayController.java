package com.bookshop01.order.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bookshop01.order.service.ApiService01;

@Controller
public class naverpayController {

	@Autowired
	private ApiService01 apiService01;
	
	@RequestMapping(value = "/test/naverPay.do") //포스트 방식 지우고 responseCode 바꿔서  /test/kakaoPay.do 주소창에서 직접 들어가면 실패창 띄우기 가능 
	public ModelAndView kakaoPay(@RequestParam Map<String, String> map) throws Exception {
		ModelAndView mav=new ModelAndView();
		
		//주문 DB에 저장....
		
		System.out.println(map.toString());
		//{ordr_idxx=20230502101432KK0630, good_name=����ũ����Ʈ ������ �����ϱ�, good_mny=100, buyr_name=�׽�Ʈ�̸�, site_cd=A8QOB, req_tx=pay, pay_method=100000000000, currency=410, kakaopay_direct=Y, module_type=01, ordr_chk=20230502101432KK0630|100, param_opt_1=, param_opt_2=, param_opt_3=, res_cd=0000, res_msg=����, enc_info=4mIWIkNvws7XZDQgSTv2Weyg-AY9ac.XpNz1bZP9kN8DvS0MJyXOZn6-EnAjP8459R2EuWI1lVMYJpINy2VaAMKv4Q-6Jpx4lMmdMGLNBdsTe-oZFTzKC0AVeso45p.2ZoMcddzislmB4n2lNNRWCdeU1wDW8I8Z00KH9S-Ge4kKGvqgf6lwhYb7t5jgKiuqqRcL737Cwnr__, enc_data=0Ct7W3mclv6O2B0g0Vz6GBs9DjuaVbvXSikBpDDZBn0fyQxaxVF.OLinBW2BmOV6gQaOojvYMgBNXPvOhNR4lNr1Y8GPKwwOXwy24dvGIHokGjsoY2Kk4q3NO.2aGzyNgZCUQ3dU8VD-i3wzfXMkrq8wNaXqtAUz1L14RmW0tGfcP3gi5SIuQdkHUj-8Yf35jvj5Wy-tlYWlKvgLu3mtbgnx1X7eBWJOkJP.9-i5qU8wyXwMaxSTwTYQJj9ZsYKOM.W00H8tIQqY6Hxk0aCgAswE-Hoayuk4g2ZhhkOsB63zF.QnpBOaTEVBYMl7lbZQl6qPQkXp.fl9iSb1UVlq-64DiIjFsnVyBMM8hKenXJU26k67DAwICqd2HpGaCutZGUhJaLH4350T9cn2I3zJdc4rnBMKDSmlxHTwXKFvrXhzCT9s03U.oZYpkCCl4cLUZkMFhuNRwLrmiVo2qRbt-bOy.dEt1pb6DHG3B7lmY03pqJhLJRGMz-vOId6POkopBZasUJc-ZvTkSDUZQGptfaNnBzbbonao0Y9GgF89U97NDEAS7WRkCgYUQ3RZbZ-OmTLgRipS1xwxFyK57XNTOXSNG.w6blapXz2zyBlSIt5X67QEekmPeGYTBn1-UZ5o8dlV4Y9NIRoFpcWmGe72ayuULCCeXwxDG9hnK-ATdKncK91orx7fAbuLX2Y6wJlcPDNLgSHiOtVlAIF2Xr3p31X__, ret_pay_method=CARD, tran_cd=00100000, use_pay_method=100000000000, card_pay_method=KAKAO_MONEY}
		
		//4. 인증데이터로 결제 요청하기 (rest api 사용)
		Map<String,String> dataMap=new HashMap<String,String>();
		Map<String,Object> resultMap=new HashMap<String, Object>();
		
		
		String merchantId="";
		String url="";
		String res_cd="";
		String enc_info="";
		String enc_data="";		
		String ordr_idxx="";
		String card_pay_method="";
		
		
		merchantId="himedia";
		res_cd=map.get("res_cd");
		enc_info=map.get("enc_info");
		enc_data=map.get("enc_data");
		ordr_idxx=map.get("ordr_idxx");
		card_pay_method=map.get("card_pay_method");
		
		url="https://api.testpayup.co.kr/ep/api/naver/"+merchantId+"/pay";
		
		dataMap.put("merchantId", merchantId);
		dataMap.put("res_cd", res_cd);
		dataMap.put("enc_info", enc_info);
		dataMap.put("enc_data", enc_data);
		dataMap.put("ordr_idxx", ordr_idxx);
		dataMap.put("ordr_idxx", ordr_idxx);

		
		System.out.println("데이터맵:"+dataMap.toString());
		
		resultMap=apiService01.restApi(dataMap, url); //맵을 받으면 json으로 변환하고 결과를 map으로 변환해서 리턴
//		if(card_pay_method.equals("NAVERPAY_POINT")) { //type이 포인트로 안돼는 거는 naverController에서 payType을 paytype으로 오타내서 생긴 문제였음
//			resultMap.put("type","NAVER_POINT");
//		}
		
		//테스트 데이터(위에 resultMap막고 직접 임의로 쳐서 넣기)
//		resultMap.put("responseCode","0000");
////		resultMap.put("type","NAVER_POINT");
//		resultMap.put("type","NAVER_CARD");
//		resultMap.put("authDateTime","202305021207");
//		resultMap.put("amount","100원");
//		resultMap.put("cashReceipt","현금영수증발급");
//		resultMap.put("authNumber","24164821");
//		resultMap.put("cardName","국민카드");
//		resultMap.put("cardNo","1234********4564");
//		resultMap.put("quota","일시불");
//		resultMap.put("fail","잘못된 접근입니다.");
		
		
		
		//JSON은 단순 스트림 {"":"", "":"", ....}
		
		System.out.println("네이버페이 승인 응답 데이터 = "+resultMap.toString());
		
		//view 설정
		//승인 성공 or 실패
				String responseCode = (String)resultMap.get("responseCode"); //테스트중

				// "0000" == responseCode (X) 
				if("0000".equals(responseCode)) {
					//성공
					//페이지설정
					mav.setViewName("/naver/naverResult");
					
					//화면에서 보여줄 값들 mav에 넣기 얘는 하나씩
//					mav.addObject("authDateTime", resultMap.get("authDateTime"));//승인일시
//					mav.addObject("type", resultMap.get("type"));//결제타입
					
					//통째로 화면으로 보냄
					mav.addObject("resultMap",resultMap); //mav로 attribute같이 보낸거라 session.setAttribute 필요없음
				}else {
					//실패
					//페이지설정
					mav.setViewName("/naver/naverResultFail");
					mav.addObject("resultMap",resultMap);
					
//					session.setAttribute("returnMap", returnMap);
//					mav.setViewName("/orderFail");//이거로 뷰네임 설정한거는 인터셉터를 거치지 않기 때문에 .do를 붙이면 타일즈에서도 붙이거나 안 붙이고 타일즈에서도 안 붙여야 됨

					//mav.addObject("responseCode",resultMap.get("responseCode"));
					//mav.addObject("responseMsg",resultMap.get("responseMsg"));
					
					
				}

		
		
		return mav;
	}
}
