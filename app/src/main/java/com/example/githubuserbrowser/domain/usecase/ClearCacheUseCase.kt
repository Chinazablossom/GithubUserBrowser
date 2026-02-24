package com.example.githubuserbrowser.domain.usecase

import com.example.githubuserbrowser.domain.repository.UserRepository
import javax.inject.Inject

class ClearCacheUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke() {
        userRepository.clearCache()
    }
}
