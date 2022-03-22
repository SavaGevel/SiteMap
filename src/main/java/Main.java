import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ForkJoinPool;


public class Main {

    private static final int START_DEPTH = 0;

    public static void main(String[] args) {

        String path = "/Users/savelijgevel/desktop/websiteMap.txt";

        try {
            if(Files.notExists(Path.of(path))) {
                Files.createFile(Path.of(path));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String website = "https://lenta.ru";
        String domain = "https://lenta.ru";

        String websiteMap = new ForkJoinPool().invoke(new WebsiteMapCreator(website, domain, START_DEPTH));

        System.out.println(websiteMap);

        try(FileWriter writer = new FileWriter(path ,false)) {

            writer.write(websiteMap);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
