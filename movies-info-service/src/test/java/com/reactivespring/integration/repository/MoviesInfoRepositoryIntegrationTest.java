package com.reactivespring.integration.repository;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@ActiveProfiles("test")
public class MoviesInfoRepositoryIntegrationTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @BeforeEach
    void setUp() {

        var movieinfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        movieInfoRepository.saveAll(movieinfos)
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void findById(){

        var movieInfo = movieInfoRepository.findById("abc");
        StepVerifier.create(movieInfo).assertNext(mInfo -> {
            assertEquals("Dark Knight Rises", mInfo.getName());
        });
    }

    @Test
    void findAll(){

        var moviesFlux = movieInfoRepository.findAll();
        StepVerifier.create(moviesFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void saveMovieInfo(){

        var movieInfo = new MovieInfo(null, "Batman Begins1",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        var savedMovieInfo = movieInfoRepository.save(movieInfo);

        StepVerifier.create(savedMovieInfo)
                .assertNext(mInfo -> {
                    assertNotNull(mInfo.getMovieInfoId());
                });
    }

    @Test
    void deleteMovieInfo(){

        movieInfoRepository.deleteById("abc").block();

        var movieInfos = movieInfoRepository.findAll();

        StepVerifier.create(movieInfos)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void findByName() {

        var movieInfosMono = movieInfoRepository.findByName("Batman Begins").log();

        StepVerifier.create(movieInfosMono)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findMovieInfoByYear(){

        var movieInfosFlux = movieInfoRepository.findByYear(2005).log();

        StepVerifier.create(movieInfosFlux)
                .expectNextCount(1)
                .verifyComplete();
    }
}


