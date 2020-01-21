package nl.reshadf.rxjavatests

import android.graphics.Bitmap
import io.mockk.every
import io.mockk.mockk
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.junit.After
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private val disposeBag = CompositeDisposable()
    private lateinit var viewModel: DeclarationViewModel

    @Test
    fun rxjava_test() {

        val ocrProcessor = mockk<FirebaseFormProcessor>()
        val date = listOf(DateTextExtraction())
        val bitmap = mockk<Bitmap>()

        viewModel = DeclarationViewModel(ocrProcessor)

        every { ocrProcessor.scan(any(), any(), any()) } answers {
            thirdArg<OcrResultCallback>().invoke(date)
        }


        viewModel.addImage(bitmap)

        viewModel.ocrAlert
            .test()
            .assertValue {
                it == date
            }
            .addTo(disposeBag)

    }

    @After
    fun tearDown() {
        disposeBag.clear()
    }
}
