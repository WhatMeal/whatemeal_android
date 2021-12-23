package com.beside.whatmeal.splash

import com.beside.whatmeal.data.SettingLocalDataSource
import com.beside.whatmeal.splash.viewmodel.SplashViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class SplashViewModelTest {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var settingLocalDataSource: SettingLocalDataSource

    private lateinit var splashViewModel: SplashViewModel

    @Before
    fun setUp() {
        splashViewModel = SplashViewModel(settingLocalDataSource)
    }

    @Test
    fun `Test tutorialShownOrNull`() {
        var expected: Boolean? = null

        // Verify
        splashViewModel.tutorialShownOrNull.observeForever { result ->
            assertEquals(expected = expected, actual = result)
        }

        // Test - not past time
        expected = null
        splashViewModel.mutablePastMinimumTime.value = false

        // Test - past time & tutorial not shown
        expected = true
        whenever(settingLocalDataSource.isTutorialShown()).thenReturn(true)
        splashViewModel.mutablePastMinimumTime.value = true

        // Test - past time & tutorial shown
        expected = null
        splashViewModel.mutablePastMinimumTime.value = false
        whenever(settingLocalDataSource.isTutorialShown()).thenReturn(false)
        expected = false
        splashViewModel.mutablePastMinimumTime.value = true
    }
}