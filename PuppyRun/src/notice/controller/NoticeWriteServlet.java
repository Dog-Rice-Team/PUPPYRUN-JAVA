package notice.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import notice.model.service.NoticeService;
import notice.model.vo.Notice;
import photo.model.service.PhotoService;
import photo.model.vo.Photo;

@WebServlet("/notice/write")
public class NoticeWriteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public NoticeWriteServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/notice/noticeWrite.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		// 사진파일 저장 (실제 upload 폴더 경로에 저장)
		String uploadFilePath = request.getServletContext().getRealPath("upload");
		int uploadFileSizeLimit = 5*1024*1024; // 5MB
		String encType = "UTF-8";
		MultipartRequest multi = new MultipartRequest(request, uploadFilePath, uploadFileSizeLimit, encType, new DefaultFileRenamePolicy());
//		
		String noticeTitle = multi.getParameter("title");
		String noticeContent = multi.getParameter("content");
		
		Notice notice = new Notice();
		notice.setNoticeTitle(noticeTitle);
		notice.setNoticeContent(noticeContent);
		
		// 파일 업로드시
		int photoResult = 0;
		if(multi.getFilesystemName("upFile") != null) {
			String noticePhoto = multi.getFilesystemName("upFile");
			notice.setNoticePhoto(noticePhoto);
			
			// Photo DB에 저장
			File uploadFile = multi.getFile("upFile");
			
			String photoName = multi.getFilesystemName("upFile");
			String photoPath = uploadFile.getPath();
			long photoSize = uploadFile.length();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS"); // 날짜데이터를 내가 원하는 형태로 바꿔줌
			Timestamp uploadTime = Timestamp.valueOf(formatter.format(Calendar.getInstance().getTimeInMillis()));
			
			Photo photo = new Photo();
			photo.setPhotoName(photoName);
			photo.setPhotoPath(photoPath);
			photo.setPhotoSize(photoSize);
			photo.setPhotoId("admin");
			photo.setUploadTime(uploadTime);
			photo.setBoardType('N');
			
			photoResult = new PhotoService().registerPhotoInfo(photo); // Photo DB에 저장
		} else {
			photoResult = 1;
		}
		int noticeResult = new NoticeService().insertNotice(notice); // Notice DB에 저장
			
			
		// 결과 확인 ---------------------------------------------------------------------------------------------------
		if(noticeResult > 0 && photoResult > 0) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('게시글이 등록되었습니다.'); location.href='/notice/list';</script>");
			out.flush();
		} else {
			request.getRequestDispatcher("/WEB-INF/views/notice/noticeError.html").forward(request, response);
		}
	}

}
