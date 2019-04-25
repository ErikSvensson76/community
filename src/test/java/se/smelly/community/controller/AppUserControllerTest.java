package se.smelly.community.controller;

import net.bytebuddy.asm.Advice;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import se.smelly.community.document.AppUser;
import se.smelly.community.dto.AppUserDto;
import se.smelly.community.repository.AppUserRepo;
import se.smelly.community.security.Role;
import se.smelly.community.service.AppUserService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@DirtiesContext
@SpringBootTest
public class AppUserControllerTest {

    private List<AppUser> getTestData(){
        return Arrays.asList(
                new AppUser.Builder(LocalDate.parse("2017-01-23")).email("erik@gmail.com").firstName("Erik").lastName("Olsson").password("password").role(Role.ADMIN).active(true).id("testId").build(),
                new AppUser.Builder(LocalDate.parse("2017-09-12")).email("nisse@gmail.com").firstName("Nils").lastName("Nilsson").password("password").role(Role.USER).active(true).build(),
                new AppUser.Builder(LocalDate.parse("2018-09-12")).email("anna@gmail.com").firstName("Anna").lastName("Andersson").password("password").role(Role.USER).active(false).build(),
                new AppUser.Builder(LocalDate.parse("2018-02-10")).email("svenne@gmail.com").firstName("Svenne").lastName("Svensson").password("password").role(Role.ADMIN).active(true).build(),
                new AppUser.Builder(LocalDate.parse("2019-02-01")).email("ulliz@hotmail.com").firstName("Ulrika").lastName("Karlsson").password("password").role(Role.USER).active(true).build(),
                new AppUser.Builder(LocalDate.parse("2015-06-25")).email("arnold@gmail.com").firstName("Arnold").lastName("Schwarzenegger").password("password").role(Role.USER).active(false).build()
        );
    }

    @Autowired private WebTestClient webTestClient;

    @Autowired private AppUserRepo appUserRepo;

    @Before
    public void setUp() throws Exception {
        appUserRepo.deleteAll().thenMany(Flux.fromIterable(getTestData()))
                .flatMap(appUserRepo::save)
                .doOnNext(appUser -> System.out.println("Created " + appUser))
        .blockLast();
    }

    @Test
    public void getByEmail() {
        webTestClient.get().uri("/user/email"+"?email=erik@gmail.com")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(AppUserDto.class)
                .value(x -> x.getEmail().equals("erik@gmail.com"));
    }

    @Test
    public void getByEmail_notFound(){
        webTestClient.get().uri("/user/email"+"?email=invalid")
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8);
    }

    @Test
    public void getAll(){
        Flux<AppUserDto> result = webTestClient.get().uri("/user/all")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .returnResult(AppUserDto.class)
                .getResponseBody();

        StepVerifier.create(result)
                .expectSubscription()
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    public void getById(){
        webTestClient.get().uri("/user/" + "testId")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(AppUserDto.class)
                .value(appUserDto -> appUserDto.getId().equals("testId"));
    }

    @Test
    public void getById_notFound(){
        webTestClient.get().uri("/user/" + "invalid")
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8);
    }

    @Test
    public void addNewAppUser_created(){
        //String id, String email, Role role, String firstName, String lastName, LocalDate regDate, boolean active, String password
        AppUserDto formData = new AppUserDto(null, "rolf.andersson@gmail.com", Role.USER, "Rolf", "Andersson", null, true, "password");

        webTestClient.post().uri("/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(formData), AppUserDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.email").isEqualTo("rolf.andersson@gmail.com")
                .jsonPath("$.role").isEqualTo("USER")
                .jsonPath("$.firstName").isEqualTo("Rolf")
                .jsonPath("$.lastName").isEqualTo("Andersson")
                .jsonPath("$.regDate").isEqualTo(LocalDate.now().toString())
                .jsonPath("$.active").isEqualTo(true)
                .jsonPath("$.password").doesNotExist();
    }

    @Test
    public void addNewAppUser_badRequest(){
        //String id, String email, Role role, String firstName, String lastName, LocalDate regDate, boolean active, String password
        AppUserDto formData = new AppUserDto(null, "rolf.andersson", Role.USER, "Rolf", "Andersson", null, true, "pass");

        webTestClient.post().uri("/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(formData), AppUserDto.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void testUpdateAppUser_ok(){
        //String id, String email, Role role, String firstName, String lastName, LocalDate regDate, boolean active, String password
        AppUserDto updated = new AppUserDto("testId", "erik.svensson@gmail.com", Role.ADMIN, "Erik", "Svensson", LocalDate.parse("2017-01-23"), false, "password");

        webTestClient.put().uri("/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(updated), AppUserDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.email").isEqualTo("erik.svensson@gmail.com")
                .jsonPath("$.lastName").isEqualTo("Svensson");
    }








}