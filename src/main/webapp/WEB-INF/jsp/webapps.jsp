<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head><title>LogView</title></head>
<body>
<h1>LogView - Available Webapps</h1>
<ul>
<c:forEach var="app" items="${apps}">
	<li><a href="/${app}/">${app}</a></li>
</c:forEach>
</ul>
</body>
</html>