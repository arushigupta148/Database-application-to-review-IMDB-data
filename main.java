package movieapp;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.border.LineBorder;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;

import java.awt.Dimension;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextArea;

class connection {
	
	static HashSet<String> hset=null;
	public static Connection con = null;
	
	public static HashSet<String> getGenre () {
		return hset;
	}
	
	public static void genres(Connection con) {
		try {
			PreparedStatement ps = con.prepareStatement("Select genre from MOVIE_GENRES");
			ResultSet rs = ps.executeQuery();
			ArrayList<String> arr = new ArrayList<String>();
			while (rs.next()) {
			    String columnValue = rs.getString(1);
			    arr.add(columnValue);
			}
			hset = new HashSet<String>(arr);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static String query1=null;
	public static String query2=null;

	public static HashSet<String> selectedGenre(ArrayList<String> selectedG, String op) {
		try {
			con=populate.getcon();
			char[] markers = new char[selectedG.size() * 2 - 1];
			for (int i = 0; i < markers.length; i++)
			    markers[i] = ((i & 1) == 0 ? '?' : ',');
			String mark="";
			for (char m :markers) {
				mark=mark+m;
			}
			
			PreparedStatement ps =null;
			if (op=="OR"){
				ps = con.prepareStatement("SELECT MOVIE_COUNTRIES.country FROM MOVIE_COUNTRIES INNER JOIN MOVIE_GENRES ON MOVIE_COUNTRIES.movieID = MOVIE_GENRES.movieID WHERE MOVIE_GENRES.genre IN ( "+mark+" )");
			}
			else {
				ps = con.prepareStatement("SELECT DISTINCT country FROM (SELECT ID, country, count(genre) as counter FROM (SELECT MOVIE_GENRES.movieID as ID, MOVIE_COUNTRIES.country as country, MOVIE_GENRES.genre as genre FROM MOVIE_GENRES INNER JOIN MOVIE_COUNTRIES ON MOVIE_GENRES.movieID = MOVIE_COUNTRIES.movieID WHERE MOVIE_GENRES.genre in ( " + mark + " )) g GROUP BY ID, country) h WHERE counter = " + selectedG.size()); 
			}
			for (int i=1;i<=selectedG.size();i++) {
				ps.setString(i, selectedG.toArray()[i-1].toString());
			}
			ResultSet rs = ps.executeQuery();
			
			ArrayList<String> arr = new ArrayList<String>();
			while (rs.next()) {
			    String columnValue = rs.getString(1);
			    arr.add(columnValue);
			}
			hset = new HashSet<String>(arr);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	
	return hset;
		
	}

	public static HashSet<String> selectedLocation(ArrayList<String> selectedC, ArrayList<String> selectedG, String op) {
		   try {
			  String mark="";
		      con=populate.getcon();
		      for (String m: selectedG) {
		         mark=mark+"'"+m+"',";
		      }
		      mark = mark.substring(0,mark.length()-1);
		      String mark2="";
		      if (selectedC.size()>0) {
		         for (String m: selectedC) {
		            mark2=mark2+"'"+m+"',";
		         }
		         mark2 = mark2.substring(0,mark2.length()-1);
		      }
		      
		      PreparedStatement ps =null;
		      String query1 = "";
		      if (op=="OR"){
		          query1 = "SELECT MOVIE_LOCATIONS.location1 FROM MOVIE_LOCATIONS INNER JOIN MOVIE_GENRES ON MOVIE_LOCATIONS.movieID = MOVIE_GENRES.movieID INNER JOIN MOVIE_COUNTRIES ON MOVIE_COUNTRIES.movieID = MOVIE_GENRES.movieID WHERE MOVIE_GENRES.genre IN ( "+mark+" ) AND MOVIE_COUNTRIES.country IN ("+mark2+")";
		         ps= con.prepareStatement(query1);
		      }
		      else {
		          query1 = "SELECT DISTINCT location FROM (SELECT ID, country, count(distinct genre) as counter, location, count(distinct country) as counter2 FROM (SELECT MOVIE_GENRES.movieID as ID, MOVIE_COUNTRIES.country as country, MOVIE_GENRES.genre as genre, MOVIE_LOCATIONS.location1 as location FROM MOVIE_GENRES INNER JOIN MOVIE_COUNTRIES ON MOVIE_GENRES.movieID = MOVIE_COUNTRIES.movieID INNER JOIN MOVIE_LOCATIONS ON MOVIE_LOCATIONS.movieID = MOVIE_COUNTRIES.movieID WHERE MOVIE_GENRES.genre in ( " + mark + " ) and MOVIE_COUNTRIES.country in ("+ mark2 +")) g GROUP BY ID, country, location) h WHERE counter = " + selectedG.size()+" and counter2 = "+selectedC.size();
		         ps = con.prepareStatement(query1);
		      }
		      System.out.println(query1);
		      ResultSet rs = ps.executeQuery();
		      
		      ArrayList<String> arr = new ArrayList<String>();
		      while (rs.next()) {
		          String columnValue = rs.getString(1);
		          arr.add(columnValue);
		      }
		      hset = new HashSet<String>(arr);
		   } catch (SQLException e) {
		      e.printStackTrace();
		   }
		return hset;
		}

	public static ArrayList<String> getAttributes(Integer id) {
		try {
			
			query1="SELECT title, year, genres, countries, locations, avg(rtAllCriticsRating), "
					+ "avg(rtAllCriticsNumReviews), avg(rtTopCriticsRating), avg(rtTopCriticsNumReviews), "
					+ "avg(rtAudienceNumRatings) "
				+ "FROM MOVIES INNER JOIN (SELECT movieID, listagg(genre,',') within group(order by genre) as genres"
							+ " FROM MOVIE_GENRES GROUP BY movieID) GENRES "
						+ "ON MOVIES.id = GENRES.movieID "
					+ "INNER JOIN (SELECT movieID, listagg(country,',') within group(order by country) as countries "
							+ "FROM MOVIE_COUNTRIES GROUP BY movieID) COUNTRIES "
							+ "ON MOVIES.id = COUNTRIES.movieID "
					+ "INNER JOIN (SELECT movieID, listagg(location1,',') within group(order by location1) as locations "
							+ "FROM MOVIE_LOCATIONS GROUP BY movieID) LOCATIONS "
							+ "ON MOVIES.id = LOCATIONS.movieID "
				+ "WHERE MOVIES.id = "+id
				+ " GROUP BY title, year, genres, countries, locations";
			PreparedStatement ps= con.prepareStatement(query1);
	
			ResultSet rs = ps.executeQuery();
			ArrayList<String> arr = new ArrayList<String>();
			while (rs.next()) {
				String op = "";
				for (int i=1; i<11; i++) {
					if (i != 10)
						op += rs.getString(i) + ", ";
					else
						op += rs.getString(i);
				}
			    arr.add(op);
			}
			return arr;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HashMap<Integer, String> getMovieID(ArrayList<String> selectedG, ArrayList<String> selectedC,
			ArrayList<String> selectedL, String op, Integer rating, String symbol,
			Integer numReviews, String reviewSymbol, Integer from2, Integer to,
			String tagWeightsymbol, String tagWeightValue) {
		try {
			con=populate.getcon();
			String mark = "", mark2 = "", mark3 = "";
			
			for (String m: selectedG) {
				mark=mark+"'"+m+"',";
			}
			mark = mark.substring(0,mark.length()-1);
			
			if (selectedC.size()>0) {
				for (String m: selectedC) {
					mark2=mark2+"'"+m+"',";
				}
				mark2 = mark2.substring(0,mark2.length()-1);
			}
			
			if (selectedL.size()>0) {
				for (String m: selectedL) {
					mark3=mark3+"'"+m+"',";
				}
				mark3 = mark3.substring(0,mark3.length()-1);
			}
			
			PreparedStatement ps =null;
			if (op=="OR"){
				String query = "SELECT DISTINCT MOVIE_LOCATIONS.movieID, tagText FROM MOVIE_LOCATIONS "
						+ "INNER JOIN MOVIE_GENRES ON MOVIE_LOCATIONS.movieID = MOVIE_GENRES.movieID "
						+ "INNER JOIN MOVIE_COUNTRIES ON MOVIE_COUNTRIES.movieID = MOVIE_GENRES.movieID "
						+ "INNER JOIN (SELECT id, year, avg(rtAllCriticsRating) as ratings, avg(rtAllCriticsNumReviews) as reviews "
						+ "FROM MOVIES GROUP BY id, year) MVS ON MOVIE_GENRES.movieID = MVS.id ";
				
				if (!tagWeightValue.equals("")) {
					query += " INNER JOIN (SELECT movieID, tagText FROM (SELECT movieID, listagg(TAGS.value,',') within group(order by TAGS.value) as tagText,"
							+ " max(tagWeight) as tagw FROM MOVIE_TAGS INNER JOIN TAGS ON MOVIE_TAGS.tagID = TAGS.id GROUP BY movieID) temp1 "
							+ " WHERE tagw " + tagWeightsymbol + tagWeightValue + ") temp2 ON MOVIE_GENRES.movieID = temp2.movieID ";
				}
				else {
					query += " INNER JOIN (SELECT movieID, listagg(TAGS.value,',') within group(order by TAGS.value) as tagText"
							+ " FROM MOVIE_TAGS INNER JOIN TAGS ON MOVIE_TAGS.tagID = TAGS.id GROUP BY movieID) temp2 "
							+ "ON MOVIE_GENRES.movieID = temp2.movieID ";
				}
				
				query += "WHERE MOVIE_GENRES.genre IN ("+mark+") ";
				if (selectedC.size()>0) {
					query += "AND MOVIE_COUNTRIES.country IN ("+mark2+") ";
				}
				if (selectedL.size()>0) {
					query += "AND MOVIE_LOCATIONS.location1 IN ("+mark3+") ";
				}
				if (rating != null) {
					query += "AND ratings" + symbol + rating + " ";
				}
				if (numReviews != null) {
					query += "AND reviews" + reviewSymbol + numReviews + " ";
				}
				if (from2 < to) {
					query += "AND year >= " + from2 + " AND year <= " + to + " ";
				}

				query += " GROUP BY MOVIE_LOCATIONS.movieID, tagText";
				query2 = query;
				ps= con.prepareStatement(query);
			}
			else {
				String select = "SELECT MOVIE_GENRES.movieID as ID, tagText, count(distinct genre) as counter";
				String from = " FROM MOVIE_GENRES INNER JOIN (SELECT id, year, avg(rtAllCriticsRating) as ratings, "
						+ "avg(rtAllCriticsNumReviews) as reviews FROM MOVIES GROUP BY id, year) MVS ON MOVIE_GENRES.movieID = MVS.id ";
				if (!tagWeightValue.equals("")) {
					from += " INNER JOIN (SELECT movieID, tagText FROM (SELECT movieID, listagg(TAGS.value,',') within group(order by TAGS.value) as tagText,"
							+ " max(tagWeight) as tagw FROM MOVIE_TAGS INNER JOIN TAGS ON MOVIE_TAGS.tagID = TAGS.id GROUP BY movieID) temp1 "
							+ " WHERE tagw " + tagWeightsymbol + tagWeightValue + ") temp2 ON MOVIE_GENRES.movieID = temp2.movieID ";
				}
				else {
					from += " INNER JOIN (SELECT movieID, listagg(TAGS.value,',') within group(order by TAGS.value) as tagText"
							+ " FROM MOVIE_TAGS INNER JOIN TAGS ON MOVIE_TAGS.tagID = TAGS.id GROUP BY movieID) temp2 "
							+ "ON MOVIE_GENRES.movieID = temp2.movieID ";
				}
				
				String where = " WHERE counter = " + selectedG.size();
				String innerwhere = " WHERE MOVIE_GENRES.genre in (" + mark + ") ";
				if (selectedC.size()>0) {
					from += "INNER JOIN (SELECT movieID, count(distinct country) as counter2 FROM MOVIE_COUNTRIES WHERE MOVIE_COUNTRIES.country in (" + mark2 + ") GROUP BY movieID) MVC ON MOVIE_GENRES.movieID = MVC.movieID ";
					innerwhere += " and counter2 = " + selectedC.size();
				}
				if (selectedL.size()>0) {
					from += "INNER JOIN (SELECT movieID, count(distinct location1) as counter3 FROM MOVIE_LOCATIONS WHERE MOVIE_LOCATIONS.location1 in (" + mark3 + ") GROUP BY movieID) MVL ON MVL.movieID = MOVIE_GENRES.movieID ";
					innerwhere += " and counter3 = " + selectedL.size();
				}
				if (rating != null) {
					innerwhere += " AND ratings" + symbol + rating + " ";
				}
				if (numReviews != null) {
					innerwhere += " AND reviews" + reviewSymbol + numReviews + " ";
				}
				if (from2 < to) {
					innerwhere += " AND year >= " + from2 + " AND year <= " + to + " ";
				}
				
				String query = "SELECT DISTINCT ID, tagText FROM (" + select + from + innerwhere + " GROUP BY MOVIE_GENRES.movieID, tagText) g" + where;
				ps = con.prepareStatement(query);
				query2=query;
			}
			ResultSet rs = ps.executeQuery();
			HashMap<Integer, String> hm = new HashMap<Integer, String>();
			while (rs.next()) {
				hm.put(Integer.parseInt(rs.getString(1)), rs.getString(2));
			}
			return hm;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<Integer> getYears() {
		try {
			con=populate.getcon();
			PreparedStatement ps =null;
			ps = con.prepareStatement("SELECT  MAX(year), MIN(year) from MOVIES");
			ResultSet rs = ps.executeQuery();
			
			ArrayList<Integer> arr = new ArrayList<Integer>();
			while (rs.next()) {
			    int min = rs.getInt(2);
			    int max = rs.getInt(1);
			    arr.add(min);
			    arr.add(max);
			}
			return arr;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getQuery1() {
		return query1;
	}

	public static String getQuery2() {
		return query2;
	}
}


public class hw3 {

	public static String op="AND";
	private JFrame frame;
	private JTextField txtMovie;
	private JScrollPane scrollPane_0;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;
	private JScrollPane scrollPane_3;
	private JPanel panel;
	private JButton btnNewButton_2;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_3;
	private JLabel lblRating;
	private JLabel lblValue;
	private JTextField textField;
	private JTextField textField_1;
	private JPanel panel_4;
	private JLabel lblTagWeight;
	private JLabel lblValue_2;
	private JTextField textField_2;
	private JComboBox comboBox_3;
	private JPanel panel_7;
	private JComboBox comboBox;
	private JLabel lblNewLabel;
	private JLabel lblAttributesValues;
	private ArrayList<String> selectedG;
	private ArrayList<String> selectedC;
	private JPanel p2;
	private JPanel p3;
	private HashSet<String> location;
	private ArrayList<String> selectedL;
	private Integer from;
	private Integer to;
	private JButton btnNewButton_1;
	private JPanel panel_9;
	private JLabel lblMovieTagValues;
	private JTextArea textArea_1;
	private JTextArea textArea_2;
	private JLabel lblMovieIdQuery;
	private JLabel lblMovieAttrQuery;
	private JTextArea textArea_3;
	private JScrollPane scrollPane_5;
	private JScrollPane scrollPane_6;
	private JTextArea textArea;
	private JScrollPane scrollPane_7;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					hw3 window = new hw3();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public hw3() {
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBackground(new Color(240, 240, 240));
		frame.setBounds(100, 100, 1454, 824);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		connection c = new connection();
		HashSet<String> h = new HashSet<String>();
		populate.startConnection();
		connection.genres(populate.getcon());
		h = connection.getGenre();
		ArrayList<Integer> years=connection.getYears();

		selectedC= new ArrayList<String>();
		JPanel p = new JPanel();
		p2=new JPanel();
		BoxLayout layout = new BoxLayout(p, BoxLayout.Y_AXIS);
		p.setLayout(layout);
		for (int i=0;i<h.size();i++) {
			String name=(String) h.toArray()[i];
			JCheckBox b = new JCheckBox(name);
			p.add(b);
			selectedG = new ArrayList<String>();
			selectedC = new ArrayList<String>();
			selectedL= new ArrayList<String>();
			b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (b.isSelected()) {
					selectedG.add(b.getText());
				}
				else {
					selectedG.remove(b.getText());
				}
				if (selectedG.size()==0) {
					if (p2 != null) {
						for (Component c: p2.getComponents()) {
							p2.remove(c);
						}	
						p2.repaint();
						selectedC = new ArrayList<String>();
					}
					
					if (p3 != null) {
						for (Component c: p3.getComponents()) {
							p3.remove(c);
						}
						p3.repaint();
						selectedL= new ArrayList<String>();
					}
					frame.revalidate();
				}
				else {
					if (p2 != null) {
						for (Component c: p2.getComponents()) {
							p2.remove(c);
						}	
						p2.repaint();
						selectedC = new ArrayList<String>();
					}
					
					if (p3 != null) {
						for (Component c: p3.getComponents()) {
							p3.remove(c);
						}
						p3.repaint();
						selectedL= new ArrayList<String>();
					}
					frame.revalidate();
					HashSet<String> countries = connection.selectedGenre(selectedG,op);
					if (countries.contains(null)) {
						countries.remove(null);
					}
					if (p2 == null) {
						p2 = new JPanel();	
					}
					
					for (Component c: p2.getComponents()) {
						p2.remove(c);
					}
					p2.repaint();
					frame.revalidate();
					BoxLayout layout = new BoxLayout(p2, BoxLayout.Y_AXIS);
					p2.setLayout(layout);
					for (int i=0;i<countries.size();i++) {
						String name=(String) countries.toArray()[i];
						JCheckBox b2 = new JCheckBox(name);
						p2.add(b2);
						frame.revalidate();
						
						b2.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								if (b2.isSelected()) {
									selectedC.add(b2.getText());
								}
								else {
									selectedC.remove(b2.getText());
								}
								
								if (selectedC.size()==0) {
									location = new HashSet<String>();
									for (Component c: p3.getComponents()) {
										p3.remove(c);
									}
									p3.repaint();
									selectedL= new ArrayList<String>();
									frame.revalidate();
								}
								else {
									location = connection.selectedLocation(selectedC, selectedG, op);
									if (p3 != null) {
										for (Component c: p3.getComponents()) {
											p3.remove(c);
										}
										p3.repaint();
										selectedL= new ArrayList<String>();
									}
								}
								
								if (location.contains(null)) {
									location.remove(null);
								}
								if (p3==null) {
									p3 = new JPanel();	
								}
								for (Component c: p3.getComponents()) {
									p3.remove(c);
								}
								p3.repaint();
								frame.revalidate();
								BoxLayout layout = new BoxLayout(p3, BoxLayout.Y_AXIS);
								p3.setLayout(layout);
								
								for (int i=0;i<location.size();i++) {
									String name=(String) location.toArray()[i];
									JCheckBox b3 = new JCheckBox(name);
									p3.add(b3);
									
									b3.addActionListener(new ActionListener() {
										public void actionPerformed(ActionEvent e) {
											if (b3.isSelected()) {
												selectedL.add(b3.getText());
											}
											else {
												selectedL.remove(b3.getText());
											}
										}
									});
								}
								frame.revalidate();
								if (scrollPane_2 == null) {
									scrollPane_2 = new JScrollPane(p3);
									scrollPane_2.setBounds(460, 42, 231, 278);
									scrollPane_2.setViewportBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Filming Location Country", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
									frame.getContentPane().add(scrollPane_2);	
								}
								frame.revalidate();
							}
						});
					}
					if (scrollPane_1 == null) {
						scrollPane_1 = new JScrollPane(p2);
						scrollPane_1.setBounds(228, 55, 231, 265);
						scrollPane_1.setViewportBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Country", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
						frame.getContentPane().add(scrollPane_1);
					}
					frame.revalidate();
				}
			}
		});
		}
		frame.getContentPane().setLayout(null);
		
		JScrollPane scrollPane=new JScrollPane(p);
		scrollPane.setBounds(0, 42, 230, 278);
		scrollPane.setViewportBorder(new TitledBorder(null, "Genres", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		frame.getContentPane().add(scrollPane);
		
		txtMovie = new JTextField();
		txtMovie.setBounds(0, 0, 1428, 40);
		txtMovie.setFont(new Font("Tahoma", Font.PLAIN, 24));
		txtMovie.setForeground(new Color(0, 0, 0));
		txtMovie.setText("Movie");
		txtMovie.setHorizontalAlignment(SwingConstants.CENTER);
		txtMovie.setBackground(new Color(100, 149, 237));
		frame.getContentPane().add(txtMovie);
		txtMovie.setColumns(10);
		
		scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(693, 42, 231, 241);
		scrollPane_3.setViewportBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Critics' Rating", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		frame.getContentPane().add(scrollPane_3);
		
		panel_1 = new JPanel();
		panel_1.setBounds(693, 55, 100, 241);
		scrollPane_3.setViewportView(panel_1);
		panel_1.setLayout(null);
		
		panel_2 = new JPanel();
		panel_2.setBounds(0, 0, 217, 104);
		panel_1.add(panel_2);
		panel_2.setLayout(null);
		
		lblRating = new JLabel("Rating:");
		lblRating.setBounds(0, 21, 67, 26);
		panel_2.add(lblRating);
		
		lblValue = new JLabel("Value:");
		lblValue.setBounds(0, 57, 67, 26);
		panel_2.add(lblValue);
		
		textField = new JTextField();
		textField.setBounds(68, 57, 138, 26);
		
		panel_2.add(textField);
		textField.setColumns(10);
		
		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"=", "<", ">", "<=", ">="}));
		comboBox_1.setBounds(68, 21, 138, 23);
		panel_2.add(comboBox_1);
		
		panel_3 = new JPanel();
		panel_3.setBounds(0, 106, 217, 98);
		panel_1.add(panel_3);
		panel_3.setLayout(null);
		
		JLabel lblNumOfReviews = new JLabel("Num of Reviews:");
		lblNumOfReviews.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNumOfReviews.setBounds(0, 0, 143, 34);
		panel_3.add(lblNumOfReviews);
		
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setModel(new DefaultComboBoxModel(new String[] {"=", "<", ">", "<=", ">="}));
		comboBox_2.setBounds(61, 32, 143, 20);
		panel_3.add(comboBox_2);
		
		JLabel lblValue_1 = new JLabel("Value:");
		lblValue_1.setBounds(0, 62, 65, 22);
		panel_3.add(lblValue_1);
		
		textField_1 = new JTextField();
		textField_1.setBounds(61, 62, 143, 22);
		panel_3.add(textField_1);
		textField_1.setColumns(10);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBounds(0, 0, 10, 10);
		frame.getContentPane().add(panel_5);
		
		panel = new JPanel();
		panel.setBounds(21, 448, 627, 305);
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setForeground(new Color(0, 0, 0));
		panel.setToolTipText("");
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		
		panel_4 = new JPanel();
		panel_4.setBounds(926, 322, 502, 84);
		panel_4.setBorder(new LineBorder(new Color(0, 0, 0)));
		frame.getContentPane().add(panel_4);
		panel_4.setLayout(null);
		
		lblTagWeight = new JLabel("Tag Weight:");
		lblTagWeight.setBounds(21, 10, 121, 26);
		panel_4.add(lblTagWeight);
		
		lblValue_2 = new JLabel("Value:");
		lblValue_2.setBounds(21, 45, 92, 26);
		panel_4.add(lblValue_2);
		
		textField_2 = new JTextField();
		textField_2.setBounds(165, 45, 186, 26);
		panel_4.add(textField_2);
		textField_2.setColumns(10);
		
		comboBox_3 = new JComboBox();
		comboBox_3.setModel(new DefaultComboBoxModel(new String[] {"=", ">", "<", ">=", "<="}));
		comboBox_3.setBounds(163, 7, 188, 26);
		panel_4.add(comboBox_3);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBounds(693, 282, 231, 126);
		panel_6.setBorder(new LineBorder(new Color(0, 0, 0)));
		frame.getContentPane().add(panel_6);
		panel_6.setLayout(null);
		
		JLabel lblNewLabel_3 = new JLabel("Movie Year");
		lblNewLabel_3.setBackground(Color.GRAY);
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setBounds(0, 0, 231, 41);
		panel_6.add(lblNewLabel_3);
		
		JLabel lblFrom = new JLabel("from");
		lblFrom.setBounds(10, 52, 51, 26);
		panel_6.add(lblFrom);
		
		JLabel lblTo = new JLabel("to");
		lblTo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTo.setBounds(10, 79, 51, 26);
		panel_6.add(lblTo);
		
		JComboBox comboBox_4 = new JComboBox();
		comboBox_4.setBounds(62, 49, 148, 26);
		int min=years.get(0);
		int max=years.get(1);
		for(int i=min;i<=max;i++) {
			comboBox_4.addItem(i);
		}
		panel_6.add(comboBox_4);
		
		JComboBox comboBox_5 = new JComboBox();
		comboBox_5.setBounds(61, 82, 149, 26);
		for(int i=max;i>=min;i--) {
			comboBox_5.addItem(i);
		}
		panel_6.add(comboBox_5);
		
		panel_7 = new JPanel();
		panel_7.setBounds(0, 322, 692, 126);
		panel_7.setBackground(new Color(100, 149, 237));
		panel_7.setBorder(new LineBorder(new Color(0, 0, 0)));
		frame.getContentPane().add(panel_7);
		panel_7.setLayout(null);
		
		comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String Operation=comboBox.getSelectedItem().toString();
				op=Operation;
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"AND", "OR"}));
		comboBox.setBounds(469, 35, 172, 32);
		panel_7.add(comboBox);
		
		lblNewLabel = new JLabel("Search Between");
		lblNewLabel.setBounds(90, 21, 212, 26);
		panel_7.add(lblNewLabel);
		
		lblAttributesValues = new JLabel("Attributes' Values:");
		lblAttributesValues.setBounds(90, 56, 185, 26);
		panel_7.add(lblAttributesValues);
		
		btnNewButton_1 = new JButton("Execute Query");
		btnNewButton_1.setBounds(234, 235, 201, 35);
		panel.add(btnNewButton_1);
		
		textArea_2 = new JTextArea();
		textArea_2.setEditable(false);
		textArea_2.setFont(new Font("Monospaced", Font.PLAIN, 18));
		textArea_2.setBounds(201, 21, 405, 86);
		panel.add(textArea_2);
		
		lblMovieIdQuery = new JLabel("Movie ID Query:");
		lblMovieIdQuery.setBounds(21, 47, 159, 26);
		panel.add(lblMovieIdQuery);
		
		lblMovieAttrQuery = new JLabel("Movie Attr. Query:");
		lblMovieAttrQuery.setBounds(21, 158, 171, 26);
		panel.add(lblMovieAttrQuery);
		
		textArea_3 = new JTextArea();
		textArea_3.setEditable(false);
		textArea_3.setFont(new Font("Monospaced", Font.PLAIN, 18));
		textArea_3.setBounds(201, 134, 405, 86);
		panel.add(textArea_3);
		
		JScrollPane scrollPane_4 = new JScrollPane(textArea_2);
		scrollPane_4.setBounds(201, 21, 405, 86);
		panel.add(scrollPane_4);
		
		scrollPane_5 = new JScrollPane(textArea_3);
		scrollPane_5.setBounds(201, 134, 405, 84);
		panel.add(scrollPane_5);
		
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedG.size()==0) {
					JOptionPane.showMessageDialog(null, "Select at least one genre!", "Error", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					
					Integer rating = null;
					if(!textField.getText().equals(""))
						rating = Integer.parseInt(textField.getText());
					String symbol = comboBox_1.getSelectedItem().toString();
					Integer numReviews = null;
					if(!textField_1.getText().equals(""))
						numReviews = Integer.parseInt(textField_1.getText());
					String reviewSymbol = comboBox_2.getSelectedItem().toString();
					
					if( (Integer)comboBox_4.getSelectedItem()>(Integer) comboBox_5.getSelectedItem()) {
						JOptionPane.showMessageDialog(null, "Invalid range", "Error", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						from = (Integer) comboBox_4.getSelectedItem();
						to = (Integer) comboBox_5.getSelectedItem();
					}
					
					String tagWeightsymbol = comboBox_3.getSelectedItem().toString();
					String tagWeightValue = textField_2.getText();
					
					
					HashMap<Integer, String> ids = connection.getMovieID(selectedG, selectedC, selectedL, op, rating, symbol, numReviews, reviewSymbol, from, to, tagWeightsymbol, tagWeightValue);
					textArea.setText("");
					textArea_1.setText("");
					textArea_2.setText("");
					textArea_3.setText("");
					
					for(Integer id: ids.keySet()) {
						ArrayList<String> attr = connection.getAttributes(id);
						textArea.append(id+": "+attr+"\n");
						textArea_1.append(id+": "+ids.get(id)+"\n");
					}
					String q1=connection.getQuery1();
					String q2=connection.getQuery2();
					textArea_2.setText(q2);
					textArea_3.setText(q1);
				}
				
			}
		});
		
		
		JPanel panel_8 = new JPanel();
		panel_8.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_8.setBounds(693, 410, 735, 343);
		frame.getContentPane().add(panel_8);
		panel_8.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Result");
		lblNewLabel_1.setBounds(316, 0, 92, 26);
		panel_8.add(lblNewLabel_1);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
		textArea.setBounds(21, 47, 681, 230);
		panel_8.add(textArea);
		
		scrollPane_7 = new JScrollPane(textArea);
		scrollPane_7.setBounds(21, 47, 681, 230);
		panel_8.add(scrollPane_7);
		
		panel_9 = new JPanel();
		panel_9.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_9.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		panel_9.setBounds(926, 42, 502, 278);
		frame.getContentPane().add(panel_9);
		panel_9.setLayout(null);
		
		lblMovieTagValues = new JLabel("Movie Tag Values");
		lblMovieTagValues.setBounds(188, 21, 184, 26);
		panel_9.add(lblMovieTagValues);
		
		textArea_1 = new JTextArea();
		textArea_1.setEditable(false);
		textArea_1.setFont(new Font("Monospaced", Font.PLAIN, 18));
		textArea_1.setBounds(21, 67, 460, 190);
		panel_9.add(textArea_1);
		
		scrollPane_6 = new JScrollPane(textArea_1);
		scrollPane_6.setBounds(21, 70, 460, 187);
		panel_9.add(scrollPane_6);
	}
}
