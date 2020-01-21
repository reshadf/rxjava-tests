package nl.reshadf.rxjavatests

import android.graphics.Bitmap
import io.reactivex.subjects.PublishSubject

interface TextExtractionInterface {
    var newValue: String?
    val formFieldName: String
    val message: Int

}

class DateTextExtraction : TextExtractionInterface {
    override var newValue: String? = "s"

    override val formFieldName: String = "f"

    override val message: Int = 1
}

interface OcrFormProcessorInterface {
    fun scan(bitmap: Bitmap, extract: List<TextExtractionInterface>, resultCallback: OcrResultCallback)
}

class FirebaseFormProcessor : OcrFormProcessorInterface {

    override fun scan(
        bitmap: Bitmap,
        extract: List<TextExtractionInterface>,
        resultCallback: OcrResultCallback
    ) {

        resultCallback(extract)
    }
}

typealias OcrResultCallback = (List<TextExtractionInterface>) -> Unit

class DeclarationViewModel(val ocrProcessor: OcrFormProcessorInterface = FirebaseFormProcessor()) {
    val ocrAlert: PublishSubject<List<TextExtractionInterface>> = PublishSubject.create()
    private val textExtractionItems: List<TextExtractionInterface> = listOf(DateTextExtraction())

    fun addImage(bitmap: Bitmap) {

            ocrProcessor.scan(bitmap, extract = textExtractionItems) { ocrResult ->
                ocrAlert.onNext(ocrResult)
            }
    }
}