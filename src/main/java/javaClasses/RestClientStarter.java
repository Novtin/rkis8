package javaClasses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Класс для запуска тестирования запросов для REST контроллера
 */
@Component
public class RestClientStarter implements CommandLineRunner {

    private static RestClient restClient;
    private static final Logger logger = LoggerFactory.getLogger(RestClientStarter.class);

    @Autowired
    public RestClientStarter(RestClient restClient) {
        RestClientStarter.restClient = restClient;
    }

    @Override
    public void run(String... args) {
        try {
            restClient.testAddGlasses();
            restClient.testAddGlasses();
            restClient.testGetAllGlasses();
            restClient.testGetFilterAllGlasses(0);
            restClient.testGetGlassesById(1);
            restClient.testUpdateGlasses(1);
            restClient.testDeleteGlasses(1);
            restClient.testDeleteGlasses(2);
        } catch (Exception exception){
            logger.error("Ошибка выполнения REST запросов: {}", exception.getMessage());
        }
    }
}
