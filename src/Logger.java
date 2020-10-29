import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.Date;

public class Logger {
    private static final String FORMAT = "# %8s - %30s - %s - %s";;
    private JTextComponent logger;
    public Logger(JTextArea logger){
        this.logger = logger;
    }
    public Logger(JTextField logger){
        this.logger = logger;
    }

    public void log(String text, String method){
        Date now = new Date();
        String date = String.format("%tc", now);
        logger.setText(logger.getText() + "\n" + String.format(FORMAT, "LOG", date, method, text));
    }

    public void log(String text){
        Date now = new Date();
        String date = String.format("%tc", now);
        logger.setText(logger.getText() + "\n" + String.format(FORMAT, "LOG", date, "unknow", text));
    }

    public void warn(String text, String method){
        Date now = new Date();
        String date = String.format("%tc", now);
        logger.setText(logger.getText() + "\n" + String.format(FORMAT, "WARN", date, method, text));
    }
    public void warn(String text){
        Date now = new Date();
        String date = String.format("%tc", now);
        logger.setText(logger.getText() + "\n" + String.format(FORMAT, "WARN", date, "unknow", text));
    }


    public void error(String text, String method){
        Date now = new Date();
        String date = String.format("%tc", now);
        logger.setText(logger.getText() + "\n" + String.format(FORMAT, "ERROR", date, method, text));
    }

    public void error(String text){
        Date now = new Date();
        String date = String.format("%tc", now);
        logger.setText(logger.getText() + "\n" + String.format(FORMAT, "ERROR", date, "unknow", text));
    }

    public void info(String text, String method){
        Date now = new Date();
        String date = String.format("%tc", now);
        logger.setText(logger.getText() + "\n" + String.format(FORMAT, "INFO", date, method, text));
    }

    public void info(String text){
        Date now = new Date();
        String date = String.format("%tc", now);
        logger.setText(logger.getText() + "\n" + String.format(FORMAT, "INFO", date, "unknow", text));
    }
}
