package se.smelly.community.dto;

import net.bytebuddy.asm.Advice;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import se.smelly.community.document.AppUser;
import se.smelly.community.security.Role;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


@RunWith(SpringRunner.class)
public class ConvertersTest {

    @TestConfiguration
    static class ConvertersTestConfig{
        @Bean
        public Converters converters(){
            return new Converters();
        }
    }

    @Autowired Converters converters;

    private AppUser firstAppUser;
    private AppUser secondAppUser;

    private AppUserDto firstAppUserDto;
    private AppUserDto secondAppUserDto;

    @Before
    public void init(){
        this.firstAppUser = new AppUser.Builder(LocalDate.parse("2019-04-18"))
                .lastName("Testsson")
                .firstName("Test1")
                .asRole(Role.USER)
                .withEmail("test@test.com")
                .setId("test1")
                .password("password")
                .setActiveStatus(false)
                .build();

        this.firstAppUserDto = new AppUserDto();
        firstAppUserDto.setLastName("Testsson");
        firstAppUserDto.setFirstName("Test1");
        firstAppUserDto.setRole(Role.USER);
        firstAppUserDto.setEmail("test@test.com");
        firstAppUserDto.setId("test1");
        firstAppUserDto.setPassword("password");
        firstAppUserDto.setActive(false);
        firstAppUserDto.setRegDate(LocalDate.parse("2019-04-18"));

        this.secondAppUser = new AppUser.Builder(LocalDate.parse("2019-04-17"))
                .lastName("Testsson2")
                .firstName("Test2")
                .asRole(Role.ADMIN)
                .withEmail("test2@test.com")
                .setId("test2")
                .password("password")
                .setActiveStatus(true)
                .build();

        this.secondAppUserDto = new AppUserDto();
        secondAppUserDto.setLastName("Testsson2");
        secondAppUserDto.setFirstName("Test2");
        secondAppUserDto.setRole(Role.ADMIN);
        secondAppUserDto.setEmail("test2@test.com");
        secondAppUserDto.setId("test2");
        secondAppUserDto.setPassword("password");
        secondAppUserDto.setActive(true);
        secondAppUserDto.setRegDate(LocalDate.parse("2019-04-17"));
    }

    @Test
    public void convertAppUserToDto_single() {
        Publisher<AppUserDto> result = converters.convertAppUserToDto(Mono.just(firstAppUser));

        StepVerifier.create(result)
                .expectNext(firstAppUserDto)
                .verifyComplete();
    }

    @Test
    public void convertAppUserToDto_many(){
        Publisher<AppUserDto> result = converters.convertAppUserToDto(Flux.just(firstAppUser, secondAppUser));

        StepVerifier.create(result)
                .expectNext(firstAppUserDto)
                .expectNext(secondAppUserDto)
                .verifyComplete();
    }


    @Test
    public void convertDtoToAppUser_single() {
        Publisher<AppUser> result = converters.convertDtoToAppUser(Mono.just(firstAppUserDto));

        StepVerifier.create(result)
                .expectNext(firstAppUser)
                .verifyComplete();

    }

    @Test
    public void convertDtoToAppUser_many(){
        Publisher<AppUser> result = converters.convertDtoToAppUser(Flux.just(firstAppUserDto, secondAppUserDto));

        StepVerifier.create(result)
                .expectNext(firstAppUser)
                .expectNext(secondAppUser)
                .verifyComplete();
    }
}