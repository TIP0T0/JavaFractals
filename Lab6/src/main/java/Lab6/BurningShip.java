package Lab6;

import java.awt.geom.Rectangle2D;

public class BurningShip extends FractalGenerator {
    
    // ��������� - max �������� ��� ���������� ������� �������� ������������
    public final int MAX_ITERATIONS = 4000;
    
    // �������� ��� FractalGenerator "����������" ����� ����������� �������
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2.0;
        range.y = -2.5;
        range.width = 4;
        range.height = 4;
    }

    // ���������� ����������� ������� �������� ������������
    public int numIterations(double x, double y) {
        double z_x = 0.;
        double z_y = 0.;
        for (int i = 0; i <= MAX_ITERATIONS; ++i) {
            double prev_z_x = z_x;
            z_x = z_x * z_x - z_y * z_y + x;
            z_y = Math.abs(2 * prev_z_x * z_y) + y;
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
        return "Burning Ship";
    }
    
}
