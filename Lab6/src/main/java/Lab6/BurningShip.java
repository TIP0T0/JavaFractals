package Lab6;

import java.awt.geom.Rectangle2D;

public class BurningShip extends FractalGenerator {
    
    // константа - max итераций для вычисления функции фрактала Мандельброта
    public final int MAX_ITERATIONS = 4000;
    
    // указание для FractalGenerator "интересной" части комплексной области
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2.0;
        range.y = -2.5;
        range.width = 4;
        range.height = 4;
    }

    // вычисление итеративной функции фрактала Мандельброта
    public int numIterations(double x, double y) {
        double z_x = 0.;
        double z_y = 0.;
        for (int i = 0; i <= MAX_ITERATIONS; ++i) {
            double prev_z_x = z_x;
            z_x = z_x * z_x - z_y * z_y + x;
            z_y = Math.abs(2 * prev_z_x * z_y) + y;
            // отсутствие корня в if увеличивает скорость работы программы
            // нет pow() также для улучшения скорости
            if ((z_x * z_x + z_y * z_y) > 4.) {
                return i;
            }
        }
        // точка не вышла за пределы множества
        return -1;
    }
    
    @Override
    public String toString() {
        return "Burning Ship";
    }
    
}
