package dynamiccompile;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import remotedebug.HackSystem;
import util.EscapeUnescape;

public class DynamicCompileServlet extends HttpServlet {

	private static final long serialVersionUID = -7097591245540959127L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html");
		
		String className = req.getParameter("fileName");
		String code = req.getParameter("code");
		
		out.print(EscapeUnescape.escape(DynamicCompiler.run(className, code)));
		HackSystem.clearBuffer();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
	
}
