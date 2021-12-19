package com.githuh.carloscontrerasruiz.aggregator.service;

import com.githuh.carloscontrerasruiz.aggregator.dto.RecommendedMovie;
import com.githuh.carloscontrerasruiz.aggregator.dto.UserGenre;
import com.githuh.carloscontrerasruiz.common.Genre;
import com.githuh.carloscontrerasruiz.movie.MovieSearchRequest;
import com.githuh.carloscontrerasruiz.movie.MovieSearchResponse;
import com.githuh.carloscontrerasruiz.movie.MovieServiceGrpc;
import com.githuh.carloscontrerasruiz.user.UserGenreUpdateRequest;
import com.githuh.carloscontrerasruiz.user.UserResponse;
import com.githuh.carloscontrerasruiz.user.UserServiceGrpc;
import com.githuh.carloscontrerasruiz.user.UserSearchRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class UserMovieService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

    @GrpcClient("movie-service")
    private MovieServiceGrpc.MovieServiceBlockingStub movieServiceBlockingStub;

    public List<RecommendedMovie> getUserMovieSuggestion(String loginId) {

        UserSearchRequest userSearchRequest = UserSearchRequest.newBuilder().setLoginId(loginId).build();
        UserResponse userResponse = this.userServiceBlockingStub.getUserGenre(userSearchRequest);
        System.out.println(userResponse);
        MovieSearchResponse movieSearchResponse = this.movieServiceBlockingStub.getMovies(MovieSearchRequest
                .newBuilder()
                .setGenre(userResponse.getGenre())
                .build()
        );
        System.out.println(movieSearchResponse);
        return movieSearchResponse.getMoviesList()
                .stream()
                .map(movieDto -> new RecommendedMovie(
                        movieDto.getTitle()
                        , movieDto.getYear()
                        , movieDto.getRating())
                ).collect(Collectors.toList());

    }

    public void setUserGenre(UserGenre userGenre){
        UserResponse userResponse = this.userServiceBlockingStub.updateUserGenre(
                UserGenreUpdateRequest.newBuilder()
                        .setLoginId(userGenre.getLoginId())
                        .setGenre(Genre.valueOf(userGenre.getGenre().toUpperCase()))
                        .build()
        );
        System.out.println(userResponse);
    }
}
