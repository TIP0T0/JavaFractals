package Lab6;

import java.awt.geom.Rectangle2D;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JFileChooser;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.imageio.ImageIO;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;


public class FractalExplorer {
    // ������ ������ ����� ����������
    private final int display_size;
    // ������ �� ������ JImageDisplay
    private JImageDisplay img;
    // ������ �� ������� �����
    private FractalGenerator f_generator;
    // ������ �� ��������
    private final FractalGenerator mandelbrot;
    private final FractalGenerator tricorn;
    private final FractalGenerator burningShip;
    // ������ �� ������, ���. ������ ������������ �������� ����������� �������
    private final Rectangle2D.Double range;
    private JFrame frame;
    private int rows_remaining;
    private JComboBox f_list;
    private JButton reset_bt;
    private JButton save_bt;
    
    public FractalExplorer(int d_size) {
        display_size = d_size;
        range = new Rectangle2D.Double();
        mandelbrot = new Mandelbrot();
        mandelbrot.getInitialRange(range);
        tricorn = new Tricorn();
        burningShip = new BurningShip();
        f_generator = mandelbrot;
    }
    
    public void createAndShowGUI() {
        // �������������� ���� (frame)
        frame = new JFrame("Mandelbrot Fractal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // BorderLayout - ������ ��������, ��������� ����������� ���������� � 5
        // ��������� �������� ������������ ������� ����������
        // ������ ���������� ������ ������ ��������
        frame.setLayout(new BorderLayout());
        
        // ������� ��������� - ���������� ������
        f_list = new JComboBox();
        // ��������� �������� � ������
        f_list.addItem(mandelbrot);
        f_list.addItem(tricorn);
        f_list.addItem(burningShip);
        // ������������ ������� �� ���������� ������
        ActionListener bt_action_lnr = new ButtonActionListener();
        f_list.addActionListener(bt_action_lnr);
        
        // �������� ��������� ������, ���� ����� ������� ���������� ������
        JPanel f_list_panel = new JPanel();
        f_list_panel.setBackground(Color.GRAY);
        JLabel f_list_panel_label = new JLabel("Fractal: ");
        f_list_panel.add(f_list_panel_label);
        f_list_panel.add(f_list);
        
        // ������� ��������� - �����������
        img = new JImageDisplay(display_size, display_size);
        // ������������ ������� �� �������� �� ������ ����
        MouseListener img_ac_lnr = new ClickMouseAdapter();
        img.addMouseListener(img_ac_lnr);
        
        // ������� ��������� - ������ "�����"
        reset_bt = new JButton("Reset");
        reset_bt.setActionCommand("reset");
        reset_bt.setBackground(Color.yellow);
        // ������������ ������� �� ����� ������ �� ������� �� ��
        reset_bt.addActionListener(bt_action_lnr);
        
        // ������� ��������� - ������ "���������"
        save_bt = new JButton("Save");
        save_bt.setActionCommand("save");
        save_bt.setBackground(Color.green);
        // ������������ ������� �� ����� ������ �� ������� �� ��
        save_bt.addActionListener(bt_action_lnr);
        
        // �������� ������ ��� ������ ������ � ���������� �����������
        JPanel buttons_panel = new JPanel();
        buttons_panel.setBackground(Color.GRAY);
        buttons_panel.add(save_bt);
        buttons_panel.add(reset_bt);
        
        // ����������� ���������� �� ���� �������, ���-�� ��������� BorderLayout
        frame.add(img, BorderLayout.CENTER);
        frame.add(buttons_panel, BorderLayout.SOUTH);
        frame.add(f_list_panel, BorderLayout.NORTH);
        
        // ��� ����������� ����������� ����
        frame.pack();
        frame.setVisible(true);
        // ������ ���� ����������
        frame.setResizable(false);
    }
    
    // ������� ����������/������������� ����. ����������
    private void enableUI(boolean val) {
        f_list.setEnabled(val);
        reset_bt.setEnabled(val);
        save_bt.setEnabled(val);
    }
    
    /* ���������� ����� �������� ��� ����������� ��������
     * ��������� ��������� (������� ������ ������ �����������
     * � ������� ������ � �������������� �� ���������� ����������
     * ��� ���� ������)*/
    public void drawFractal() {
        // ��������� ������ � ����. �����. ����������
        enableUI(false);
        rows_remaining = display_size;
        // ��� ������� ���� ��������
        for (int i = 0; i < display_size; ++i) {
            SwingWorker<Object, Object> current_worker = new FractalWorker(i);
            current_worker.execute();
        }
    }
    
    // ����� ��� "���������" ���������� ����� �������� �����������
    // � ������� �����
    private class FractalWorker extends SwingWorker<Object, Object> {
       // ���, ��� �������� ����������� ����� � ������� ������
       private final int row;
       // ������ � ������� ��� ����� ����
       //private ArrayList<Integer> colours;
       private int[] colours;
       
       public FractalWorker(int y) {
           row = y;
       }
       
       // ����� ��������� ����� �������� ������ ������ � ������� ������
       // ��������� �������� ����� RGB ������
       @Override
       protected Object doInBackground() throws Exception {
           colours = new int[display_size];
           // ��������� ����������, ��������������� �������
           double yCoord = FractalGenerator.getCoord(range.y,
                   range.y + range.height, display_size, row);
           //  ��� ������� ������� � ��� ������
           for (int j = 0; j < display_size; ++j) {
                double xCoord = FractalGenerator.getCoord(range.x,
                                       range.x + range.width, display_size, j);
                // ������������ ���-�� �������� ��� ���� �����-�
                int num_iters = f_generator.numIterations(xCoord, yCoord);
                // ������ � ������ ���� � �����. �� ���-�� ��������
                if (num_iters == -1) {
                    colours[j] = 0;
                } else {
                    float hue = 0.7f + (float) num_iters / 150f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    colours[j] = rgbColor;
                }
            }
           return null;
       }
       
       // ����� ������������ ����� ������� ������ ������,
       // ���������������� ������ � ����. �����. ����������
       @Override
       protected void done() {
           for (int j = 0; j < display_size; ++j) {
               img.drawPixel(j, row, colours[j]);
               --rows_remaining;
           }
           img.repaint(0, 0, row, display_size, 1);
           if (rows_remaining == 0) {
               enableUI(true);
           }
       }
    }
    
    // ���������� ������� ������� ������ (��� JButton)
   private class ButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JComboBox) {
                JComboBox f_list = (JComboBox) e.getSource();
                f_generator = (FractalGenerator) f_list.getSelectedItem();
                f_generator.getInitialRange(range);
                drawFractal();
                img.repaint();
            } else if (e.getActionCommand().equals("reset")) {
                f_generator.getInitialRange(range);
                drawFractal();
                img.repaint();
            } else if (e.getActionCommand().equals("save")) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new FileNameExtensionFilter("PNG Images",
                        "png"));
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showSaveDialog(frame) ==
                        JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    try {
                        ImageIO.write(img.getImage(), "png", file);
                    } catch (IOException io_ex) {
                        JOptionPane.showMessageDialog(frame, io_ex.getMessage(),
                                "Writing error!", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
   }
   
   // ���������� ������� ������ ���� (��� JImageDisplay)
   private class ClickMouseAdapter extends MouseAdapter {
       @Override
       public void mouseClicked(MouseEvent me) {
           // ���������������� ������ � ����. �����. ����������
           if (0 == rows_remaining) {
               img.setEnabled(true);
           }
               double xCoord = FractalGenerator.getCoord(range.x,
                       range.x + range.width, display_size, me.getX());
               double yCoord = FractalGenerator.getCoord(range.y,
                       range.y + range.height, display_size, me.getY());
               f_generator.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
               drawFractal();
               img.repaint();
           
       }
   }
   
    public static void main(String[] args) {
        FractalExplorer my_fractal = new FractalExplorer(550);
        my_fractal.createAndShowGUI();
        my_fractal.drawFractal();
    }
    
}
