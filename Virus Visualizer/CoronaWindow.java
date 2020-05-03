import javax.swing.JFrame;

public class CoronaWindow
{
	JFrame frame;
    public CoronaWindow() 
    {
        
        frame = new JFrame("\"Fictional\" Disease");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		//frame.setUndecorated(true);
		frame.getContentPane().add(new CoronaDriver());
		frame.pack();
		frame.setFocusable(true);
		frame.setVisible(true);
    	
    }

    public static void main(String[] args)
    {
    	CoronaWindow myWind = new CoronaWindow();
    }
}
