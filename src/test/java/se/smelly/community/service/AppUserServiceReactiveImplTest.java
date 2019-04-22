package se.smelly.community.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import se.smelly.community.document.AppUser;
import se.smelly.community.dto.AppUserDto;
import se.smelly.community.dto.Converters;
import se.smelly.community.repository.AppUserRepo;
import se.smelly.community.security.Role;

import java.time.LocalDate;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AppUserServiceReactiveImplTest {


    @Mock
    private Converters converters;

    @Mock
    private AppUserRepo appUserRepo;

    @InjectMocks
    private AppUserServiceReactiveImpl appUserService;



    private AppUser firstAppUser;
    private AppUser secondAppUser;
    private AppUserDto firstAppUserDto;
    private AppUserDto secondAppUserDto;

    @Before
    public void setUp() throws Exception {

        this.firstAppUser = new AppUser.Builder(LocalDate.parse("2019-04-18"))
                .lastName("Testsson")
                .firstName("Test1")
                .role(Role.USER)
                .email("test@test.com")
                .id("test1")
                .password("password")
                .active(false)
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
                .role(Role.ADMIN)
                .email("test2@test.com")
                .id("test2")
                .password("password")
                .active(true)
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
    public void findByEmail() {
        Mono<String> param = Mono.just("Test@test.com");

        when(appUserRepo.findByEmailIgnoreCase(param)).thenReturn(Mono.just(firstAppUser));
        when(converters.convertAppUserToDto(any(Publisher.class))).thenReturn(Mono.just(firstAppUserDto));

        Mono<AppUserDto> result = appUserService.findByEmail(param).log();

        StepVerifier.create(result)
                .expectNext(firstAppUserDto)
                .verifyComplete();
    }

    @Test
    public void findById() {
        Mono<String> idParam = Mono.just("test1");

        when(appUserRepo.findById(idParam)).thenReturn(Mono.just(firstAppUser));
        when(converters.convertAppUserToDto(any(Publisher.class))).thenReturn(Mono.just(firstAppUserDto));

        StepVerifier.create(appUserService.findById(idParam))
                .expectNext(firstAppUserDto)
                .verifyComplete();
    }

    @Test
    public void findByRole() {
        Mono<Role> roleParam = Mono.just(Role.ADMIN);

        when(appUserRepo.findByRole(roleParam)).thenReturn(Flux.just(secondAppUser));
        when(converters.convertAppUserToDto(any(Publisher.class))).thenReturn(Flux.just(secondAppUserDto));

        StepVerifier.create(appUserService.findByRole(roleParam))
                .expectNext(secondAppUserDto)
                .verifyComplete();
    }


    @Test
    public void findByActiveStatus() {
        Mono<Boolean> booleanMono = Mono.just(false);

        when(appUserRepo.findByActive(booleanMono)).thenReturn(Flux.just(firstAppUser));
        when(converters.convertAppUserToDto(any(Publisher.class))).thenReturn(Flux.just(firstAppUserDto));

        StepVerifier.create(appUserService.findByActiveStatus(booleanMono).log())
                .expectNext(firstAppUserDto)
                .verifyComplete();
    }

    @Test
    public void findByRegDateBefore() {
        Mono<LocalDate> localDateMono = Mono.just(LocalDate.parse("2019-04-21"));
        Flux<AppUserDto> expected = Flux.just(firstAppUserDto, secondAppUserDto);

        when(appUserRepo.findByRegDateBefore(localDateMono)).thenReturn(Flux.just(firstAppUser, secondAppUser));
        when(converters.convertAppUserToDto(any(Publisher.class))).thenReturn(expected);

        StepVerifier.create(appUserService.findByRegDateBefore(localDateMono).log())
                .expectNext(firstAppUserDto, secondAppUserDto)
                .verifyComplete();
    }

    @Test
    public void findByRegDateAfter() {
        Mono<LocalDate> localDateMono = Mono.just(LocalDate.parse("2019-04-17"));

        when(appUserRepo.findByRegDateAfter(localDateMono)).thenReturn(Flux.just(firstAppUser));
        when(converters.convertAppUserToDto(any(Publisher.class))).thenReturn(Flux.just(firstAppUserDto));

        StepVerifier.create(appUserService.findByRegDateAfter(localDateMono))
                .thenConsumeWhile(x -> x.getRegDate().isAfter(LocalDate.parse("2017-04-17")))
                .verifyComplete();

    }

    @Test
    public void findByRegDate() {
        Mono<LocalDate> localDateMono = Mono.just(LocalDate.parse("2019-04-17"));

        when(appUserRepo.findByRegDate(localDateMono)).thenReturn(Flux.just(secondAppUser));
        when(converters.convertAppUserToDto(any(Publisher.class))).thenReturn(Flux.just(secondAppUserDto));

        StepVerifier.create(appUserService.findByRegDate(localDateMono))
                .thenConsumeWhile(x -> x.getRegDate().equals(LocalDate.parse("2019-04-17")))
                .verifyComplete();
    }
}