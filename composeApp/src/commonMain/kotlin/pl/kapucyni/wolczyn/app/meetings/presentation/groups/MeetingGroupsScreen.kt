package pl.kapucyni.wolczyn.app.meetings.presentation.groups

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import pl.kapucyni.wolczyn.app.common.presentation.BasicViewModel.State
import pl.kapucyni.wolczyn.app.common.presentation.composables.LoadingBox
import pl.kapucyni.wolczyn.app.common.presentation.composables.ScreenLayout
import pl.kapucyni.wolczyn.app.meetings.domain.model.Group
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction.SaveGroups
import pl.kapucyni.wolczyn.app.meetings.presentation.groups.MeetingGroupsScreenAction.ToggleAnimatorsDialog
import pl.kapucyni.wolczyn.app.theme.wolczynColors
import smwolczyn.composeapp.generated.resources.Res
import smwolczyn.composeapp.generated.resources.copy
import smwolczyn.composeapp.generated.resources.meeting_groups
import smwolczyn.composeapp.generated.resources.save
import smwolczyn.composeapp.generated.resources.select_animators

@Composable
fun MeetingGroupsScreen(
    navigateUp: () -> Unit,
    viewModel: MeetingGroupsViewModel,
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    val clipboard = LocalClipboardManager.current

    ScreenLayout(
        title = stringResource(Res.string.meeting_groups),
        onBackPressed = navigateUp,
        actionIcon =
            if ((state as? State.Success)?.data?.newGroups.isNullOrEmpty().not()) {
                {
                    IconButton(onClick = { viewModel.handleAction(ToggleAnimatorsDialog) }) {
                        Icon(
                            imageVector = Icons.Filled.SupervisedUserCircle,
                            tint = wolczynColors.primary,
                            contentDescription = stringResource(Res.string.select_animators),
                        )
                    }
                }
            } else null,
        floatingActionButton = {
            (state as? State.Success)?.data?.let { data ->
                when {
                    data.saveAvailable -> {
                        FloatingActionButton(onClick = { viewModel.handleAction(SaveGroups) }) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = stringResource(Res.string.save),
                            )
                        }
                    }

                    data.copyAvailable -> {
                        FloatingActionButton(onClick = {
                            clipboard.setText(data.savedGroups.getClipboardData())
                        }) {
                            Icon(
                                imageVector = Icons.Default.CopyAll,
                                contentDescription = stringResource(Res.string.copy),
                            )
                        }
                    }
                }
            }
            if ((state as? State.Success<MeetingGroupsScreenState>)?.data?.saveAvailable == true) {
                FloatingActionButton(onClick = { viewModel.handleAction(SaveGroups) }) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = stringResource(Res.string.save),
                    )
                }
            }
        },
    ) {
        when (state) {
            is State.Loading -> LoadingBox()
            is State.Success -> (state as? State.Success)?.data?.let { state ->
                MeetingGroupsScreenContent(
                    state = state,
                    handleAction = viewModel::handleAction,
                )
            } ?: LoadingBox()
        }
    }
}

private fun List<Group>.getClipboardData() = buildAnnotatedString {
    forEach { group ->
        withStyle(style = SpanStyle(fontWeight = Bold)) {
            appendLine(
                "${group.number} - ${group.animatorName}" +
                        if (group.animatorContact.isNotBlank()) " (${group.animatorContact})"
                        else ""
            )
        }
        group.members.keys.forEachIndexed { index, email ->
            appendLine("${group.members[email]}")
        }
        appendLine()
        appendLine()
    }
}