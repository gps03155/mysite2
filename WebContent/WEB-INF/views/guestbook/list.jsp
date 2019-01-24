<%@page import="com.douzon.mysite.vo.GuestBookVo"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%
	List<GuestBookVo> list = (List)request.getAttribute("list");
	int count = list.size() + 1;
%>

<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="<%= request.getContextPath() %>/assets/css/guestbook.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<jsp:include page="/WEB-INF/views/includes/header.jsp"></jsp:include>
		
		<div id="content">
			<div id="guestbook">
				<form action="<%= request.getContextPath() %>/guestbook" method="post">
					<input type="hidden" name="action" value="insert">
					<table>
						<tr>
							<td>이름</td><td><input type="text" name="name"></td>
							<td>비밀번호</td><td><input type="password" name="pass"></td>
						</tr>
						<tr>
							<td colspan=4><textarea name="content" id="content"></textarea></td>
						</tr>
						<tr>
							<td colspan=4 align=right><input type="submit" VALUE=" 확인 "></td>
						</tr>
					</table>
				</form>
				<ul>
					<li>
						<%
							for(GuestBookVo vo : list){
								count--;
						%>
							<table>
								<tr>
									<td><%= count %></td>
									<td><%= vo.getName() %></td>
									<td><%= vo.getMsgDate() %></td>
									<td><a href="<%= request.getContextPath() %>/guestbook?action=deleteform&no=<%= vo.getNo() %>">삭제</a></td>
								</tr>
								<tr>
									<td colspan=4><%= vo.getMessage().replace("\r\n", "<br/>") %></td>
								</tr>
							</table>
							<br>
						<%
							}
						%>
					</li>
				</ul>
			</div>
		</div>
		
		<jsp:include page="/WEB-INF/views/includes/navigation.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/views/includes/footer.jsp"></jsp:include>
	</div>
</body>
</html>