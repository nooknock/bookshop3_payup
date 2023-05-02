<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"
	isELIgnored="false"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>



<!-- 카드일때 머니일때 다르게 만들기 -->
<c:choose>
<c:when test="${resultMap.type eq 'NAVER_POINT' }">
<br>
	<br>
	<br>
	<h1>네이버 결제 성공</h1>
	<DIV class="detail_table">
		<table>
			<TBODY>
				<TR class="dot_line">
					<TD class="fixed_join">결제 타입</TD>
					<TD>
					  	${resultMap.type }
				    </TD>
				</TR>
				<TR class="dot_line">
					<TD class="fixed_join">승인일시</TD>
					<TD>
					   ${resultMap.authDateTime}
				    </TD>
				</TR>
				<TR class="dot_line">
					<TD class="fixed_join">결제금액</TD>
					<TD>
					   ${resultMap.amount}
				    </TD>
				</TR>
				<TR class="dot_line">
					<TD class="fixed_join">현금영수증 발급대상금액</TD>
					<TD>
					   발급
				    </TD>
				</TR>
				
			</TBODY>
		</table>
	</DIV>
</c:when>
<c:otherwise>
<br>
	<br>
	<br>
	<h1>네이버 결제 성공</h1>
	<DIV class="detail_table">
		<table>
			<TBODY>
				<TR class="dot_line">
					<TD class="fixed_join">결제 타입</TD>
					<TD>
					  	${resultMap.type }
				    </TD>
				</TR>
				<TR class="dot_line">
					<TD class="fixed_join">승인일시</TD>
					<TD>
					   ${resultMap.authDateTime}
				    </TD>
				</TR>
				<TR class="dot_line">
					<TD class="fixed_join">결제금액</TD>
					<TD>
					   ${resultMap.amount}
				    </TD>
				</TR>
				<TR class="dot_line">
					<TD class="fixed_join">승인번호</TD>
					<TD>
					   ${resultMap.authNumber}
				    </TD>
				</TR>
				<TR class="dot_line">
					<TD class="fixed_join">카드 명</TD>
					<TD>
					   ${resultMap.cardName}
				    </TD>
				</TR>
				<TR class="dot_line">
					<TD class="fixed_join">카드번호 </TD>
					<TD>
					   1244********1412
				    </TD>
				</TR>
				<TR class="dot_line">
					<TD class="fixed_join">할부개월  </TD>
					<TD>
					   ${resultMap.quota }
				    </TD>
				</TR>
				
			</TBODY>
		</table>
	</DIV>
</c:otherwise>
</c:choose>

    <DIV class="clear"></DIV>
	<br>
	<br>
	<br>
	<center>
		<br>
		<br> 
		<a href="${contextPath}/main/main.do"> 
		   <IMG width="75" alt="" src="${contextPath}/resources/image/btn_shoping_continue.jpg">
		</a>
<DIV class="clear"></DIV>	
</body>
</html>