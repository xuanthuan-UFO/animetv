package comm.xuanthuan.watchanime.Object;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class GetFromAsset {
    public static String getFromAssets(String fileName, Context context) {

        try {//from  ww w .ja  v a 2  s .com
            InputStreamReader inputReader = new InputStreamReader(context
                    .getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null) {
                Result += line;
            }
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
