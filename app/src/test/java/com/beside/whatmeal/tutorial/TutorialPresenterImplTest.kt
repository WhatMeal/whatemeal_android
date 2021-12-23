package com.beside.whatmeal.tutorial

import com.beside.whatmeal.data.SettingLocalDataSource
import com.beside.whatmeal.tutorial.presenter.TutorialPresenter
import com.beside.whatmeal.tutorial.presenter.TutorialPresenterImpl
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TutorialPresenterImplTest {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var settingLocalDataSource: SettingLocalDataSource

    @Mock
    lateinit var tutorialViewInterface: TutorialActivityInterface

    private lateinit var tutorialPresenter: TutorialPresenter

    @Before
    fun setUp() {
        tutorialPresenter = TutorialPresenterImpl(tutorialViewInterface, settingLocalDataSource)
    }

    @Test
    fun `Test onClickStartButton`() {
        // Test
        tutorialPresenter.onClickStartButton()

        // Verify
        verify(settingLocalDataSource).setTutorialShown(eq(true))
        verify(tutorialViewInterface).startSurveyActivity()
    }
}