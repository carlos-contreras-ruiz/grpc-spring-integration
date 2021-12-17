package com.githuh.carloscontrerasruiz.userservice.service;

import com.githuh.carloscontrerasruiz.common.Genre;
import com.githuh.carloscontrerasruiz.user.UserGenreUpdateRequest;
import com.githuh.carloscontrerasruiz.user.UserResponse;
import com.githuh.carloscontrerasruiz.user.UserSearchRequest;
import com.githuh.carloscontrerasruiz.user.UserServiceGrpc;
import com.githuh.carloscontrerasruiz.userservice.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.util.Locale;

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void getUserGenre(UserSearchRequest request, StreamObserver<UserResponse> responseObserver) {
        UserResponse.Builder builder = UserResponse.newBuilder();
        this.userRepository.findById(request.getLoginId())
                .ifPresent(user -> {
                    builder.setName(user.getName())
                            .setLoginId(user.getLogin())
                            .setGenre(Genre.valueOf(user.getGenre().toUpperCase(Locale.ROOT)));
                });
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void updateUserGenre(UserGenreUpdateRequest request, StreamObserver<UserResponse> responseObserver) {
        //Usando el annottation Transaccional hace automaticamente el update al
        // alterar el objeto traido de la base de datos
        //Personally I prefferd the explicit save method
        UserResponse.Builder builder = UserResponse.newBuilder();
        this.userRepository.findById(request.getLoginId())
                .ifPresent(user -> {
                    user.setGenre(request.getGenre().toString());
                    builder.setName(user.getName())
                            .setLoginId(user.getLogin())
                            .setGenre(Genre.valueOf(user.getGenre().toUpperCase(Locale.ROOT)));
                });
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
