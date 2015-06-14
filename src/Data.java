import java.io.*;
import java.util.Arrays;


/**
 * Created by Bañka on 2015-06-14.
 */
public class Data {

    /**
     * Sta³a mówi¹ca ile bloków szerokoœci ma plansza.
     */
    int BoardWidth;

    /**
     * Sta³a mówi¹ca ile bloków wysokoœci ma plansza.
     */
    int BoardHeight;

    int scale;

    /**
     * Iloœæ punktów za skasowanie jednej linii.
     */
    int lineScore;

    /**
     * Liczba punktów za zniszczenie jednego bloku przy wybuchu.
     */
    int blockScore;

    /**
     * Kara za u¿ycie power-upu.
     */
    int penalty;

    /**
     * Liczba punktów za przejœcie na kolejny poziom.
     */
    int levelScore;

    /**
     * Prêdkoœæ opadania klocka.
     */
    int speed;

    /**
     * Maksymalna iloœæ power-upów.
     */
    int maxPowerUp;

    /**
     * Nazwa zawodnika.
     */
    String playerName;

    String [] levels;

    int [] highScoreValues=new int[10];

    String [] highScoreNames = new String[10];

    public Data() {

    }
    /**
     * Metoda wczytuj¹ca konfiguracjê z pliku config.properties
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

    private void InitializeLevels()
    {
        levels = new String[5];
        try{
            for (int i=1;i<=5;++i)
               LoadLevel(i);
        }catch (Exception e){}

    }

    private void LoadLevel(int levelNumber) throws IOException {
        BufferedWriter levelWriter = null;
        levels[levelNumber]="";
        try {
            BufferedReader levelParser = new BufferedReader(new FileReader(new File("levels/level" + levelNumber + ".eiti")));
            levelWriter = new BufferedWriter(new FileWriter(new File("levels/level" + levelNumber + ".out")));

            String line;
            int row = BoardHeight - 1;
            while ((line = levelParser.readLine()) != null) {
                levels[levelNumber]+=line;
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

    private void LoadHighScore(){
        BufferedWriter hsWriter = null;
        try {
            BufferedReader hsParser = new BufferedReader(new FileReader(new File("hs.eiti")));
            hsWriter = new BufferedWriter(new FileWriter(new File("hs.out")));

            String line;
            for (int i=0; i<10; ++i){
                line = hsParser.readLine();
                String [] tmp = line.split(" ");
                highScoreNames[i] = tmp[0];
                highScoreValues[i] = Integer.valueOf(tmp[1]);
            }
        } catch (IOException e) {

        } finally {
            if (hsWriter != null) {
                try {
                    hsWriter.close();
                } catch (IOException e) {

                }
            }
        }
    }

    public void ChangeHighScore(int score, String name){
        if (score <= highScoreValues[9])
            return;
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
    }

   private void SaveHighScore()throws  IOException{
       PrintWriter pw=null;
       try{
           pw = new PrintWriter("hs.eiti");
           pw.print(HighScoreToString());
       }catch (Exception e){}
       finally {
           if (pw!=null)
               try{pw.close();}
               catch(Exception e){}
       }
   }

    public String HighScoreToString() {
        String result = "";
        for (int i=0;i<10;++i)
            result += highScoreNames[i] + " " + highScoreValues[i] + "\r\n";
        return result.trim();
    }

    public static void main(String[] args) throws IOException {
        Data d = new Data();
        d.LoadHighScore();
        d.ChangeHighScore(100,"Bahia");
        try {
            d.SaveHighScore();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String [] tmp = d.HighScoreToString().split("\\W+");
        System.out.println(d.HighScoreToString());
    }
}
