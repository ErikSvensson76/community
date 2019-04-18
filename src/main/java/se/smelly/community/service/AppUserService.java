package se.smelly.community.service;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.smelly.community.dto.AppUserDto;
import se.smelly.community.security.Role;

import java.time.LocalDate;

public interface AppUserService {
    Mono<AppUserDto> findByEmail(Mono<String> email);

    Mono<AppUserDto> findById(Mono<String> id);

    Flux<AppUserDto> findByRole(Mono<Role> role);

    Flux<AppUserDto> getAll();

    Flux<AppUserDto> findByActiveStatus(Mono<Boolean> isActive);

    Flux<AppUserDto> findByRegDateBefore(Mono<LocalDate> regDate);

    Flux<AppUserDto> findByRegDateAfter(Mono<LocalDate> regDate);

    Flux<AppUserDto> findByRegDate(Mono<LocalDate> regDate);

    Flux<AppUserDto> save(Publisher<AppUserDto> toSave);
}
