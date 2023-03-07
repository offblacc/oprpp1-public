package hr.fer.oprpp1.hw08.jnotepadpp.statusbar;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Class that represents a clock as a label.
 */
public class Clock extends JLabel {
    /**
     * Current time
     */
    private volatile String time;

    /**
     * Bool indicating if the thread should be stopped
     */
    private volatile boolean stopRequested;

    /**
     * Formatter used to format time
     */
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").withZone(ZoneId.systemDefault());

    /**
     * Constructor that starts the clock thread
     */
    public Clock() {
        super();
        setHorizontalAlignment(SwingConstants.RIGHT);
        updateTime();
        Thread t = new Thread(() -> {
            while (!stopRequested) {
                updateTime();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    /**
     * Updates the time
     */
    private void updateTime() {
        time = formatter.format(LocalDateTime.now());
        SwingUtilities.invokeLater(() -> setText(time));
        repaint();
    }

    /**
     * Stops the clock thread
     */
    public void stop() {
        stopRequested = true;
    }

}
