package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Collegamento;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	 public void getVertici(Map<Integer,Director> idMap, int year) {
		 String sql = "SELECT DISTINCT D.id,D.first_name,D.last_name\r\n"
		 		+ "FROM movies_directors MG, movies M, directors D\r\n"
		 		+ "WHERE D.id=MG.director_id AND MG.movie_id=M.id AND M.year=?";
		 Connection conn = DBConnect.getConnection();

			try {
				PreparedStatement st = conn.prepareStatement(sql);
				st.setInt(1,year);
				ResultSet res = st.executeQuery();
				while (res.next()) {
                  if(!idMap.containsKey(res.getInt("id"))) {
					Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
					idMap.put(res.getInt("id"), director);
                  }
				}
				conn.close();
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			
			}
	 }
	public List<Collegamento> getArchi(Map<Integer,Director> idMap, int year){
		String sql = "SELECT  MG.director_id,MG2.director_id, COUNT(R.actor_id=R2.actor_id) AS peso "
				+ "FROM movies_directors MG, roles R, movies_directors MG2, roles R2, movies M, movies M2 "
				+ "WHERE MG.director_id>MG2.director_id AND R.actor_id=R2.actor_id AND MG.movie_id=R.movie_id AND MG2.movie_id=R2.movie_id AND M.id=R.movie_id AND M.year=? AND M2.id=R2.movie_id AND M2.year=? "
				+ "GROUP BY MG.director_id, MG2.director_id ";
		List<Collegamento> result = new LinkedList<>();
		 Connection conn = DBConnect.getConnection();

			try {
				PreparedStatement st = conn.prepareStatement(sql);
				st.setInt(1,year);
				st.setInt(2,year);
				ResultSet res = st.executeQuery();
				while (res.next()) {
               if(idMap.containsKey(res.getInt("MG.director_id")) && idMap.containsKey(res.getInt("MG2.director_id"))) {
					Director d1 = idMap.get(res.getInt("MG.director_id"));
					Director d2 = idMap.get(res.getInt("MG2.director_id"));
					int peso = res.getInt("peso");
					Collegamento c = new Collegamento(d1,d2,peso);
					result.add(c);
               }
				}
				conn.close();
				return result;
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			return null;
			}
	}
	
	
	
	
}
