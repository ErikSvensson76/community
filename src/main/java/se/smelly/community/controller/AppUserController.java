package se.smelly.community.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import se.smelly.community.dto.AppUserDto;
import se.smelly.community.exception.CouldNotBeFoundException;
import se.smelly.community.service.AppUserService;

@RestController
public class AppUserController {

    private AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/user/{email}")
    public Mono<AppUserDto> getByEmail(@PathVariable  String email){
        return appUserService.findByEmail(Mono.just(email))
                .switchIfEmpty(Mono.error(() -> new CouldNotBeFoundException("AppUser with email: " + email + " could not be found")));
    }




}
