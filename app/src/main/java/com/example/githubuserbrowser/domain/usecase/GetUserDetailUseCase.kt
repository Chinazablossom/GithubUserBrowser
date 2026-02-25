package com.example.githubuserbrowser.domain.usecase

import com.example.githubuserbrowser.domain.model.UserDetail
import com.example.githubuserbrowser.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserDetailUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    operator fun invoke(
        username: String,
        forceRefresh: Boolean = false
    ): Flow<Result<UserDetail>> {
        return userRepository.getUserDetail(username, forceRefresh)
    }
}
