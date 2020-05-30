package telegramBot;

import aggregator_main.Aggregator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

public class MyBot extends TelegramLongPollingBot {

    private static final Logger log = LoggerFactory.getLogger(MyBot.class);

    private final Helper helper = new Helper();
    private final Aggregator aggregator = new Aggregator();
    private long current_chat_id;
    private final String startText = "Привет. Я тестовый бот.\nВведите пароль для подтверждения.";
    private final String helpText = "Я могу составить список вакансий на сайте hh.ru по ключевым " +
            "словам в выбранном вами городе и отправить его в формате PDF.\n" +
            "Просто отправь мне:\n" +
            "/getJob - если хочешь, чтобы я отправил тебе список вакансий;\n" +
            "/getHoba - если хочешь получить ободряющую хобу;\n" +
            "/help - если снова хочешь увидеть эту подсказку.";

    public MyBot(){
        super();
    }

    // It is planned to use the bot by several users.
    // Therefore, here are some synchronize methods. So far, only a verified user can use the bot,
    // therefore, global variables are used instead of the database.
    String job_name = null;
    String city_name = null;
    boolean isGetJob = false; // check if the user ran /getJob command

    // this method will be called every time the bot receives a new message from the user.
    // Here we will analyze the message, form and send a response
    public void onUpdateReceived(Update update) {
        new Thread(() -> {
            log.info("A new thread was created.");
            // receive the id of this chat (where we take the message)
            long chat_id = update.getMessage().getChatId();
            SendMessage message = new SendMessage();
            String userMessage = update.getMessage().getText();

            if (userMessage!=null && userMessage.equals("/start")) {

                if (chat_id == current_chat_id)
                    message.setChatId(chat_id).setText("Авторизация уже была успешно произведена.");
                else {
                    message.setChatId(chat_id).setText(startText);
                    log.info("New user connected.");
                }
                sendMessage(message);

            } else if (chat_id == current_chat_id) {
                if (update.hasMessage() && update.getMessage().hasText()) {

                    if (userMessage.equals("/getHoba")) {
                        log.info("\"/getHoba\" command selected.");
                        SendPhoto sendPhoto = null;
                        try {
                            sendPhoto = new SendPhoto().setChatId(chat_id).setPhoto(
                                    new File(helper.getHoba(chat_id)));
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            log.error("File was not found.");
                            log.error(e.getMessage());
                        }
                        sendPhoto(sendPhoto);

                        // Send helpText
                    } else if (userMessage.equals("/help")) {
                        log.info("\"/help\" command selected.");
                        message.setChatId(chat_id).setText(helpText);
                        sendMessage(message);

                    } else if (userMessage.startsWith("City") && isGetJob) {
                        // Check the entered city name matches the proposed template.
                        userMessage = userMessage.replace("City", "").toLowerCase().trim();
                        boolean isTrue = helper.checkCityName(userMessage);
                        if (isTrue) city_name = userMessage;
                        log.info("User entered city name.");
                        // Send a response to the user, depending on the check result
                        message = helper.setTextWithCityAnswer(chat_id, isTrue);
                        sendMessage(message);

                    } else if (userMessage.startsWith("Job") && city_name != null) {

                        // Check if the entered keywords match the intended template.
                        // if it yes, then start the aggregator, otherwise print a negative answer
                        userMessage = userMessage.replace("Job", "").toLowerCase().trim();
                        if (helper.checkJobName(userMessage)) {
                            log.info("User entered keywords.");
                            job_name = userMessage;
                            aggregateAndSendJobListInPDF(message, chat_id, job_name, city_name);
                            isGetJob = false;
                            city_name = null;
                            job_name = null;
                            log.info("Job search complete.");

                        } else {
                            log.info("User entered invalid job data.");
                            message.setChatId(chat_id).setText("Указаны неверные данные.\n" +
                                    "Вы не можете ввести более 10 слов и использовать некоторые символы.\n" +
                                    "Попробуйте ввести данные еще раз.");
                            sendMessage(message);
                        }

                    } else if (userMessage.equals("/getJob")) {
                        log.info("\"/getJob\" command selected.");
                        isGetJob = true; // mark the start of the script
                        // Get information about the city and the job.
                        message.setChatId(chat_id).setText("Укажите город, где вы ищете вакансию.\n" +
                                "Введите \"City город\".");
                        sendMessage(message);

                    }
                }
            } else if (userMessage!=null && helper.verify(userMessage)) {
                log.info("Verification successful.");
                // If the user has entered the correct information,
                // then send the welcome text and the help text.
                current_chat_id = chat_id;
                message.setChatId(chat_id).setText("Добро пожаловать!");
                sendMessage(message);
                message.setChatId(chat_id).setText(helpText);
                sendMessage(message);

            } else {
                log.info("Verification failed");
                // Or inform that the user entered incorrect data
                message.setChatId(chat_id).setText("Извини, в доступе отказано.\n" +
                        "Для доступа нужно ввести необходимые данные пользователя.");
                sendMessage(message);
            }
        }).start();
    }



    // Method for sending a text message
    private synchronized void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.error("Message failed to send.");
            log.error(e.getMessage());
        }
    }

    // Method for sending the image
    private synchronized void sendPhoto(SendPhoto message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.error("Photo failed to send.");
            log.error(e.getMessage());
        }
    }

    // Method for sending documents
    private synchronized void sendDoc(SendDocument document) {
        try {
            execute(document);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.error("Document failed to send.");
            log.error(e.getMessage());
        }
    }

    // Run aggregator method
    private synchronized void aggregateAndSendJobListInPDF(SendMessage message, long chat_id, String jobName, String city) {
        log.info("aggregateAndSendJobListInPDF method selected.");
        message.setChatId(chat_id).setText("Подождите, это займет немного времени :З");
        sendMessage(message);

        // Run aggregator method
        if (aggregator.runAggregator(jobName, city)) {
            message.setChatId(chat_id).setText("Отправляю файл с вакансиями :З");
            sendMessage(message);

            log.info("Aggregator formed a list.");
            // Convert html data to pdf file
            convertAndSendFile(message, chat_id);

        } else {
            message.setChatId(chat_id).setText("Вакансий по данному запросу не найдено ¯\\_(ツ)_/¯");
            sendMessage(message);
            log.info("No job was found.");
        }

    }

    // Method for converting data from html to pdf file
    private synchronized void convertAndSendFile(SendMessage message, long chat_id) {
        log.info("convertAndSendFile method selected.");
        File aggregatorResultInPDF = helper.fromHtmlToPdf(
                new File("/app/src/main/java/view/vacancies.html"));
        if (aggregatorResultInPDF != null) {
            try {
                SendDocument sendDocument = new SendDocument().setChatId(chat_id)
                        .setDocument(aggregatorResultInPDF);
                sendDoc(sendDocument);
                log.info("The document has been converted to a PDF.");
            } catch (Exception e) {
                e.printStackTrace();
                message.setChatId(chat_id).setText("Документ не удалось отправить.");
                sendMessage(message);
                log.error("Документ не удалось отправить.");
                log.error(e.getMessage());
            }
        } else {
            message.setChatId(chat_id).setText("Конвертация не удалась.");
            sendMessage(message);
        }
    }

    public String getBotUsername() {
        return System.getenv().get("USERNAME");
    }

    public String getBotToken() {
        return System.getenv().get("TOKEN");
    }
}