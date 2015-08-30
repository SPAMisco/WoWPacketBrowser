import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.Properties;

import javax.swing.JFrame;

import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.TextAnchor;

import javax.swing.JSplitPane;

import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.Toolkit;

import javax.swing.JTextField;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTabbedPane;

public class WoWPacketTools {
	public static String sql_url = "jdbc:mysql://localhost:3306/wod_world";
	public static String sql_user = "trinity";
	public static String sql_password = "Q1pEJ3";

	public static HashMap<Integer, G3DVector> editPoints = new HashMap<Integer, G3DVector>();

	private static JTable table;
	private static JTable table_1;
	private static JTextField txtSearchEntry;

	public static JFreeChart chart;

	public static Properties configFile;

	private static XYDataset createDataset() {
		LabeledXYDataset ds = new LabeledXYDataset();
		return ds;
	}

	private static class LabeledXYDataset extends AbstractXYDataset {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private static final int N = 26;
		private List<Number> x = new ArrayList<Number>(N);
		private List<Number> y = new ArrayList<Number>(N);
		private List<String> label = new ArrayList<String>(N);

		public void add(double x, double y, String label) {
			this.x.add(x);
			this.y.add(y);
			this.label.add(label);
		}

		public String getLabel(int series, int item) {
			return label.get(item);
		}

		@Override
		public int getSeriesCount() {
			return 1;
		}

		@Override
		public Comparable<String> getSeriesKey(int series) {
			return "Unit";
		}

		@Override
		public int getItemCount(int series) {
			return label.size();
		}

		@Override
		public Number getX(int series, int item) {
			return x.get(item);
		}

		@Override
		public Number getY(int series, int item) {
			return y.get(item);
		}
	}

	private static class LabelGenerator implements XYItemLabelGenerator {
		@Override
		public String generateLabel(XYDataset dataset, int series, int item) {
			LabeledXYDataset labelSource = (LabeledXYDataset) dataset;
			return labelSource.getLabel(series, item);
		}
	}

	private static JFreeChart createChart(final XYDataset dataset) {
		NumberAxis domain = new NumberAxis("X");
		NumberAxis range = new NumberAxis("Y");
		domain.setAutoRangeIncludesZero(false);
		XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
		renderer.setBaseItemLabelGenerator(new LabelGenerator());
		renderer.setBaseItemLabelPaint(Color.green.darker());
		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(
				ItemLabelAnchor.CENTER, TextAnchor.CENTER));
		renderer.setBaseItemLabelFont(renderer.getBaseItemLabelFont()
				.deriveFont(14f));
		renderer.setBaseItemLabelsVisible(true);
		renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());
		XYPlot plot = new XYPlot(dataset, domain, range, renderer);
		JFreeChart chart = new JFreeChart("WaypointPath",
				JFreeChart.DEFAULT_TITLE_FONT, plot, false);
		return chart;
	}

	public static void main(String[] args) {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); //$NON-NLS-1$
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}

		configFile = new Properties();
		try {
			InputStream input = new FileInputStream("config.properties");
			configFile.load(input);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		JFrame f = new JFrame();
		f.setIconImage(Toolkit.getDefaultToolkit().getImage(
				WoWPacketTools.class
						.getResource("/icons/world_of_warcraft_icon.png")));
		f.setTitle("WoW Packet Browser By Spamisco");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		XYDataset dataset = createDataset();
		chart = createChart(dataset);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		f.getContentPane().add(tabbedPane, BorderLayout.NORTH);

		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Waypoint Editor", null, panel_2, null);
		panel_2.setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		panel_2.add(splitPane, BorderLayout.CENTER);
		splitPane.setResizeWeight(0.9);

		@SuppressWarnings("serial")
		ChartPanel chartPanel = new ChartPanel(chart) {
			@Override
			public Dimension getPreferredSize() {
				return new Dimension(400, 320);
			}
		};
		splitPane.setLeftComponent(chartPanel);

		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setResizeWeight(0.7);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPane_1);

		JPanel panel = new JPanel();
		splitPane_1.setLeftComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JMenuBar menuBar = new JMenuBar();
		panel.add(menuBar, BorderLayout.NORTH);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int[] rows = table.getSelectedRows();
				if (rows.length > 0) {
					for (int i : rows)
						editPoints.remove(i);

					List<G3DVector> tmp = new ArrayList<G3DVector>();
					tmp.addAll(editPoints.values());

					editPoints.clear();

					int id = 0;
					for (G3DVector v : tmp) {
						editPoints.put(id, v);
						id++;
					}

					drawVectorsToTableAndGraph(editPoints);

				}
			}
		});
		menuBar.add(btnDelete);

		JButton btnCopygo = new JButton("Copy .go xyz");
		btnCopygo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int row = table.getSelectedRow();

				if (row == -1)
					return;

				String myString = ".go " + table.getValueAt(row, 1) + " "
						+ table.getValueAt(row, 2) + " "
						+ table.getValueAt(row, 3);
				StringSelection stringSelection = new StringSelection(myString);
				Clipboard clpbrd = Toolkit.getDefaultToolkit()
						.getSystemClipboard();
				clpbrd.setContents(stringSelection, null);
			}
		});

		JButton btnWriteToDb = new JButton("Write to DB");
		menuBar.add(btnWriteToDb);
		btnWriteToDb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String input = JOptionPane.showInputDialog(f, "Enter DB GUID:");
				if (input != null) {
					savePathToDB(Long.valueOf(input), new ArrayList<G3DVector>(
							editPoints.values()));
				}
			}
		});

		JButton btnLoadSniff = new JButton("Load sniff");
		menuBar.add(btnLoadSniff);
		btnLoadSniff.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();

				String defaultPath = configFile.getProperty("FileDefaultPath");
				if (defaultPath != null)
					fileChooser.setCurrentDirectory(new File(defaultPath));

				if (fileChooser.showOpenDialog(f) == JFileChooser.APPROVE_OPTION) {
					try {
						OutputStream output = new FileOutputStream(
								"config.properties");
						configFile.setProperty("FileDefaultPath", fileChooser
								.getSelectedFile().getAbsolutePath());
						configFile.store(output, null);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

					MovementPackets.waypoints.clear();
					PackerParser.parseSniffFile(fileChooser.getSelectedFile());

					DefaultTableModel model = new DefaultTableModel();
					model.addColumn("Entry");
					model.addColumn("GUID");
					table_1.setModel(model);

					for (Entry<Long, List<BigInteger>> entry : MovementPackets.guids
							.entrySet()) {
						for (BigInteger guid : entry.getValue()) {
							List<Object> lst = new ArrayList<Object>();
							lst.add(entry.getKey());
							lst.add(guid);
							model.addRow(lst.toArray());
						}
					}
				}
			}
		});
		menuBar.add(btnCopygo);

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] {

		}, new String[] { "ID", "X", "Y", "Z", "Orientation", "Delay" }));
		scrollPane.setViewportView(table);

		JPanel panel_1 = new JPanel();
		splitPane_1.setRightComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		JMenuBar menuBar_1 = new JMenuBar();
		panel_1.add(menuBar_1, BorderLayout.NORTH);

		JButton btnLoadPath = new JButton("Load path");
		btnLoadPath.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int rowId = table_1.getSelectedRow();
				if (rowId == -1)
					return;

				BigInteger guid = (BigInteger) table_1.getValueAt(rowId, 1);

				if (!MovementPackets.waypoints.containsKey(guid))
					return;

				editPoints.clear();

				int id = 0;
				for (G3DVector vec : MovementPackets.waypoints.get(guid)) {
					editPoints.put(id++, vec);
				}

				drawVectorsToTableAndGraph(editPoints);
			}
		});
		menuBar_1.add(btnLoadPath);

		JButton btnSearch = new JButton("Search");
		btnSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				updateEntrySearch(txtSearchEntry.getText());
			}
		});
		menuBar_1.add(btnSearch);

		txtSearchEntry = new JTextField();
		menuBar_1.add(txtSearchEntry);
		txtSearchEntry.setColumns(10);

		JScrollPane scrollPane_1 = new JScrollPane();
		panel_1.add(scrollPane_1, BorderLayout.CENTER);

		table_1 = new JTable();
		table_1.setModel(new DefaultTableModel(new Object[][] {}, new String[] {
				"Entry", "GUID" }));
		scrollPane_1.setViewportView(table_1);

		JMenuBar menuBar_2 = new JMenuBar();
		panel_2.add(menuBar_2, BorderLayout.NORTH);

		JButton btnFilterDuplicatePoints = new JButton("Filter Duplications");
		menuBar_2.add(btnFilterDuplicatePoints);
		btnFilterDuplicatePoints.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				filterPointsByMask(0, true);
			}
		});

		JButton btnRemovePoints = new JButton("Remove Points");
		btnRemovePoints.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				filterPointsByMask(2 | 4 | 8 | 16, false);
			}
		});
		menuBar_2.add(btnRemovePoints);

		JButton btnRemoveWaypoints = new JButton("Remove WayPoints");
		btnRemoveWaypoints.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				filterPointsByMask(1 | 4 | 8 | 16, false);
			}
		});
		menuBar_2.add(btnRemoveWaypoints);

		JButton btnRemoveDestinations = new JButton("Remove Destinations");
		btnRemoveDestinations.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				filterPointsByMask(1 | 2 | 8 | 16, false);
			}
		});
		menuBar_2.add(btnRemoveDestinations);

		JButton btnRemoveUpdatePoints = new JButton("Remove Update Points");
		btnRemoveUpdatePoints.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				filterPointsByMask(1 | 2 | 4 | 16, false);
			}
		});
		menuBar_2.add(btnRemoveUpdatePoints);

		JButton btnRemoveUpdatePos = new JButton("Remove Update Pos");
		btnRemoveUpdatePos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				filterPointsByMask(1 | 2 | 4 | 8, false);
			}
		});
		menuBar_2.add(btnRemoveUpdatePos);

		JButton btnAddBackpath = new JButton("Add backpath");
		menuBar_2.add(btnAddBackpath);

		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Ability editor", null, panel_3, null);
		btnAddBackpath.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				List<G3DVector> tmp = new ArrayList<G3DVector>();
				tmp.addAll(editPoints.values());

				ListIterator<G3DVector> li = tmp.listIterator(tmp.size());

				int id = 0;
				int index = 0;
				while (li.hasPrevious()) {
					G3DVector vector = li.previous();

					if (id != 0 && tmp.size() != (id + 1))
						editPoints.put(tmp.size() + index++, vector);

					id++;
				}

				drawVectorsToTableAndGraph(editPoints);
			}
		});
		f.setSize(1024, 640);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	public static void drawVectorsToTableAndGraph(
			HashMap<Integer, G3DVector> editPoints) {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("ID");
		model.addColumn("X");
		model.addColumn("Y");
		model.addColumn("Z");
		model.addColumn("Orientation");
		model.addColumn("Delay");

		LabeledXYDataset ds = new LabeledXYDataset();

		for (Entry<Integer, G3DVector> v : editPoints.entrySet()) {
			G3DVector point = v.getValue();

			ds.add(Math.abs(point.x), Math.abs(point.y),
					String.valueOf(v.getKey()));

			List<Object> lst = new ArrayList<Object>();
			lst.add(v.getKey());
			lst.add(point.x);
			lst.add(point.y);
			lst.add(point.z);
			lst.add(point.o);
			lst.add(point.delay);
			model.addRow(lst.toArray());
		}

		chart.getXYPlot().setDataset(ds);

		model.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent evt) {
				int col = evt.getColumn();
				int row = evt.getLastRow();

				if ((col == 5 || col == 4) && row != -1) {
					int id = (int) table.getValueAt(row, 0);
					editPoints.get(id).delay = Integer.valueOf(table
							.getValueAt(row, 5).toString());
					editPoints.get(id).o = Float.valueOf(table.getValueAt(row,
							4).toString());
				}
			}
		});

		table.setModel(model);
	}

	public static void updateEntrySearch(String key) {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Entry");
		model.addColumn("GUID");
		table_1.setModel(model);

		if (!key.matches("[+-]?\\d*(\\.\\d+)?"))
			return;

		if (!MovementPackets.guids.containsKey(Long.valueOf(key))) {
			for (Entry<Long, List<BigInteger>> entry : MovementPackets.guids
					.entrySet()) {
				if (entry.getKey() == 0)
					continue;

				for (BigInteger guid : entry.getValue()) {
					List<Object> lst = new ArrayList<Object>();
					lst.add(entry.getKey());
					lst.add(guid);
					model.addRow(lst.toArray());
				}
			}
			return;
		}

		for (BigInteger guid : MovementPackets.guids.get(Long.valueOf(key))) {
			List<Object> lst = new ArrayList<Object>();
			lst.add(Long.valueOf(key));
			lst.add(guid);
			model.addRow(lst.toArray());
		}
	}

	public static void filterPointsByMask(int type, boolean duplication) {
		List<G3DVector> tmp = new ArrayList<G3DVector>();
		List<G3DVector> filtered = new ArrayList<G3DVector>();
		tmp.addAll(editPoints.values());

		for (G3DVector v : tmp) {
			boolean found = false;
			if (duplication)
				for (G3DVector v2 : filtered)
					if (v.Distance(v2) < 0.15f && v.o == 0.0f)
						found = true;

			if (!found) {
				if (type == 0)
					filtered.add(v);
				else if ((type & v.type) == v.type)
					filtered.add(v);
			}
		}

		editPoints.clear();

		int id = 0;
		for (G3DVector v : filtered) {
			editPoints.put(id, v);
			id++;
		}

		drawVectorsToTableAndGraph(editPoints);
	}

	public static void savePathToDB(long guid, List<G3DVector> points) {
		java.sql.Connection con = null;
		java.sql.Statement st = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(sql_url, sql_user, sql_password);
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM creature_addon WHERE guid = "
					+ guid);

			G3DVector fp = points.get(0);

			st = con.createStatement();
			st.executeUpdate("UPDATE creature SET MovementType = 2, position_x = '"
					+ fp.x
					+ "', position_y = '"
					+ fp.y
					+ "', position_z = '"
					+ fp.z + "' WHERE guid = " + guid);
			if (rs.next()) {
				System.out.println(rs.getInt(1));
				st = con.createStatement();
				st.executeUpdate("UPDATE creature_addon SET path_id = "
						+ (guid * 10) + " WHERE guid = " + guid);
			} else {
				st.executeUpdate("INSERT INTO creature_addon (guid, path_id, mount, bytes1, bytes2, emote, auras) VALUES ("
						+ guid + ", " + (guid * 10) + ", 0, 0, 0, 0, '')");
			}

			long wpPath = guid * 10;

			st = con.createStatement();
			st.executeUpdate("DELETE FROM waypoint_data WHERE id = " + wpPath);

			PreparedStatement stmt = con
					.prepareStatement("INSERT INTO `waypoint_data` (`id`, `point`, `position_x`, `position_y`, `position_z`, `orientation`, `delay`) VALUES (?, ?, ?, ?, ?, ?, ?);");

			int id = 0;
			for (G3DVector v : points) {
				stmt.setLong(1, wpPath);
				stmt.setInt(2, id);
				stmt.setFloat(3, v.x);
				stmt.setFloat(4, v.y);
				stmt.setFloat(5, v.z);
				stmt.setFloat(6, v.o);
				stmt.setInt(7, v.delay);
				stmt.executeUpdate();
				id++;
			}

			rs.close();
			st.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
