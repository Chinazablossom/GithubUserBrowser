package com.example.githubuserbrowser.domain.usecase

import com.example.githubuserbrowser.domain.model.PaginationInfo
import com.example.githubuserbrowser.domain.model.User
import com.example.githubuserbrowser.domain.repository.UserRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(
        since: Long = 0,
        perPage: Int = 30,
        forceRefresh: Boolean = false
    ): Result<Pair<List<User>, PaginationInfo>> {
        val result = userRepository.getUsers(since, perPage, forceRefresh)

        return result.map { users ->
            val hasMore = users.size >= perPage
            val nextSince = if (users.isNotEmpty()) {
                users.maxOf { it.id }
            } else {
                since
            }

            val paginationInfo = PaginationInfo(
                currentSince = nextSince,
                hasMore = hasMore,
                isLoading = false
            )

            Pair(users, paginationInfo)
        }
    }
}
