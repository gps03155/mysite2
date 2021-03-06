package com.douzon.mysite.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.douzon.mysite.vo.BoardVo;

public class BoardDao {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	// 댓글 삭제
	public int deleteComment(long no) {
		int result = 0;
		
		try {
			conn = getConnection();
			
			String sql = "delete from comment where no = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, no);
			
			result = pstmt.executeUpdate();
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
	
	// 댓글 보여주기
	public List<BoardVo> getCommentList(long boardNo){
		List<BoardVo> list = new ArrayList<BoardVo>();
		
		try {
			conn = getConnection();
			
			String sql = "select c.no, u.name, c.content, c.write_date, c.user_no from comment c join user u on c.user_no = u.no where c.board_no = ? order by c.group_no DESC, c.order_no ASC";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, boardNo);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				long no = rs.getLong("c.no");
				String name = rs.getString("u.name");
				String content = rs.getString("c.content");
				String writeDate = rs.getString("c.write_date");
				long userNo = rs.getLong("c.user_no");
				
				BoardVo vo = new BoardVo();
				
				vo.setNo(no);
				vo.setName(name);
				vo.setContent(content);
				vo.setWriteDate(writeDate);
				vo.setUserNo(userNo);
				
				list.add(vo);
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
		
		return list;
	}
	
	// 댓글 등록
	public int insertComment(String content, long userNo, int boardNo) {
		int result = 0;
		
		try {
			conn = getConnection();
			
			String sql = "insert into comment values (null, ?, CURRENT_TIMESTAMP(), (select IFNULL(max(group_no), 0) + 1 as group_no from comment tmp), 1, 0, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, content);
			pstmt.setLong(2, userNo);
			pstmt.setInt(3, boardNo);
			
			result = pstmt.executeUpdate();
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
	
	// 검색한 게시글 수
	public int getSearchCount(String search, String kwd) {
		int searchCount = 0;
		String sql = null;
		
		try {
			conn = getConnection();
			
			if ("title".equals(search)) {
				System.out.println("title Count");
				
				sql = "select count(*) " +
					  "from (select b.no as b_no, b.title, u.name, b.hit, b.write_date, b.depth, u.no " + 
					         "from board b join user u on b.user_no = u.no " +
					         "where b.title Like ?) tmp";
				
				pstmt = conn.prepareStatement(sql);

				pstmt.setString(1, "%" + kwd + "%");
			} 
			else if ("content".equals(search)) {
				sql = "select count(*) " +
						  "from (select b.no as b_no, b.title, u.name, b.hit, b.write_date, b.depth, u.no " + 
						         "from board b join user u on b.user_no = u.no " + 
						         "where b.contents Like ?) tmp";
				
				pstmt = conn.prepareStatement(sql);

				pstmt.setString(1, "%" + kwd + "%");
			} 
			else if ("name".equals(search)) {
				sql =  "select count(*) " +
						  "from (select b.no as b_no, b.title, u.name, b.hit, b.write_date, b.depth, u.no " + 
				                 "from board b join user u on b.user_no = u.no " + 
				                 "where u.name Like ?) tmp";
				pstmt = conn.prepareStatement(sql);

				pstmt.setString(1, "%" + kwd + "%");
			} 
			else if ("full".equals(search)) {
				if ("".equals(kwd)) {
					sql = "select count(*) " +
							  "from (select b.no as b_no, b.title, u.name, b.hit, b.write_date, b.depth, u.no " + 
					                 "from board b join user u on b.user_no = u.no) tmp";
					pstmt = conn.prepareStatement(sql);
				} 
				else {
					sql = "select count(*) " +
							  "from (select b.no as b_no, b.title, u.name, b.hit, b.write_date, b.depth, u.no " + 
				                     "from board b join user u on b.user_no = u.no " + 
					                 "where b.title Like ? or b.contents Like ? or u.name Like ?) tmp";

					pstmt = conn.prepareStatement(sql);

					pstmt.setString(1, "%" + kwd + "%");
					pstmt.setString(2, "%" + kwd + "%");
					pstmt.setString(3, "%" + kwd + "%");
				}
			}
			
			rs = pstmt.executeQuery();
			System.out.println(pstmt.toString());
			
			if(rs.next()) {
				searchCount = rs.getInt("count(*)");
				System.out.println(searchCount);
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
		
		return searchCount;
	}
	
	// 게시글 전체 수
	public int getTotalCount() {
		int totalCount = 0;
		
		try {
			conn = getConnection();
			
			String sql = "select count(*) as total_count from board";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				totalCount = rs.getInt("total_count");
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
		
		return totalCount;
	}

	// 조회수
	public int updateViews(long no) {
		int result = 0;
		
		try {
			conn = getConnection();
			
			String sql = "update board set hit = hit+1 where no = ?";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setLong(1, no);
			
			result = pstmt.executeUpdate();
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
	
	// 게시글 검색
	public List<BoardVo> getSearch(String search, String kwd, int page) {
		List<BoardVo> list = new ArrayList<BoardVo>();
		String sql = null;

		try {
			conn = getConnection();

			if ("title".equals(search)) {
				sql = "select * " + 
					  "from (select * " + 
						     "from (select @rownum:=@rownum + 1 as row_num, b_no, title, name, hit, write_date, depth, no " + 
						            "from ((select b.no as b_no, b.title, u.name, b.hit, b.write_date, b.depth, u.no " + 
						                    "from board b join user u on b.user_no = u.no " + 
						                    "where b.title Like ? " + 
						                    "order by b.group_no DESC, b.order_no ASC) pagetable, (SELECT @rownum:=0) tmp)) pagetable " + 
						     "where row_num <= ?) pagetable " + 
						"where row_num >= ?";
				pstmt = conn.prepareStatement(sql);

				pstmt.setString(1, "%" + kwd + "%");
				pstmt.setInt(2, page * 10);
				pstmt.setInt(3, (page * 10) -9 ); 
			} 
			else if ("content".equals(search)) {
				sql = "select * " + 
					  "from (select * " + 
						     "from (select @rownum:=@rownum + 1 as row_num, b_no, title, name, hit, write_date, depth, no " + 
						            "from ((select b.no as b_no, b.title, u.name, b.hit, b.write_date, b.depth, u.no " + 
						                    "from board b join user u on b.user_no = u.no " + 
						                    "where b.contents Like ? " + 
						                    "order by b.group_no DESC, b.order_no ASC) pagetable, (SELECT @rownum:=0) tmp)) pagetable " + 
						     "where row_num <= ?) pagetable " + 
						"where row_num >= ?";
				pstmt = conn.prepareStatement(sql);

				pstmt.setString(1, "%" + kwd + "%");
				pstmt.setInt(2, page * 10);
				pstmt.setInt(3, (page * 10) - 9);
			} 
			else if ("name".equals(search)) {
				sql = "select * " + 
						"from(select * " + 
						      "from( select @rownum:=@rownum + 1 as row_num, b_no, title, name, hit, write_date, depth, no " + 
						             "from ((select b.no as b_no, b.title, u.name, b.hit, b.write_date, b.depth, u.no " + 
						                     "from board b join user u on b.user_no = u.no " + 
						                     "where u.name Like ? " + 
						                     "order by b.group_no DESC, b.order_no ASC) pagetable, (SELECT @rownum:=0) tmp)) pagetable " + 
						      "where row_num <= ?) pagetable " + 
						"where row_num >= ?";
				pstmt = conn.prepareStatement(sql);

				pstmt.setString(1, "%" + kwd + "%");
				pstmt.setInt(2, page * 10);
				pstmt.setInt(3, (page * 10) - 9); 
			} 
			else if ("full".equals(search)) {
				if ("".equals(kwd)) {
					sql = "select * " + 
						  "from (select * " + 
							     "from (select @rownum:=@rownum + 1 as row_num, b_no, title, name, hit, write_date, depth, no " + 
							            "from ((select b.no as b_no, b.title, u.name, b.hit, b.write_date, b.depth, u.no " + 
							                    "from board b join user u on b.user_no = u.no " + 
							                    "order by b.group_no DESC, b.order_no ASC) pagetable, (SELECT @rownum:=0) tmp)) pagetable " + 
							     "where row_num <= ?) pagetable " + 
							"where row_num >= ?";
					pstmt = conn.prepareStatement(sql);
					
					pstmt.setInt(1, page * 10);
					pstmt.setInt(2, (page * 10) - 9);
				} 
				else {
					sql = "select * " + 
							"from (select * " + 
							       "from (select @rownum:=@rownum + 1 as row_num, b_no, title, name, hit, write_date, depth, no " + 
							              "from ((select b.no as b_no, b.title, u.name, b.hit, b.write_date, b.depth, u.no " + 
							                      "from board b join user u on b.user_no = u.no " + 
							                      "where b.title Like ? or b.contents Like ? or u.name Like ? " + 
							                      "order by b.group_no DESC, b.order_no ASC) pagetable, (SELECT @rownum:=0) tmp)) pagetable " + 
							       "where row_num <= ?) pagetable " + 
							"where row_num >= ?";

					pstmt = conn.prepareStatement(sql);

					pstmt.setString(1, "%" + kwd + "%");
					pstmt.setString(2, "%" + kwd + "%");
					pstmt.setString(3, "%" + kwd + "%");
					pstmt.setInt(4, page * 10);
					pstmt.setInt(5, (page * 10) - 9);
				}
			}

			rs = pstmt.executeQuery();
			System.out.println(pstmt.toString());

			while (rs.next()) {
				long no = rs.getLong("b_no");
				String title = rs.getString("title");
				String name = rs.getString("name");
				int hit = rs.getInt("hit");
				String writeDate = rs.getString("write_date");
				int depth = rs.getInt("depth");
				long userNo = rs.getLong("no");
				int rowNum = rs.getInt("row_num");

				BoardVo vo = new BoardVo();

				vo.setNo(no);
				vo.setTitle(title);
				vo.setName(name);
				vo.setHit(hit);
				vo.setWriteDate(writeDate);
				vo.setDepth(depth);
				vo.setUserNo(userNo);
				vo.setRowNum(rowNum);

				list.add(vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("검색 조건을 입력해주세요.");
		} finally {
			try {
				if (rs != null) {
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

		return list;
	}

	// 답글 - order_no
	public int updateReply(int orderNo, int groupNo) {
		int result = 0;

		try {
			conn = getConnection();

			String sql = "update board set order_no = order_no + 1 where order_no > ? and group_no = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, orderNo);
			pstmt.setInt(2, groupNo);

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

	// 답글 등록
	public int insertReply(String title, String content, int groupNo, int orderNo, int depth, long userNo) {
		int result = 0;

		try {
			conn = getConnection();

			String sql = "insert into board values (null, ?, ?, CURRENT_TIMESTAMP(), 0, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, title);
			pstmt.setString(2, content);
			pstmt.setInt(3, groupNo);
			pstmt.setInt(4, orderNo + 1);
			pstmt.setInt(5, depth + 1);
			pstmt.setLong(6, userNo);

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

	// 답글 - 부모글 정보 (group_no, order_no, depth)
	public BoardVo getInfo(long no) {
		BoardVo vo = null;

		try {
			conn = getConnection();

			String sql = "select group_no, order_no, depth from board where no = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, no);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				vo = new BoardVo();

				int groupNo = rs.getInt("group_no");
				int orderNo = rs.getInt("order_no");
				int depth = rs.getInt("depth");

				vo.setGroupNo(groupNo);
				vo.setOrderNo(orderNo);
				vo.setDepth(depth);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				try {
					if (rs != null) {
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return vo;
	}

	// 게시글 삭제하기
	public int delete(long no) {
		int result = 0;

		try {
			conn = getConnection();

			String sql = "SET foreign_key_checks = 0; delete from board where no = ?; SET foreign_key_checks = 1";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, no);

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

	// 게시글 수정하기
	public int update(String title, String content, long no) {
		int result = 0;

		try {
			conn = getConnection();

			String sql = "update board set title = ?, contents = ? where no = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, title);
			pstmt.setString(2, content);
			pstmt.setLong(3, no);

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

	// 글 내용 가져오기
	public BoardVo get(long no) {
		BoardVo vo = null;

		try {
			conn = getConnection();

			String sql = "select title, contents, user_no, no from board where no = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, no);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				vo = new BoardVo();

				String title = rs.getString("title");
				String content = rs.getString("contents");
				long userNo = rs.getLong("user_no");
				long board_no = rs.getLong("no");

				vo.setTitle(title);
				vo.setContent(content);
				vo.setUserNo(userNo);
				vo.setNo(board_no);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
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

	// 페이지별 게시글 목록 가져오기
		public List<BoardVo> getPageList(int page) {
			List<BoardVo> list = new ArrayList<BoardVo>();

			try {
				conn = getConnection();

				String sql = "select * " + 
						     "from (select * " + 
						            "from (select @rownum:=@rownum + 1 as row_num, b_no, title, name, hit, write_date, depth, no " + 
						                   "from ((select b.no as b_no, b.title, u.name, b.hit, b.write_date, b.depth, u.no " + 
						                           "from board b join user u on b.user_no = u.no " +
						                           "order by b.group_no DESC, b.order_no ASC) pagetable, (SELECT @rownum:=0) tmp)) pagetable " +
						            "where row_num <= ?) pagetable " + 
						     "where row_num >= ?;";
				pstmt = conn.prepareStatement(sql);
				
				pstmt.setInt(1, page * 10);
				pstmt.setInt(2, (page * 10) - 9);

				rs = pstmt.executeQuery();

				while (rs.next()) {
					long no = rs.getLong("b_no");
					String title = rs.getString("title");
					String name = rs.getString("name");
					int hit = rs.getInt("hit");
					String writeDate = rs.getString("write_date");
					int depth = rs.getInt("depth");
					long userNo = rs.getLong("no");
					int rowNum = rs.getInt("row_num");

					BoardVo vo = new BoardVo();

					vo.setNo(no);
					vo.setTitle(title);
					vo.setName(name);
					vo.setHit(hit);
					vo.setWriteDate(writeDate);
					vo.setDepth(depth);
					vo.setUserNo(userNo);
					vo.setRowNum(rowNum);

					list.add(vo);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (rs != null) {
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

			return list;
		}
	
	// 게시글 목록 가져오기
	public List<BoardVo> getList() {
		List<BoardVo> list = new ArrayList<BoardVo>();

		try {
			conn = getConnection();

			String sql = "select b.no, b.title, u.name, b.hit, b.write_date, b.depth, u.no from board b join user u on b.user_no = u.no order by b.group_no DESC, b.order_no ASC";
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				long no = rs.getLong("b.no");
				String title = rs.getString("b.title");
				String name = rs.getString("u.name");
				int hit = rs.getInt("b.hit");
				String writeDate = rs.getString("b.write_date");
				int depth = rs.getInt("b.depth");
				long userNo = rs.getLong("u.no");

				BoardVo vo = new BoardVo();

				vo.setNo(no);
				vo.setTitle(title);
				vo.setName(name);
				vo.setHit(hit);
				vo.setWriteDate(writeDate);
				vo.setDepth(depth);
				vo.setUserNo(userNo);

				list.add(vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
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

		return list;
	}

	// 게시글 등록
	public int insert(String title, String content, long userNo) {
		int result = 0;

		try {
			conn = getConnection();

			String sql = "insert into board values (null, ?, ?, CURRENT_TIMESTAMP(), 0, (select IFNULL(max(group_no), 0) + 1 as group_no from board a),  1, 0, ?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, title);
			pstmt.setString(2, content);
			pstmt.setLong(3, userNo);

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

			String url = "jdbc:mysql://localhost:3306/webdb?useSSL=false&allowMultiQueries=true";
			conn = DriverManager.getConnection(url, "webdb", "webdb");

			System.out.println("DB 연결 성공");
		} catch (Exception e) {
			System.out.println("DB 드라이버 로딩 실패");
			e.printStackTrace();
		}

		return conn;
	}
}
