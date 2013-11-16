package com.cyendra.tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.cyendra.tetris.object.Piece;
import com.cyendra.tetris.util.ImageUtil;
/**
 * 俄罗斯方块的窗口类
 * @author cyendra
 * */
public class TetrisFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	//按钮所用图片
	private final static ImageIcon PAUSE_ICON = new ImageIcon("images/button-bg-pause.gif");
	private final static ImageIcon PAUSE_ON_ICON = new ImageIcon("images/button-bg-pause-on.gif");
	private final static ImageIcon RESUME_ICON = new ImageIcon("images/button-bg-resume.gif");
	private final static ImageIcon RESUME_ON_ICON = new ImageIcon("images/button-bg-resume-on.gif");
	private final static ImageIcon START_ICON = new ImageIcon("images/button-bg-start.gif");
	private final static ImageIcon START_ON_ICON = new ImageIcon("images/button-bg-start-on.gif");
	
	//游戏行为管理器
	private GameManager gameManager;
	
	//游戏的画板
	private GamePanel gamePanel;

	//游戏循环
	TetrisTask tetrisTask;
	private Timer timer;
	
	//分数
	private Box scoreTextBox = Box.createHorizontalBox();
	private Box scoreBox = Box.createHorizontalBox();
	private JLabel scoreTextLabel = new JLabel("分     数");
	private JLabel scoreLabel = new JLabel();
	
	//级别
	private Box levelTextBox = Box.createHorizontalBox();
	private Box levelBox = Box.createHorizontalBox();
	private JLabel levelTextLabel = new JLabel("级     别");
	private JLabel levelLabel = new JLabel();
	
	//继续
	private Box resumeBox = Box.createHorizontalBox();
	private JLabel resumeLabel = new JLabel();
	
	//暂停
	private Box pauseBox = Box.createHorizontalBox();
	private JLabel pauseLabel = new JLabel();
	
	//开始
	private Box startBox = Box.createHorizontalBox();
	private JLabel startLabel = new JLabel();
	
	//下一个
	private Box nextTextBox = Box.createHorizontalBox();
	private JLabel nextTextLabel = new JLabel("下一个");
	
	//工具栏
	private Box blankBox = Box.createHorizontalBox();
	private JPanel toolPanel = new JPanel();
	
	/**
	 * 构造一个窗口
	 * 调用initialize()方法
	 * */	
	public TetrisFrame() {
		super();
		initialize();
	}
	
	/**
	 * 获取GamePanel
	 * 若不存在则新建一个
	 * */
	public GamePanel getGamePanel() {
		if (this.gamePanel == null) {
			this.gamePanel = new GamePanel(this);
		}
		return this.gamePanel;
	}
	
	/**
	 * 初始化窗口
	 * */	
	public void initialize() {
		this.gameManager = new GameManager(this);
		this.gamePanel = getGamePanel();
		
		//布局
		BoxLayout toolPanelLayout = new BoxLayout(this.toolPanel, BoxLayout.Y_AXIS); 
		this.toolPanel.setLayout(toolPanelLayout);
		this.toolPanel.setBorder(new EtchedBorder());
		this.toolPanel.setBackground(Color.gray);
		
		//添加分数
		this.scoreTextBox.add(this.scoreTextLabel);
		this.scoreLabel.setText(String.valueOf(gameManager.getScore()));
		this.scoreBox.add(this.scoreLabel);
		
		//级别
		this.levelTextBox.add(this.levelTextLabel);
		this.levelLabel.setText(String.valueOf(gameManager.getCurrentLevel()));
		this.levelBox.add(this.levelLabel);
		
		//继续按钮
		this.resumeLabel.setIcon(RESUME_ICON);
		this.resumeLabel.setPreferredSize(new Dimension(3, 25));
		this.resumeBox.add(this.resumeLabel);
		
		//暂停按钮
		this.pauseLabel.setIcon(PAUSE_ICON);
		this.pauseLabel.setPreferredSize(new Dimension(3, 25));
		this.pauseBox.add(this.pauseLabel);
		
		//开始
		this.startLabel.setIcon(START_ICON);
		this.startLabel.setPreferredSize(new Dimension(3, 25));
		this.startBox.add(this.startLabel);
		
		//下一个
		this.nextTextBox.add(this.nextTextLabel);
		
		//添加布局
		this.toolPanel.add(Box.createVerticalStrut(10));
		this.toolPanel.add(scoreTextBox);
		this.toolPanel.add(Box.createVerticalStrut(10));
		this.toolPanel.add(scoreBox);
		this.toolPanel.add(Box.createVerticalStrut(10));
		this.toolPanel.add(levelTextBox);
		this.toolPanel.add(Box.createVerticalStrut(10));
		this.toolPanel.add(levelBox);
		this.toolPanel.add(Box.createVerticalStrut(15));
		this.toolPanel.add(this.resumeBox);
		this.toolPanel.add(Box.createVerticalStrut(15));
		this.toolPanel.add(this.pauseBox);
		this.toolPanel.add(Box.createVerticalStrut(15));
		this.toolPanel.add(this.startBox);
		this.toolPanel.add(Box.createVerticalStrut(30));
		this.toolPanel.add(this.nextTextBox);
		this.blankBox.add(Box.createHorizontalStrut(99));
		this.toolPanel.add(blankBox);				
		this.add(this.gamePanel, BorderLayout.CENTER);
		this.add(this.toolPanel, BorderLayout.EAST);
		
		//设置窗口属性
		this.setPreferredSize(new Dimension(349, 416));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(350, 200);
		this.setResizable(false);
		this.setTitle("俄罗斯方块");
		this.pack();
		initListeners();
	}
	
	//初始化监听器
	private void initListeners() {
		this.resumeLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				resumeLabel.setIcon(RESUME_ON_ICON);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				resumeLabel.setIcon(RESUME_ICON);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!gameManager.getPauseFlag()) return;
				gameManager.resume();
				timer = new Timer();
				tetrisTask = new TetrisTask(getTetrisFrame());
				int time = 1000 / gameManager.getCurrentLevel();
				timer.schedule(tetrisTask, 0, time);
			}
		});
		this.pauseLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				pauseLabel.setIcon(PAUSE_ON_ICON);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				pauseLabel.setIcon(PAUSE_ICON);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				gameManager.pause();
				if (timer != null) timer.cancel();
				timer = null;
			}
		});
		this.startLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				startLabel.setIcon(START_ON_ICON);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				startLabel.setIcon(START_ICON);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				gameManager.start();				
				//初始化定时器
				if (timer != null) timer.cancel();
				timer = new Timer();
				tetrisTask = new TetrisTask(getTetrisFrame());
				int time = 1000 / gameManager.getCurrentLevel();
				timer.schedule(tetrisTask, 0, time);
				scoreLabel.setText(String.valueOf(gameManager.getScore()));
			}
		});
		//添加键盘监听器
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				//上
				if (e.getKeyCode() == 38) gameManager.change();
				//下
				if (e.getKeyCode() == 40) gameManager.down();
				//左
				if (e.getKeyCode() == 37) gameManager.left(1);
				//右
				if (e.getKeyCode() == 39) gameManager.right(1);
			}
		});
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (gameManager.getNextPiece() == null) return;
		ImageUtil.paintPiece(g, gameManager.getNextPiece());
	}

	public GameManager getGameManager() {
		return this.gameManager;
	}
	
 	public TetrisFrame getTetrisFrame() {
 		return this;
 	}
 	
 	public void clearGame() {
		repaint();
		timer.cancel();
 	}
 	
 	public void setScore(int score) {
 		this.scoreLabel.setText(String.valueOf(score));
 	}
 	public void setLevel(int level) {
 		this.levelLabel.setText(String.valueOf(level));
		//重新设置定时器
		this.timer.cancel();
		this.timer = new Timer();
		this.tetrisTask = new TetrisTask(this);
		int time = 1000 / gameManager.getCurrentLevel();
		timer.schedule(this.tetrisTask, 0, time);
 	}

}

class TetrisTask extends TimerTask {
	//主界面对象
	private TetrisFrame frame;
	public TetrisTask(TetrisFrame frame) {
		this.frame = frame;
	}
	@Override
	public void run() {
		//得到当前正在运动的大方块
		Piece currentPiece = this.frame.getGameManager().getCurrentPiece();
		//判断快整下降后是否有障碍或者到底部
		if (this.frame.getGameManager().isBlock() || this.frame.getGameManager().isButtom()) {
			this.frame.getGameManager().showNext();
			return;
		}
		currentPiece.setSquaresYLocation(Piece.SQUARE_BORDER);
		this.frame.getGamePanel().repaint();
	}
	
}
