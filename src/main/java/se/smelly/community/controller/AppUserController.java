package se.smelly.community.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import se.smelly.community.document.AppUser;
import se.smelly.community.repository.AppUserRepo;

@RestController
public class AppUserController {

    private AppUserRepo appUserRepo;

    public AppUserController(AppUserRepo appUserRepo) {
        this.appUserRepo = appUserRepo;
    }

    @GetMapping("/user/{email}")
    public Mono<AppUser> getByEmail(@PathVariable  String email){
        return appUserRepo.findByEmailIgnoreCase(Mono.just(email));
    }


}
