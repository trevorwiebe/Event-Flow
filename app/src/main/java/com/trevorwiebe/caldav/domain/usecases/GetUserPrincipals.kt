package com.trevorwiebe.caldav.domain.usecases

import com.trevorwiebe.caldav.data.CalDavApi
import com.trevorwiebe.caldav.domain.parser.UserPrincipalsParser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetUserPrincipals (
    private val calDavApi: CalDavApi
){

    private val principalParser = UserPrincipalsParser()
    suspend operator fun invoke(
        userName: String, password: String, url: String
    ): Flow<List<String>> {
        return calDavApi.getPrincipals(userName, password, url)
            .map { principalParser.parsePrincipals(it) }
    }
}