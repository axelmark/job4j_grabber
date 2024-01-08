package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.Post;
import ru.job4j.grabber.utils.DateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {
    private static final String SOURCE_LINK = "https://career.habr.com";
    public static final String PREFIX = "/vacancies?page=";
    public static final String SUFFIX = "&q=Java%20developer&type=all";
    private final DateTimeParser dateTimeParser;
    private List<Post> posts = new ArrayList<>();

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> list(String link) {
        int pageNumber = 1;
        for (int i = pageNumber; i < 6; i++) {
            Connection connection = Jsoup.connect("%s%s".formatted(link, i));
            Document document = null;
            try {
                document = connection.get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                String vacancyName = titleElement.text();
                Element dateElement = row.select(".vacancy-card__date").first();
                String dateChild = dateElement.child(0).attr("datetime");
                String descLink = "%s%s".formatted(SOURCE_LINK, linkElement.attr("href"));
                String descriptions = retrieveDescription(descLink);
                posts.add(new Post(vacancyName, SOURCE_LINK, descriptions, dateTimeParser.parse(dateChild)));
            });
        }
        return posts;
    }

    private static String retrieveDescription(String link) {
        Connection connection = Jsoup.connect(link);
        String resString;
        try {
            Document document = connection.get();
            resString = document.select(".faded-content__body").text();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resString;
    }

    public static void main(String[] args) throws IOException {
      /*  int pageNumber = 1;
        String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, pageNumber, SUFFIX);*/
    }
}