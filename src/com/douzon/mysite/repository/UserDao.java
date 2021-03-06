package com.douzon.mysite.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.douzon.mysite.vo.UserVo;

public class UserDao {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	// email 중복 체크
	public UserVo get(String email) {
		UserVo vo = null;
		
		try {
			conn = getConnection();
			
			String sql = "select no, name from user where email = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, email);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				vo = new UserVo();
				
				vo.setNo(rs.getLong("no"));
				vo.setName(rs.getString("name"));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				
				if(pstmt != null) {
					pstmt.close();
				}
				
				if(conn != null) {
					conn.close();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return vo;
	}
	
	// 회원 정보 수정하기
	public int update(UserVo vo) {
		int result = 0;
		String sql = null;
		
		try {
			conn = getConnection();
			
			if(!vo.getPassword().equals("") && !"".equals(vo.getName())) {
				sql = "update user set name = ?, password = ?, gender = ? where no = ?";
			
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, vo.getName());
				pstmt.setString(2, vo.getPassword());
				pstmt.setString(3, vo.getGender());
				pstmt.setLong(4, vo.getNo());
				
				result = pstmt.executeUpdate();
			}
			else if(vo.getPassword().equals("") && !"".equals(vo.getName())) {
				sql = "update user set name = ?, gender = ? where no = ?";
				
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setString(1, vo.getName());
				pstmt.setString(2, vo.getGender());
				pstmt.setLong(3, vo.getNo());
				
				result = pstmt.executeUpdate();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				
				if(conn != null) {
					conn.close();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	// 회원 정보 가져오기
	public UserVo get(long no) {
		UserVo vo = null;
		
		try {
			conn = getConnection();
			
			String sql = "select no, name, email, gender from user where no = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, no);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				vo = new UserVo();
				
				vo.setNo(rs.getLong("no"));
				vo.setName(rs.getString("name"));
				vo.setEmail(rs.getString("email"));
				vo.setGender(rs.getString("gender"));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				
				if(pstmt != null) {
					pstmt.close();
				}
				
				if(conn != null) {
					conn.close();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return vo;
	}
	
	// 로그인
	public UserVo get(String email, String password) {
		UserVo vo = null;
		
		try {
			conn = getConnection();

			// 최소 정보만 빼내기
			String sql = "select no, name from user where email = ? and password = ?"; // 정보를 많이 담아두면 메모리 문제가 생길 수 있음 - 비밀번호는 가능하면 보이지 않는 것이 좋음
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, email);
			pstmt.setString(2, password);
			
			rs = pstmt.executeQuery();

			System.out.println(pstmt.toString());
			
			// 하나의 결과만 나올 경우
			if(rs.next()) {
				vo = new UserVo();
				
				vo.setNo(rs.getLong("no"));
				vo.setName(rs.getString("name"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				
				if (pstmt != null) {
					pstmt.close();
				}

				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return vo;
	}

	// 댓글 등록
	public int insert(UserVo vo) {
		int result = 0;

		try {
			conn = getConnection();

			String sql = "insert into user values(null, ?, ?, ?, ?, now())";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getEmail());
			pstmt.setString(3, vo.getPassword());
			pstmt.setString(4, vo.getGender());

			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}

				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			String url = "jdbc:mysql://localhost:3306/webdb?useSSL=false";
			conn = DriverManager.getConnection(url, "webdb", "webdb");

			System.out.println("DB 연결 성공");
		} catch (Exception e) {
			System.out.println("DB 드라이버 로딩 실패");
			e.printStackTrace();
		}

		return conn;
	}
}
