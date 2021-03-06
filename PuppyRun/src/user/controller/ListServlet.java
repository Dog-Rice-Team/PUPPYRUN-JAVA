package user.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import matching.model.service.MatchingService;
import matching.model.vo.Matching;
import matching.model.vo.MatchingPage;
import notice.model.service.NoticeService;
import notice.model.vo.Notice;
import notice.model.vo.NoticePage;
import user.model.service.UserService;
import user.model.vo.User;
import user.model.vo.UserPage;

@WebServlet("/user/list")
public class ListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ListServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<Notice> noticeList = (ArrayList<Notice>)request.getAttribute("noticeList");
		ArrayList<Matching> matchingList = (ArrayList<Matching>)request.getAttribute("matchingList");
		
		// 유저 검색하기
		String userChoice = request.getParameter("userChoice");
		String search = request.getParameter("searchKeyword");
		System.out.println(userChoice);
		
		// 회원 전체정보 가져오기
		int currentPage = 0;
		if(request.getParameter("currentPage") == null) {
			currentPage = 1;
		} else {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		String dogCheck = request.getParameter("dogCheck");
		UserPage up = new UserService().selectAllUserList(currentPage);
		
		if(userChoice == null && noticeList == null && matchingList == null) {

		// 전체 보기일 경우
			if(dogCheck.equals("all")) {
				ArrayList<User> uList = up.getuList();
				request.setAttribute("uList", uList);
			} else {
				ArrayList<User> uList = new UserService().adminDogCheckList(dogCheck);
				request.setAttribute("uList", uList);
			}
		}else if (userChoice != null && noticeList == null && matchingList == null) {
			if(userChoice.equals("userId")) {
				userChoice = "USER_ID";
			}else {
				userChoice = "USER_NAME";
			}
			UserPage userPage = new UserService().selectSearchUserList(search, userChoice);
			ArrayList<User> userList = userPage.getuList();
			
			if(!userList.isEmpty()) {
				request.setAttribute("uList", userList);
			}
		}
		
		
		// 퍼피런 이야기 전체 정보 가져오기
		NoticePage pd = new NoticeService().selectAllNotice(currentPage);
		ArrayList<Notice> nList = pd.getnList();
		
		// 산책짝꿍 전체 정보 가져오기
		MatchingPage np = new MatchingService().printAllMatching(currentPage);
		ArrayList<Matching> mList = np.getmList();
		
		String pageNavi = up.getPageNavi();
		
		System.out.println(noticeList);
		
		// 멍멍이야기
		if(noticeList == null) {
			request.setAttribute("nList", nList);
		}else {
			request.setAttribute("nList", noticeList);
		}
		
		// 산책짝꿍
		if(matchingList == null) {
			System.out.println("1번");
			request.setAttribute("mList", mList);
		}else {
			System.out.println("2번");
			request.setAttribute("mList", matchingList);
		}
		request.setAttribute("pageNavi", pageNavi);
		request.getRequestDispatcher("/WEB-INF/views/user/myInfo-m.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
