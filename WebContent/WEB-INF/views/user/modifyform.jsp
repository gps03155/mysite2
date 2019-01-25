<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="<%= request.getContextPath() %>/assets/css/user.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<jsp:include page="/WEB-INF/views/includes/header.jsp"></jsp:include>
		
		<div id="content">
			<div id="user">

				<form id="join-form" name="joinForm" method="POST" action="<%= request.getContextPath() %>/user">
					<input type="hidden" name="action" value="modify"/> <!-- 보안처리 후 session에 있는 no를 받아야함  -->
					
					<label class="block-label" for="name">이름</label>
					<input id="name" name="name" type="text" value="">

					<label class="block-label" for="email">이메일</label>
					<h3>kickscar@gmail.com</h3>
					
					<label class="block-label">패스워드</label> <!-- 패스워드는 비워두는 것이 좋음 -->
					<input name="password" type="password" value="">
					
					<fieldset>
						<legend>성별</legend> <!-- 성별에 따라 checked 표시 -->
						<label>여</label> <input type="radio" name="gender" value="female" checked="checked">
						<label>남</label> <input type="radio" name="gender" value="male">
						
						 <!-- radio 그룹으로 생각 - if문으로 구분 -->
						
						<label>여</label> <input type="radio" name="gender" value="female">
						<label>남</label> <input type="radio" name="gender" value="male" checked="checked">
					</fieldset>
					
					<input type="submit" value="수정하기">
					
				</form>
			</div>
		</div>
		
		<jsp:include page="/WEB-INF/views/includes/navigation.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/views/includes/footer.jsp"></jsp:include>
	</div>
</body>
</html>