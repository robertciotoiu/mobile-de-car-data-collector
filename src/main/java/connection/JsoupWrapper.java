package connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Random;

public final class JsoupWrapper {
    private static final Random random = new Random();
    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Use this method for a responsible scraping. It sleeps for 2-5 seconds.
     *
     * @param Url
     * @return
     * @throws IOException
     */
    public Document getHtml(String Url) throws IOException {
        try {
            wait(random.nextInt(2, 5));
        } catch (InterruptedException e) {
            logger.fatal("Sleep failed! Irresponsible parsing.", e);
            throw new RuntimeException("Sleep failed! Code bug.");
        }

        return Jsoup.connect(Url).get();
    }
}
