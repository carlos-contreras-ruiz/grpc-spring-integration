package com.githuh.carloscontrerasruiz.movieservice.service;

import com.githuh.carloscontrerasruiz.movie.MovieDto;
import com.githuh.carloscontrerasruiz.movie.MovieSearchRequest;
import com.githuh.carloscontrerasruiz.movie.MovieSearchResponse;
import com.githuh.carloscontrerasruiz.movie.MovieServiceGrpc;
import com.githuh.carloscontrerasruiz.movieservice.repository.MovieRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class MovieService extends MovieServiceGrpc.MovieServiceImplBase {

    @Autowired
    private MovieRepository repository;

    @Override
    public void getMovies(MovieSearchRequest request, StreamObserver<MovieSearchResponse> responseObserver) {
        List<MovieDto> movieDtoList = this.repository.getMovieByGenreOrderByYearDesc(request.getGenre().toString())
                .stream()
                .map(movie -> MovieDto.newBuilder()
                        .setTitle(movie.getTitle())
                        .setYear(movie.getYear())
                        .setRating(movie.getRating())
                        .build()
                )
                .collect(Collectors.toList());

        MovieSearchResponse.newBuilder()
                .addAllMovies(movieDtoList)
                .build();

        responseObserver.onNext(
                MovieSearchResponse.newBuilder()
                        .addAllMovies(movieDtoList)
                        .build()
        );

        responseObserver.onCompleted();
    }
}
