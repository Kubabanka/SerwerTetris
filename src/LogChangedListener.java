import java.awt.*;
import java.util.EventListener;

/**
 * Interfejs, dzi�ki kt�remu mo�emy zmienia� poziom wiadomo�ci, jakie zapisujemy w logu.
 * Mo�emy wybra� jeden z trzech filtr�w:
 * wszystko     - zbiera informacj� o tym, co przys�ali klienci oraz informacje o ich pod��czeniu.
 * nic          - nie zbiera niczego.
 * uzytkownicy  - u�ytkownicy wci�� mog� si� pod��cza�, ale nie zostanie to zanotowane
 */

public interface LogChangedListener
        extends EventListener
{
    void logChanged(Event e);
}