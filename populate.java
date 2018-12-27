package movieapp;
import java.io.BufferedReader;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

class populate {
	
	public static Connection con = null;
	
	public static void insert(Connection con) {
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("CREATE INDEX GENRE_IDX ON MOVIE_GENRES(genre)");
			stmt.executeUpdate("CREATE INDEX ID_IDX ON MOVIE_COUNTRIES(movieID)");

	        String sql = " INSERT INTO MOVIES(id, title,imdbID,spanishTitle,imdbPictureURL,year,rtID,rtAllCriticsRating,rtAllCriticsNumReviews,rtAllCriticsNumFresh,rtAllCriticsNumRotten,rtAllCriticsScore,rtTopCriticsRating,rtTopCriticsNumReviews,rtTopCriticsNumFresh,rtTopCriticsNumRotten,rtTopCriticsScore,rtAudienceRating,rtAudienceNumRatings,rtAudienceScore,rtPictureURL) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	        String sql2 = " INSERT INTO MOVIE_ACTORS(movieID, actorID,actorName,ranking) VALUES(?,?,?,?)";
	        String sql3 = " INSERT INTO MOVIE_COUNTRIES(movieID, country) VALUES (?,?)";
	        String sql4 = " INSERT INTO MOVIE_DIRECTORS(movieID, directorID, directorName) VALUES (?,?,?)";
	        String sql5 = " INSERT INTO MOVIE_GENRES(movieID, genre) VALUES (?,?)";
	        String sql6 = " INSERT INTO MOVIE_LOCATIONS(movieID, location1) VALUES (?,?)";
	        String sql7 = " INSERT INTO MOVIE_TAGS(movieID, tagID, tagWeight) VALUES (?,?,?)";
	        String sql8 = " INSERT INTO TAGS(id, value) VALUES (?,?)";
	        String sql9 = " INSERT INTO USER_RATEDMOVIESTIMESTAMPS(userID, movieID, rating, timestamp) VALUES (?,?,?,?)";
	        String sql10 = " INSERT INTO USER_RATEDMOVIES(userID, movieID, rating) VALUES (?,?,?)";
	        String sql11 = " INSERT INTO USER_TAGGEDMOVIES(userID, movieID, tagID) VALUES (?,?,?)";
	        String sql12 = " INSERT INTO USER_TAGGEDMOVIESTIMESTAMPS(userID, movieID, tagID, timestamp) VALUES (?,?,?,?)";
	        
	        BufferedReader bReader = new BufferedReader(new FileReader("C:/Users/arushi.gupta/Downloads/movies.dat"));
	        BufferedReader bReader2 = new BufferedReader(new FileReader("C:/Users/arushi.gupta/Downloads/movie_actors.dat"));
	        BufferedReader bReader3 = new BufferedReader(new FileReader("C:/Users/arushi.gupta/Downloads/movie_countries.dat"));
	        BufferedReader bReader4 = new BufferedReader(new FileReader("C:/Users/arushi.gupta/Downloads/movie_directors.dat"));
	        BufferedReader bReader5 = new BufferedReader(new FileReader("C:/Users/arushi.gupta/Downloads/movie_genres.dat"));
	        BufferedReader bReader6 = new BufferedReader(new FileReader("C:/Users/arushi.gupta/Downloads/movie_locations.dat"));
	        BufferedReader bReader7 = new BufferedReader(new FileReader("C:/Users/arushi.gupta/Downloads/movie_tags.dat"));
	        BufferedReader bReader8 = new BufferedReader(new FileReader("C:/Users/arushi.gupta/Downloads/tags.dat"));
	        BufferedReader bReader9 = new BufferedReader(new FileReader("C:/Users/arushi.gupta/Downloads/user_ratedmovies-timestamps.dat"));
	        BufferedReader bReader10 = new BufferedReader(new FileReader("C:/Users/arushi.gupta/Downloads/user_ratedmovies.dat"));
	        BufferedReader bReader11 = new BufferedReader(new FileReader("C:/Users/arushi.gupta/Downloads/user_taggedmovies.dat"));
	        BufferedReader bReader12 = new BufferedReader(new FileReader("C:/Users/arushi.gupta/Downloads/user_taggedmovies-timestamps.dat"));
	        
	        String line = bReader.readLine();
	        String line2 = bReader2.readLine(); 
	        String line3 = bReader3.readLine(); 
	        String line4 = bReader4.readLine(); 
	        String line5 = bReader5.readLine(); 
	        String line6 = bReader6.readLine(); 
	        String line7 = bReader7.readLine(); 
	        String line8 = bReader8.readLine(); 
	        String line9 = bReader9.readLine(); 
	        String line10 = bReader10.readLine(); 
	        String line11 = bReader11.readLine(); 
	        String line12 = bReader12.readLine(); 
	        
	        //movies
	        while ((line = bReader.readLine()) != null) {
	                if (line != null) 
	                {
	                    String[] array = line.split("	");
	                    
                        PreparedStatement ps = con.prepareStatement(sql);
                        try{
                        	ps.setInt(1,Integer.parseInt(array[0]));
                        }
                        catch(NumberFormatException e){
                        	ps.setNull(1, java.sql.Types.INTEGER);
                        	//ps.setInt(1,0);
                        }
                        
                        if(array[1]=="\\N"){
                        	ps.setString(2,null);
                        }
                        else{
                        	ps.setString(2,array[1]);
                        }
                        
                        try{
                        	ps.setInt(3,Integer.parseInt(array[2]));
                        }
                        catch(NumberFormatException e){
                        	ps.setNull(3, java.sql.Types.INTEGER);
                        	//ps.setInt(3,0);
                        }
                        
                        if(array[3]=="\\N"){
                        	ps.setString(4,null);
                        }
                        else{
                        	ps.setString(4,array[3]);
                        }
                        
                        if(array[4]=="\\N"){
                        	ps.setString(5,null);
                        }
                        else{
                        	ps.setString(5,array[4]);
                        }
                        
                        try{
                        	ps.setInt(6,Integer.parseInt(array[5]));
                        }
                        catch(NumberFormatException e){
                        	ps.setNull(6, java.sql.Types.INTEGER);
                        	//ps.setInt(6,0);
                        }
                        
                        if(array[6]=="\\N"){
                        	ps.setString(7,null);
                        }
                        else{
                        	ps.setString(7,array[6]);
                        }
                        
                        try{
                        	ps.setFloat(8,Float.parseFloat(array[7]));
                        }
                        catch(NumberFormatException e){
                        	ps.setNull(8, java.sql.Types.FLOAT);
                        	//ps.setFloat(8, 0);
                        }
                        
                        try{
                        	ps.setInt(9,Integer.parseInt(array[8]));
                        }
                        catch(NumberFormatException e){
                        	ps.setNull(9, java.sql.Types.INTEGER);
                        	//ps.setInt(9,0);
                        }
                        
                        try{
                        	ps.setInt(10,Integer.parseInt(array[9]));
                        }
                        catch(NumberFormatException e){
                        	ps.setNull(10, java.sql.Types.INTEGER);
                        	//ps.setInt(10,0);
                        }
                        
                        try{
                        	ps.setInt(11,Integer.parseInt(array[10]));
                        }
                        catch(NumberFormatException e){
                        	ps.setNull(11, java.sql.Types.INTEGER);
                        	//ps.setInt(11,0);
                        }
                        
                        try{
                        	ps.setInt(12,Integer.parseInt(array[11]));
                        }
                        catch(NumberFormatException e){
                        	ps.setNull(12, java.sql.Types.INTEGER);
                        	//ps.setInt(12,0);
                        }
                        
                        try{
                        	ps.setFloat(13, Float.parseFloat(array[12]));
                        }
                        catch(NumberFormatException e){
                        	ps.setNull(13, java.sql.Types.FLOAT);
                        	//ps.setFloat(13, 0);
                        }
                        
                        try{
                        	ps.setInt(14,Integer.parseInt(array[13]));
                        }
                        catch(NumberFormatException e){
                        	ps.setNull(14, java.sql.Types.INTEGER);
                        	//.setInt(14,0);
                        }
                        
                        try{
                        	ps.setInt(15,Integer.parseInt(array[14]));
                        }
                        catch(NumberFormatException e){
                        	ps.setNull(15, java.sql.Types.INTEGER);
                        	//ps.setInt(15,0);
                        }
                        
                        try{
                        	ps.setInt(16,Integer.parseInt(array[15]));
                        }
                        catch(NumberFormatException e){
                        	ps.setNull(16, java.sql.Types.INTEGER);
                        	//ps.setInt(16,0);
                        }
                        
                        try{
                        	ps.setInt(17,Integer.parseInt(array[16]));
                        }
                        catch(NumberFormatException e){
                        	ps.setNull(17, java.sql.Types.INTEGER);
                        	//ps.setInt(17,0);
                        }
                        
                        try{
                        	ps.setFloat(18, Float.parseFloat(array[17]));
                        }
                        catch(NumberFormatException e){
                        	ps.setNull(18, java.sql.Types.FLOAT);
                        	//ps.setFloat(18, 0);
                        }
                        
                        try{
                        	ps.setInt(19,Integer.parseInt(array[18]));
                        }
                        catch(NumberFormatException e){
                        	ps.setNull(19, java.sql.Types.INTEGER);
                        	//ps.setInt(19,0);
                        }
                        
                        try{
                        	ps.setInt(20,Integer.parseInt(array[19]));
                        }
                        catch(NumberFormatException e){
                        	ps.setNull(20, java.sql.Types.INTEGER);
                        	//ps.setInt(20,0);
                        }
                        
                        if(array[20]=="\\N"){
                        	ps.setString(21,null);
                        }
                        else{
                        	ps.setString(21,array[20]);
                        }
                        
                        ps.executeUpdate();
                        ps. close();
	                 }
	        }
	        
	        //movie_actors
	        while ((line2 = bReader2.readLine()) != null) {
                if (line2 != null) 
                {
                    String[] array = line2.split("	");
                    
                    PreparedStatement ps = con.prepareStatement(sql2);
                    try{
                    	ps.setInt(1,Integer.parseInt(array[0]));
                    }
                    catch(NumberFormatException e){
                    	ps.setNull(1, java.sql.Types.INTEGER);
                    	//ps.setInt(1,0);
                    }
                    
                    if(array[1]=="\\N"){
                    	ps.setString(2,null);
                    }
                    else{
                    	ps.setString(2,array[1]);
                    }
                    
                    if(array[2]=="\\N"){
                    	ps.setString(3,null);
                    }
                    else{
                    	ps.setString(3,array[2]);
                    }
                    
                    try{
                    	ps.setInt(4,Integer.parseInt(array[3]));
                    }
                    catch(NumberFormatException e){
                    	ps.setNull(4, java.sql.Types.INTEGER);
                    	//ps.setInt(1,0);
                    }
                    ps.executeUpdate();
                    ps. close();
                }
	        }
	        
	        //movie_countries
	        while ((line3 = bReader3.readLine()) != null) {
                if (line3 != null) 
                {
                    String[] array = line3.split("	");
                    PreparedStatement ps = con.prepareStatement(sql3);
                    try{
                    	ps.setInt(1,Integer.parseInt(array[0]));
                    }
                    catch(NumberFormatException e){
                    	ps.setNull(1, java.sql.Types.INTEGER);
                    	//ps.setInt(1,0);
                    }
                    
                    if (array.length<2){
                    	ps.setString(2, null);
                    }
                    else {
                    	if(array[1]=="\\N"){
                        	ps.setString(2,null);
                        }
                        else{
                        	ps.setString(2,array[1]);
                        }
                    }
                    ps.executeUpdate();
                    ps. close();
                }
	        }
	        
	        //movie_directors
	        while ((line4 = bReader4.readLine()) != null) {
                if (line4 != null) 
                {
                    String[] array = line4.split("	");
                    
                    PreparedStatement ps = con.prepareStatement(sql4);
                    try{
                    	ps.setInt(1,Integer.parseInt(array[0]));
                    }
                    catch(NumberFormatException e){
                    	ps.setNull(1, java.sql.Types.INTEGER);
                    	//ps.setInt(1,0);
                    }
                    
                    if(array[1]=="\\N"){
                    	ps.setString(2,null);
                    }
                    else{
                    	ps.setString(2,array[1]);
                    }
                    
                    if(array[2]=="\\N"){
                    	ps.setString(3,null);
                    }
                    else{
                    	ps.setString(3,array[2]);
                    }
                    ps.executeUpdate();
                    ps. close();
                }
	        }
	        
	        
	        //movie_genres
	        while ((line5 = bReader5.readLine()) != null) {
                if (line5 != null) 
                {
                    String[] array = line5.split("	");
                    
                    PreparedStatement ps = con.prepareStatement(sql5);
                    try{
                    	ps.setInt(1,Integer.parseInt(array[0]));
                    }
                    catch(NumberFormatException e){
                    	ps.setNull(1, java.sql.Types.INTEGER);
                    	//ps.setInt(1,0);
                    }
                    
                    if(array[1]=="\\N"){
                    	ps.setString(2,null);
                    }
                    else{
                    	ps.setString(2,array[1]);
                    }
                    ps.executeUpdate();
                    ps. close();
                }
	        }
	        
	        
	        //movie_locations
	        while ((line6 = bReader6.readLine()) != null) {
                if (line6 != null) 
                {
                    String[] array = line6.split("	");
                    PreparedStatement ps = con.prepareStatement(sql6);
                    try{
                    	ps.setInt(1,Integer.parseInt(array[0]));
                    }
                    catch(NumberFormatException e){
                    	ps.setNull(1, java.sql.Types.INTEGER);
                    	//ps.setInt(1,0);
                    }
                    
                    if (array.length<2){
                    	ps.setString(2, null);
                    }
                    else {
	                    if(array[1]=="\\N"){
	                    	ps.setString(2,null);
	                    }
	                    else{
	                    	ps.setString(2,array[1]);
	                    }
                    }
                    ps.executeUpdate();
                    ps. close();
                }
	        }
	        
	        
	        //tags
	        while ((line8 = bReader8.readLine()) != null) {
                if (line8 != null) 
                {
                    String[] array = line8.split("	");
                    PreparedStatement ps = con.prepareStatement(sql8);
                    try{
                    	ps.setInt(1,Integer.parseInt(array[0]));
                    }
                    catch(NumberFormatException e){
                    	ps.setNull(1, java.sql.Types.INTEGER);
                    	//ps.setInt(1,0);
                    }
                    
                    if (array.length<2){
                    	ps.setString(2, null);
                    }
                    else {
	                    if(array[1]=="\\N"){
	                    	ps.setString(2,null);
	                    }
	                    else{
	                    	ps.setString(2,array[1]);
	                    }
                    }
                    ps.executeUpdate();
                    ps. close();
                }
	        }
	        
	        
	        //movie_tags
	        while ((line7 = bReader7.readLine()) != null) {
                if (line7 != null) 
                {
                    String[] array = line7.split("	");
                    
                    PreparedStatement ps = con.prepareStatement(sql7);
                    try{
                    	ps.setInt(1,Integer.parseInt(array[0]));
                    }
                    catch(NumberFormatException e){
                    	ps.setNull(1, java.sql.Types.INTEGER);
                    }
                    
                    try{
                    	ps.setInt(2,Integer.parseInt(array[1]));
                    }
                    catch(NumberFormatException e){
                    	ps.setNull(2, java.sql.Types.INTEGER);
                    }
                    
                    try{
                    	ps.setInt(3,Integer.parseInt(array[2]));
                    }
                    catch(NumberFormatException e){
                    	ps.setNull(3, java.sql.Types.INTEGER);
                    }
                    ps.executeUpdate();
                    ps. close();
                }
	        }
	        
	      //USER_RATEDMOVIES
	        while ((line10 = bReader10.readLine()) != null) {
                if (line10 != null) 
                {
                    String[] array = line10.split("	");
                    
                    PreparedStatement ps = con.prepareStatement(sql10);
                    try{
                    	ps.setInt(1,Integer.parseInt(array[0]));
                    }
                    catch(NumberFormatException e){
                    	ps.setNull(1, java.sql.Types.INTEGER);
                    }
                    
                    try{
                    	ps.setInt(2,Integer.parseInt(array[1]));
                    }
                    catch(NumberFormatException e){
                    	ps.setNull(2, java.sql.Types.INTEGER);
                    }
                    
                    try{
                    	ps.setFloat(3, Float.parseFloat(array[2]));
                    }
                    catch(NumberFormatException e){
                    	ps.setNull(3, java.sql.Types.FLOAT);
                    }
                    ps.executeUpdate();
                    ps. close();
                }
	        }
	        
	        
	        //USER_TAGGEDMOVIES
	        while ((line11 = bReader11.readLine()) != null) {
                if (line11 != null) 
                {
                    String[] array = line11.split("	");
                    
                    PreparedStatement ps = con.prepareStatement(sql11);
                    try{
                    	ps.setInt(1,Integer.parseInt(array[0]));
                    }
                    catch(NumberFormatException e){
                    	ps.setNull(1, java.sql.Types.INTEGER);
                    }
                    
                    try{
                    	ps.setInt(2,Integer.parseInt(array[1]));
                    }
                    catch(NumberFormatException e){
                    	ps.setNull(2, java.sql.Types.INTEGER);
                    }
                    
                    try{
                    	ps.setInt(3, Integer.parseInt(array[2]));
                    }
                    catch(NumberFormatException e){
                    	ps.setNull(3, java.sql.Types.INTEGER);
                    }
                    ps.executeUpdate();
                    ps. close();
                }
	        }
	        
	        //USER_RATEDMOVIESTIMESTAMPS
	        while ((line9 = bReader9.readLine()) != null) {
                if (line9 != null) 
                {
                    String[] array = line9.split("	");
                    
                    PreparedStatement ps = con.prepareStatement(sql9);
                    try{
                    	ps.setInt(1,Integer.parseInt(array[0]));
                    }
                    catch(NumberFormatException e){
                    	ps.setNull(1, java.sql.Types.INTEGER);
                    }
                    
                    try{
                    	ps.setInt(2,Integer.parseInt(array[1]));
                    }
                    catch(NumberFormatException e){
                    	ps.setNull(2, java.sql.Types.INTEGER);
                    }
                    
                    try{
                    	ps.setFloat(3, Float.parseFloat(array[2]));
                    }
                    catch(NumberFormatException e){
                    	ps.setNull(3, java.sql.Types.FLOAT);
                    }

                    if(array[3]=="\\N"){
                    	ps.setString(4,null);
                    }
                    else{
                    	ps.setString(4,array[3]);
                    }

                    ps.executeUpdate();
                    ps. close();
                }
	        }
	        
	        
	        //USER_TAGGEDMOVIESTIMESTAMPS
	        while ((line12 = bReader12.readLine()) != null) {
                if (line12 != null) 
                {
                	String[] array = line12.split("	");
                    
                    PreparedStatement ps = con.prepareStatement(sql12);
                    try{
                    	ps.setInt(1,Integer.parseInt(array[0]));
                    }
                    catch(NumberFormatException e){
                    	ps.setNull(1, java.sql.Types.INTEGER);
                    }
                    
                    try{
                    	ps.setInt(2,Integer.parseInt(array[1]));
                    }
                    catch(NumberFormatException e){
                    	ps.setNull(2, java.sql.Types.INTEGER);
                    }
                    
                    try{
                    	ps.setInt(3, Integer.parseInt(array[2]));
                    }
                    catch(NumberFormatException e){
                    	ps.setNull(3, java.sql.Types.INTEGER);
                    }

                    if(array[3]=="\\N"){
                    	ps.setString(4,null);
                    }
                    else{
                    	ps.setString(4,array[3]);
                    }

                    ps.executeUpdate();
                    ps. close();
                }
	        }
	        
	        
	        
	        
	        
	        bReader.close();
	        bReader2.close();
	        bReader3.close();
	        bReader4.close();
	        bReader5.close();
	        bReader6.close();
	        bReader7.close();
	        bReader8.close();
	        bReader9.close();
	        bReader10.close();
	        bReader11.close();
	        bReader12.close();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void startConnection() {
		try {
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			String url="jdbc:oracle:thin:@localhost:1521:orcl";
			con=DriverManager.getConnection(url,"system","Arista1234");
			if (con!=null){
				System.out.println("Connected");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
public static void main(String args[]) {
		startConnection();
		
		insert(con);
		
	}

	public static Connection getcon() {
		return con;
	}
}