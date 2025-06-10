package pl.kapucyni.wolczyn.app.auth.presentation.manager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import pl.kapucyni.wolczyn.app.auth.presentation.manager.composables.SearchUserDialog
import pl.kapucyni.wolczyn.app.auth.presentation.manager.composables.UserCard
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel.State
import pl.kapucyni.wolczyn.app.common.presentation.composables.EmptyListInfo
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingDialog
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.theme.wolczynColors
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.account_manager_title
import smwolczyn.composeapp.generated.resources.empty_users_list
import smwolczyn.composeapp.generated.resources.ic_cap_archive
import smwolczyn.composeapp.generated.resources.search

@OptIn(KoinExperimentalAPI::class)
@Composable
fun AccountManagerScreen(
    navigateUp: () -> Unit,
    viewModel: AccountManagerViewModel = koinViewModel(),
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    var searchDialogVisible by remember { mutableStateOf(false) }

    ScreenLayout(
        title = stringResource(Res.string.account_manager_title),
        onBackPressed = navigateUp,
        actionIcon = {
            IconButton(onClick = { searchDialogVisible = true }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    tint = wolczynColors.primary,
                    contentDescription = stringResource(Res.string.search),
                )
            }
        },
    ) {
        when (state) {
            is State.Loading -> LoadingBox()
            is State.Success -> (state as? State.Success)?.data?.let { users ->
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp),
                ) {
                    items(items = users, key = { it.id }) { user ->
                        UserCard(
                            user = user,
                            onTypeChanged = { type -> viewModel.updateUserType(user, type) },
                        )
                    }

                    if (users.isEmpty())
                        item {
                            EmptyListInfo(
                                messageRes = Res.string.empty_users_list,
                                drawableRes = Res.drawable.ic_cap_archive,
                            )
                        }
                }
            } ?: LoadingBox()
        }
    }

    SearchUserDialog(
        isVisible = searchDialogVisible,
        onSearch = { query ->
            searchDialogVisible = false
            viewModel.searchUsers(query)
        },
        onCancel = { searchDialogVisible = false },
    )

    LoadingDialog(visible = isLoading)
}