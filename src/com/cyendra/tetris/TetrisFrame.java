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
 * ����˹����Ĵ�����
 * @author cyendra
 * */
public class TetrisFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	//��ť����ͼƬ
	private final static ImageIcon PAUSE_ICON = new ImageIcon("images/button-bg-pause.gif");
	private final static ImageIcon PAUSE_ON_ICON = new ImageIcon("images/button-bg-pause-on.gif");
	private final static ImageIcon RESUME_ICON = new ImageIcon("images/button-bg-resume.gif");
	private final static ImageIcon RESUME_ON_ICON = new ImageIcon("images/button-bg-resume-on.gif");
	private final static ImageIcon START_ICON = new ImageIcon("images/button-bg-start.gif");
	private final static ImageIcon START_ON_ICON = new ImageIcon("images/button-bg-start-on.gif");
	
	//��Ϸ��Ϊ������
	private GameManager gameManager;
	
	//��Ϸ�Ļ���
	private GamePanel gamePanel;

	//��Ϸѭ��
	TetrisTask tetrisTask;
	private Timer timer;
	
	//����
	private Box scoreTextBox = Box.createHorizontalBox();
	private Box scoreBox = Box.createHorizontalBox();
	private JLabel scoreTextLabel = new JLabel("��     ��");
	private JLabel scoreLabel = new JLabel();
	
	//����
	private Box levelTextBox = Box.createHorizontalBox();
	private Box levelBox = Box.createHorizontalBox();
	private JLabel levelTextLabel = new JLabel("��     ��");
	private JLabel levelLabel = new JLabel();
	
	//����
	private Box resumeBox = Box.createHorizontalBox();
	private JLabel resumeLabel = new JLabel();
	
	//��ͣ
	private Box pauseBox = Box.createHorizontalBox();
	private JLabel pauseLabel = new JLabel();
	
	//��ʼ
	private Box startBox = Box.createHorizontalBox();
	private JLabel startLabel = new JLabel();
	
	//��һ��
	private Box nextTextBox = Box.createHorizontalBox();
	private JLabel nextTextLabel = new JLabel("��һ��");
	
	//������
	private Box blankBox = Box.createHorizontalBox();
	private JPanel toolPanel = new JPanel();
	
	/**
	 * ����һ������
	 * ����initialize()����
	 * */	
	public TetrisFrame() {
		super();
		initialize();
	}
	
	/**
	 * ��ȡGamePanel
	 * �����������½�һ��
	 * */
	public GamePanel getGamePanel() {
		if (this.gamePanel == null) {
			this.gamePanel = new GamePanel(this);
		}
		return this.gamePanel;
	}
	
	/**
	 * ��ʼ������
	 * */	
	public void initialize() {
		this.gameManager = new GameManager(this);
		this.gamePanel = getGamePanel();
		
		//����
		BoxLayout toolPanelLayout = new BoxLayout(this.toolPanel, BoxLayout.Y_AXIS); 
		this.toolPanel.setLayout(toolPanelLayout);
		this.toolPanel.setBorder(new EtchedBorder());
		this.toolPanel.setBackground(Color.gray);
		
		//��ӷ���
		this.scoreTextBox.add(this.scoreTextLabel);
		this.scoreLabel.setText(String.valueOf(gameManager.getScore()));
		this.scoreBox.add(this.scoreLabel);
		
		//����
		this.levelTextBox.add(this.levelTextLabel);
		this.levelLabel.setText(String.valueOf(gameManager.getCurrentLevel()));
		this.levelBox.add(this.levelLabel);
		
		//������ť
		this.resumeLabel.setIcon(RESUME_ICON);
		this.resumeLabel.setPreferredSize(new Dimension(3, 25));
		this.resumeBox.add(this.resumeLabel);
		
		//��ͣ��ť
		this.pauseLabel.setIcon(PAUSE_ICON);
		this.pauseLabel.setPreferredSize(new Dimension(3, 25));
		this.pauseBox.add(this.pauseLabel);
		
		//��ʼ
		this.startLabel.setIcon(START_ICON);
		this.startLabel.setPreferredSize(new Dimension(3, 25));
		this.startBox.add(this.startLabel);
		
		//��һ��
		this.nextTextBox.add(this.nextTextLabel);
		
		//��Ӳ���
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
		
		//���ô�������
		this.setPreferredSize(new Dimension(349, 416));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(350, 200);
		this.setResizable(false);
		this.setTitle("����˹����");
		this.pack();
		initListeners();
	}
	
	//��ʼ��������
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
				//��ʼ����ʱ��
				if (timer != null) timer.cancel();
				timer = new Timer();
				tetrisTask = new TetrisTask(getTetrisFrame());
				int time = 1000 / gameManager.getCurrentLevel();
				timer.schedule(tetrisTask, 0, time);
				scoreLabel.setText(String.valueOf(gameManager.getScore()));
			}
		});
		//��Ӽ��̼�����
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				//��
				if (e.getKeyCode() == 38) gameManager.change();
				//��
				if (e.getKeyCode() == 40) gameManager.down();
				//��
				if (e.getKeyCode() == 37) gameManager.left(1);
				//��
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
		//�������ö�ʱ��
		this.timer.cancel();
		this.timer = new Timer();
		this.tetrisTask = new TetrisTask(this);
		int time = 1000 / gameManager.getCurrentLevel();
		timer.schedule(this.tetrisTask, 0, time);
 	}

}

class TetrisTask extends TimerTask {
	//���������
	private TetrisFrame frame;
	public TetrisTask(TetrisFrame frame) {
		this.frame = frame;
	}
	@Override
	public void run() {
		//�õ���ǰ�����˶��Ĵ󷽿�
		Piece currentPiece = this.frame.getGameManager().getCurrentPiece();
		//�жϿ����½����Ƿ����ϰ����ߵ��ײ�
		if (this.frame.getGameManager().isBlock() || this.frame.getGameManager().isButtom()) {
			this.frame.getGameManager().showNext();
			return;
		}
		currentPiece.setSquaresYLocation(Piece.SQUARE_BORDER);
		this.frame.getGamePanel().repaint();
	}
	
}
