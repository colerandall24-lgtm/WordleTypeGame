import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Testing {
    public static void main(String[] args) throws FileNotFoundException {
        /*String dictionary1 = "dic.txt";
        String dictionary2 = "dic1.txt";
        List<String> dic1 = loadFile(dictionary1);
        List<String> dic2 = loadFile(dictionary2);
        //int dic1Size = dic1.size();
        int dic2Size = dic2.size();
        *//*for (int i = 0; i < dic2Size; i++) {
            if (!dic1.contains(dic1.get(i))) {
                System.out.println(dic1.get(i));
            }
        }*//*
        for (String word : dic2) {
            if (!dic1.contains(word)) {
                System.out.println(word);
            }
        }*/
        InputStream inputStream = Testing.class.getResourceAsStream("/META-INF/dic1.txt");
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            String word = scanner.nextLine();
            if((word.substring(0,1)+ word.substring(2,3) + word.substring(4)).equals("pae")){
                System.out.println(word);
            }
        }
    }




    public static List<String> loadFile(String dictionary) throws FileNotFoundException {
        //Scanner dictScan = new Scanner(new File("dic.txt"));
        InputStream inputStream = Testing.class.getClassLoader().getResourceAsStream(dictionary);
        if (inputStream == null) {
            throw new FileNotFoundException("dic.txt not found in resources");
        }
        Scanner dictScan = new Scanner(inputStream);
        List<String> contents = new ArrayList<>();
        while (dictScan.hasNext()) {
            contents.add(dictScan.next());
        }
        return contents;
    }
}
