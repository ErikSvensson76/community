package se.smelly.community.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.smelly.community.dto.AppUserDto;
import se.smelly.community.dto.Converters;
import se.smelly.community.repository.AppUserRepo;
import se.smelly.community.security.Role;

import java.time.LocalDate;

@Service
public class AppUserServiceReactiveImpl implements AppUserService {

    public AppUserRepo appUserRepo;
    public Converters converters;

    @Autowired
    public AppUserServiceReactiveImpl(AppUserRepo appUserRepo, Converters converters) {
        this.appUserRepo = appUserRepo;
        this.converters = converters;
    }

    @Override
    public Mono<AppUserDto> findByEmail(Mono<String> email){
        return Mono.from(converters.convertAppUserToDto(appUserRepo.findByEmailIgnoreCase(email)));
    }

    @Override
    public Mono<AppUserDto> findById(Mono<String> id){
        return Mono.from(converters.convertAppUserToDto(appUserRepo.findById(id)));
    }

    @Override
    public Flux<AppUserDto> findByRole(Mono<Role> role){
        return Flux.from(appUserRepo.findByRole(role)
                .flatMap(x -> converters.convertAppUserToDto(Mono.just(x))));
    }

    @Override
    public Flux<AppUserDto> getAll(){
        return Flux.from(appUserRepo.findAll().flatMap(x -> converters.convertAppUserToDto(Mono.just(x))));
    }

    @Override
    public Flux<AppUserDto> findByActiveStatus(Mono<Boolean> isActive){
        return Flux.from(appUserRepo.findByActive(isActive).flatMap(x -> converters.convertAppUserToDto(Mono.just(x))));
    }

    @Override
    public Flux<AppUserDto> findByRegDateBefore(Mono<LocalDate> regDate){
        return Flux.from(appUserRepo.findByRegDateBefore(regDate).flatMap(x -> converters.convertAppUserToDto(Mono.just(x))));
    }

    @Override
    public Flux<AppUserDto> findByRegDateAfter(Mono<LocalDate> regDate){
        return Flux.from(appUserRepo.findByRegDateAfter(regDate).flatMap(x -> converters.convertAppUserToDto(Mono.just(x))));
    }

    @Override
    public Flux<AppUserDto> findByRegDate(Mono<LocalDate> regDate){
        return Flux.from(appUserRepo.findByRegDate(regDate).flatMap(x -> converters.convertAppUserToDto(Mono.just(x))));
    }

    @Override
    public Mono<AppUserDto> save(Mono<AppUserDto> toSave){
        return Mono.from(appUserRepo
                .saveAll(converters.convertDtoToAppUser(toSave)))
                .flatMap(x -> converters.convertAppUserToDto(Mono.just(x))).single();
    }
}
