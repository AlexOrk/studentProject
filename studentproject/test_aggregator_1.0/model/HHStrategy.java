package studentproject.model;

import studentproject.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HHStrategy implements Strategy {
//    private static final String URL_FORMAT = "http://hh.ua/search/vacancy?text=spanish+%s&page=%d";
    private static final String URL_FORMAT = "http://hh.ua/search/vacancy?text=%s+%s&page=%d";
//    private static final String URL_FORMAT = "https://javarush.ru/testdata/big28data.html";

    @Override
    public List<Vacancy> getVacancies(String searchString) {
        List<Vacancy> vacancyList = new ArrayList<>();
        int page = 0;
        try {
            while (true) {
                Document doc = getDocument(searchString, page);
                Elements vacancies = doc.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy");
                if (vacancies.size() == 0) break;

                for (Element element : vacancies) {
                    Vacancy vacancy = new Vacancy();
                    if (element != null) {
                        vacancy.setTitle(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title").text());
//                        System.out.println("Title: " + vacancy.getTitle());
                        vacancy.setCompanyName(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer").text().trim());
//                        System.out.println("Company name: " + vacancy.getCompanyName());
//                        URL url = new URL(doc.location());
//                        vacancy.setSiteName(url.getHost());
                        vacancy.setSiteName(URL_FORMAT.substring(0, 12));
//                        System.out.println("Site name: " + vacancy.getSiteName());
                        vacancy.setUrl(element.select("a").attr("href"));
//                        System.out.println("URL: " + vacancy.getUrl());
                        String salary = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-compensation").text();
                        vacancy.setSalary(salary.length() == 0 ? "" : salary);
//                        System.out.println("Salary: " + vacancy.getSalary());
                        vacancy.setCity(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-address").text());
//                        System.out.println("City: " + vacancy.getCity());
                        vacancyList.add(vacancy);
                    }
                }
                page++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vacancyList;
    }

    protected Document getDocument(String searchString, int page) throws IOException {
        Document doc = Jsoup.connect(String.format(URL_FORMAT, "java", searchString, page))
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36")
                .timeout(5000)
                .referrer("")
                .get();
        return doc;
    }
}

