<%@ page import="java.lang.*" %>
<%@ page import="java.io.*" %>
<%@ page import="remotedebug.*" %>

<%
	InputStream is = new FileInputStream("D:\\Java\\Workspaces\\workspace-jee\\UnderstandingJVM\\bin\\TestClass.class");
	byte[] b = new byte[is.available()];
	is.read(b);
	is.close();
	
	out.println("<textarea style='width: 1000; height: 800'>");
	out.println(JavaClassExecuter.execute(b));
	out.println("</textarea>");
%>