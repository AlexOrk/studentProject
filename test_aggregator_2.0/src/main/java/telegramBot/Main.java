package telegramBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;


public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Application has started.");
        ApiContextInitializer.init(); // bot initialization
        log.info("Bot was initialized.");

        // make an object TelegramBotsApi. It will be control the bot.
        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new MyBot());
            log.info("Bot has been registered.");
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
