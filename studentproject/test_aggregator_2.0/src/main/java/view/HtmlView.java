package view;

import aggregator_main.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.List;

public class HtmlView implements View {
    private final static Logger log = LoggerFactory.getLogger(HtmlView.class);

    private Controller controller;
    private Boolean isVacancyListEmpty;
    private final String filePath = "./src/main/java/" +
            this.getClass().getPackage().getName().replace('.', '/') + "/vacancies.html";

    @Override
    public void update(List<Vacancy> vacancies) {
        log.info("update method selected.");
        isVacancyListEmpty = vacancies.isEmpty();
        if (!isVacancyListEmpty) {
            String updateFileContent = getUpdatedFileContent(vacancies);
            updateFile(updateFileContent);
        }
    }

    private String getUpdatedFileContent(List<Vacancy> vacancies) {
        log.info("getUpdatedFileContent method selected.");
        Document document = null;
        try {
            document = getDocument();

            Element templateOriginal = document.getElementsByClass("template").first();
            Element copyTemplate = templateOriginal.clone();
            copyTemplate.removeAttr("style");
            copyTemplate.removeClass("template");
            document.select("tr[class=vacancy]").remove().not("tr[class=vacancy template");

            for (Vacancy vacancy : vacancies) {
                Element localClone = copyTemplate.clone();
                localClone.getElementsByClass("city").first().text(vacancy.getCity());
                localClone.getElementsByClass("companyName").first().text(vacancy.getCompanyName());
                localClone.getElementsByClass("salary").first().text(vacancy.getSalary());
                Element element =localClone.getElementsByTag("a").first();
                element.text(vacancy.getTitle());
                element.attr("href", vacancy.getUrl());

                templateOriginal.before(localClone.outerHtml());
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Some exception occurred while updating job list.");
            log.error(e.getMessage());
            return "Some exception occurred";
        }
        return document.html();
    }

    private void updateFile(String updateFileContent) {
        log.info("updateFile method selected.");
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)));
            bw.write(updateFileContent);
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    protected Document getDocument() throws IOException {
        return Jsoup.parse(new File(filePath), "UTF-8");
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void userCitySelectMethod(String jobName, String city) {
        controller.onJobNameAndCitySelect(jobName, city);
    }

    public boolean isVacancyListEmpty() {
        return isVacancyListEmpty;
    }
}
