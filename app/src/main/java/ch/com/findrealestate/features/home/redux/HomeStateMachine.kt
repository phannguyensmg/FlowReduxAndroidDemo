package ch.com.findrealestate.features.home.redux

import androidx.annotation.VisibleForTesting
import ch.com.findrealestate.domain.usecase.GetPropertiesUseCase
import ch.com.findrealestate.features.home.redux.sideeffects.FavoriteSideEffect
import com.freeletics.flowredux.FlowReduxStateMachine
import com.freeletics.flowredux.Reducer
import com.freeletics.flowredux.SideEffect
import com.freeletics.flowredux.ofType
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@ViewModelScoped
class HomeStateMachine @Inject constructor(
    private val getPropertiesUseCase: GetPropertiesUseCase,
    private val favoriteSideEffect: FavoriteSideEffect
) : FlowReduxStateMachine<HomeState, HomeAction, HomeNavigation>() {

    override val initialState: HomeState = HomeState.Init

    override val initAction: HomeAction = HomeAction.StartLoadData

    override fun sideEffects(): List<SideEffect<HomeState, HomeAction>> =
        listOf(
            loadPropertiesSideEffect,
            favoriteSideEffect,
            navigationSideEffect
        )

    override fun reducer(): Reducer<HomeState, HomeAction> = this::reducer

    fun reducer(state: HomeState, action: HomeAction): HomeState {
        return when (action) {
            is HomeAction.StartLoadData -> HomeState.Loading(state)

            is HomeAction.DataLoadedError -> HomeState.Error(state, action.error ?: "")

            is HomeAction.DataLoaded -> HomeState.PropertiesLoaded(state, action.properties)

            is HomeAction.FavoriteUpdated -> {
                val properties = state.properties.map {
                    if (it.id == action.propertyId)
                        it.copy(isFavorite = action.isFavorite)
                    else it
                }
                if (action.isFavorite) {
                    HomeState.AddFavoriteSuccessful(
                        state,
                        properties = properties,
                        favoriteProperty = properties.first { it.id == action.propertyId }
                    )
                } else {
                    HomeState.PropertiesListUpdated(state, properties = properties)
                }
            }

            is HomeAction.FavoriteDialogYesClick,
            is HomeAction.ConfirmRemoveFavoriteNoClick -> HomeState.PropertiesListUpdated(
                state,
                properties = state.properties
            )

            is HomeAction.ConfirmRemoveFavorite -> HomeState.ConfirmFavoriteRemoved(
                state,
                action.propertyId
            )
            else -> state
        }
    }

    @VisibleForTesting
    val loadPropertiesSideEffect: SideEffect<HomeState, HomeAction> = { actions, _ ->
        actions.ofType(HomeAction.StartLoadData::class)
            .flatMapLatest {
                flow {
                    try {
                        // load data
                        val properties = getProperties()
                        emit(HomeAction.DataLoaded(properties))
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        emit(HomeAction.DataLoadedError(ex.message))
                    }
                }
            }
    }


    @VisibleForTesting
    val navigationSideEffect = createNavigationSideEffect<HomeAction> { _, action ->
        when (action) {
            is HomeAction.PropertyClick -> HomeNavigation.OpenDetailScreen(action.propertyId)
            else -> null
        }
    }

    private suspend fun getProperties() = getPropertiesUseCase.invoke()
}
