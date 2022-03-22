import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class WebsiteMapCreator extends RecursiveTask<String> {

    private static final int MAX_DEPTH = 5;
    private static final int MAX_LINKS_ON_ONE_PAGE = 5;
    private static final List<String> visitedLinks = new LinkedList<>();

    private final String website;
    private final int depth;
    private final String domain;


    public WebsiteMapCreator(String website, String domain, int depth) {
        this.website = website;
        this.domain = domain;
        this.depth = depth;
    }

    @Override
    protected String compute() {

        String websiteMap = "";
        List<WebsiteMapCreator> taskList = new ArrayList<>();

        try {

            for(int i = 0; i < depth; i++) {
                websiteMap = websiteMap.concat("\t");
            }
            websiteMap = websiteMap.concat(website);
            websiteMap = websiteMap.concat("\n");
            visitedLinks.add(website);

            Thread.sleep(200);

            List<String> links = getLinks(website);

            if(depth < MAX_DEPTH) {
                for (String link : links) {
                    WebsiteMapCreator task = new WebsiteMapCreator(link, domain, depth + 1);
                    task.fork();
                    taskList.add(task);
                }
            }

            for (WebsiteMapCreator task : taskList) {
                websiteMap = websiteMap.concat(task.join());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return websiteMap;

    }

    private List<String> getLinks(String website) throws Exception {

        Document doc = Jsoup.connect(website).get();
        return doc.body().select("a").stream()
                .map(element -> element.attr("href"))
                .filter(link -> !link.contains("https://"))
                .filter(link -> !link.contains("@"))
                .filter(link -> !link.contains("#"))
                .map(domain::concat)
                .filter(link -> !visitedLinks.contains(link))
                .limit(MAX_LINKS_ON_ONE_PAGE)
                .toList();

    }

}
