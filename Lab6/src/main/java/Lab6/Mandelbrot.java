package Lab6;

import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator {
    
    // ��������� - max �������� ��� ���������� ������� �������� ������������
    public final int MAX_ITERATIONS = 2000;
    
    // �������� ��� FractalGenerator "����������" ����� ����������� �������
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2.0;
        range.y = -1.5;
        range.width = 3;
        range.height = 3;
    }

    // ���������� ����������� ������� �������� ������������
    public int numIterations(double x, double y) {
        double z_x = 0.;
        double z_y = 0.;
        for (int i = 0; i <= MAX_ITERATIONS; ++i) {
            double prev_z_x = z_x;
            z_x = z_x * z_x - z_y * z_y + x;
            z_y = 2 * prev_z_x * z_y + y;
            // ���������� ����� � if ����������� �������� ������ ���������
            // ��� pow() ����� ��� ��������� ��������
            if ((z_x * z_x + z_y * z_y) > 4.) {
                return i;
            }
        }
        // ����� �� ����� �� ������� ���������
        return -1;
    }
    
    @Override
    public String toString() {
        return "Mandelbrot";
    }
    
}
