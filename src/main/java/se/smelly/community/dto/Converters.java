package se.smelly.community.dto;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import se.smelly.community.document.AppUser;

@Component
public class Converters {

    public Mono<AppUserDto> convertAppUserToDto(Mono<AppUser> appUserMono){
        return appUserMono
                .flatMap(x -> Mono.just(new AppUserDto(
                        x.getId(),
                        x.getEmail(),
                        x.getRole(),
                        x.getFirstName(),
                        x.getFirstName(),
                        x.getRegDate(),
                        x.isActive(),
                        x.getPassword())));
    }

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
}
