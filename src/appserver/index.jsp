<%@page contentType="text/html;charset=utf-8"%>
<%@include file="/include/common.jsp"%>
<%@include file="/taglib.jsp"%>

<c:if test="${account==null}">
    alert(1111111111111);
	<%--<%response.sendRedirect("login.jsp");%>--%>
    <%request.getRequestDispatcher("/login.jsp").forward(request, response);%>
</c:if>
<c:if test="${account!=null}">
    <input type="hidden" value="${account.name==null?account.userName:account.name}" id="userName.account.info">
</c:if>

<html>
    <head>
        <title>管理中心</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="pragma" content="no-cache" />
        <meta http-equiv="cache-control" content="no-cache" />
        <meta http-equiv="expires" content="0" />
        <META http-equiv="x-ua-compatible" content="ie=EmulateIE6" />

        <script type="text/javascript" language="Javascript" src="<c:url value="/js/index.js"/>"></script>

    </head>
    <body>
    </body>
</html>