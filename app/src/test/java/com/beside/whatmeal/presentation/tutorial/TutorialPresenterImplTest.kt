package com.beside.whatmeal.presentation.tutorial

import com.beside.whatmeal.presentation.common.WhatMealBoDelegator
import com.beside.whatmeal.presentation.tutorial.view.TutorialView
import com.beside.whatmeal.presentation.tutorial.presenter.TutorialPresenter
import com.beside.whatmeal.presentation.tutorial.presenter.TutorialPresenterImpl
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
    lateinit var whatMealBo: WhatMealBoDelegator

    @Mock
    lateinit var tutorialViewInterface: TutorialView

    private lateinit var tutorialPresenter: TutorialPresenter

    @Before
    fun setUp() {
        tutorialPresenter = TutorialPresenterImpl(tutorialViewInterface, whatMealBo)
    }

    @Test
    fun `Test onClickStartButton`() {
        // Test
        tutorialPresenter.onClickStartButton()

        // Verify
        verify(whatMealBo).setTutorialShown(eq(true))
        verify(tutorialViewInterface).startSurveyActivity()
    }
}