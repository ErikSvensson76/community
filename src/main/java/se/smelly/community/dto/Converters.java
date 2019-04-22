package se.smelly.community.dto;

import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import se.smelly.community.document.AppUser;



@Component(value = "converters")
public class Converters {

    public Publisher<AppUserDto> convertAppUserToDto(Publisher<AppUser> appUserMono){
        return Flux.from(appUserMono)
                .map(x ->new AppUserDto(
                        x.getId(),
                        x.getEmail(),
                        x.getRole(),
                        x.getFirstName(),
                        x.getLastName(),
                        x.getRegDate(),
                        x.isActive(),
                        x.getPassword()));
    }

    public Publisher<AppUser> convertDtoToAppUser(Publisher<AppUserDto> appUserDtoPublisher){
        return Flux.from(appUserDtoPublisher).map(x -> new AppUser.Builder(x.getRegDate())
                    .role(x.getRole())
                    .firstName(x.getFirstName())
                    .lastName(x.getLastName())
                    .email(x.getEmail())
                    .active(x.isActive())
                    .password(x.getPassword())
                    .id(x.getId())
                    .build());
    }
}
