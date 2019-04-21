package se.smelly.community.repository_tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import se.smelly.community.document.AppUser;
import se.smelly.community.repository.AppUserRepo;
import se.smelly.community.security.Role;

import java.time.LocalDate;


@RunWith(SpringRunner.class)
@DataMongoTest
public class AppUserRepoTest {

    @Autowired  private AppUserRepo testRepo;

    private AppUser testUser;

    @Before
    public void before(){
        AppUser testUser = new AppUser.Builder(LocalDate.parse("2019-03-15"))
                .email("test@test.com")
                .role(Role.USER)
                .firstName("Test")
                .lastName("Testsson")
                .password("123456")
                .build();

        this.testUser = testRepo.save(testUser).block();
    }

    @After
    public void tearDown(){
        testRepo.deleteAll().block();
    }

    @Test
    public void test_findByEmailIgnoreCase(){
        final String emailParam = "Test@TesT.com";

        StepVerifier
                .create(testRepo.findByEmailIgnoreCase(Mono.just(emailParam)))
                .expectNext(testUser)
                .verifyComplete();
    }

    @Test
    public void test_findByRole(){
        final Role param = Role.USER;

        StepVerifier
                .create(testRepo.findByRole(Mono.just(param)))
                .expectNextMatches(x -> x.getRole() == param)
                .verifyComplete();
    }

    @Test
    public void test_findByActive_true(){
        StepVerifier
                .create(testRepo.findByActive(Mono.just(true)))
                .expectNextMatches(AppUser::isActive)
                .verifyComplete();
    }

    @Test
    public void test_findByRegDateBefore(){
        final LocalDate regDate = LocalDate.now();

        StepVerifier
                .create(testRepo.findByRegDateBefore(Mono.just(regDate)))
                .expectNextMatches(x -> x.getRegDate().isBefore(regDate))
                .verifyComplete();
    }

    @Test
    public void test_findByRegDateAfter(){
        final LocalDate regDate = LocalDate.parse("2019-03-14");

        StepVerifier
                .create(testRepo.findByRegDateAfter(Mono.just(regDate)))
                .expectNextMatches(x -> x.getRegDate().isAfter(regDate))
                .verifyComplete();
    }

    @Test
    public void test_findByRegDate(){
        final LocalDate regDate = LocalDate.parse("2019-03-15");

        StepVerifier
                .create(testRepo.findByRegDate(Mono.just(regDate)))
                .expectNextMatches(x -> x.getRegDate().equals(regDate))
                .verifyComplete();
    }
}
