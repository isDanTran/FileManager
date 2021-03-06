/**
 * A modified JFrame class that will serve as the main window of the file manager.
 * @author Ian Ho-Sing-Loy
 * @author Brandon Moore
 * @author Bradley Nickle
 * @author Dan Tran
 */
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Our Window class.
 */
public class Window extends JFrame{
    // Version number
    final private double version = 1.0;
    private DirectoryPanel centerPanel;
    private FileManagerToolbar upperToolbar;
    private FileManagerUI ui;

    /***
     * Start a window.
     * @param args Required Java syntax for main()
     */
    public static void main(String[] args){
        Window w = new Window();
    }

    /**
     * Default Window constructor.
     * @author Bradley Nickle
     * @author Ian Ho-Sing-Loy
     */
    public Window(){
        open();
        configureCloseOperations();
    }

    /**
     * Configures what to do when the user closes the window.
     * In short, this adds a WindowAdapter that will save the window's size and
     * position on close.
     * @author Dan Tran
     * @author Bradley Nickle
     */
    private void configureCloseOperations(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                Window theWindow = (Window) e.getWindow();
                
                File file;
                file = new File("properties/sizePosition.txt");
                if (!file.exists()) {
                    try{
                        file.createNewFile();
                    }
                    catch (java.io.IOException ioe){
                        System.out.println(ioe.getMessage());
                    }
                }
                if (file.exists()){
                    try{
                        /* This line is for testing. It should tell you where the program
                        is saving sizePosition.txt to. */
                        System.out.println("path: " + file.getAbsolutePath());

                        // Write size and position data to the file.
                        PrintWriter pw = new PrintWriter(file);
                        pw.println(theWindow.getX());
                        pw.println(theWindow.getY());
                        pw.println(theWindow.getHeight());
                        pw.println(theWindow.getWidth());
                        pw.close();
                    }
                    catch (java.io.IOException ioe){
                        System.out.println(ioe.getMessage());
                    }
                }
                
                // Close the program.
                theWindow.dispose();
                System.exit(0);
            }
        });
    }

    /**
     * Configures the file manager as it opens
     * @author Brad Nickle
     * @author Ian Ho-Sing-Loy
     */
    private void open(){
        try {
            // Read from sizePosition.txt the parameters
            File file = new File("properties/sizePosition.txt");
            Scanner scanner = new Scanner(file);

            int XCoor = 0;
            int YCoor = 0;
            int width = 0;
            int height = 0;

            int i = 0;
            while(scanner.hasNextInt()){
                switch (i){
                    case 0:
                        XCoor = scanner.nextInt();
                    case 1:
                        YCoor = scanner.nextInt();
                    case 2:
                        width = scanner.nextInt();
                    case 3:
                        height = scanner.nextInt();
                }
                i++;
            }

            // Size the Window with w and h as parameters
            // Place the Window on the screen
            // call setBounds(x,y,w,h) from sizePosition.txt
            setBounds(XCoor, YCoor, width, height);
        } catch (java.io.IOException ioe){
            System.out.println(ioe.getMessage());
            setBounds(100,100,640,480);
        }

        // Name the Window
        setTitle("File Manager " + version);

        // Set up the file path for the Manager
        String path;
        try{
            path = System.getProperty("user.home");
        }
        catch (SecurityException se){
            System.out.println(se.getMessage());
            path = "";
        }
        catch (NullPointerException npe){
            System.out.println(npe.getMessage());
            path = "";
        }
        catch (IllegalArgumentException iae){
            System.out.println(iae.getMessage());
            path = "";
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            path = "";
        }

        // Initialize the Manager
        Manager mngr = new Manager(path);

        // Initialize the GUI components
        DirectoryPanel centerPanel = new DirectoryPanel(mngr);
        FileManagerToolbar upperToolbar = new FileManagerToolbar(mngr);
        FileManagerUI ui = new FileManagerUI(mngr,centerPanel,upperToolbar);

        // Set up the GUI
        setContentPane(ui);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
