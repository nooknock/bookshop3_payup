package com.bookshop01.order.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bookshop01.common.base.BaseController;
import com.bookshop01.goods.vo.GoodsVO;
import com.bookshop01.member.vo.MemberVO;
import com.bookshop01.order.service.ApiService01;
import com.bookshop01.order.service.OrderService;
import com.bookshop01.order.vo.OrderVO;

@Controller("orderController")
@RequestMapping(value="/order")
public class OrderControllerImpl extends BaseController implements OrderController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderVO orderVO;
	@Autowired
	private ApiService01 apiService01;

	
	@RequestMapping(value="/orderEachGoods.do" ,method = RequestMethod.POST)
	public ModelAndView orderEachGoods(@ModelAttribute("orderVO") OrderVO _orderVO,
			                       HttpServletRequest request, HttpServletResponse response)  throws Exception{
		
		request.setCharacterEncoding("utf-8");
		HttpSession session=request.getSession();
		session=request.getSession();
		
		Boolean isLogOn=(Boolean)session.getAttribute("isLogOn");
		String action=(String)session.getAttribute("action");
		//로그인 여부 체크
		//이전에 로그인 상태인 경우는 주문과정 진행
		//로그아웃 상태인 경우 로그인 화면으로 이동
		if(isLogOn==null || isLogOn==false){
			session.setAttribute("orderInfo", _orderVO);
			session.setAttribute("action", "/order/orderEachGoods.do");
			return new ModelAndView("redirect:/member/loginForm.do");
		}else{
			 if(action!=null && action.equals("/order/orderEachGoods.do")){
				orderVO=(OrderVO)session.getAttribute("orderInfo");
				session.removeAttribute("action");
			 }else {
				 orderVO=_orderVO;
			 }
		 }
		
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		
		List myOrderList=new ArrayList<OrderVO>();
		myOrderList.add(orderVO);

		MemberVO memberInfo=(MemberVO)session.getAttribute("memberInfo");
		
		session.setAttribute("myOrderList", myOrderList);
		session.setAttribute("orderer", memberInfo);
		return mav;
	}
	
	@RequestMapping(value="/orderAllCartGoods.do" ,method = RequestMethod.POST)
	public ModelAndView orderAllCartGoods( @RequestParam("cart_goods_qty")  String[] cart_goods_qty,
			                 HttpServletRequest request, HttpServletResponse response)  throws Exception{
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		HttpSession session=request.getSession();
		Map cartMap=(Map)session.getAttribute("cartMap");
		List myOrderList=new ArrayList<OrderVO>();
		
		List<GoodsVO> myGoodsList=(List<GoodsVO>)cartMap.get("myGoodsList");
		MemberVO memberVO=(MemberVO)session.getAttribute("memberInfo");
		
		for(int i=0; i<cart_goods_qty.length;i++){
			String[] cart_goods=cart_goods_qty[i].split(":");
			for(int j = 0; j< myGoodsList.size();j++) {
				GoodsVO goodsVO = myGoodsList.get(j);
				int goods_id = goodsVO.getGoods_id();
				if(goods_id==Integer.parseInt(cart_goods[0])) {
					OrderVO _orderVO=new OrderVO();
					String goods_title=goodsVO.getGoods_title();
					int goods_sales_price=goodsVO.getGoods_sales_price();
					String goods_fileName=goodsVO.getGoods_fileName();
					_orderVO.setGoods_id(goods_id);
					_orderVO.setGoods_title(goods_title);
					_orderVO.setGoods_sales_price(goods_sales_price);
					_orderVO.setGoods_fileName(goods_fileName);
					_orderVO.setOrder_goods_qty(Integer.parseInt(cart_goods[1]));
					myOrderList.add(_orderVO);
					break;
				}
			}
		}
		session.setAttribute("myOrderList", myOrderList);
		session.setAttribute("orderer", memberVO);
		return mav;
	}	
	
	@RequestMapping(value="/payToOrderGoods.do" ,method = RequestMethod.POST)
	public ModelAndView payToOrderGoods(@RequestParam Map<String, String> receiverMap,
			                       HttpServletRequest request, HttpServletResponse response)  throws Exception{
		String viewName=(String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);
		HttpSession session=request.getSession();
		
		//결제하기
		System.out.println("테스트 확인:"+receiverMap.toString());
		//{receiver_name=홍사동, receiver_hp1=010, receiver_hp2=123, receiver_hp3=123, receiver_tel1=, receiver_tel2=, receiver_tel3=, delivery_address=우편번호:123<br>도로명 주소:123<br>[지번 주소:123]<br>123, delivery_message=, delivery_method=일반택배, gift_wrapping=no, pay_method=신용카드<Br>카드사:삼성<br>할부개월수:일시불, card_com_name=삼성, card_pay_month=일시불, pay_orderer_hp_num=해당없음, cardNo=3123, expireMonth=1231, expireYear=3231, birthday=12, cardPwd=313}

		//결제 승인 값 (필수만 넣음) (변수명은 규격서내 이름과 일치하게 작성)
		String merchantId=""; //가맹점아이디
		String orderNumber=""; //주문번호
		String cardNo=""; //카드번호
		String expireMonth=""; //유효기간
		String expireYear="";
		String birthday=""; //생년월일
		String cardPw=""; //비밀번호2자리
		String amount=""; //상품가격
		String quota=""; //할부개월
		String itemName=""; //상품이름
		String userName=""; // 구매자의 이름
		String signature=""; // 서명값
		String timestamp=""; //타임스탬프 YYYYMMDDHHMI24SS형식
		String apiCertKey=""; //api 인증키

		//값 세팅
		merchantId="himedia";
		apiCertKey="ac805b30517f4fd08e3e80490e559f8e";
		orderNumber="Test_choi1234"; //주문번호 생성
		cardNo=receiverMap.get("cardNo"); //화면에서 받은 값
		expireMonth=receiverMap.get("expireMonth"); 
		expireYear=receiverMap.get("expireYear"); 
		birthday=receiverMap.get("birthday"); 
		cardPw=receiverMap.get("cardPw"); 
		amount="1000"; 
		quota="0"; //일시불 
		itemName="책"; 
		userName=receiverMap.get("receiver_name"); 
		timestamp="20230501112700";
		
		//예시)sha256_hash({merchantId}|{orderNumber}|{amount}|{apiCertKey}|{timestamp})
		signature=apiService01.encrypt(merchantId+"|"+orderNumber+"|"+amount+"|"+apiCertKey+"|"+timestamp); //서명값 //위변조방지 암호화 값을 만든다. 해킹당해서 가격 같은 거 변경 되는 것을 막기 위해서

		//rest api를 라이브러리 써서 사용 
		// *가장 평범한 통신은 httpURLconnection 으로 하는 통신 (x)
		String url="https://api.testpayup.co.kr/v2/api/payment/"+merchantId+"/keyin2"; //통신 url은 규격서에 있다
		Map<String,String> map=new HashMap<String,String>();
		Map<String,Object> returnMap=new HashMap<String,Object>();

		//map에 다가 요청데이터값들을 넣으시면 됩니다.
		map.put("merchantId", merchantId);
		map.put("orderNumber", orderNumber);
		map.put("cardNo", cardNo);
		map.put("expireMonth", expireMonth);
		map.put("expireYear",expireYear );
		map.put("birthday", birthday); //6자리
		map.put("cardPw",cardPw );
		map.put("amount", amount);
		map.put("quota", quota);
		map.put("itemName", itemName);
		map.put("userName", userName);
		map.put("timestamp", timestamp);
		map.put("signature", signature);

		returnMap=apiService01.restApi(map, url);

		System.out.println("카드결제 승인 응답 데이터 = "+returnMap.toString());

		//카드결제 승인 응답 데이터 = {noinf=N, amount=1000, orderNumber=Test_choi1234, cardName=국민카드, authNumber=73652583, authDateTime=20230501124823, kakaoResultCode=, transactionId=20230501124823ST0321, responseCode=0000, responseMsg=정상처리}

		//응답값을 잘 받으면

		//승인 성공 or 실패
		String responseCode = (String)returnMap.get("responseCode");

		// "0000" == responseCode (X) "0000".equals(responseCode)
		if("0000".equals(responseCode)) {
			//성공
			//페이지설정
			session.setAttribute("returnMap", returnMap);
			
		}else {
			//실패
			//페이지설정
			
			session.setAttribute("returnMap", returnMap);
			mav.setViewName("/orderFail");//이거로 뷰네임 설정한거는 인터셉터를 거치지 않기 때문에 .do를 붙이면 타일즈에서도 붙이거나 안 붙이고 타일즈에서도 안 붙여야 됨

		}

		//결제하기 끝

		
		
		MemberVO memberVO=(MemberVO)session.getAttribute("orderer");
		String member_id=memberVO.getMember_id();
		String orderer_name=memberVO.getMember_name();
		String orderer_hp = memberVO.getHp1()+"-"+memberVO.getHp2()+"-"+memberVO.getHp3();
		List<OrderVO> myOrderList=(List<OrderVO>)session.getAttribute("myOrderList");
		
		for(int i=0; i<myOrderList.size();i++){
			OrderVO orderVO=(OrderVO)myOrderList.get(i);
			orderVO.setMember_id(member_id);
			orderVO.setOrderer_name(orderer_name);
			orderVO.setReceiver_name(receiverMap.get("receiver_name"));
			
			orderVO.setReceiver_hp1(receiverMap.get("receiver_hp1"));
			orderVO.setReceiver_hp2(receiverMap.get("receiver_hp2"));
			orderVO.setReceiver_hp3(receiverMap.get("receiver_hp3"));
			orderVO.setReceiver_tel1(receiverMap.get("receiver_tel1"));
			orderVO.setReceiver_tel2(receiverMap.get("receiver_tel2"));
			orderVO.setReceiver_tel3(receiverMap.get("receiver_tel3"));
			
			orderVO.setDelivery_address(receiverMap.get("delivery_address"));
			orderVO.setDelivery_message(receiverMap.get("delivery_message"));
			orderVO.setDelivery_method(receiverMap.get("delivery_method"));
			orderVO.setGift_wrapping(receiverMap.get("gift_wrapping"));
			orderVO.setPay_method(receiverMap.get("pay_method"));
			orderVO.setCard_com_name(receiverMap.get("card_com_name"));
			orderVO.setCard_pay_month(receiverMap.get("card_pay_month"));
			orderVO.setPay_orderer_hp_num(receiverMap.get("pay_orderer_hp_num"));	
			orderVO.setOrderer_hp(orderer_hp);	
			myOrderList.set(i, orderVO); //각 orderVO에 주문자 정보를 세팅한 후 다시 myOrderList에 저장한다.
		}//end for
		
	    orderService.addNewOrder(myOrderList);
		mav.addObject("myOrderInfo",receiverMap);//OrderVO로 주문결과 페이지에  주문자 정보를 표시한다.
		mav.addObject("myOrderList", myOrderList);
		return mav;
	}
	

}
