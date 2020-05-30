package studentproject.model;

import studentproject.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MoikrugStrategy implements Strategy {
    private static final String URL_FORMAT = "https://moikrug.ru/vacancies?page=%d&q=%s+%s";
    @Override
    public List<Vacancy> getVacancies(String searchString) {
        List<Vacancy> vacancyList = new ArrayList<>();
        int page = 0;
        try {
            while (true) {
                Document doc = getDocument(searchString, page);
                Elements vacancies = doc.getElementsByClass("job");
                if (vacancies.size() == 0) break;

                for (Element element : vacancies) {
                    Vacancy vacancy = new Vacancy();
                    if (element != null) {
                        vacancy.setTitle(element.getElementsByAttributeValue("class", "title").text());
//                        System.out.println("Title: " + vacancy.getTitle());
                        vacancy.setCompanyName(element.getElementsByAttributeValue("class", "company_name").text().trim());
//                        System.out.println("Company name: " + vacancy.getCompanyName());
                        vacancy.setSiteName(URL_FORMAT.substring(0, 18));
//                        System.out.println("Site name: " + vacancy.getSiteName());
                        vacancy.setUrl(URL_FORMAT.substring(0, 18) + element.getElementsByAttributeValue("class", "title")
                                .select("a").attr("href"));
//                        System.out.println("URL: " + vacancy.getUrl());
                        String salary = element.getElementsByAttributeValue("class", "salary").text();
                        vacancy.setSalary(salary.length() == 0 ? "" : salary);
//                        System.out.println("Salary: " + vacancy.getSalary());
                        vacancy.setCity(element.getElementsByAttributeValue("class", "location").text());
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
        Document doc = Jsoup.connect(String.format(URL_FORMAT, page, "java", searchString))
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36")
                .timeout(5000)
                .referrer("")
                .get();
        return doc;
    }
}
