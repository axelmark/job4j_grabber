package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.time.*;

public class HabrCareerParse {

    private static final String SOURCE_LINK = "https://career.habr.com";
    public static final String PREFIX = "/vacancies?page=";
    public static final String SUFFIX = "&q=Java%20developer&type=all";

    public static void main(String[] args) throws IOException {
        int pageNumber = 1;
        HabrCareerDateTimeParser timeParser = new HabrCareerDateTimeParser();
        for (int i = pageNumber; i < 6; i++) {
            System.out.println("страница " + i);
            String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, pageNumber, SUFFIX);
            Connection connection = Jsoup.connect(fullLink);
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                String vacancyName = titleElement.text();

                Element dateElement = row.select(".vacancy-card__date").first();
                String dateChild = dateElement.child(0).attr("datetime");

                LocalDateTime dateTime = timeParser.parse(dateChild);
                String link = String.format("%s%s %s", SOURCE_LINK, linkElement.attr("href"), dateTime);
                System.out.printf("%s %s%n", vacancyName, link);
            });
        }
    }

    private String retrieveDescription(String link) {
        return null;
    }
}