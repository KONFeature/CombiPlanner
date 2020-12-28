package com.nivelais.combiplanner.app.ui.modules.settings.create_category

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.usecases.CreateCategoryParams
import com.nivelais.combiplanner.domain.usecases.CreateCategoryUseCase
import org.koin.core.scope.inject

class CreateCategoryViewModel : GenericViewModel() {

    // View model used to create our scope
    private val createCategoryUseCase: CreateCategoryUseCase by inject()

    /**
     * State flow for our category creation
     */
    val creationFlow = createCategoryUseCase.stateFlow

    // Current name and color picked by the user
    val name = mutableStateOf(TextFieldValue())
    val color: MutableState<Color?> = mutableStateOf(null)

    /**
     * All the different color available
     */
    val colors: List<Color> = listOf(
        Color(0xff2A9D8F),
        Color(0xffE9C46A),
        Color(0xffEB5957),
        Color(0xff33719E),
        Color(0xff9E3C3A),
        Color(0xff239E4A),
        Color(0xff9E691B),
    )

    /**
     * Call to proceed to the category creation
     */
    @OptIn(ExperimentalUnsignedTypes::class)
    fun launchCreation() {
        createCategoryUseCase.run(
            CreateCategoryParams(
                name = name.value.text,
                color = color.value?.value?.toLong()
            )
        )
    }

    override fun clearUseCases() {
        createCategoryUseCase.clear()
    }
}