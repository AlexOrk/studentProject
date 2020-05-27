package telegramBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
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

        // make an object with starter settings for the bot
        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);

        // VPN-server's IP
        try {
            botOptions.setProxyHost(getProxyHost());
        } catch (NullPointerException e) {
            e.printStackTrace();
            log.error("ProxyHost is null.");
            log.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        // VPN-server's port
        try {
            botOptions.setProxyPort(getProxyPort());
        } catch (NullPointerException e) {
            e.printStackTrace();
            log.error("ProxyPort is null.");
            log.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        // specify which socket we will use on the VPN server
        botOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);

        try {
            botsApi.registerBot(new MyBot(botOptions));
            log.info("Bot has been registered.");
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    private static String getProxyHost() {
        return System.getenv().get("PROXYHOST");
    }

    private static int getProxyPort() {
        return Integer.parseInt(System.getenv().get("PROXYPORT"));
    }
}
