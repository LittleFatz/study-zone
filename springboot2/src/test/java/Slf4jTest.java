import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class Slf4jTest {

    @Test
    public void test1() {
        int number = 10;
        log.info("test number is: {}", number);
        log.debug("hihihihihihi");
        log.error("errorororor");
        System.out.println("end");
    }
}
