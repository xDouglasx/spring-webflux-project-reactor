package com.reactivespring.service;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.MovieInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class MoviesInfoService {

    private final MovieInfoRepository movieInfoRepository;

    private MoviesInfoService(MovieInfoRepository movieInfoRepository){
        this.movieInfoRepository = movieInfoRepository;
    }

    public Flux<MovieInfo> getAllMovies() {
        return movieInfoRepository.findAll();
    }

    public Flux<MovieInfo> getMovieInfoByYear(Integer year) {
        return movieInfoRepository.findByYear(year);
    }

    public Mono<MovieInfo> addMovieInfo(MovieInfo movieInfo) {
        log.info("addMovieInfo : {} ", movieInfo);
        return movieInfoRepository.save(movieInfo);
    }

    public Mono<MovieInfo> getMovieInfoById(String id) {
        return movieInfoRepository.findById(id);
    }

    public Mono<MovieInfo> updateMovieInfo(MovieInfo movieInfo, String id) {
        return movieInfoRepository.findById(id)
                .flatMap((movieInfoUpdate -> {
                    movieInfoUpdate.setCast(movieInfo.getCast());
                    movieInfoUpdate.setName(movieInfo.getName());
                    movieInfoUpdate.setRelease_date(movieInfo.getRelease_date());
                    movieInfoUpdate.setYear(movieInfo.getYear());
                    return movieInfoRepository.save(movieInfoUpdate);
                }));
    }

    public Mono<Void> deleteMovieInfoById(String id){
        return movieInfoRepository.deleteById(id);
    }
}
