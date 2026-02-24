package com.example.githubuserbrowser.domain.usecase

import com.example.githubuserbrowser.domain.model.UserDetail
import com.example.githubuserbrowser.domain.repository.UserRepository
import javax.inject.Inject

class GetUserDetailUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        username: String,
        forceRefresh: Boolean = false
    ): Result<UserDetail> {
        return userRepository.getUserDetail(username, forceRefresh)
    }
}
