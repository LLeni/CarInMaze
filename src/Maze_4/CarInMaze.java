/** Animations on Java
	Made February 05 2017
	 */
package Maze_4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class CarInMaze {
	
	final String TITLE_OF_PROGRAM = "CarInMaze";
	final String TITLE_OF_HELP = "Help";
	final String GAME_OVER_MSG = "Game over";
	final String FINISH_MSG = "Finish";
	final String FINISH_SUBMSG = "Ура! Вы доехали до финиша";
	final String DEATH_OBSTRUCTION_MSG = "Вы врезались";
	final String DEATH_FUEL_OFF_MSG = "У вас закончилось топливо";
	final static int SIZE_POINT = 20; // и по x, и по y
	final static int FIELD_HEIGHT = 30; // in point
	final int FIELD_WIDTH = 40;
	final int HELP_HEIGHT = 20;
	final int HELP_WIDTH = 30;
	final int INTERFACE_WIDTH = 40;
	final int INTERFACE_HEIGHT = 5;
	final int OTSTUP_OT_KRAEV = 2;
	final int START_LOCATION_X = 400;
	final int START_LOCATION_Y = 100; 
	final int COUNTER_DELAY = 500;
	final int SHOW_DELAY = 1000;
	final int KEY_FUEL = 90; // Z
	final int KEY_RESTART = 82; // R
	final int KEY_HELP = 72; // H
	final int KEY_EXIT = 27; // Esc
	final int LEFT = 37;
	final int UP = 38 ;
	final int RIGHT = 39;
	final int DOWN = 40;
	final int STOP = 0;
	final int START_DIRECTION = 39;
	final int START_CAR_X = 2;
	final int START_CAR_Y = 1;
	final static Color MAZE_COLOR = Color.black;
	final Color BACKGROUND_COLOR = Color.white;
	final Color CAR_COLOR = Color.blue;
	final Color EXIT_COLOR = Color.green;
	final Color FUEL_COLOR = Color.red;
	final Color INTERFACE_COLOR = Color.LIGHT_GRAY;
	final Color TEXT_COLOR = Color.black;
	final int START_FUEL = 50;
	
	final String HELP_CAR = "Машина";
	final String HELP_MAZE = "Стена лабиринта";
	final String HELP_FUEL = "В интерфейсе - это топливо, на карте - это заправка";
	final String HELP_EXIT = "Выход из лабиринта";
	final String HELP_CONTROL_1 = "Управление машиной происходит стрелками.";
	final String HELP_CONTROL_2 = "Z Заправляться. H помощь. R рестарт";
	final String HELP_DEATH = "Вы проигрываете либо при столкновении, либо когда закончится топливо";
	
	JFrame frame;
	Canvas canvasPanel;
	Car car;
	FuelInterface fuel;
	File fileMusic;
	File fileFinishSound;
	File fileGameOverSound;
	Sound music;
	Sound gameOverSound;
	Sound finishSound;
	
	boolean pressPause = false;
	boolean pressRestart = true;
	static boolean pressExit = false;
	
	boolean gameOver = false;
	boolean finish = false;
	boolean deathFuelOff = false;
	boolean deathObstruction = false;
	ArrayList<Point> pointsMaze;
	ArrayList<Point> pointsStation;
	static int map[][] = {
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 0, 1, 0, 1, 1, 0, 2, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0 },
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
		{1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0 },
		{1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0 },
		{1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 1, 1 },
		{0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 2, 0, 0, 1, 0, 0 },
		{0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0 },
		{0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 2, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 2, 1 },
		{0, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 0 },
		{0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0 },
		{1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1 },
		{0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1 },
		{0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 2, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0 },
		{0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1 },
		{1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1 },
		{0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
		{0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
		{0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1 },
		{1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1 },
		{1, 0, 1, 2, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0 },
		{1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0 },
		{1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
		{1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 1, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1 },
		{0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 2, 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0 },
		{1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1 },
		{1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0 },
		{1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0 },
		{1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0 },
		{0, 2, 2, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
		{1, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1 },
		

	};
	int countFuel;
	int countFuelCopy;
	
	public class Music implements Runnable{
		String name;
		Thread music;
		
		Music(String name){
			this.name = name;
			music = new Thread(this,"Музыка");
			music.start();
		}
		
		@Override
		public void run() {
			Sound.playSound(name).join();
		}
		
		void stop(){
		}
		
	}
	

	public class Canvas extends JPanel {
		
		private static final long serialVersionUID = 1L;

		@Override
		public void paint(Graphics g){
			super.paint(g);
			if(pressPause == true){
				drawHelp(g);
			} else{
			car.paint(g);
			fuel.drawInterface(g);
			for (Point x: pointsMaze){
				x.paint(g);
			}
			
			for (Point x: pointsStation){
				x.paint(g);
			}
			if (gameOver) {
				if (!finish){
					music.stop();
					gameOverSound.play();
					g.setFont(new Font("Arial", Font.BOLD, 30));
					FontMetrics fm = g.getFontMetrics();
					g.setColor(Color.black);
					if (deathFuelOff){
						//g.drawChars(DEATH_FUEL_OFF_MSG.toCharArray(), 0, 25, FIELD_WIDTH * SIZE_POINT - OTSTUP_OT_KRAEV  * SIZE_POINT * 10, (FIELD_HEIGHT + OTSTUP_OT_KRAEV + 2) * SIZE_POINT);
						g.drawString(DEATH_FUEL_OFF_MSG, (FIELD_WIDTH * SIZE_POINT - fm.stringWidth(DEATH_FUEL_OFF_MSG) - OTSTUP_OT_KRAEV), ((FIELD_HEIGHT + (INTERFACE_HEIGHT/2)) * SIZE_POINT) + 10);
					} else {
						g.drawString(DEATH_OBSTRUCTION_MSG, (FIELD_WIDTH * SIZE_POINT - fm.stringWidth(DEATH_OBSTRUCTION_MSG) - OTSTUP_OT_KRAEV), ((FIELD_HEIGHT + (INTERFACE_HEIGHT/2)) * SIZE_POINT) + 10);
						//g.drawChars(DEATH_OBSTRUCTION_MSG.toCharArray(), 0, 15, FIELD_WIDTH * SIZE_POINT - OTSTUP_OT_KRAEV  * SIZE_POINT * 10, (FIELD_HEIGHT + OTSTUP_OT_KRAEV + 2) * SIZE_POINT);
					}
					g.setFont(new Font("Arial", Font.BOLD, 80));
					fm = g.getFontMetrics();
					g.setColor(Color.red);
					g.drawString(GAME_OVER_MSG, (FIELD_WIDTH * SIZE_POINT - fm.stringWidth(GAME_OVER_MSG))/2, (FIELD_HEIGHT * SIZE_POINT)/2);
					return;
				} else {
					finishSound.play();
					
					g.setFont(new Font("Arial", Font.BOLD, 30));
					FontMetrics fm = g.getFontMetrics();
					g.setColor(Color.magenta);
					g.drawString(FINISH_SUBMSG, (FIELD_WIDTH * SIZE_POINT - fm.stringWidth(FINISH_SUBMSG) - OTSTUP_OT_KRAEV), ((FIELD_HEIGHT + (INTERFACE_HEIGHT/2)) * SIZE_POINT) + 7);
					
					g.setFont(new Font("Arial", Font.BOLD, 80));
					fm = g.getFontMetrics();
					g.setColor(Color.orange);
					g.drawString(FINISH_MSG, (FIELD_WIDTH * SIZE_POINT - fm.stringWidth(FINISH_MSG))/2, (FIELD_HEIGHT * SIZE_POINT)/2);
					return;
				}
			}
		}
		}
	}
	
	public class Point extends CarInMaze {
		int x,y;
		Color color ;
		
		public Point(int x, int y, Color color){
			this.setXY(x, y);
			this.color = color;
		}
		

		public void paint(Graphics g){
			g.setColor(color);
			g.fillRect(x * SIZE_POINT, y * SIZE_POINT, SIZE_POINT, SIZE_POINT);
		}
	
		public int getX(){
			return x;
		}
		
		public int getY(){
			return y;
		}
		
		public void setXY(int x, int y){
			this.x = x;
			this.y = y;
		}
	}
	
	public class FuelInterface implements Runnable{
		Thread counter;
		
		FuelInterface(int countFuel){
			this.setCountFuel(countFuel);
			counter = new Thread(this,"Счетчик");
		}
		
		void setCountFuel(int countFuell){
			countFuel = countFuell;
		}
		
		void stopCounter(){
			counter.interrupt();
		}
		void restart(){
			counter.start();
		}
		
		void decrement(){
			countFuel--;
		}
		
		void startCounter(){
			counter.run();
			counter.start();
		}
		
		void drawInterface(Graphics g){
			g.setColor(INTERFACE_COLOR);
			g.fillRect(0, FIELD_HEIGHT * SIZE_POINT, INTERFACE_WIDTH * SIZE_POINT, INTERFACE_HEIGHT * SIZE_POINT);
			g.setColor(FUEL_COLOR);
			g.fillRect(OTSTUP_OT_KRAEV * SIZE_POINT,(FIELD_HEIGHT + OTSTUP_OT_KRAEV) * SIZE_POINT,SIZE_POINT,SIZE_POINT);
			g.setColor(TEXT_COLOR);
			if(countFuel>=10){
				g.drawChars(Integer.toString(countFuel).toCharArray(), 0, 2, OTSTUP_OT_KRAEV  * SIZE_POINT, (FIELD_HEIGHT + OTSTUP_OT_KRAEV + 2) * SIZE_POINT);
			} else {
				g.drawChars(Integer.toString(countFuel).toCharArray(), 0, 1, OTSTUP_OT_KRAEV  * SIZE_POINT, (FIELD_HEIGHT + OTSTUP_OT_KRAEV + 2) * SIZE_POINT);
			};
		}

		public void run() {
			while(!gameOver){
				while(!pressPause && !pressRestart){
				try {
					decrement();
					if(countFuel == 0){
						gameOver = true;
						deathFuelOff = true;
					}
					Thread.sleep(COUNTER_DELAY);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
				counter.interrupt();
			}
				
		}
	}
	
	public class Car{
		int x,y;
		int checkX, checkY;
		int direction;
		Point point, checkPoint;
		
		public Car(int x, int y, int direction){
			this.x = x;
			this.y = y;
			this.direction = direction;
		}
		
		void paint(Graphics g){
			point = new Point(x,y, CAR_COLOR);
			point.paint(g);
		}
		
		void move(){
			if (!gameOver){
				if (direction == LEFT) { x--; }
				if (direction == RIGHT) { x++; }
				if (direction == UP) { y--; }
				if (direction == DOWN) { y++; }
				if (x > FIELD_WIDTH - 1) { x = 0; }
				if (x < 0) { x = FIELD_WIDTH - 1; }
				if (y > FIELD_HEIGHT - 1) { y = 0; }
				if (y < 0) { y = FIELD_HEIGHT - 1; }
			}
		}
		
		boolean isFinish(){
			if (map[y][x] == 3){
				return true;
			} else {
				return false;
			}
		}
		
		void aroundStation(){
			if (x > 0 && x < 39 && y > 0 && y < 29 ){
				if ((map[y][x + 1] == 2) || (map[y][x - 1] == 2) || (map[y + 1][x] == 2) || (map[y - 1][x] == 2)){
					countFuel = 50;
				}
			}
			if (x == 0){
				if ((map[y][x + 1] == 2) || (map[y + 1][x] == 2) || (map[y - 1][x] == 2)){
					countFuel = 50;
				}
			}
			if (x == FIELD_WIDTH-1){
				if ((map[y][x - 1] == 2) || (map[y + 1][x] == 2) || (map[y - 1][x] == 2)){
					countFuel = 50;
				}
			}
			if (y == 0){
				if ((map[y][x + 1] == 2) || (map[y][x - 1] == 2) || (map[y + 1][x] == 2)){
					countFuel = 50;
				}
			}
			if (y == FIELD_HEIGHT-1){
				if ((map[y][x + 1] == 2) || (map[y][x - 1] == 2) ||(map[y - 1][x] == 2)){
					countFuel = 50;
				}
			}
		}
		
		void nextStation(){
			if (x >= 0 && x <= 39 && y >= 0 && y <= 29 ){
				if (direction == LEFT ){
					if(x == 0){
						if(map[y][FIELD_WIDTH-1] == 1 || map[y][FIELD_WIDTH-1] == 2){
							gameOver = true;
							deathObstruction = true;
						}	
					} else {
						if (map[y][x - 1] == 1 || map[y][x - 1] == 2){
							gameOver = true;
							deathObstruction = true;
						}
					}
				}
				if (direction == RIGHT ){
					if(x == FIELD_WIDTH-1){
						if(map[y][0] == 1 || map[y][0] == 2){
							gameOver = true;
							deathObstruction = true;
						} 	
					} else {
						if (map[y][x + 1] == 1 || map[y][x + 1] == 2){
							gameOver = true;
							deathObstruction = true;
						}
					}
				}
				if (direction == DOWN ){
					if(y == FIELD_HEIGHT-1){
						if(map[0][x] == 1 || map[0][x] == 2){
							gameOver = true;
							deathObstruction = true;
						} 	
					} else {
						if (map[y + 1][x] == 1 || map[y + 1][x] == 2){
							gameOver = true;
							deathObstruction = true;
						}
					}
				}
				if (direction == UP ){
					if(y == 0){
						if(map[FIELD_HEIGHT-1][x] == 1 || map[FIELD_HEIGHT-1][x] == 2){
							gameOver = true;
							deathObstruction = true;
						} 	
					} else {
						if (map[y - 1][x] == 1 || map[y - 1][x] == 2){
							gameOver = true;
							deathObstruction = true;
						}
					}
				}
			}
			
		}
		
		void setDirection(int direction) {
	        this.direction = direction;
	    }
	}
	
	public static void main(String[] args) {
		new CarInMaze().playGame();
	}
	
	void playGame(){
		startValue();
		while(!pressExit){
			if (pressRestart = true){
				fuel.restart();
				go();
			}
		}
	}
	
	void drawHelp(Graphics g){
		g.setFont(new Font("Arial", Font.BOLD, 50));
		FontMetrics fm = g.getFontMetrics();
		g.drawString(TITLE_OF_HELP, (FIELD_WIDTH * SIZE_POINT - fm.stringWidth(TITLE_OF_HELP))/2, 4 * SIZE_POINT);
		
		g.setColor(CAR_COLOR);
		g.fillRect((FIELD_WIDTH * SIZE_POINT)/5, (FIELD_HEIGHT * SIZE_POINT)/3,SIZE_POINT,SIZE_POINT);
		
		g.setColor(MAZE_COLOR);
		g.fillRect((FIELD_WIDTH * SIZE_POINT)/5, (FIELD_HEIGHT * SIZE_POINT)/3 + 3 *SIZE_POINT,SIZE_POINT,SIZE_POINT);
		
		g.setColor(FUEL_COLOR);
		g.fillRect((FIELD_WIDTH * SIZE_POINT)/5, (FIELD_HEIGHT * SIZE_POINT)/3 + 6 *SIZE_POINT,SIZE_POINT,SIZE_POINT);
		
		g.setColor(EXIT_COLOR);
		g.fillRect((FIELD_WIDTH * SIZE_POINT)/5, (FIELD_HEIGHT * SIZE_POINT)/3 + 9 * SIZE_POINT,SIZE_POINT,SIZE_POINT);
		
		g.setColor(INTERFACE_COLOR);
		g.fillRect(0, FIELD_HEIGHT * SIZE_POINT, INTERFACE_WIDTH * SIZE_POINT, INTERFACE_HEIGHT * SIZE_POINT);
		
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.setColor(TEXT_COLOR);
		g.drawChars(HELP_CAR.toCharArray(), 0, HELP_CAR.length(), (FIELD_WIDTH * SIZE_POINT)/5 + 4 *SIZE_POINT, (FIELD_HEIGHT * SIZE_POINT)/3 + SIZE_POINT);
		g.drawChars(HELP_MAZE.toCharArray(), 0, HELP_MAZE.length(), (FIELD_WIDTH * SIZE_POINT)/5 + 4 *SIZE_POINT, (FIELD_HEIGHT * SIZE_POINT)/3 + 4 * SIZE_POINT);
		g.setFont(new Font("Arial", Font.BOLD, 15));
		g.drawChars(HELP_FUEL.toCharArray(), 0, HELP_FUEL.length(), (FIELD_WIDTH * SIZE_POINT)/5 + 4 *SIZE_POINT, (FIELD_HEIGHT * SIZE_POINT)/3 + 7 * SIZE_POINT);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.drawChars(HELP_EXIT.toCharArray(), 0, HELP_EXIT.length(), (FIELD_WIDTH * SIZE_POINT)/5 + 4 *SIZE_POINT, (FIELD_HEIGHT * SIZE_POINT)/3 + 10 * SIZE_POINT);
		g.setFont(new Font("Arial", Font.BOLD, 15));
		g.drawChars(HELP_CONTROL_1.toCharArray(), 0, HELP_CONTROL_1.length(), OTSTUP_OT_KRAEV * SIZE_POINT, (FIELD_HEIGHT + OTSTUP_OT_KRAEV -1)* SIZE_POINT);
		g.drawChars(HELP_CONTROL_2.toCharArray(), 0, HELP_CONTROL_2.length(), (OTSTUP_OT_KRAEV -1) * SIZE_POINT, (FIELD_HEIGHT + OTSTUP_OT_KRAEV)* SIZE_POINT);
		g.drawChars(HELP_DEATH.toCharArray(), 0, HELP_DEATH.length(), OTSTUP_OT_KRAEV * SIZE_POINT, (FIELD_HEIGHT + OTSTUP_OT_KRAEV + 2)* SIZE_POINT);

	}
	
	void restart(){
		car.x = START_CAR_X;
		car.y = START_CAR_Y;
		countFuel = 50;
		gameOver = false;
		finish = false;
		deathFuelOff = false;
		deathObstruction = false;
		//music.stop();
	}
	
	void readMap(){
		pointsMaze = new ArrayList<Point>();
		pointsStation = new ArrayList<Point>();
		for (int x = 0; x<30; x++){
			for (int y = 0; y < 40; y++){
				if (map[x][y] == 1){
					pointsMaze.add(new Point(y,x, MAZE_COLOR));
				}
				if (map[x][y] == 2){
					pointsMaze.add(new Point(y,x, FUEL_COLOR));
				}
				if (map[x][y] == 3){
					pointsMaze.add(new Point(y,x, EXIT_COLOR));
				}
			}
		}	
	}
	
	void frame(){
		readMap();
		frame = new JFrame();
		frame.setSize(FIELD_WIDTH * SIZE_POINT, FIELD_HEIGHT * SIZE_POINT + INTERFACE_HEIGHT * SIZE_POINT);
		frame.setTitle(TITLE_OF_PROGRAM);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setLocation(START_LOCATION_X,START_LOCATION_Y);
		
		canvasPanel = new Canvas();
		canvasPanel.setBackground(BACKGROUND_COLOR);
		
		frame.getContentPane().add(BorderLayout.CENTER, canvasPanel);
		frame.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
	            if ((e.getKeyCode() >= LEFT) && (e.getKeyCode() <= DOWN) && pressPause == false){
	            	car.setDirection(e.getKeyCode());
	            	car.nextStation();
	            	car.move();
	            }
	            if (car.isFinish() == true){
	            	finish = true;
	            	gameOver = true;
	            }
	            if (e.getKeyCode() ==  KEY_FUEL && pressPause == false){
	            	car.aroundStation();
	            }
	            if (e.getKeyCode() == KEY_RESTART ){
	            	pressRestart = true;
	            	restart();
	            }
	            if (e.getKeyCode() == KEY_HELP){
	            	if (pressPause == true){
	            		pressPause = false;
	            		countFuel = countFuelCopy;
	            	} else{
	            		pressPause = true;
	            		countFuelCopy = countFuel;
	            	}
	    
	            }
	            if (e.getKeyCode() == KEY_EXIT){
	            	music.stop();
	            	gameOverSound.stop();
	            	finishSound.stop();
	            	frame.dispose();
	            }
			}
		});
		frame.setVisible(true);	
	}
	void startValue(){
		frame();
		car = new Car(START_CAR_X, START_CAR_Y, START_DIRECTION);
		fuel = new FuelInterface(START_FUEL) ;
		
		fileGameOverSound = new File("4.wav");
		gameOverSound = new Sound(fileGameOverSound);
		gameOverSound.setVolume(0.8f);
		
		fileFinishSound = new File("5.wav");
		finishSound = new Sound(fileFinishSound);
		finishSound.setVolume(0.9f);
		
		fileMusic = new File("3.wav");
		music =new Sound(fileMusic);
		music.setVolume(0.7f);
	}
	void go(){
		pressRestart = false;
		music.play();
		
//		//Это необходимо для того, чтобы интерфейс весь успел прорисоваться
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e2) {
//			e2.printStackTrace();
//		}
		while (!gameOver){
			try {
				Thread.sleep(40);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			canvasPanel.repaint();
		}
//		while(!pressExit || !pressRestart){
//			if (pressRestart){
//				gameOver = false;
//				finish = false;
//			}
//			
//			if (pressExit){
//				return;
//			}
//		}
			
			music.stop();
			canvasPanel.repaint();
			fuel.stopCounter();
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			finishSound.stop();
			gameOverSound.stop();
	}
	

	
}
