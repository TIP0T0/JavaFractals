package Lab6;

import javax.swing.JComponent;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.Graphics;


// наш графический интерфейс
public class JImageDisplay extends JComponent {
    // сслыка на изображение, с которым производится работа
    private BufferedImage image;
    
    public JImageDisplay(int width, int height) {
        // создаем объект - новое изображение
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Dimension dim = new Dimension(width, height);
        // метод род. класса
        // созданный компонент будет прорисовывать изображение сразу после того,
        // как он будет добавлен к польз. интерфейсу
        setPreferredSize(dim);
    }
    
    // чтоб Swing прорисовывал сам себя
    @Override
    public void paintComponent(Graphics g) {
        // для правильной прорисовки рамок и др. эл-ов
        super.paintComponent(g);
        // рисуем изображение в области компонента
        g.drawImage(image, 0,0, image.getWidth(),
                    image.getHeight(), null);
    }
    
    // закрашиваем компонент черным цветом
    public void clearImage() {
        for (int i = 0; i < image.getWidth(); ++i) {
            for (int j = 0; j < image.getHeight(); ++j) {
                image.setRGB(i, j, 0);
            }
        }
    }
    
    // задаем цвет пикселю
    public void drawPixel(int width, int height, int rgbColor) {
        image.setRGB(width, height, rgbColor);
    }
    
    public BufferedImage getImage() {
        return image;
    }

}
