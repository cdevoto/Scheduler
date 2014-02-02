package com.compuware.ruxit.synthetic.dispatcher;

import java.io.IOException;
import java.util.Queue;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "testDispatch", 
            urlPatterns = { "/dispatch/poll" }, 
            asyncSupported = true)
public class TestDispatchServlet extends HttpServlet {


	private static final long serialVersionUID = 1L;
	
	private TestDispatchEventListener eventListener = new TestDispatchEventListener();
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
        AsyncContext asyncContext = request.startAsync(request, response);
        asyncContext.addListener(eventListener);
        asyncContext.setTimeout(0);
        ServletContext appScope = request.getServletContext();
        ((Queue<AsyncContext>)appScope.getAttribute("dispatchJobQueue")).add(asyncContext);
	}
}
