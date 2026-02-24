package com.example.githubuserbrowser.domain.usecase

import com.example.githubuserbrowser.domain.model.User
import com.example.githubuserbrowser.domain.repository.UserRepository
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(query: String, perPage: Int = 30): Result<List<User>> {
        if (query.isBlank()) return Result.success(emptyList())
        return userRepository.searchUsers(query.trim(), perPage)
    }
}
