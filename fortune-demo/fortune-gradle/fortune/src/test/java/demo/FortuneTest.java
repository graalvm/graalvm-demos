    package demo;

    import static org.junit.jupiter.api.Assertions.assertTrue;
    import org.junit.jupiter.api.DisplayName;
    import org.junit.jupiter.api.Test;

    import com.fasterxml.jackson.core.JsonProcessingException;

    class FortuneTest {
        @Test
        @DisplayName("Returns a fortune")
        void testItWorks() throws JsonProcessingException {
            Fortune fortune = new Fortune();
            assertTrue(fortune.randomFortune().length()>0);
        }
    }