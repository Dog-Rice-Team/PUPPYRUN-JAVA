package petdiary.model.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import common.JDBCTemplate;
import petdiary.model.dao.PetDiaryDAO;
import petdiary.model.vo.PetDiary;

public class PetDiaryService {
	private JDBCTemplate factory;
	
	public PetDiaryService() {
		factory = JDBCTemplate.getConnection();
	}
	
	public ArrayList<PetDiary> selectAllDiary(String userId) {
		Connection conn = null;
		ArrayList<PetDiary> pList = null;
		
		try {
			conn = factory.createConnection();
			pList = new PetDiaryDAO().selectAllDiary(conn, userId);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(conn);
		}
		
		return pList;
	}
	
	public PetDiary selectOneDiary(String diaryDate, String diaryId) {
		Connection conn = null;
		PetDiary petDiary = null;
		
		try {
			conn = factory.createConnection();
			petDiary = new PetDiaryDAO().selectOneDiary(conn, diaryDate, diaryId);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(conn);
		}
		
		return petDiary;
	}
	
	public int insertDiary(PetDiary petDiary) {
		Connection conn = null;
		int result = 0;
		
		try {
			conn = factory.createConnection();
			result = new PetDiaryDAO().insertDiary(conn, petDiary);
			if(result > 0) {
				JDBCTemplate.commit(conn);
			}else {
				JDBCTemplate.rollback(conn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(conn);
		}
		return result;
	}
	
	public int updateDiary(PetDiary petDiary) {
		Connection conn = null;
		int result = 0;
		
		try {
			conn = factory.createConnection();
			result = new PetDiaryDAO().updateDiary(conn, petDiary);
			
			if(result > 0) {
				JDBCTemplate.commit(conn);
			} else {
				JDBCTemplate.rollback(conn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(conn);
		}
		
		return result;
	}
	
	public int deleteDiary(int diaryNo) {
		Connection conn = null;
		int result = 0;
		
		try {
			conn = factory.createConnection();
			result = new PetDiaryDAO().deleteDiary(conn, diaryNo);
			
			if(result > 0) {
				JDBCTemplate.commit(conn);
			}else {
				JDBCTemplate.rollback(conn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(conn);
		}
		
		return result;
	}

	public int deleteDiary(String userId) {
		Connection conn = null;
		int result = 0;
		
		try {
			conn = factory.createConnection();
			result = new PetDiaryDAO().deleteDiary(conn, userId);
			
			if(result > 0) {
				JDBCTemplate.commit(conn);
			}else {
				JDBCTemplate.rollback(conn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(conn);
		}
		
		return result;
	}
}
