import java.util.UUID;

public class TestResult extends TestCase {
    private UUID uuid;

    public TestResult(UUID uuid) {
        super();
        this.uuid = uuid;
    }

    public TestResult(UUID uuid, String name, String expectedResult, Object[] args) {
        super(name, expectedResult, args);
        this.uuid = uuid;
    }
}