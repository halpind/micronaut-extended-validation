package validated.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import validated.dto.PersonRequest;
import validated.support.CrudStates;
import validated.support.UsingGroups;
import validated.support.Validated;

import javax.validation.Valid;

@Validated
@Controller("/person")
public class PersonController {
    @Post
    @UsingGroups(groups = {CrudStates.Read.class})
    public HttpResponse<String> get(@Body @Valid PersonRequest request) {
        return HttpResponse.ok(request.getDisplayAs());
    }
}
