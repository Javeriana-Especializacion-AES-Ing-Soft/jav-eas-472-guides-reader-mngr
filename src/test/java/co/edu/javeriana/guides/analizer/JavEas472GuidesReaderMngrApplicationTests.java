package co.edu.javeriana.guides.analizer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JavEas472GuidesReaderMngrApplicationTests {

    @Test
    void contextLoads() {
        Assertions.assertDoesNotThrow(() -> JavEas472GuidesReaderMngrApplication.main(new String[]{"args"}));
    }

}
