package se.smelly.community.repository_tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import se.smelly.community.document.AppUser;
import se.smelly.community.dto.AppUserDto;
import se.smelly.community.security.Role;
import se.smelly.community.service.AppUserService;

import java.time.LocalDate;



@SpringBootTest
@RunWith(SpringRunner.class)
public class SaveAppUserIntegrationTest {


    @Autowired
    private AppUserService appUserService;

    @Autowired
    private ReactiveMongoOperations mongoOperations;

    @Before
    public void tearDown(){
        mongoOperations.dropCollection(AppUser.class).block();
    }

    @Test
    public void test_save_one_user_return_dto(){
        //String id, String email, Role role, String firstName, String lastName, LocalDate regDate, boolean active, String password
        AppUserDto fromWeb = new AppUserDto(null, "test@test.com", Role.USER, "Test", "Testsson", LocalDate.parse("2019-04-22"), true, "password");
        Publisher<AppUserDto> param = Mono.just(fromWeb);


        Publisher<AppUserDto> result = appUserService.save(param).log();

        StepVerifier.create(result)
                .thenConsumeWhile(x -> x.getEmail().equals("test@test.com") && x.getId() != null)
                .verifyComplete();
    }

    @Test
    public void test_save_two_users_return_two_dto(){
        AppUserDto fromWeb = new AppUserDto(null, "test@test.com", Role.USER, "Test", "Testsson", LocalDate.parse("2019-04-22"), true, "password");
        AppUserDto fromWeb2 = new AppUserDto(null, "test2@test.com", Role.USER, "Darth", "Vader", LocalDate.parse("2019-04-22"), true, "empire");

        Publisher<AppUserDto> param = Flux.just(fromWeb, fromWeb2);

        Publisher<AppUserDto> result = appUserService.save(param).log();

        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void test_get_all_return_two_users(){
        AppUserDto fromWeb = new AppUserDto(null, "test@test.com", Role.USER, "Test", "Testsson", LocalDate.parse("2019-04-22"), true, "password");
        AppUserDto fromWeb2 = new AppUserDto(null, "test2@test.com", Role.USER, "Darth", "Vader", LocalDate.parse("2019-04-22"), true, "empire");

        appUserService.save(Flux.just(fromWeb, fromWeb2)).then().block();

        StepVerifier.create(appUserService.getAll().log())
                .expectNextCount(2)
                .verifyComplete();
    }


}
