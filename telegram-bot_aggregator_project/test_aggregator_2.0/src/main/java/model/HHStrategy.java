package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HHStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(HHStrategy.class);
    private final static String URL_FORMAT = "http://hh.ru/search/vacancy?text=%s+%s&page=%d";

    @Override
    public List<Vacancy> getVacancies(String jobName, String city) {
        log.info("getVacancies method selected.");
        List<Vacancy> vacancyList = new ArrayList<>();
        int page = 0;
        try {
            while (true) {
                Document doc = getDocument(jobName, city, page);
                Elements vacancies = doc.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy");
                if (vacancies.size() == 0 || page == 39) break;

                for (Element element : vacancies) {
                    Vacancy vacancy = new Vacancy();
                    if (element != null) {
                        vacancy.setTitle(element.getElementsByAttributeValue(
                                "data-qa", "vacancy-serp__vacancy-title").text());
                        vacancy.setCompanyName(element.getElementsByAttributeValue(
                                "data-qa", "vacancy-serp__vacancy-employer").text().trim());
                        vacancy.setSiteName(URL_FORMAT.substring(0, 12));
                        vacancy.setUrl(element.select("a").attr("href"));
                        String salary = element.getElementsByAttributeValue(
                                "data-qa", "vacancy-serp__vacancy-compensation").text();
                        vacancy.setSalary(salary.length() == 0 ? "" : salary);
                        vacancy.setCity(element.getElementsByAttributeValue(
                                "data-qa", "vacancy-serp__vacancy-address").text());
                        vacancyList.add(vacancy);
                    }
                }
                page++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return vacancyList;
    }

    protected Document getDocument(String jobName, String city, int page) throws IOException {
        log.info("getDocument method selected.");
        String jobNameUtf8 = URLEncoder.encode(jobName, StandardCharsets.UTF_8);
        String cityUtf8 = URLEncoder.encode(city, StandardCharsets.UTF_8);
        Document doc = Jsoup.connect(String.format(URL_FORMAT, jobNameUtf8, cityUtf8, page))
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36")
                .timeout(5000)
                .referrer("")
                .get();
        return doc;
    }
}

