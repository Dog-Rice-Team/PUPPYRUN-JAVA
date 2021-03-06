package petdiary.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import petdiary.model.service.GoalService;
import petdiary.model.service.PetDiaryService;
import petdiary.model.vo.Goal;
import petdiary.model.vo.PetDiary;
import user.model.vo.User;

@WebServlet("/petdiary/detail")
public class PetDiaryDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PetDiaryDetailServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		String diaryId = user.getUserId();
		String year = request.getParameter("year");
	 	String month = request.getParameter("month");
	 	if(month.length() == 1) {
	 		month = "0" + month;
	 	}
	 	String date = request.getParameter("date");
	 	if(date.length() == 1) {
	 		date = "0" + date;
	 	}
	 	
	 	String diaryDate = year.substring(2, 4)+"/"+month+"/"+date;
		String goalDate = year.substring(2, 4) + month + date;
		Date today = new Date();
		SimpleDateFormat sdformat = new SimpleDateFormat("yyMMdd");
	    String todayString = sdformat.format(today);
		
	 	PetDiary petDiary = new  PetDiaryService().selectOneDiary(diaryDate, diaryId); // 하나 가져오기
	 	ArrayList<PetDiary> pList = new PetDiaryService().selectAllDiary(diaryId); // 전부 가져오기
		Goal goal = new GoalService().weekGoal(diaryId, goalDate); // 목표 가져오기
		Goal weekGoal = new GoalService().weekGoal(diaryId, todayString); // 이번주 기록 가져오기
	 	
 		request.setAttribute("petDiary", petDiary);
 		request.setAttribute("pList", pList);
		request.setAttribute("goal", goal);
		request.setAttribute("weekGoal", weekGoal);
 		
 		request.getRequestDispatcher("/WEB-INF/views/pet-diary/pet-diary.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
