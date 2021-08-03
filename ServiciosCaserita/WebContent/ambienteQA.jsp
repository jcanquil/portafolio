<%@page contentType="text/html" pageEncoding="UTF-8" import="java.net.*, java.io.*,java.util.Properties"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><%@page
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<title>ambienteQA</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
<%
   URL url = application.getResource("/home/ServiciosCaserita/properties/config.properties");
   InputStream in = url.openStream();
   Properties p = new Properties();  
   p.load(in);
   out.println(p.getProperty("mi.propiedad"));
%>

</body>
</html>