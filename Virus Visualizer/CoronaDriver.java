import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.Scanner;
public class CoronaDriver extends JPanel implements ActionListener
{
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
	private final Color BROWN = new Color(160,82,45);
    private final double MAX_WIDTH = screenSize.getWidth();
    private final double MAX_HEIGHT =screenSize.getHeight();
    private final int RECOVER_TIME = 2000;
    private Random random;
    private JLabel label;
    private double startX;
    private double startY;
    
    private int numSick;
    private int numHealthy;
    private int numRecovered;
    private int MAX_PEOPLE;
    private ArrayList<Person> totalPeople;
    private ArrayList<Person> healthyPeople;
    private ArrayList<Person> sickPeople;
    private ArrayList<Person> recoveredPeople;
    private ArrayList<Rectangle2D> myRects;
    private Timer timer;
    
    public CoronaDriver() 
    {
    	random = new Random();
    	System.out.println("Enter the number of people in the town");
    	Scanner in = new Scanner(System.in);
    	if(in.hasNextInt())
    		MAX_PEOPLE = in.nextInt();
    	else
    	{
    		//default
    		MAX_PEOPLE = 40;
    	}
    	in.close();
    	setBackground(new Color(10,10,10));
    	setPreferredSize(new Dimension((int)MAX_WIDTH,(int)MAX_HEIGHT));
    	setFocusable(true);
    	totalPeople = new ArrayList<Person>();
    	healthyPeople = new ArrayList<Person>();
    	sickPeople = new ArrayList<Person>();
    	recoveredPeople = new ArrayList<Person>();
    	myRects = new ArrayList<Rectangle2D>();
    	initializeSim();
    }
    public int getRandomDxSpeed()
    {
		int randomDx = random.nextInt(2);
		int speed = 3;
		int dx = randomDx == 0 ? -speed : speed;
		return dx;
    }
    public int getRandomDySpeed()
    {
    	int randomDy = random.nextInt(2);
		int speed = 3;
		
		int dy = randomDy == 0 ? -speed : speed;
		return dy;
    }
    @Override
    public void paintComponent(Graphics g)
    {
        // calling super class paintComponent method
        // background will not be colored otherwise
        super.paintComponent(g);
        for(Person s : totalPeople)
        {
        	checkBounds(s);
        	for(Person pers : totalPeople)
        	{
        		if(s.isColliding(pers) && !pers.equals(s))
        			checkCollision(s,pers);
        	}
        	s.move(s.getDx(),s.getDy());
        	s.draw(g);	
        }
        int count = 1;
        for(Rectangle2D r : myRects)
        {
        	Graphics2D g2 = (Graphics2D) g;
        	if(count == 1)
        	{
        		//recovered
        		Color myC = new Color(Color.PINK.getRed()/255f,Color.PINK.getGreen()/255f,Color.PINK.getBlue()/255f,.6f);
        		g2.setColor(myC);    	
        		g2.fill(r);
        		count++;
        	}
        	else if (count == 2)
        	{
        		//healthy
        		Color myC = new Color(.678f,.847f,.902f,.6f);
        		g2.setColor(myC);    	
        		g2.fill(r);
        		count++;
        	}
        	else if(count == 3)
        	{
        		//sick
        		Color myC = new Color(BROWN.getRed()/255f,BROWN.getGreen()/255f,BROWN.getBlue()/255f,.6f);
        		g2.setColor(myC);
        		g2.fill(r);
        		count = 1;
        	}
        }
        try 
        {
			Thread.sleep(15);
			label.setText("<html>Healthy:  "+ numHealthy + "<br>Sick:  "+numSick+"<br>Recovered:  "+numRecovered+"</html>");
	        this.repaint();

		} 
        catch (InterruptedException e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    private void checkBounds(Person s)
    {
    	int randDx = random.nextInt(3) + 1;
    	int randDy = random.nextInt(3) + 1;
    	boolean topBorder = s.getX() <=MAX_WIDTH-s.getRadius() && s.getY() <=s.getRadius();
    	boolean rightBorder =s.getX() >= MAX_WIDTH -2*s.getRadius();
    	boolean bottomBorder = s.getY() >= MAX_HEIGHT - 3 *s.getRadius() && s.getX() <=MAX_WIDTH-s.getRadius();
    	boolean leftBorder = s.getX() <= 2*s.getRadius();
    	//top border
    	if(topBorder)
    	{
    		if(s.getDx() <0 && s.getDy() <0)
    		{
    			s.setDx(-randDx);
    			s.setDy(randDy);
    		}
    		else if(s.getDx() > 0 && s.getDy() <0)
    		{
    			s.setDx(randDx);
    			s.setDy(randDy);
    		}
    	}
    	//right border
    	else if(rightBorder)
    	{
    		if(s.getDx() > 0 && s.getDy() < 0)
    		{
    			s.setDx(-randDx);
    			s.setDy(-randDy);
    		}
    		else if(s.getDx() > 0 && s.getDy() > 0)
    		{
    			s.setDx(-randDx);
    			s.setDy(randDy);
    		}
    	}
    	//bottom border
    	else if(bottomBorder)
    	{
    		if(s.getDx() <0 && s.getDy() >0)
    		{
    			s.setDx(-randDx);
    			s.setDy(-randDy);
    		}
    		else if(s.getDx()>0 && s.getDy() > 0)
    		{
    			s.setDx(randDx);
    			s.setDy(-randDy);
    		}
    	}
    	//left border
    	else if(leftBorder) 
    	{
    		if(s.getDx() < 0 && s.getDy() < 0)
    		{
    			
    			s.setDx(randDx);
    			s.setDy(-randDy);
    		}
    		else if(s.getDx() < 0 && s.getDy() > 0)
    		{
    			s.setDx(randDx);
    			s.setDy(randDy);
    		}
    	}
    	int choice = random.nextInt(3);
    	//corner
    	if(bottomBorder && rightBorder)
    	{
    		switch(choice)
    		{
    		case 0:
    			s.setDx(0);
        		s.setDy(-randDy);
        		break;
    		case 1:
    			s.setDx(-randDx);
    			s.setDy(0);
    			break;
    		case 2:
    			s.setDx(-randDx);
    			s.setDy(-randDy);
    			break;
    		}
    		
    	}
    	else if(topBorder && leftBorder)
    	{
    		switch(choice)
    		{
    		case 0:
    			s.setDx(0);
        		s.setDy(randDy);
        		break;
    		case 1:
    			s.setDx(randDx);
    			s.setDy(0);
    			break;
    		case 2:
    			s.setDx(randDx);
    			s.setDy(randDy);
    			break;
    		}
    	}
    	else if(topBorder && rightBorder)
    	{
    		switch(choice)
    		{
    		case 0:
    			s.setDx(0);
        		s.setDy(-randDy);
        		break;
    		case 1:
    			s.setDx(-randDx);
    			s.setDy(0);
    			break;
    		case 2:
    			s.setDx(-randDx);
    			s.setDy(-randDy);
    			break;
    		}
    	}
    	else if(bottomBorder && leftBorder)
    	{
    		switch(choice)
    		{
    		case 0:
    			s.setDx(0);
        		s.setDy(-randDy);
        		break;
    		case 1:
    			s.setDx(randDx);
    			s.setDy(0);
    			break;
    		case 2:
    			s.setDx(randDx);
    			s.setDy(-randDy);
    			break;
    		}
    	}
    }
    private void checkCollision(Person s1,Person s2)
    {
    	int randDx = random.nextInt(3) + 1;
    	int randDy = random.nextInt(3) + 1;
    	boolean upRightS1 = s1.getDx() >= 0 && s1.getDy() <= 0;
    	boolean upRightS2 = s2.getDx() >= 0 && s2.getDy() <= 0;
    	boolean upLeftS1 = s1.getDx() <= 0 && s1.getDy() <= 0;
    	boolean upLeftS2 = s2.getDx() <= 0 && s2.getDy() <= 0;
    	boolean downLeftS1 = s1.getDx() <=0 && s1.getDy() >= 0;
    	boolean downRightS1 = s1.getDx() >= 0 && s1.getDy() >= 0;
    	boolean downLeftS2 = s2.getDx() <= 0 && s2.getDy() >= 0;
    	boolean downRightS2 = s2.getDx() >= 0 && s2.getDy() >=0;
    	checkBounds(s1);
    	checkBounds(s2);
    	if(s1.getHealthyStatus() && !s2.getHealthyStatus() &&!s1.getRecoveryStatus() && !s2.getRecoveryStatus())
		{
    		s1.setHealthyStatus(false);
    		s1.setFillColor(BROWN);
    		sickPeople.add(s1);
    		healthyPeople.remove(s1);
    		numSick++;
    		numHealthy--;
    		updateGraph();
		}
    	else if(!s1.getHealthyStatus() && s2.getHealthyStatus()&&!s1.getRecoveryStatus() && !s2.getRecoveryStatus())
    	{
    		s2.setHealthyStatus(false);
    		s2.setFillColor(BROWN);
    		sickPeople.add(s2);
    		numSick++;
    		healthyPeople.remove(s2);
    		numHealthy--;
    		updateGraph();
    	}
    	//s1 - up and right, s2 - down and left collision
    	if(upRightS1 && downLeftS2)
    	{
    		if(s1.getX() < s2.getX())
    		{
    			s1.setDx(-randDx);
        		s1.setDy(randDy);
        		s2.setDx(randDx);
        		s2.setDy(-randDy);
    		}
    		else
    		{
    			s1.setDx(randDx);
        		s1.setDy(-randDy);
        		s2.setDx(-randDx);
        		s2.setDy(randDy);
    		}
    	}
    	//s1 - up and right, s2 - up and left collision
    	else if(upRightS1 && upLeftS2)
    	{
    		if(s1.getX() < s2.getX())
    		{
    			s1.setDx(-randDx);
        		s1.setDy(-randDy);
        		s2.setDx(randDx);
        		s2.setDy(randDy);
    		}
    		else
    		{
    			s1.setDx(randDx);
        		s1.setDy(-randDy);
        		s2.setDx(-randDx);
        		s2.setDy(-randDy);
    		}
    		
    	}
    	//s1 - up and right, s2 - down and right
    	else if(upRightS1 && downRightS2)
    	{
    		if(s1.getY() > s2.getY())
    		{
    			s1.setDx(randDx);
        		s1.setDy(randDy);
        		s2.setDx(randDx);
        		s2.setDy(-randDy);
    		}
    		else
    		{
    			s1.setDx(randDx);
        		s1.setDy(-randDy);
        		s2.setDx(randDx);
        		s2.setDy(randDy);
    		}
    		
    	}
    	//s1 - down and right, s2, down and left
    	else if(downRightS1 && downLeftS2)
    	{
    		if(s1.getX() < s2.getX())
    		{
    			s1.setDx(-randDx);
        		s1.setDy(randDy);
        		s2.setDx(randDx);
        		s2.setDy(randDy);
    		}
    		else
    		{
    			s1.setDx(randDx);
        		s1.setDy(randDy);
        		s2.setDx(-randDx);
        		s2.setDy(randDy);
    		}
    		
    	}
    	//s1 - up and left, s2 - down and right
    	else if(upLeftS1 && downRightS2)
    	{
    		if(s1.getX() > s2.getX())
    		{
    			s1.setDx(randDx);
        		s1.setDy(randDy);
        		s2.setDx(-randDx);
        		s2.setDy(-randDy);
    		}
    		else
    		{
    			s1.setDx(-randDx);
        		s1.setDy(-randDy);
        		s2.setDx(randDx);
        		s2.setDy(randDy);
    		}
    		
    	}
    	//s1 - up and left, s2, down and left
    	else if(upLeftS1 && downLeftS2)
    	{
    		if(s1.getY() > s2.getY())
    		{
    			s1.setDx(-randDx);
        		s1.setDy(randDy);
        		s2.setDx(-randDx);
        		s2.setDy(-randDy);
    		}
    		else
    		{
    			s1.setDx(-randDx);
        		s1.setDy(-randDy);
        		s2.setDx(-randDx);
        		s2.setDy(randDy);
    		}
    		
    	}
    	else if(upRightS1 && upRightS2)
    	{
    		if(s1.getY() < s2.getY())
    		{
    			s1.setDx(randDx);
        		s1.setDy(-randDy);
        		s2.setDx(-randDx);
        		s2.setDy(randDy);
    		}
    		else
    		{
    			s1.setDx(-randDx);
        		s1.setDy(randDy);
        		s2.setDx(randDx);
        		s2.setDy(-randDy);
    		}
    		
    	}
    	else if(upLeftS1 && upLeftS2)
    	{
    		if(s1.getY() <= s2.getY())
    		{
    			s1.setDx(-randDx);
    			s1.setDy(-randDy);
    			s2.setDx(randDx);
    			s2.setDy(randDy);
    		}
    		else
    		{
    			s1.setDx(randDx);
    			s1.setDy(randDy);
    			s2.setDx(-randDx);
    			s2.setDy(-randDy);
    		}
    	}
    	else if(downLeftS1 && downLeftS2)
    	{
    		if(s1.getY() < s2.getY())
    		{
    			s1.setDx(randDx);
    			s1.setDy(-randDy);
    			s2.setDx(-randDx);
    			s2.setDy(randDy);
    		}
    		else
    		{
    			s1.setDx(-randDx);
    			s1.setDy(randDy);
    			s2.setDx(randDx);
    			s2.setDy(-randDy);
    		}
    	}
    	else if(downRightS1 && downRightS2)
    	{
    		if(s1.getY() < s2.getY())
    		{
    			s1.setDx(-randDx);
    			s1.setDy(-randDy);
    			s2.setDx(randDx);
    			s2.setDy(randDy);
    		}
    		else
    		{
    			s1.setDx(randDx);
    			s1.setDy(randDy);
    			s2.setDx(-randDx);
    			s2.setDy(-randDy);
    		}
    	}
    }
	private void initializeSim()
	{
		startX = 0;
		startY = 0;
		//pick random sick person
    	int index = random.nextInt(MAX_PEOPLE);
    	for(int i = 0; i < MAX_PEOPLE; i++)
    	{
    		int x = random.nextInt((int)MAX_WIDTH);
    		int y = random.nextInt((int)MAX_HEIGHT);
    		if(index == i)
    		{
    			Person pers = new Person(BROWN, x, y, 20, getRandomDxSpeed(), getRandomDySpeed(),false,false);
        		totalPeople.add(pers);
        		sickPeople.add(pers);
    		}
    		else
    		{
    			Person pers = new Person(Color.BLUE, x, y, 20, getRandomDxSpeed(), getRandomDySpeed(),true,false);
    			totalPeople.add(pers);
    			healthyPeople.add(pers);
    		}
    	}
    	numSick = sickPeople.size();
    	numRecovered = 0;
    	numHealthy = MAX_PEOPLE- numSick;
    	label = new JLabel("PlaceHold");
    	label.setFont(new Font("Verdana",1,20));
    	label.setForeground(Color.WHITE);
    	add(label);
    	timer = new Timer(RECOVER_TIME, this);
    	timer.start();
	}
    private void updateGraph()
    {
    	double percentageRecovered = (double) (numRecovered)/totalPeople.size();
    	double percentageHealthy =(double) (numHealthy) / totalPeople.size();
		double width = MAX_WIDTH/totalPeople.size();
		width/=2;

		double recoveredHeight = 100 * percentageRecovered;
		double healthyHeight =  100 *percentageHealthy;
		double startHealthyY = startY+recoveredHeight;
		double startSickY = startHealthyY + healthyHeight;
		Rectangle2D recoveredRect = new Rectangle2D.Double(startX,startY,width,recoveredHeight);
		myRects.add(recoveredRect);
		Rectangle2D healthyRect = new Rectangle2D.Double(startX, startHealthyY, width, healthyHeight);
		myRects.add(healthyRect);
		Rectangle2D sickRect = new Rectangle2D.Double(startX,startSickY,width,100-startSickY);
		myRects.add(sickRect);
		startX+=width;
    }
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		// TODO Auto-generated method stub
		int randChoice=0;
		if(numSick >0)
		{
			randChoice = random.nextInt(sickPeople.size());	
			Person pers = sickPeople.get(randChoice);
			
			pers.setFillColor(new Color(203,138,192));
			pers.setRecoveryStatus(true);
			sickPeople.set(randChoice, pers);
			recoveredPeople.add(sickPeople.get(randChoice));
			sickPeople.remove(randChoice);
			numSick--;
			numRecovered++;
			updateGraph();
			if(numRecovered+numHealthy == MAX_PEOPLE)
			{
				System.out.println("Everyone is cured");
				timer.stop();
				
			}
		}
		
	}

} 
