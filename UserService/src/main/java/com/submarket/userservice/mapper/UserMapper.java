package com.submarket.userservice.mapper;

import com.submarket.userservice.dto.UserDto;
import com.submarket.userservice.jpa.entity.UserEntity;
import com.submarket.userservice.vo.UserRequest;
import com.submarket.userservice.vo.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper{
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // @Mapping(source = "nick", target = "nickname") 파라미터 객체 변수 이름, 리턴 객체 변수 이름
    //    @Mapping(target = "Age", ignore = true)

    // DB에 Enc 된 Password 를 저장
    @Mapping(source = "userEncPassword", target = "userPassword")
    UserEntity userDtoToUserEntity(UserDto userDto);

    UserDto userEntityToUserDto(UserEntity userEntity);

    UserDto RequestUserToUserDto(UserRequest userRequest);

    UserResponse UserDtoToResponseUser(UserDto userDto);

}
