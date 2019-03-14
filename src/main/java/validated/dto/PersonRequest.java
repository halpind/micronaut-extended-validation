package validated.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.validation.Validated;
import validated.support.CrudStates;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Validated
public class PersonRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank(groups = {CrudStates.Create.class})
    @Null(groups = {CrudStates.Remove.class, CrudStates.Read.class})
    private String displayAs;

    @JsonCreator
    public PersonRequest(@JsonProperty("email") String email, @JsonProperty("displayAs") String displayAs) {
        this.email = email;
        this.displayAs = displayAs;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayAs() {
        return displayAs;
    }

    public void setDisplayAs(String displayAs) {
        this.displayAs = displayAs;
    }

    @Override
    public String toString() {
        return "PersonRequest{" +
                "email='" + email + '\'' +
                ", displayAs='" + displayAs + '\'' +
                '}';
    }
}
