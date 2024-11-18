package com.template.kmp.randomuser.models

@kotlinx.serialization.Serializable
data class RandomUserDto(
    val results: List<UserDto>,
    val info: InfoDto
)

@kotlinx.serialization.Serializable
data class UserDto(
    val gender: String,
    val name: NameDto,
    val location: LocationDto,
    val email: String,
    val login: LoginDto,
    val dob: DobDto,
    val registered: RegisteredDto,
    val phone: String,
    val cell: String,
    val id: IdDto,
    val picture: PictureDto,
    val nat: String
)

@kotlinx.serialization.Serializable
data class NameDto(
    val title: String,
    val first: String,
    val last: String
)

@kotlinx.serialization.Serializable
data class LocationDto(
    val street: StreetDto,
    val city: String,
    val state: String,
    val country: String,
    val postcode: String,
    val coordinates: CoordinatesDto,
    val timezone: TimezoneDto
)

@kotlinx.serialization.Serializable
data class StreetDto(
    val number: Int,
    val name: String
)

@kotlinx.serialization.Serializable
data class CoordinatesDto(
    val latitude: String,
    val longitude: String
)

@kotlinx.serialization.Serializable
data class TimezoneDto(
    val offset: String,
    val description: String
)

@kotlinx.serialization.Serializable
data class LoginDto(
    val uuid: String,
    val username: String,
    val password: String,
    val salt: String,
    val md5: String,
    val sha1: String,
    val sha256: String
)

@kotlinx.serialization.Serializable
data class DobDto(
    val date: String,
    val age: Int
)

@kotlinx.serialization.Serializable
data class RegisteredDto(
    val date: String,
    val age: Int
)

@kotlinx.serialization.Serializable
data class IdDto(
    val name: String?,
    val value: String?
)

@kotlinx.serialization.Serializable
data class PictureDto(
    val large: String,
    val medium: String,
    val thumbnail: String
)

@kotlinx.serialization.Serializable
data class InfoDto(
    val seed: String,
    val results: Int,
    val page: Int,
    val version: String
)