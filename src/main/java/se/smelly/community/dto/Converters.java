package se.smelly.community.dto;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.smelly.community.document.AppUser;



@Component
public class Converters {

    public Publisher<AppUserDto> convertAppUserToDto(Publisher<AppUser> appUserMono){
        return Flux.from(appUserMono)
                .flatMap(x -> Mono.just(new AppUserDto(
                        x.getId(),
                        x.getEmail(),
                        x.getRole(),
                        x.getFirstName(),
                        x.getLastName(),
                        x.getRegDate(),
                        x.isActive(),
                        x.getPassword())));
    }

    /*
    public Mono<AppUser> convertDtoToAppUser(Mono<AppUserDto> appUserDto){
        return appUserDto.flatMap(x -> Mono.just(new AppUser.Builder(x.getRegDate())
                    .asRole(x.getRole())
                    .firstName(x.getFirstName())
                    .lastName(x.getLastName())
                    .withEmail(x.getEmail())
                    .setActiveStatus(x.isActive())
                    .password(x.getPassword())
                    .setId(x.getId())
                    .build()));
    }
    */

    public Publisher<AppUser> convertDtoToAppUser(Publisher<AppUserDto> appUserDtoPublisher){
        return Flux.from(appUserDtoPublisher).flatMap(x -> Mono.just(new AppUser.Builder(x.getRegDate())
                    .asRole(x.getRole())
                    .firstName(x.getFirstName())
                    .lastName(x.getLastName())
                    .withEmail(x.getEmail())
                    .setActiveStatus(x.isActive())
                    .password(x.getPassword())
                    .setId(x.getId())
                    .build()));
    }
}
