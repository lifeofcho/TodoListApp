package com.todo.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.todo.service.DbConnect;
import com.todo.service.TodoSortByDate;
import com.todo.service.TodoSortByName;

public class TodoList 
{
	Connection conn;
	private List<TodoItem> list;

	public TodoList() 
	{
		this.list = new ArrayList<TodoItem>();
		this.conn = DbConnect.getConnection();
	}
	
	public void alter()
	{
		String sql = "ALTER TABLE list ADD COLUMN is_completed integer default 0;";
		Statement stmt;
		try 
		{
		stmt = conn.createStatement();
		stmt.executeUpdate(sql);
		stmt.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	public int addItem(TodoItem t)
	{
		String sql = "insert into list (title, memo, category, current_date, due_date)" + " values (?,?,?,?,?);";
		PreparedStatement pstmt;
		int count = 0;
		try
		{
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, t.getTitle());
			pstmt.setString(2, t.getDesc());
			pstmt.setString(3, t.getCategory());
			pstmt.setString(4, t.getCurrent_date());
			pstmt.setString(5, t.getDue_date());
			count = pstmt.executeUpdate();
			pstmt.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return count;
	}
	
	public int updateItem(TodoItem t)
	{
		String sql = "update list set title = ?, memo = ?, category = ?, current_date = ?, due_date = ?" + " where id = ?;";
		PreparedStatement pstmt;
		int count = 0;
		try
		{
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, t.getTitle());
			pstmt.setString(2, t.getDesc());
			pstmt.setString(3, t.getCategory());
			pstmt.setString(4, t.getCurrent_date());
			pstmt.setString(5, t.getDue_date());
			pstmt.setInt(6, t.getId());
			count = pstmt.executeUpdate();
			pstmt.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return count;
	}

	public int deleteItem(int index) 
	{
		String sql = "delete from list where id=?;";
		PreparedStatement pstmt;
		int count = 0;
		try
		{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, index);
			count = pstmt.executeUpdate();
			pstmt.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return count;
	}
	
	public int completeItem(int index)
	{
		String sql = "update list set is_completed = ?" + " where id = ?;";
		PreparedStatement pstmt;
		int count = 0;
		try
		{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, 1);
			pstmt.setInt(2, index);
			count = pstmt.executeUpdate();
			pstmt.close();
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return count;
	}
	public int getCount()
	{
		Statement stmt;
		int count = 0;
		try
		{
			stmt = conn.createStatement();
			String sql = "SELECT count(id) FROM list;";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			count = rs.getInt("count(id)");
			stmt.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return count;	
	}

	public ArrayList<TodoItem> getList() 
	{
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		Statement stmt;
		try
		{
			stmt = conn.createStatement();
			String sql = "SELECT * FROM list";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				int id = rs.getInt("id");
				String category = rs.getString("category");
				String title = rs.getString("title");
				String description = rs.getString("memo");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int is_comp = rs.getInt("is_completed");
				TodoItem t = new TodoItem(title, description, category, due_date);
				t.setId(id);
				t.setCurrent_date(current_date);
				t.setis_Comp(is_comp);
				list.add(t);
			}
			stmt.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<TodoItem> getList(String keyword)
	{
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		PreparedStatement pstmt;
		keyword = "%" + keyword + "%";
		try
		{
			String sql = "SELECT * FROM list WHERE title like ? or memo like ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			pstmt.setString(2, keyword);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{
				int id = rs.getInt("id");
				String category = rs.getString("category");
				String title = rs.getString("title");
				String description = rs.getString("memo");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int is_comp = rs.getInt("is_completed");
				TodoItem t = new TodoItem(title, description, category, due_date);
				t.setId(id);
				t.setCurrent_date(current_date);
				t.setis_Comp(is_comp);
				list.add(t);
			}
			pstmt.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<TodoItem> getListCategory(String keyword)
	{
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		PreparedStatement pstmt;
		try
		{
			String sql = "SELECT * FROM list WHERE category =  ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{
				int id = rs.getInt("id");
				String category = rs.getString("category");
				String title = rs.getString("title");
				String description = rs.getString("memo");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int is_comp = rs.getInt("is_completed");
				TodoItem t = new TodoItem(title, description, category, due_date);
				t.setId(id);
				t.setCurrent_date(current_date);
				t.setis_Comp(is_comp);
				list.add(t);
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return list;
	}
	public ArrayList<TodoItem> getCompletedItems()
	{
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		Statement stmt;
		try
		{
			stmt = conn.createStatement();
			String sql = "SELECT * from list WHERE is_completed = 1";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				int id = rs.getInt("id");
				String category = rs.getString("category");
				String title = rs.getString("title");
				String description = rs.getString("memo");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int is_comp = rs.getInt("is_completed");
				TodoItem t = new TodoItem(title, description, category, due_date);
				t.setId(id);
				t.setCurrent_date(current_date);
				t.setis_Comp(is_comp);
				list.add(t);
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return list;
	}
	public ArrayList<String> getCategories()
	{
		ArrayList<String> list = new ArrayList<String>();
		Statement stmt;
		try
		{
			stmt = conn.createStatement();
			String sql = "SELECT DISTINCT category FROM list";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				String category = rs.getString("category");
				list.add(category);
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return list;
		
	}
	
	public ArrayList<TodoItem> getOrderedList(String orderby, int ordering)
	{
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		Statement stmt;
		try
		{
			stmt = conn.createStatement();
			String sql = "SELECT * FROM list ORDER BY " + orderby;
			if(ordering == 0)
				sql += " desc";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
			{
				int id = rs.getInt("id");
				String category = rs.getString("category");
				String title = rs.getString("title");
				String description = rs.getString("memo");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int is_comp = rs.getInt("is_completed");
				TodoItem t = new TodoItem(title, description, category, due_date);
				t.setId(id);
				t.setCurrent_date(current_date);
				t.setis_Comp(is_comp);
				list.add(t);
			}
			stmt.close();
		}
		catch(SQLException e)	
		{
			e.printStackTrace();
		}
		return list;
	}
	
	public void sortByName() 
	{
		Collections.sort(list, new TodoSortByName());
	}
	public TodoItem getItem(int index)
	{
		return list.get(index);
	}


	public int indexOf(TodoItem t) {
		return list.indexOf(t);
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public Boolean isDuplicate(String title) 
	{
		PreparedStatement pstmt;
		boolean ans = true;
		try
		{
			String sql = "SELECT * FROM list WHERE title =  ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,title);
			ResultSet rs = pstmt.executeQuery();
			if(!rs.next())
				ans = false;
			pstmt.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return ans;
	}
	
	public void importData(String filename)
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
			String sql = "insert into list (title, memo, category, current_date, due_date)" + "values (?, ?, ?, ?, ?);";
			
			int records = 0;
			while((line = br.readLine()) != null)
			{
				StringTokenizer st = new StringTokenizer(line, "##");
				String category = st.nextToken();
				String title = st.nextToken();
				String description = st.nextToken();
				String due_date = st.nextToken();
				String current_date = st.nextToken();
				
				PreparedStatement ptsmt = conn.prepareStatement(sql);
				ptsmt.setString(1, title);
				ptsmt.setString(2, description);
				ptsmt.setString(3, category);
				ptsmt.setString(4, current_date);
				ptsmt.setString(5, due_date);
				int count = ptsmt.executeUpdate();
				if(count > 0)
					records++;
				ptsmt.close();
			}
			System.out.println(records + " records read succesfully");
			br.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void listAll() {
		for (TodoItem myitem : list) 
		{
			System.out.println(myitem.getTitle() + myitem.getDesc());
		}
	}
	
	public void reverseList() {
		Collections.reverse(list);
	}

	public void sortByDate() {
		Collections.sort(list, new TodoSortByDate());
	}
}
