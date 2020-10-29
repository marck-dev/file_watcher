import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.util.ArrayList;

public class WindowMain {
    private JPanel main;
    private JTextField source;
    private JTextArea log;
    private JTextArea targets;
    private JButton start;
    private Logger logger;
    private WatcherWorker ww;

    private String srcFolder;

    public  WindowMain(){
        ww = new WatcherWorker("");
        logger = new Logger(log);
        this.srcFolder = "";
        startEvents();
    }

    public WindowMain(String srcFolder){
        this();
        this.srcFolder = srcFolder;
        source.setText(srcFolder);

        ww = new WatcherWorker("C:/");
        ww.setLogger( logger );
    }

   private ArrayList<String> processTarget(){
        ArrayList<String> out = new ArrayList<>();
        String[] data = targets.getText().split("\n");
        for(String dir : data){
            System.out.println(dir);
            out.add(dir.trim());
        }
        return out;
    }

    private void startEvents(){
        source.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                srcFolder = source.getText();
                logger.info("Set new dir: " + srcFolder);
            }
        });
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ww.setSourceDir(srcFolder);
                ww.setTargets(processTarget());
                try {
                    ww.preStart();
                    ww.start();
                } catch (Exception exception) {
                    logger.error(exception.getMessage(), "WindowMain.actionEvent.start");
                }
            }
        });
    }

    public JPanel getMain() {
        return main;
    }

    public WatcherWorker getWw() {
        return ww;
    }

    public JTextArea getLog() {
        return log;
    }
}
