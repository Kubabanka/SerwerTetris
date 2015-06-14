import java.awt.*;
import java.util.EventListener;

/**
 * Interfejs, dziêki któremu mo¿emy zmieniaæ poziom wiadomoœci, jakie zapisujemy w logu.
 * Mo¿emy wybraæ jeden z trzech filtrów:
 * wszystko     - zbiera informacjê o tym, co przys³ali klienci oraz informacje o ich pod³¹czeniu.
 * nic          - nie zbiera niczego.
 * uzytkownicy  - u¿ytkownicy wci¹¿ mog¹ siê pod³¹czaæ, ale nie zostanie to zanotowane
 */

public interface LogChangedListener
        extends EventListener
{
    void logChanged(Event e);
}