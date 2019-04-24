package se.smelly.community.controller;

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

    @Autowired private AppUserController appUserController;

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
        webTestClient.get().uri("/user/" + "erik@gmail.com")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(AppUserDto.class)
                .value(x -> x.getEmail().equals("erik@gmail.com"));
    }

    @Test
    public void getByEmail_notFound(){
        webTestClient.get().uri("/user/" + "invalid")
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8);
    }
}