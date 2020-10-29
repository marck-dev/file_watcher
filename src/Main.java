import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        JFrame a = new JFrame("File Watcher");
        WindowMain m = new WindowMain("C:/");
        a.setContentPane(m.getMain());
        a.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        a.pack();

        WatcherWorker ww = new WatcherWorker("C:/");
        a.setVisible(true);
        a.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for(Thread t : Thread.getAllStackTraces().keySet())
                    if (t.getState() == Thread.State.RUNNABLE)
                        t.interrupt();
            }

        });
    }
}
