package se.smelly.community.service;


import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.smelly.community.document.AppUser;
import se.smelly.community.dto.AppUserDto;
import se.smelly.community.dto.Converters;
import se.smelly.community.repository.AppUserRepo;
import se.smelly.community.security.Role;

import java.time.LocalDate;

@Service
public class AppUserServiceReactiveImpl implements AppUserService {

    private AppUserRepo appUserRepo;
    private Converters converters;

    @Autowired
    public AppUserServiceReactiveImpl(AppUserRepo appUserRepo, Converters converters) {
        this.appUserRepo = appUserRepo;
        this.converters = converters;
    }

    @Override
    public Mono<AppUserDto> findByEmail(Mono<String> email){
        Mono<AppUser> result = Mono.from(appUserRepo.findByEmailIgnoreCase(email));
        return Mono.from(converters.convertAppUserToDto(result));
    }

    @Override
    public Mono<AppUserDto> findById(Mono<String> id){
        Mono<AppUser> result = Mono.from(appUserRepo.findById(id));
        return Mono.from(converters.convertAppUserToDto(result));
    }

    @Override
    public Flux<AppUserDto> findByRole(Mono<Role> role){
        Flux<AppUser> result = appUserRepo.findByRole(role);
        return Flux.from(converters.convertAppUserToDto(result));
    }

    @Override
    public Flux<AppUserDto> getAll(){
        Flux<AppUser> result = appUserRepo.findAll();
        return Flux.from(converters.convertAppUserToDto(result));
    }

    @Override
    public Flux<AppUserDto> findByActiveStatus(Mono<Boolean> isActive){
        Flux<AppUser> result = appUserRepo.findByActive(isActive);
        return Flux.from(converters.convertAppUserToDto(result));
    }

    @Override
    public Flux<AppUserDto> findByRegDateBefore(Mono<LocalDate> regDate){
        Flux<AppUser> result = appUserRepo.findByRegDateBefore(regDate);
        return Flux.from(converters.convertAppUserToDto(result));
    }

    @Override
    public Flux<AppUserDto> findByRegDateAfter(Mono<LocalDate> regDate){
        Flux<AppUser> result = appUserRepo.findByRegDateAfter(regDate);
        return Flux.from(converters.convertAppUserToDto(result));
    }

    @Override
    public Flux<AppUserDto> findByRegDate(Mono<LocalDate> regDate){
        return Flux.from(appUserRepo.findByRegDate(regDate).flatMap(x -> converters.convertAppUserToDto(Mono.just(x))));
    }

    @Override
    public Flux<AppUserDto> save(Publisher<AppUserDto> toSave){
        Publisher<AppUser> convertToSave = converters.convertDtoToAppUser(toSave);
        Publisher<AppUser> result = appUserRepo.saveAll(convertToSave);
        return Flux.from(converters.convertAppUserToDto(result));
    }
}
