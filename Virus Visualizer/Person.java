import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Person 
{
	private int radius;
	private int dx;
	private int dy;
	private Color fillColor;
	private Point Location;
	private boolean isHealthy;
	private boolean isRecovered;
	public Person(Color fillColor, int x, int y,int radius,int dx, int dy,boolean isHealthy, boolean isRecovered) 
	{
		this.radius=radius;
		this.dx =dx;
		this.dy = dy;
		this.fillColor = fillColor;
		this.dx = dx;
		this.dy = dy;
		this.isHealthy = isHealthy;
		this.isRecovered = isRecovered;
		Location = new Point(x,y);
	}
	
	public void draw(Graphics g)
	{
		g.setColor(getFillColor());
		
		g.fillOval(getX(), getY(), radius, radius);
		g.setColor(Color.BLACK);
		g.drawOval(getX(), getY(), radius, radius);
		
	}
	public int getRadius()
	{
		return radius;
	}
	public int getDx()
	{
		return dx;
	}
	public int getDy()
	{
		return dy;
	}
	public boolean getHealthyStatus()
	{
		return isHealthy;
	}
	public void setHealthyStatus(boolean status)
	{
		isHealthy = status;
	}
	public boolean getRecoveryStatus()
	{
		return isRecovered;
	}
	public void setRecoveryStatus(boolean status)
	{
		isRecovered = status;
	}
	public void setDx(int dx)
	{
		this.dx = dx;
	}
	public void setDy(int dy)
	{
		this.dy = dy;
	}
	public void setFillColor(Color c)
	{
		fillColor = c;
	}
	public Color getFillColor()
	{
		return fillColor;
	}
	
	public void setLocation(Point pt)
	{
		Location = pt;
	}
	public Point getLocation()
	{
		return Location;
	}
	public int getX()
	{
		return (int) getLocation().getX();
	}
	public void setX(int x)
	{
		getLocation().x = x;
	}
	public int getY()
	{
		return (int) getLocation().getY();
	}
	public void setY(int y)
	{
		getLocation().y = y;
	}
	public void move(int dx, int dy)
	{
		setX(getX() + dx);
		setY(getY() + dy);
	}
	public boolean isColliding(Person other)
	{
		Point personOne = getCenter(this);
		Point personTwo = getCenter(other);
		
		boolean inBoundsX = personOne.getX() < personTwo.getX() + other.getRadius() && personOne.getX() > personTwo.getX() - other.getRadius();
		boolean inBoundsY = personOne.getY() < personTwo.getY() + other.getRadius() && personOne.getY() > personTwo.getY() - other.getRadius();
		
		return inBoundsX && inBoundsY;                                                 
	}
	private Point getCenter(Person circ)
	{
		Point myCenter = new Point(circ.getX() + getRadius(), circ.getY() + getRadius());
		return myCenter;
	}
}
