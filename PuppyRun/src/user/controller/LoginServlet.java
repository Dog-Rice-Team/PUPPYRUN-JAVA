package user.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import user.model.service.UserService;
import user.model.vo.User;

@WebServlet("/user/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public LoginServlet() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userId = request.getParameter("userId");
		String userPw = request.getParameter("userPw");
		
		User user = new UserService().selectOneUser(userId, userPw);
		
		if(user != null && userId.equals(user.getUserId()) && userPw.equals(user.getUserPw())) {
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			
			// 성공
			response.sendRedirect("/index.jsp");
		} else {
			// 실패
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script> alert('아이디 혹은 비밀번호가 잘못되었습니다'); location.href='/login.jsp'; </script>");
			out.flush();
//			request.getRequestDispatcher("/login.jsp").forward(request, response);

		}
	}

}
