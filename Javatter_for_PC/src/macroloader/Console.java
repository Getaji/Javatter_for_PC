package macroloader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.io.Writer;

/**
 * javadoc here.
 *
 * @author Getaji
 */
public class Console extends JFrame {

    private final JTextArea area;

    private final Writer writer;

    public Console() {
        super("Console");
        area = new JTextArea();
        area.setBorder(new EmptyBorder(7, 7, 7, 7));
        area.setBackground(Color.BLACK);
        area.setForeground(Color.WHITE);
        area.setFont(new Font("MS PGothic", Font.PLAIN, 12));
        add(area);
        setSize(640, 480);

        writer = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                print(new String(cbuf));
            }

            @Override
            public void flush() throws IOException {
                //
            }

            @Override
            public void close() throws IOException {
                //
            }
        };
    }

    public void print(Object obj) {
        area.append(obj.toString());
    }

    public void println(Object obj) {
        print(obj.toString());
        area.append("\n");
    }

    public Writer getWriter() {
        return writer;
    }
}
