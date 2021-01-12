package tokens;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.*;

public class TokenSteps {

    TokenClient tokenClient = new TokenClient();
    String id = "";


    @Given("a customer with account id {string}")
    public void aCustomerWithAccountIdWithTokens(String id) {
        this.id = id;
    }

    @When("the customer requests {int} token")
    public void theCustomerRequestsToken(int count) {
        tokenClient.requestTokens(id, count);
    }

    @And("the customer requests {int} tokens")
    public void theCustomerRequestsTokens(int count) {
        tokenClient.requestTokens(id, count);
    }

    @Then("he has {int} tokens")
    public void heHasTokens(int count) {
        assertEquals(count, tokenClient.tokens.size());
    }

    @And("he has gotten an error")
    public void heHasGottenAnError() {
        assertNotEquals("", tokenClient.error);
    }

    @And("he has not gotten an error")
    public void heHasNotGottenAnError() {
        assertEquals("", tokenClient.error);
    }
}
