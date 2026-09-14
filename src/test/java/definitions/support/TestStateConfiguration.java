package definitions.support;

import io.cucumber.spring.ScenarioScope;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration(proxyBeanMethods = false)
public class TestStateConfiguration {

    @Bean
    @ScenarioScope
    public TestState testState() {
        return new TestState();
    }
}
