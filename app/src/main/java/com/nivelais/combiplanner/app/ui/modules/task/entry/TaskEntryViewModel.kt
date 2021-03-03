package com.nivelais.combiplanner.app.ui.modules.task.entry

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.nivelais.combiplanner.app.ui.modules.main.GenericViewModel
import com.nivelais.combiplanner.domain.entities.TaskEntry
import com.nivelais.combiplanner.domain.usecases.task.entry.DeleteEntryParams
import com.nivelais.combiplanner.domain.usecases.task.entry.DeleteEntryUseCase
import com.nivelais.combiplanner.domain.usecases.task.entry.UpdateEntryParams
import com.nivelais.combiplanner.domain.usecases.task.entry.UpdateEntryUseCase
import com.skydoves.whatif.whatIfNotNull
import com.skydoves.whatif.whatIfNotNullAs
import org.koin.core.scope.inject

/**
 * View model used to handle all the logic related to the management of our task entries
 */
class TaskEntryViewModel : GenericViewModel() {

    private var taskEntry: TaskEntry? = null

    // Use cases to create, update or delete our task
    private val updateEntryUseCase: UpdateEntryUseCase by inject()
    private val deleteEntryUseCase: DeleteEntryUseCase by inject()

    // Content resolver, used to get our bitmap from a given uri
    private val contentResolver: ContentResolver by inject()

    // The element of the view
    val nameState = mutableStateOf(taskEntry?.name ?: "")
    val isDoneState = mutableStateOf(taskEntry?.isDone ?: false)

    // State indicating us if we display the advanced option possible for a new entry
    val isAdvancedVisibleState = mutableStateOf(false)

    // List of picture of this entry : TODO : Load base pictures from file path
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
     * Update the current displayed entry
     */
    fun updateEntry(newEntry: TaskEntry?) {
        // Update the displayed entry
        taskEntry = newEntry
        // Reset the field
        nameState.value = newEntry?.name ?: "null"
        isDoneState.value = newEntry?.isDone ?: false
        // Hide the advanced panel if it was visible
        if (isAdvancedVisibleState.value) isAdvancedVisibleState.value = false
        // TODO : Reset the pictures associated

    }

    /**
     * Delete the current entry
     */
    fun onDeleteClicked() {
        taskEntry?.let {
            deleteEntryUseCase.run(
                DeleteEntryParams(entryId = it.id)
            )
        }
    }

    /**
     * When the name of the task entry is changed
     */
    fun onNameChange(name: String) {
        nameState.value = name
        taskEntry?.let {
            updateEntryUseCase.run(
                UpdateEntryParams(
                    entryId = it.id,
                    name = name
                )
            )
        }
    }

    /**
     * When the is done state of the entry is changed
     */
    fun onIsDoneChanged(isDone: Boolean) {
        isDoneState.value = isDone
        taskEntry?.let {
            updateEntryUseCase.run(
                UpdateEntryParams(
                    entryId = it.id,
                    isDone = isDone
                )
            )
        }
    }

    /**
     * Handle the result of the get pictures intent
     */
    fun handlePictureResult(result: ActivityResult) {
        // Check if we got the right result
        if (result.resultCode != Activity.RESULT_OK) {
            log.debug("The image selection for the task creation was aborted")
            return
        }
        // Extract the result content in the case of a uri returned (file already existing on the device)
        result.data?.data?.whatIfNotNull { pictureUri ->
            log.info("Handling picture uri as result, uri : $pictureUri")
            contentResolver.openInputStream(pictureUri)
                ?.let { BitmapFactory.decodeStream(it) } // If we successfully open the stream, decode it's content
                ?.let { pictures.add(it) } // Then add it to the pictures
        }
        // Extract the result in the case of a new picture taken
        result.data?.extras?.get("data").whatIfNotNullAs<Bitmap> { pictureBitmap ->
            log.info("Handling picture bitmap as result, bitmap bytes : ${pictureBitmap.byteCount}")
            pictures.add(pictureBitmap)
        }
    }

    override fun clearUseCases() {
        updateEntryUseCase.clear()
        deleteEntryUseCase.clear()
    }
}