import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Panelprueba extends JFrame
    implements ActionListener
{
    JPanel north;
    int i;

    public Panelprueba()
    {

    	north = new JPanel();

    	for (int i = 0; i < 5; i++)
    	{
    		JButton button = new JButton("North - " + i);
    		button.addActionListener(this);
    		north.add(button);
    	}

    	getContentPane().add(north, BorderLayout.NORTH);
        }

    public void actionPerformed(ActionEvent e)
    {
    	Component c = (Component)e.getSource();
    	c.setVisible(false);
	((JPanel)c.getParent()).revalidate();
    }

    public static void main(String[] args)
    {
    	Panelprueba frame = new Panelprueba();
    	frame.setDefaultCloseOperation( EXIT_ON_CLOSE );
    	frame.pack();
    	frame.setLocationRelativeTo( null );
    	frame.setVisible(true);
    }
}