import java.util.UUID;

public class TestResult extends TestCase {
    private UUID uuid;

    public TestCase(UUID uuid) {
        super();
        this.uuid = uuid;
    }

    public TestCase(UUID uuid, String name, String expectedResult, Object[] args) {
        super(name, expectedResult, args);
    }
}