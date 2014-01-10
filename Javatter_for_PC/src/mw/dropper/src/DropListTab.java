package mw.dropper.src;

import com.orekyuu.javatter.view.IJavatterTab;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.*;
import java.util.List;


public class DropListTab implements IJavatterTab, MouseListener {
    private JPanel panelTop = new JPanel();
    private JPanel panelButton = new JPanel();
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList list = new JList(listModel);
    private JScrollPane scrollPane = new JScrollPane(list);

    private JLabel labelButtonNew = new JLabel(getImageIcon("new.png"));
    private JLabel labelButtonEdit = new JLabel(getImageIcon("edit.png"));
    private JLabel labelButtonDelete = new JLabel(getImageIcon("delete.png"));

    private final Color colorAlpha = new Color(0, 0, 0, 0);

    private List<String> filterList;

    private FileWriter fileWriter;

    public DropListTab(List<String> filterList) {
        panelTop.setLayout(new BorderLayout());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        labelButtonNew.addMouseListener(this);
        labelButtonEdit.addMouseListener(this);
        labelButtonDelete.addMouseListener(this);

        panelButton.add(labelButtonNew);
        panelButton.add(labelButtonEdit);
        panelButton.add(labelButtonDelete);
        panelTop.add(panelButton, BorderLayout.NORTH);
        panelTop.add(scrollPane, BorderLayout.CENTER);

        this.filterList = filterList;

        File file = new File("./plugins/dropfilter.txt");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.equals(""))
                    continue;
                listModel.addElement(line);
                filterList.add(line);
            }
            scanner.close();

            fileWriter = new FileWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Component getComponent() {
        return panelTop;
    }

    private ImageIcon getImageIcon(String url) {
        ImageIcon icon = null;
        try {

            icon = new ImageIcon(ImageIO.read(Dropper.class.getResourceAsStream(url)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return icon;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().equals(labelButtonNew)) {
            new FilterItemEditor(FilterItemEditor.NEW, this).setVisible(true);
        } else if (e.getSource().equals(labelButtonEdit)) {
            new FilterItemEditor(list.getSelectedValue().toString(), FilterItemEditor.EDIT,
                    this, list.getSelectedIndex()).setVisible(true);
        } else if (e.getSource().equals(labelButtonDelete)) {
            filterList.remove(list.getSelectedIndex());
            listModel.remove(list.getSelectedIndex());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        ((JLabel) e.getSource()).setBackground(Color.GRAY);
        labelButtonEdit.repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        ((JLabel) e.getSource()).setBackground(colorAlpha);
        panelButton.repaint();
    }

    public void addFilter(String filterText) {
        if (filterText.equals(""))
            return;

        listModel.addElement(filterText);
        filterList.add(filterText);
        try {
            fileWriter.append(filterText + "\n");
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        panelTop.repaint();
    }

    public void editFilter(int index, String text) {
        listModel.removeElementAt(index);
        filterList.remove(index);

        if (!text.equals("")) {
            listModel.add(index, text);
            filterList.add(index, text);
            try {
                fileWriter.write("");
                for (String filter: filterList)
                    fileWriter.append(filter + "\n");

                fileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        panelTop.repaint();
    }


}
