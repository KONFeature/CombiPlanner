package com.nivelais.combiplanner.app.ui.modules.task.add_entry

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.app.utils.toBytes
import com.nivelais.combiplanner.domain.usecases.task.entry.CreateEntryParams
import com.nivelais.combiplanner.domain.usecases.task.entry.CreateEntryUseCase
import com.skydoves.whatif.whatIfNotNull
import com.skydoves.whatif.whatIfNotNullAs
import kotlinx.coroutines.launch
import org.koin.core.scope.inject

/**
 * View model used to handle all the logic related to the management of our add entry button
 */
class AddEntryViewModel : GenericViewModel() {

    // Use case to add an entry to our task
    private val createEntryUseCase: CreateEntryUseCase by inject()

    /**
     * State indicating us if we display the advanced option possible for a new entry
     */
    val isAdvancedOptionsVisibleState = mutableStateOf(false)

    /**
     * List of picture the user want for his entry
     */
    val pictures = mutableStateListOf<Bitmap>()

    // The intent use to fetch a pictures
    val intentForPicture: Intent
        get() {
            val getIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            val pickPictureIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                    type = "image/*"
                }
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // Return the agregation of all the intent
            return Intent.createChooser(getIntent, "Select Image").apply {
                putExtra(
                    Intent.EXTRA_INITIAL_INTENTS,
                    arrayOf(pickPictureIntent, takePictureIntent)
                )
            }
        }

    /**
     * Handle the result of the get pictures intent
     */
    fun handlePictureResult(result: ActivityResult) {
        // Check if we got the right result
        if (result.resultCode != RESULT_OK) {
            log.debug("The image selection for the task creation was aborted")
            return
        }
        // Extract the result content in the case of a uri returned (file already existing on the device)
        result.data?.data?.whatIfNotNull { pictureUri ->
            log.info("Handling picture uri as result, uri : $pictureUri")
        }
        // Extract the result in the case of a new picture taken
        result.data?.extras?.get("data").whatIfNotNullAs<Bitmap> { pictureBitmap ->
            log.info("Handling picture bitmap as result, bitmap bytes : ${pictureBitmap.byteCount}")
            pictures.add(pictureBitmap)
        }
    }


    /**
     * Add an entry to our task
     */
    fun addEntry(taskId: Long) {
        viewModelScope.launch {
            // Convert our pictures to byte array
            val rawPictures = pictures.map { it.toBytes() }

            // Send all of that to the use case
            createEntryUseCase.run(CreateEntryParams(taskId = taskId, name = "", rawPictures = rawPictures))
        }
    }

    override fun clearUseCases() {
        createEntryUseCase.clear()
    }
}