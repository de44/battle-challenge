package battlechallenge.visual;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import battlechallenge.server.ServerPlayer;
import battlechallenge.structures.Structure;

/**
 * The Class BCViz.
 * 
 * 
 * TODO: Look into JLayeredPane
 * 	http://docs.oracle.com/javase/tutorial/uiswing/components/layeredpane.html
 * 	http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/uiswing/examples/components/LayeredPaneDemo2Project/src/components/LayeredPaneDemo2.java
 */
public class BCViz extends JFrame {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The Constant DEFAULT_HEIGHT_PX. */
	public static final int DEFAULT_HEIGHT_PX = 400;
	
	/** The Constant DEFAULT_WIDTH_PX. */
	public static final int DEFAULT_WIDTH_PX = 800;
	
	/** The Constant DEFAULT_ROW_OFFSET_PX. */
	public static final int DEFAULT_ROW_OFFSET_PX = 30;
	
	/** The Constant DEFAULT_COL_OFFSET_PX. */
	public static final int DEFAULT_COL_OFFSET_PX = 0;
	
	/** The TOTA l_ heigh t_ px. */
	public final int TOTAL_HEIGHT_PX;
	
	/** The TOTA l_ widt h_ px. */
	public final int TOTAL_WIDTH_PX;

	/** The total width. */
	private final int totalWidth;
	
	/** The total height. */
	private final int totalHeight;
	
	/** The bp. */
	private BoardPanel bp;
	
	/** The sc l. */
	private StatsContainer scL;
	
	/** The sc r. */
	private StatsContainer scR;
	
	private HeaderPanel hp;

	/**
	 * Instantiates a new bC viz.
	 *
	 * @param players the players
	 * @param structures the structures
	 * @param boardWidth the board width
	 * @param boardHeight the board height
	 */
	public BCViz(Collection<ServerPlayer> players, List<Structure> structures, int boardWidth,
			int boardHeight) {
		super("Battle Challenge Viz 0.1");
		this.totalWidth = boardWidth;
		this.totalHeight = boardHeight;
		this.TOTAL_WIDTH_PX = DEFAULT_WIDTH_PX;
		this.TOTAL_HEIGHT_PX = DEFAULT_HEIGHT_PX;
		switch (players.size()) {
		case 2 :
			scL = new StatsContainer(players);
			this.add(scL, BorderLayout.WEST);
			break;
		case 4 :
			scL = new StatsContainer(players);
			this.add(scL, BorderLayout.WEST);
			break;
		case 8 :;
		List<ServerPlayer> playersList = new LinkedList<ServerPlayer>(players);
		List<ServerPlayer> temp = new LinkedList<ServerPlayer>();
			for (int i=0;i<4;i++)
				temp.add(playersList.get(i));
			scL = new StatsContainer(temp);
			this.add(scL, BorderLayout.WEST);
			temp = new LinkedList<ServerPlayer>();
			for (int i=4;i<8;i++)
				temp.add(playersList.get(i));
			scR = new StatsContainer(temp);
			this.add(scR, BorderLayout.EAST);
			break;
		default :
			// error, invalid players list size
			return;
		}
		
		// add header panel
		this.hp = new HeaderPanel(players);
		this.add(hp, BorderLayout.NORTH);

		// add board panel
		bp = new BoardPanel(totalWidth, totalHeight, players, structures);
		this.add(bp, BorderLayout.CENTER);
		
		this.setSize(DEFAULT_WIDTH_PX, DEFAULT_HEIGHT_PX);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setVisible(true);
		
		this.validate();
	}
	
	/**
	 * Kill.
	 */
	public void kill() {
		this.dispose();
	}

	/**
	 * Update graphics.
	 */
	public void updateGraphics() {
		this.repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.Container#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		bp.repaint();
		scL.repaint();
		if (scR != null)
			scR.repaint();
		hp.repaint();
	}
}