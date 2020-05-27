package telegramBot;

import com.itextpdf.html2pdf.HtmlConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Helper {
    private final static Logger log = LoggerFactory.getLogger(Helper.class);
    private final static Properties properties = new Properties();
    private final static File file = new File("src\\main\\resources\\AppData\\verification.properties");

    // Verification
    public synchronized Boolean verify(String password) {
        try {
            properties.load(new FileReader(file));
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error in loading property file.");
            log.error(e.getMessage());
        }
        log.info("Verification method selected.");
        password = password.toLowerCase().trim();
        return (password.equals(properties.getProperty("password.var")));
    }

    // Method to get Hoba picture :)
    public synchronized String getHoba(long chat_id) {
        File file = new File("src\\main\\resources\\BotData\\hoba");
        if (!file.exists()) return null;
        File[] listFiles = file.listFiles();
        // return the absolute path to the last file
        if (listFiles != null) {
            return listFiles[listFiles.length-1].getAbsolutePath();
        }
        return null;
    }

    // Method for converting data from html to pdf file
    public synchronized File fromHtmlToPdf(File htmlFileName) {
        try {
            HtmlConverter.convertToPdf(htmlFileName,
                    new File("src\\main\\java\\view\\job_list.pdf"));
            return new File("src\\main\\java\\view\\job_list.pdf");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Конвертация не удалась.");
            log.error(e.getMessage());
            return null;
        }
    }

    public synchronized boolean checkCityName(String cityName) {
        return (cityName.matches("^[a-z]+\\s?\\-?\\s?[a-z]*\\s?\\-?\\s?[a-z]*$") ||
                cityName.matches("^[а-я]+\\s?\\-?\\s?[а-я]*\\s?\\-?\\s?[а-я]*$"));
    }

    // Send a response depending on the result of the method checkCityName()
    public synchronized SendMessage setTextWithCityAnswer(long chat_id, boolean isTrue) {
        SendMessage message = new SendMessage();
        if (isTrue) {
            message = message.setChatId(chat_id).setText("Введите ключевые слова для поиска вакансии.\n" +
                    "Введите \"Job название вакансии\".\n" +
                    "Вы можете ввести не более 10 слов.");
        } else {
            log.info("User entered invalid city password data.");
            message = message.setChatId(chat_id).setText("Указаны неверные данные.\n" +
                    "Город может содержать только буквенные символы и состоять не более, " +
                    "чем из трех слов, разделенных пробелами и(или) дефисом.\n" +
                    "Попробуйте ввести данные еще раз.");
        }
        return message;
    }

    public synchronized boolean checkJobName(String jobName) {
        return (jobName.matches("^(\\w+\\s?\\-?\\s?){1,9}(\\w+)?$") ||
                jobName.matches("^([а-я0-9№\\/@()\\+]+\\s?\\-?\\s?){1,9}([а-я0-9№\\/@()\\+]+)?$"));
    }
}
