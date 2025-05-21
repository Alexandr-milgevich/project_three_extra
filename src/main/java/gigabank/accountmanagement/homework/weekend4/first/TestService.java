package gigabank.accountmanagement.homework.weekend4.first;

import gigabank.accountmanagement.utility.validators.ValidateAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    private final ValidateAnalyticsService validateAnalyticsService;

    @Autowired
    public TestService(ValidateAnalyticsService validateAnalyticsService) {
        this.validateAnalyticsService = validateAnalyticsService;
    }

    public void test() {
        System.out.println(validateAnalyticsService.hasValidCategory("HEALTH"));
    }
}
