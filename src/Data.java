import java.io.*;
import java.util.Arrays;


/**
 * Created by Jakub Ba�ka & Tomasz Duda
 * EiTI
 * 2015
 *
 * @author Jakub Ba�ka
 * @author Tomasz Duda
 */

/**
 * Klasa przechowuj�ca w sobie dane z plik�w
 */

public class Data {

    /**
     * Sta�a m�wi�ca ile blok�w szeroko�ci ma plansza.
     */
    int BoardWidth;

    /**
     * IP serwera
     */
    String hostIP;

    /**
     * Sta�a m�wi�ca ile blok�w wysoko�ci ma plansza.
     */
    int BoardHeight;

    /**
     * Wsp�czynnik skaluj�cy - wp�ywa na szybko�� opadania (im wi�kszy, tym wolniej opadaj� klocki)
     */
    int scale;

    /**
     * Ilo�� punkt�w za skasowanie jednej linii.
     */
    int lineScore;

    /**
     * Liczba punkt�w za zniszczenie jednego bloku przy wybuchu.
     */
    int blockScore;

    /**
     * Kara za u�ycie power-upu.
     */
    int penalty;

    /**
     * Liczba punkt�w za przej�cie na kolejny poziom.
     */
    int levelScore;

    /**
     * Pr�dko�� opadania klocka.
     */
    int speed;

    /**
     * Maksymalna ilo�� power-up�w.
     */
    int maxPowerUp;

    /**
     * Nazwa zawodnika.
     */
    String playerName;

    /**
     * Tablica poziom�w.
     */
    String [] levels;

    /**
     * Tablica najlepszych wynik�w.
     */
    int [] highScoreValues=new int[10];

    /**
     * Tablica nazw u�ytkownik�w z najlepszymi wynikami.
     */
    String [] highScoreNames = new String[10];

    /**
     * Metoda obs�uguj�ca �adowanie ustawie� gry.
     */
    public Data() {
        try {
            LoadProprieties();
            InitializeLevels();
            LoadHighScore();
        }catch (Exception e){}

    }
    /**
     * Metoda wczytuj�ca konfiguracj� z pliku config.properties
     *
     * @throws IOException
     */
    private void LoadProprieties() throws IOException {
        java.util.Properties properties = new java.util.Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("config.properties");
            properties.load(input);
            String _lineScore = properties.getProperty("lineScore");
            String _blockScore = properties.getProperty("blockScore");
            String _width = properties.getProperty("width");
            String _height = properties.getProperty("height");
            String _scale = properties.getProperty("scale");
            String _penalty = properties.getProperty("penalty");
            String _levelScore = properties.getProperty("levelScore");
            String _speed = properties.getProperty("speed");
            String _maxPowerUp = properties.getProperty("maxPowerUp");

            playerName = properties.getProperty("playerName");
            hostIP=properties.getProperty("hostIP");
            lineScore = Integer.parseInt(_lineScore);
            blockScore = Integer.parseInt(_blockScore);
            BoardWidth = Integer.parseInt(_width);
            BoardHeight = Integer.parseInt(_height);
            scale = Integer.parseInt(_scale);
            penalty = Integer.parseInt(_penalty);
            levelScore = Integer.parseInt(_levelScore);
            speed = Integer.parseInt(_speed);
            maxPowerUp = Integer.parseInt(_maxPowerUp);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    /**
     * Metoda inicjalizuj�ca poziomy.
     */
    private void InitializeLevels()
    {
        levels = new String[9];
        try{
            for (int i=0;i<=9;i++)
               LoadLevel(i+1);
        }catch (Exception e){}

    }

    /**
     * Metoda �aduj�ca poziomy
     * @param levelNumber Numer poziomu.
     * @throws IOException Wyj�tek.
     */
    private void LoadLevel(int levelNumber) throws IOException {
        BufferedWriter levelWriter = null;
        levels[levelNumber-1]="";
        try {
            BufferedReader levelParser = new BufferedReader(new FileReader(new File("levels/level" + levelNumber + ".eiti")));
           // levelWriter = new BufferedWriter(new FileWriter(new File("levels/level" + levelNumber + ".out")));
            levelNumber--;
            String line;
            while ((line = levelParser.readLine()) != null) {
                levels[levelNumber]+=(line+ "\r\n");
            }
        } catch (IOException e) {

        } finally {
            if (levelWriter != null) {
                try {
                    levelWriter.close();
                } catch (IOException e) {

                }
            }
        }
    }

    /**
     * Metoda odczytuj�ca najlepszy wynik z tablicy wynik�w (z pliku high_scores.eiti).
     */
    private void LoadHighScore() {
        try {
            BufferedReader hsParser = new BufferedReader(new FileReader(new File("high_scores.eiti")));

            String line;
            for (int i = 0; i < 10; ++i) {
                line = hsParser.readLine();
                String[] tmp = line.split(" ");
                highScoreNames[i] = tmp[0];
                highScoreValues[i] = Integer.valueOf(tmp[1]);
            }
        } catch (Exception e) {

        }
    }

    /**
     * Metoda s�u��ca zmianie rekordu w tablicy najlepszych wynik�w.
     * @param score wynik
     * @param name nazwa u�ytkownika
     */
    public int ChangeHighScore(int score, String name){
        if (score <= highScoreValues[9])
            return 0;
        else {
            highScoreNames[9] = name;
            highScoreValues[9] = score;
            for (int i=8;i>=0;i--)
                if (score<=highScoreValues[i])
                    break;
            else{
                    int tmp = highScoreValues[i];
                    highScoreValues[i] = score;
                    highScoreValues[i+1] = tmp;

                    String tmpName =highScoreNames[i];
                    highScoreNames[i]= name;
                    highScoreNames[i+1]=tmpName;
                }
        }
        try{SaveHighScore();}
        catch (Exception e){};
        return 1;
    }

    /**
     * Metoda s�u��ca zapisaniu najlepszego wyniku w pliku high_scores.eiti
     * @throws IOException Wyj�tek.
     */
   private void SaveHighScore()throws  IOException{
       PrintWriter pw=null;
       try{
           pw = new PrintWriter("high_scores.eiti");
           pw.print(HighScoreToString());
       }catch (Exception e){}
       finally {
           if (pw!=null)
               try{pw.close();}
               catch(Exception e){}
       }
   }

    /**
     * Parsowanie pliku z najlepszymi wynikami w celu �atwiejszego wys�ania.
     */
    public String HighScoreToString() {
        String result = "";
        for (int i=0;i<10;++i)
            result += highScoreNames[i] + " " + highScoreValues[i] + "\r\n";
        return result.trim();
    }

    /**
     * Parsowanie pliku konfiguracyjnego w celu �atwiejszego wys�ania.
     */
    public String ConfigToString(){
        String result = lineScore + " " +blockScore + " " + BoardWidth + " " + BoardHeight + " " +
                scale+ " " +playerName+ " " +penalty+ " " +levelScore+ " " +speed+ " " +maxPowerUp+" " + hostIP;
        return result;
    }

    /**
     * G��wna funkcja. Inizjalizuje oraz za�adowuje poziomy.
     */
    public static void main(String[] args) throws IOException {
        Data d = new Data();
        try {
            d.InitializeLevels();
            d.LoadHighScore();
            int x=0;
            x++;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(d.HighScoreToString());

    }
}
