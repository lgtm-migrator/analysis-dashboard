package edu.hm.hafner.java.uc;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import edu.hm.hafner.java.persistence.EntityService;
import edu.hm.hafner.java.persistence.IssuesTestData;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests the class {@link IssuesService}.
 *
 * @author Ullrich Hafner
 */
class IssuesServiceTest {
    @Test
    void shouldCreateCategoryDistribution() {
        // Given
        IssuesService service = createService();

        // When
        IssuePropertyDistribution distribution = service.createDistributionByCategory("dummy-id");

        // Then
        assertThat(toJson(distribution)).isEqualTo("{\"labels\":[\"Design\",\"Documentation\",\"Best Practices\",\"Performance\",\"Code Style\",\"Error Prone\"],\"datasets\":[{\"data\":[15,3,20,6,53,12]}]}");
    }

    @Test
    void shouldCreateTypeDistribution() {
        // Given
        IssuesService service = createService();

        // When
        IssuePropertyDistribution distribution = service.createDistributionByType("dummy-id");

        // Then
        assertThat(toJson(distribution)).isEqualTo("{\"labels\":[\"OptimizableToArrayCall\",\"LooseCoupling\",\"MethodArgumentCouldBeFinal\",\"UncommentedEmptyMethodBody\",\"ConfusingTernary\",\"MissingSerialVersionUID\",\"GuardLogStatement\",\"UnusedFormalParameter\",\"LoggerIsNotStaticFinal\",\"AssignmentInOperand\",\"ImmutableField\",\"CompareObjectsWithEquals\",\"UnnecessaryConstructor\",\"CyclomaticComplexity\",\"UnusedPrivateMethod\",\"ConsecutiveLiteralAppends\",\"CallSuperInConstructor\",\"UnusedPrivateField\",\"AppendCharacterWithChar\",\"ExcessivePublicCount\",\"NPathComplexity\",\"ExcessiveImports\",\"AvoidDeeplyNestedIfStmts\",\"AccessorClassGeneration\",\"UncommentedEmptyConstructor\"],\"datasets\":[{\"data\":[1,1,13,2,9,4,8,2,4,1,2,3,13,3,3,4,18,1,1,2,3,4,1,5,1]}]}");
    }

    private String toJson(final IssuePropertyDistribution distribution) {
        Gson gson = new Gson();
        return gson.toJson(distribution);
    }

    private IssuesService createService() {
        IssuesTestData data = new IssuesTestData();
        IssuesService service = new IssuesService();
        EntityService entityService = mock(EntityService.class);
        when(entityService.select(anyString())).thenReturn(data.createTestData());
        service.setEntityService(entityService);
        return service;
    }
}