package se.smelly.community.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.smelly.community.document.AppUser;
import se.smelly.community.security.Role;

import java.time.LocalDate;

public interface AppUserRepo extends ReactiveMongoRepository<AppUser, String> {

    Mono<AppUser> findByEmailIgnoreCase(Mono<String> email);
    Flux<AppUser> findByRole(Mono<Role> role);
    Flux<AppUser> findByActive(Mono<Boolean> isActive);
    Flux<AppUser> findByRegDateBefore(Mono<LocalDate> regDate);
    Flux<AppUser> findByRegDateAfter(Mono<LocalDate> regDate);
    Flux<AppUser> findByRegDate(Mono<LocalDate> regDate);

}
