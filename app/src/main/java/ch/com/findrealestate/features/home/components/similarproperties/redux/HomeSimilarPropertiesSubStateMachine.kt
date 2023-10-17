package ch.com.findrealestate.features.home.components.similarproperties.redux

import android.util.Log
import androidx.annotation.VisibleForTesting
import ch.com.findrealestate.domain.entity.Property
import ch.com.findrealestate.domain.usecase.GetSimilarProperties
import ch.com.findrealestate.features.home.HomeItem
import ch.com.findrealestate.features.home.redux.*
import com.freeletics.flowredux.Reducer
import com.freeletics.flowredux.SideEffect
import com.freeletics.flowredux.SubStateMachine
import com.freeletics.flowredux.ofType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class HomeSimilarPropertiesSubStateMachine @Inject constructor(private val getSimilarProperties: GetSimilarProperties) :
    SubStateMachine<HomeState, HomeBaseAction, HomeBaseNavigation>() {


    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val navigationSideEffect = createNavigationSideEffect<HomeBaseAction> { _, action ->
        when (action) {
            is HomeSimilarPropertiesAction.SimilarPropertyClick -> HomeSimilarPropertiesNavigation.OpenSimilarPropertyDetail(
                action.propertyId
            )
            else -> null
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val loadSimilarPropertiesSideEffect: SideEffect<HomeState, HomeBaseAction> = { actions, _ ->
        actions.ofType(HomeAction.DataLoaded::class) // continue loading similar properties when data loaded,
            // should show loading similar properties state while waiting for similar properties data loaded
            .onEach { Log.d("HomeSimilarSubSM", "Start Loading Similar Properties") }
            .flatMapLatest {
                flow {
                    try {
                        // load data
                        val properties = getSimilarProperties.invoke()
                        emit(HomeSimilarPropertiesAction.SimilarPropertiesLoaded(properties))
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        emit(HomeSimilarPropertiesAction.SimilarPropertiesLoadedError(ex.message))
                    }
                }
            }
    }

    override val sideEffects: List<SideEffect<HomeState, HomeBaseAction>> =
        listOf(navigationSideEffect, loadSimilarPropertiesSideEffect)

    override val reducer: Reducer<HomeState, HomeBaseAction> = { state, action ->
        when (action) {
            is HomeSimilarPropertiesAction.SimilarPropertiesLoaded -> {
                HomeState.PropertiesListUpdated(
                    state,
                    state.items.insertSimilarPropertiesItem(action.properties)
                )
            }
            else -> state
        }
    }

    private fun List<HomeItem>.insertSimilarPropertiesItem(similarProperties: List<Property>): List<HomeItem> =
        this.mapIndexed { index, item ->
            if (index == 4) // insert at index 4th
                listOf(HomeItem.SimilarPropertiesItem(similarProperties), item)
            else
                listOf(item)
        }.flatten()


}
