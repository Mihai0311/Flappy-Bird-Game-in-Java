import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class App {
    public static void main(String[] args) throws Exception {
        
        int boardWidth = 360;
        int boardHeight = 640;


        JFrame frame = new JFrame("Flappy Bird");
        
        frame.setVisible(true);
		frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);  //place the window at the center
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Flappy flappy_bird = new Flappy();

        frame.add(flappy_bird);
        frame.pack();
        flappy_bird.requestFocus();
        frame.setVisible(true);




    }
}
