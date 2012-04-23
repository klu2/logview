<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>LogView</title>
</head>
<body>
	<h1>LogFiles for ${app}</h1>
	<table>
		<tr>
			<th width="400" align="left">File</th>
			<th width="100" align="left">Size</th>
			<th width="200" align="left">Last Modified</th>
		</tr>
		<c:forEach var="file" items="${files}">
			<tr>
				<td><a href="/${app}/${file.name}/">${file.name}</a></td>
				<td>${file.size} bytes</td>
				<td>${file.lastModified}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>