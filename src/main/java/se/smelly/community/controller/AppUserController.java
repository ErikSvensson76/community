package se.smelly.community.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.smelly.community.dto.AppUserDto;
import se.smelly.community.exception.CouldNotBeFoundException;
import se.smelly.community.service.AppUserService;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
public class AppUserController {

    private AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }



    @GetMapping("/user/email")
    public ResponseEntity<Mono<AppUserDto>> getByEmail(@RequestParam(name = "email") String email){
        return ResponseEntity.ok().body(appUserService.findByEmail(Mono.just(email))
                .switchIfEmpty(Mono.error(() -> new CouldNotBeFoundException("AppUser with email: " + email + " could not be found"))));
    }

    @GetMapping("/user/all")
    public ResponseEntity<Flux<AppUserDto>> getAll(){
        return ResponseEntity.ok().body(appUserService.getAll());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Mono<AppUserDto>> getById(@PathVariable String id){
        return ResponseEntity.ok().body(appUserService.findById(Mono.just(id))
            .switchIfEmpty(Mono.error(()-> new CouldNotBeFoundException("AppUser with id: "+ id + " could not be found"))));
    }

    @PostMapping("/user")
    public ResponseEntity<Mono<AppUserDto>> addNewAppUser(@Valid @RequestBody AppUserDto formData){
        Mono<AppUserDto> formWithDate = Mono.just(formData).map(form -> {
            form.setRegDate(LocalDate.now());
            return form;
        });

        return ResponseEntity.status(HttpStatus.CREATED).body(appUserService.save(formWithDate).single());
    }

    @PutMapping("/user")
    public ResponseEntity<Mono<AppUserDto>> updateAppUser(@Valid @RequestBody AppUserDto updated){
        return ResponseEntity.ok().body(appUserService.save(Mono.just(updated)).single());
    }












}
