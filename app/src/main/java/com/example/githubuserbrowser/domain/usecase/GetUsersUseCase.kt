package com.example.githubuserbrowser.domain.usecase

import com.example.githubuserbrowser.domain.model.PaginationInfo
import com.example.githubuserbrowser.domain.model.User
import com.example.githubuserbrowser.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    operator fun invoke(
        since: Long = 0,
        perPage: Int = 30,
        forceRefresh: Boolean = false
    ): Flow<Result<Pair<List<User>, PaginationInfo>>> {
        return userRepository.getUsers(since, perPage, forceRefresh).map { result ->
            result.map { (users, _) ->
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
}
