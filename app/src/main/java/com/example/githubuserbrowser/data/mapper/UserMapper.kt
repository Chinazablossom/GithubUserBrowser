package com.example.githubuserbrowser.data.mapper

import com.example.githubuserbrowser.data.local.entity.UserDetailEntity
import com.example.githubuserbrowser.data.local.entity.UserEntity
import com.example.githubuserbrowser.data.remote.dto.GithubUserDetailDto
import com.example.githubuserbrowser.data.remote.dto.GithubUserDto
import com.example.githubuserbrowser.domain.model.User
import com.example.githubuserbrowser.domain.model.UserDetail


// DTO to Domain
fun GithubUserDto.toDomain(): User = User(
    id = id,
    login = login,
    avatarUrl = avatarUrl,
    type = type
)

fun GithubUserDetailDto.toDomain(): UserDetail = UserDetail(
    id = id,
    login = login,
    avatarUrl = avatarUrl,
    type = type,
    bio = bio,
    followers = followers,
    publicRepos = publicRepos,
    name = name,
    company = company,
    location = location,
    blog = blog,
    htmlUrl = htmlUrl
)

// DTO to Entity
fun GithubUserDto.toEntity(): UserEntity = UserEntity(
    id = id,
    login = login,
    avatarUrl = avatarUrl,
    type = type
)

fun GithubUserDetailDto.toEntity(): UserDetailEntity = UserDetailEntity(
    id = id,
    login = login,
    avatarUrl = avatarUrl,
    type = type,
    bio = bio,
    followers = followers,
    publicRepos = publicRepos,
    name = name,
    company = company,
    location = location,
    blog = blog,
    htmlUrl = htmlUrl
)

// Entity to Domain
fun UserEntity.toDomain(): User = User(
    id = id,
    login = login,
    avatarUrl = avatarUrl,
    type = type
)

fun UserDetailEntity.toDomain(): UserDetail = UserDetail(
    id = id,
    login = login,
    avatarUrl = avatarUrl,
    type = type,
    bio = bio,
    followers = followers,
    publicRepos = publicRepos,
    name = name,
    company = company,
    location = location,
    blog = blog,
    htmlUrl = htmlUrl
)

// Domain to Entity (for caching)
fun User.toEntity(): UserEntity = UserEntity(
    id = id,
    login = login,
    avatarUrl = avatarUrl,
    type = type
)

fun UserDetail.toEntity(): UserDetailEntity = UserDetailEntity(
    id = id,
    login = login,
    avatarUrl = avatarUrl,
    type = type,
    bio = bio,
    followers = followers,
    publicRepos = publicRepos,
    name = name,
    company = company,
    location = location,
    blog = blog,
    htmlUrl = htmlUrl
)
